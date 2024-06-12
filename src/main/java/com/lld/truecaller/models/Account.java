package com.lld.truecaller.models;

import com.lld.truecaller.exceptions.BlockLimitExceededException;
import com.lld.truecaller.exceptions.ContactsExceededException;
import com.lld.truecaller.exceptions.UserDoesNotExistException;
import com.lld.truecaller.models.common.Contact;
import com.lld.truecaller.models.common.PersonalInfo;
import com.lld.truecaller.models.common.SocialMedialnfo;
import com.lld.truecaller.models.common.Tag;
import com.lld.truecaller.models.tries.ContactTrie;
import lombok.Getter;
import lombok.Setter;
import orestes.bloomfilter.CountingBloomFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public abstract class Account {
    private String id;
    private String phoneNumber;
    private String userName;
    private String password;
    private LocalDateTime lastAccessed;
    private Tag tag;
    private Contact contact;
    private PersonalInfo personalInfo;
    private SocialMedialnfo socialMedialnfo;
    private Business business;
    private UserCategory userCategory;
    private Map<String, User> contacts;
    private CountingBloomFilter<String> blockedContacts;
    private Set<String> blocketSet;
    private ContactTrie contactTrie;

    public Account() {
    }

    public Account(String phoneNumber, String firstName) {
        this.phoneNumber = phoneNumber;
        this.personalInfo = new PersonalInfo(firstName);
    }

    public Account(String phoneNumber, String firstName, String lastName) {
        this(phoneNumber, firstName);
        this.personalInfo.setLastName(lastName);
    }

    public abstract void register(UserCategory userCategory, String userName, String password, String email, String phoneNumber, String countryCode, String firstName);

    public abstract void addContact(User user) throws ContactsExceededException;
    public abstract void removeContact(String phoneNumber) throws UserDoesNotExistException;
    public abstract void blockNumber(String number) throws BlockLimitExceededException;
    public abstract void unblockNumber(String number);
    public abstract void reportSpam(String number, String reason);
    public abstract void upgrade(UserCategory userCategory);
    public abstract boolean isBlocked(String number);
    public abstract boolean canReceive(String number);

    public abstract boolean importContacts(List<User> users);

}
