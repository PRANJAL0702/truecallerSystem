package com.lld.truecaller.models.tries;

import lombok.Getter;

public class TrieNode {

    public static final int ALPHABET_SIZE = 256;

    @Getter
    private String character;
    @Getter
    private TrieNode[] children;
    @Getter
    private boolean leaf;
    private boolean visited;

    public TrieNode(String character) {
        this.character = character;
        children = new TrieNode[ALPHABET_SIZE];
    }

    public TrieNode getChild(int index) {
        return children[index];
    }

    public void setChild(int index, TrieNode trieNode) {
        children[index] = trieNode;
    }

    public void setLeafNode(boolean isLeafNode) {
        this.leaf = isLeafNode;
    }

}
