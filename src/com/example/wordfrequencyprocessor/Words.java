package com.example.wordfrequencyprocessor;

public class Words {
    String word;
    int frequency;
    boolean known;

    public String toString() {
        return word + "," + frequency + "," + known;
    }
}

