package com.lld.truecaller;

import com.lld.truecaller.models.tries.ContactTrie;
import lombok.Getter;

@Getter
public class GlobalContacts {
    public static GlobalContacts INSTANCE = new GlobalContacts();
    private final ContactTrie contactTrie = new ContactTrie();
    private GlobalContacts() {

    }
}
