package com.lld.truecaller;

import com.lld.truecaller.models.*;
import com.lld.truecaller.models.common.Contact;
import com.lld.truecaller.models.common.Tag;

import java.util.List;

public class TruecallerApplication {
    public static void main(String[] args) throws Exception {

        User account1 = new User();
        account1.register(UserCategory.FREE, "u1", "pwd",
                "u1@email.com", "6826999256", "91", "u1");

        // Test case 2: Add contacts to user
        account1.addContact(new User("9140107431", "mahadev"));
        account1.addContact(new User("8558101117", "govind", "hs"));
        account1.addContact(new User("8723937942", "gopala"));
        account1.addContact(new User("7070063864", "mahesha"));
        account1.addContact(new User("6610448270", "parvathi"));
        account1.addContact(new User("7336175457", "parameshwari"));
        account1.addContact(new User("7202250272", "narayan"));
        account1.addContact(new User("7859999997", "lakshmi"));
        account1.addContact(new User("9653498522", "ganesh", "kumar"));
        account1.addContact(new User("7277115893", "ganapathy"));
        account1.addContact(new User("9495010564", "Bhrama"));
        account1.addContact(new User("9844296241", "Saraswathi"));
        account1.addContact(new User("7917949575", "a.Veena"));

        System.out.println("Contact Size: " + account1.getContacts().size());

        System.out.println("***** Getting name with prefix par ******");
        List<String> names = account1.getContactTrie().allWordsWithPrefix("par");

        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("***** Getting numbers with prefix 9 *****");
        names = account1.getContactTrie().allWordsWithPrefix("9");
        for (String n : names) {
            System.out.println(n);
        }

        System.out.println("***** Adding 3949345003, 4953904850, 2782348999 numbers *****");
        account1.addContact(new User("3949345003", "Blocked caller1"));
        account1.addContact(new User("4953904850", "Blocked caller2"));
        account1.addContact(new User("2782348999", "Junk caller3"));

        System.out.println("***** Blocking 3949345003, 4953904850 numbers *****");
        account1.blockNumber("3949345003");
        account1.blockNumber("4953904850");
        System.out.println("Is 3949345003 blocked?: "+account1.isBlocked("3949345003"));

        //Test case 8: should not receive call from blocked caller
        System.out.println("Can 3949345003 receive call?: "+account1.canReceive("3949345003"));


        System.out.println("Unblock above number: 3949345003");
        account1.unblockNumber("3949345003");
        System.out.println("3949345003 can receive a call: " + account1.canReceive("3949345003"));

        Account account2 = new User();
        account2.register(UserCategory.FREE, "u2", "pwd",
                "u2@email.com", "6826999256", "91", "u2");

        // Test case 13: Add contacts to user
        account2.addContact(new User("7373048205", "anil"));
        account2.addContact(new User("7373113132", "aravind", "hs"));
        account2.addContact(new User("7373292767", "vimal"));
        account2.addContact(new User("7373358224", "smitha"));
        account2.addContact(new User("7373441841", "alamelu"));
        account2.addContact(new User("7373514930", "alagappan"));
        account2.addContact(new User("7373659939", "aruna"));
        account2.addContact(new User("7373782605", "seetha"));
        account2.addContact(new User("7373860476", "rama", "kumar"));
        account2.addContact(new User("7373954330", "ramganesh"));
        account2.addContact(new User("7071043159", "muruga"));
        account2.addContact(new User("7071115445", "kandha"));
        account2.addContact(new User("7071215591", "siva"));

        //Test case 14: adding business to contact
        Business business = new Business("u2 Solutions", Tag.SERVICES);
        business.setBusinessDescription("Software product development");
        Contact contact = new Contact("91", "9945012000", "contact@u2sol.com");
        business.setContact(contact);
        business.setBusinessSize(BusinessSize.LARGE);

        account1.setBusiness(business);

        //Test case 15: getting contacts from global
        System.out.println("******** Case1 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("u2");
        for (String s : names) {
            System.out.println(s);
        }

        System.out.println("******** Case2 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("ram");
        for (String s : names) {
            System.out.println(s);
        }

        System.out.println("******** Case3 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("a");
        for (String s : names) {
            System.out.println(s);
        }


    }
}