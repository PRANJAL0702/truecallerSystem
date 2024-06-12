package com.lld.truecaller.models.tries;

import java.util.ArrayList;
import java.util.List;

public class ContactTrie {

    private TrieNode root;
    private int indexOfSingleChild;

    public ContactTrie() {
        this.root = new TrieNode("");
    }

    public void insert(String key) {
        TrieNode trieNode = root;
        for(int i=0; i<key.length(); i++) {
            char c = key.charAt(i);
            int ascii = c;
            if(trieNode.getChild(ascii) == null) {
                TrieNode tempTrieNode = new TrieNode(String.valueOf(c));
                trieNode.setChild(ascii, tempTrieNode);
                trieNode = tempTrieNode;
            }
            else {
                trieNode = trieNode.getChild(ascii);
            }
        }
        trieNode.setLeafNode(true);
    }

    public boolean search(String key) {
        TrieNode tempTrieNode = root;
        for(int i=0; i<key.length(); i++) {
            char c = key.charAt(i);
            int ascii = c;
            if(tempTrieNode.getChild(ascii) == null) {
                return false;
            }
            else {
                tempTrieNode = tempTrieNode.getChild(ascii);
            }
        }
        return true;
    }

    public List<String> allWordsWithPrefix(String prefix) {
        TrieNode trieNode = root;
        List<String> allWords = new ArrayList<>();
        for(int i=0; i<prefix.length(); i++) {
            char c = prefix.charAt(i);
            int asciiIndex = c;
            trieNode = trieNode.getChild(asciiIndex);
        }
        getSuffixes(trieNode, prefix, allWords);
        return allWords;
    }

    private void getSuffixes(TrieNode trieNode, String prefix, List<String> allWords) {
        if(trieNode == null) return;
        if(trieNode.isLeaf()) {
            allWords.add(prefix);
        }

        for(TrieNode trialTrieNode: trieNode.getChildren()) {
            getSuffixes(trialTrieNode, prefix+ trieNode.getCharacter(), allWords);
        }
    }

    public void delete(String key) {
        if ((root == null) || (key == null)) {
            System.out.println("Null key or Empty trie error");
            return;
        }

        deleteHelper(key, root, key.length(), 0);
        return;
    }

    private boolean deleteHelper(String key, TrieNode currentNode, int length, int level) {
        boolean deletedSelf = false;

        if(currentNode == null) {
            System.out.println("Key does not exist");
            return deletedSelf;
        }
        if(level == length) {
            if(currentNode.isLeaf()) {
                deletedSelf = true;
                currentNode = null;
            }
            else {
                currentNode.setLeafNode(false);
                deletedSelf = false;
            }
        }
        else {
           TrieNode childNode = currentNode.getChild(key.charAt(level));
           boolean childDeleted = deleteHelper(key, childNode, length, level + 1);
           if(childDeleted) {
               currentNode.setChild(key.charAt(level), null);
               if(currentNode.isLeaf()) {
                   deletedSelf = false;
               }
               else if(currentNode.getChildren().length > 0) {
                   deletedSelf = false;
               }
               else {
                   currentNode = null;
                   deletedSelf = true;
               }
           } else {
               deletedSelf = false;
           }
        }
        return  deletedSelf;
    }
}
