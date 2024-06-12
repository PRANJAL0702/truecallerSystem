package com.lld.truecaller.models;

import com.lld.truecaller.GlobalContacts;
import com.lld.truecaller.exceptions.BlockLimitExceededException;
import com.lld.truecaller.exceptions.ContactsExceededException;
import com.lld.truecaller.exceptions.UserDoesNotExistException;
import com.lld.truecaller.models.common.Contact;
import com.lld.truecaller.models.common.GlobalSpam;
import com.lld.truecaller.models.common.PersonalInfo;
import com.lld.truecaller.models.tries.ContactTrie;
import orestes.bloomfilter.FilterBuilder;

import java.util.*;

import static com.lld.truecaller.models.common.Constants.*;

public class User extends Account{

    public User(String phoneNumber, String firstName) {
        super(phoneNumber, firstName);
    }

    public User(String phoneNumber, String firstName, String lastName) {
        super(phoneNumber, firstName, lastName);
    }

    public User() {
        setContactTrie(new ContactTrie());
    }

    @Override
    public void register(UserCategory userCategory, String userName, String password, String email, String phoneNumber, String countryCode, String firstName) {
        setId(UUID.randomUUID().toString());
        setUserName(userName);
        setPassword(password);
        setContact(new Contact(countryCode,phoneNumber, email));
        setPersonalInfo(new PersonalInfo(firstName));
        setUserCategory(userCategory);
        init(userCategory);
        insertToPhoneTrie(phoneNumber, firstName);

    }

    private void init(UserCategory userCategory) {
        switch (userCategory) {
            case FREE:
                setContacts(new HashMap<>(MAX_FREE_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_FREE_USER_BLOCKED_CONTACTS, 0.01).buildCountingBloomFilter());
                setBlocketSet(new HashSet<>(MAX_FREE_USER_BLOCKED_CONTACTS));
                break;
            case GOLD:
                setContacts(new HashMap<>(MAX_GOLD_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_GOLD_USER_BLOCKED_CONTACTS, 0.01).buildCountingBloomFilter());
                setBlocketSet(new HashSet<>(MAX_GOLD_USER_BLOCKED_CONTACTS));
            case PLATINUM:
                setContacts(new HashMap<>(MAX_PLATINUM_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_PLATINUM_USER_BLOCKED_CONTACTS, 0.01).buildCountingBloomFilter());
                setBlocketSet(new HashSet<>(MAX_PLATINUM_USER_BLOCKED_CONTACTS));

        }
    }

    @Override
    public void addContact(User user) throws ContactsExceededException {
        checkAddUser();
        getContacts().putIfAbsent(user.getPhoneNumber(), user);
        insertToPhoneTrie(user.getPhoneNumber(), user.getPersonalInfo().getFirstName());
    }

    private void insertToPhoneTrie(String phoneNumber, String firstName) {
        getContactTrie().insert(phoneNumber);
        getContactTrie().insert(firstName);
        GlobalContacts.INSTANCE.getContactTrie().insert(phoneNumber);
        GlobalContacts.INSTANCE.getContactTrie().insert(firstName);
    }

    @Override
    public void removeContact(String phoneNumber) throws UserDoesNotExistException {
        User userContact = getContacts().get(phoneNumber);
        if(userContact == null) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        getContacts().remove(phoneNumber);
        getContactTrie().delete(phoneNumber);
        getContactTrie().delete(userContact.getPersonalInfo().getFirstName());
    }

    @Override
    public void blockNumber(String number) throws BlockLimitExceededException {
        checkBlockUser();
        getBlockedContacts().add(number);
    }

    @Override
    public void unblockNumber(String number) {
        getBlockedContacts().remove(number);
    }

    @Override
    public void reportSpam(String number, String reason) {
        getBlockedContacts().add(number);
        GlobalSpam.INSTANCE.reportSpam(number, this.getPhoneNumber(), reason);
    }

    @Override
    public void upgrade(UserCategory userCategory) {
        int count = 0;
        int blockedCount = 0;
        switch (userCategory) {
            case GOLD:
                count = MAX_GOLD_USER_CONTACTS;
                blockedCount = MAX_GOLD_USER_BLOCKED_CONTACTS;
                break;
            case PLATINUM:
                count = MAX_PLATINUM_USER_CONTACTS;
                blockedCount = MAX_PLATINUM_USER_BLOCKED_CONTACTS;
                break;
        }
        upgradeContacts(count);
        upgradeBlockedContacts(blockedCount);
    }

    private void upgradeBlockedContacts(int blockedCount) {
        this.setBlockedContacts(new FilterBuilder(blockedCount, .01).buildCountingBloomFilter());
        Set<String> upgradedSet = new HashSet<>();
        for (String blocked : this.getBlocketSet()) {
            upgradedSet.add(blocked);
            getBlockedContacts().add(blocked);
        }
    }

    private void upgradeContacts(int count) {
        Map<String, User> upgradedContacts = new HashMap<>(count);
        for (Map.Entry<String, User> entry : getContacts().entrySet()) {
            upgradedContacts.putIfAbsent(entry.getKey(), entry.getValue());
        }
        setContacts(upgradedContacts);
    }


    @Override
    public boolean isBlocked(String number) {
        return getBlockedContacts().contains(number);
    }

    @Override
    public boolean canReceive(String number) {
        return !isBlocked(number) &&
                !GlobalSpam.INSTANCE.isGlobalSpam(number);
    }

    @Override
    public boolean importContacts(List<User> users) {
        for (User user : users) {
            try {
                addContact(user);
            } catch (ContactsExceededException cee) {
                System.out.println("Some of the contact could not be imported as limit exceeded");
                return false;
            }
        }
        return true;
    }

    private void checkAddUser() throws ContactsExceededException {
        switch (this.getUserCategory()) {
            case FREE:
                if (this.getContacts().size() >= MAX_FREE_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case GOLD:
                if (this.getContacts().size() >= MAX_GOLD_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case PLATINUM:
                if (this.getContacts().size() >= MAX_PLATINUM_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
        }
    }

    private void checkBlockUser() throws BlockLimitExceededException {
        switch (this.getUserCategory()) {
            case FREE:
                if (this.getContacts().size() >= MAX_FREE_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case GOLD:
                if (this.getContacts().size() >= MAX_GOLD_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case PLATINUM:
                if (this.getContacts().size() >= MAX_PLATINUM_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
        }
    }
}
