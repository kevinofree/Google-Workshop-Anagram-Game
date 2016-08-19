package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet(); // stores sorted words
    private HashMap<String, ArrayList> lettersToWord = new HashMap(); //holds values in keys and words for wordset
    private HashMap<Integer,ArrayList> sizeToWords = new HashMap();
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(InputStream wordListStream) throws IOException {
        //Each word that is read from the dictionary file should be stored in an ArrayList (wordList)
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);

            ArrayList<String> temp;
            String sortedString = sortLetters(word);
            if(lettersToWord.containsKey(sortedString)){
                temp = lettersToWord.get(sortedString);
                temp.add(word);
                lettersToWord.put(sortedString, temp);
            }
            else {
                temp = new ArrayList<String>();
                temp.add(word);
                lettersToWord.put(sortedString, temp);
            }
            if(sizeToWords.containsKey(word.length())){
                temp = sizeToWords.get(word.length());
                temp.add(word);
            }
            else {
                temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(word.length(), temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordList.contains(word)){
            return false;
        }
        if(word.contains(base)){
            return false;
        }
        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedString = sortLetters(targetWord);
        for(String w: wordList){
            if(w.length() == targetWord.length()){
                if(sortedString.equals(sortLetters(w))){
                    result.add(w);
                }
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String temp = word;

        for(char letter: alphabet){
            temp = word + letter;
            result.addAll(getAnagrams(temp));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String word = "";
        boolean found = false;
        ArrayList<String> listOfWords = sizeToWords.get(wordLength);
        while(!found){
            int index = random.nextInt((listOfWords.size()-1));
            word = listOfWords.get(index);
            if(getAnagramsWithOneMoreLetter(word).size()< MIN_NUM_ANAGRAMS)
                index = random.nextInt((listOfWords.size()-1));
            else
                found = true;
        }
        if (wordLength < MAX_WORD_LENGTH)
            wordLength+=1;
        return word;
    }

    public String sortLetters(String sort){
        char[] charArray= sort.toCharArray();
        Arrays.sort(charArray);
        String sorted = new String(charArray);
        return sorted;
    }
}
