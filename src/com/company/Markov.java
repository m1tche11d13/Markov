package com.company;

import java.util.HashMap;
import java.util.Vector;
import java.util.Random;

public class Markov {

    private HashMap<String, HashMap<String, Integer>> dictionary = new HashMap<>();

    void feed(String filename, int couplingSize){
        fillDictionary(cleanAndSplitString(filename), couplingSize);
        //showDictionary();
    }
    void spew(int wordCount){

        String seed = getSeed();
        System.out.print(seed);
        String s = getNextWord(seed);

        for(int i = 0; i < wordCount - 1; ++i){
            if(s.equals("."))System.out.println(s);
            else System.out.print(" " + s);

            seed = getNextKey(seed,s);
            s = getNextWord(seed);

        }
    }

    private String getNextKey(String oldKey, String newWord){
        String[] oldKeyWords = oldKey.split(" ");
        for(int i = 0; i < oldKeyWords.length-1; ++i){
            oldKeyWords[i] = oldKeyWords[i+1];
        }
        oldKeyWords[oldKeyWords.length-1] = newWord;
        String s = "";
        for(int i = 0; i < oldKeyWords.length; ++i){
            s += oldKeyWords[i] + " ";
        }
        if(s.charAt(s.length()-1) == ' ')s = s.substring(0, s.length()-1);
        return s;
    }
    private String getSeed(){
        Random rand = new Random();
        String[] keys = dictionary.keySet().toArray(new String[dictionary.size()]);
        int index = rand.nextInt(keys.length) + 1;
        return keys[index];
    }
    private String getNextWord(String key){

        HashMap<String, Integer> weighted = new HashMap<>();
        int total = 0;
        for(String s: dictionary.get(key).keySet()){
            total += dictionary.get(key).get(s);
            weighted.put(s, total);
        }

        Random r = new Random();
        int weight = r.nextInt(total) + 1;
        String hold = "";
        for(String s : weighted.keySet()){
            if(weighted.get(s) < weight){
                hold = s;
            }
            else{
                return s;
            }
        }
        return hold;
    }

    //Displays the dictionary in a readable format
    private void showDictionary(){

        for(String dictKey : dictionary.keySet()){
            System.out.println("Key: " + dictKey);
            for (String valueKey : dictionary.get(dictKey).keySet()) {
                System.out.println("\t" + valueKey + ": " + dictionary.get(dictKey).get(valueKey).toString());
            }
        }
        System.out.println(dictionary.size());

    }
    private void fillDictionary(String[] words, int couplingSize){
        if(couplingSize >= words.length) System.out.println("Choose a lower coupling size. Should be 2-4.");
        else {
            for(int i = 0; i <= words.length - couplingSize; ++i){
                String key = "";
                for(int j = 0; j < couplingSize - 1; ++j){
                    key += words[i+j];
                    if(j < couplingSize - 2) key += " ";
                }

                String value = words[i-1+couplingSize];

                if(dictionary.containsKey(key)){
                    if(dictionary.get(key).containsKey(value)){
                        //The key/value pair is in the dictionary so we increment by 1
                        int count = dictionary.get(key).get(value);
                        ++count;
                        dictionary.get(key).remove(value);
                        dictionary.get(key).put(value, count);
                    }
                    else
                    {
                        //The key is in the dictionary but the value isn't so we initialize it with 1.
                        dictionary.get(key).put(value, 1);
                    }
                }
                else
                {
                    //The key is not in the dictionary so we add the key then add the value with initial value of 1.
                    //System.out.println("ELSE:\nkey: " + key[0] + "\nvalue: " + value);
                    dictionary.put(key, new HashMap<>());
                    dictionary.get(key).put(value, 1);
                }
            }
        }
    }
    public String[] cleanAndSplitString(String s){

        while(s.contains("  ")){
            s = s.replaceAll("  ", " ");
        }

        String[] words = s.split(" ");
        Vector<String> tempStorage = new Vector<>();

        for(int i = 0; i < words.length; ++i) {
            char c = words[i].charAt(words[i].length() - 1);
            if (c == ',' || c == '.' || c == '?' || c == '!' || c == ':' || c == ';') {
                tempStorage.add(words[i].substring(0, words[i].length() - 1));
                tempStorage.add(Character.toString(words[i].charAt(words[i].length() - 1)));
            }
            else{
                tempStorage.add(words[i]);
            }
        }
        words = new String[tempStorage.size()];
        int i = 0;
        for(String str : tempStorage){
            words[i] = str;
            ++i;
        }

        return words;
    }
}