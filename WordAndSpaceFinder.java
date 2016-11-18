package com.example.karthick.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

/*
 * Program to find words and number of spaces
 * from a continuous string of valid words
 * by comparing against a dictionary.
 *
 * Created By KarthicK Mariappan
 *
 */
public final class WordAndSpaceFinder {

    //Dictionary - Implemented with a Map of Alphabets containing TreeSet of words all in UpperCase
    private static Map<Character, TreeSet<String>> DICTIONARY_MAP = new HashMap<Character, TreeSet<String>>();


    private static final Integer EXACT_MATCH = 100;
    private static final Integer START_MATCH = 1;
    private static final Integer NO_MATCH = 0;

    public static void main(String[] args){
		/*Use this section to add words to the dictionary */
        addWordsToDictionary(new ArrayList<String>(Arrays.asList("A", "APPLE", "ALPHABET", "BALL", "Ball", "bat", "baloon")));
        addWordsToDictionary(new ArrayList<String>(Arrays.asList("THIS", "THAT", "Flower", "flag", "is", "a", "WAS", "APPLE", "BAT", "TABLE", "CAR", "good")));
        addWordsToDictionary(new ArrayList<String>(Arrays.asList("an", "annoying", "board", "room", "boardroom")));
        addWordsToDictionary(new ArrayList<String>(Arrays.asList("and", "android", "rock", "king", "rocking")));

        printWordsAndSpaceCount("Aflowerisaflag"); //Positive all happy run

		/*
         *  Choosing this string to test the following:
		 * 	'an' should be considered as whole word instead of 'a'
		 * 	'annoying' should be considered as whole word instead of 'an'
		 * 'boardroom' should be considered as whole word instead of 'board' and 'room';
		 */
        printWordsAndSpaceCount("Thisisanannoyingboardroom");

        /*
         *  Choosing this string to test the following:
		 * 	'android' should be considered as whole word instead of 'a' or 'an' or 'and'
		 * 	'rocking' should be considered as whole word instead of 'rock' or 'king'
		 *  Accidental space between Android and isrocking! is ignored
		 *  '!' punctuation mark is ignored
		 */
        printWordsAndSpaceCount("Android isrocking!");

        /*
         *  Choosing this string to test the following:
         *  More than one sentence
		 *  '.' between sentences
		 */
        printWordsAndSpaceCount("Android isrocking!Aflowerisaflag.Thisisanannoyingboardroom");
    }

    private static void printWordsAndSpaceCount(String continuousString){
        ArrayList<Integer> spacePositions = new ArrayList<Integer>(); //List of all possible space positions
        spacePositions.add(0);
        int lastPossibleSpacePosition = spacePositions.get(spacePositions.size()-1);
        int nextPossibleSpacePosition = lastPossibleSpacePosition + 1;
        int tempPossibleSpacePosition = 0;
        continuousString = removePunctuation(continuousString); //Remove all punctuation marks from the string
        //TODO Handle apostraphe ex: dog's tail - possibly can be handled within checkForMatchInDictionary
        while(nextPossibleSpacePosition<=continuousString.length()){
            String possibleWord = continuousString.substring(lastPossibleSpacePosition, nextPossibleSpacePosition);
            Integer matchResult = checkForMatchInDictionary(possibleWord);
            if(matchResult == EXACT_MATCH){
                //Exact match is found, but it could be a part of a bigger word. So wait and see...
                tempPossibleSpacePosition = nextPossibleSpacePosition;
                if(nextPossibleSpacePosition == continuousString.length()){
                    //Nothing more to wait and see.. so this is the full word.
                    System.out.println(possibleWord);
                }
            }
            if(matchResult == NO_MATCH){
                if(tempPossibleSpacePosition != 0){
                    //Match found earlier, not part of a bigger word. This is the full word.
                    spacePositions.add(tempPossibleSpacePosition);
                    lastPossibleSpacePosition = tempPossibleSpacePosition;
                    nextPossibleSpacePosition --;
                    System.out.println(possibleWord.substring(0, possibleWord.length()-1));
                    tempPossibleSpacePosition = 0;
                }
            }
            nextPossibleSpacePosition++; // This means its a START_MATCH, go forward and see if you get a bigger word
        }
        spacePositions.remove(0);
        System.out.println("Space Positions: " + spacePositions.toString());
        System.out.println("Number of Spaces: " + spacePositions.size());
    }

    private static int checkForMatchInDictionary(String possibleWord){
        if(possibleWord != null && !possibleWord.equals("") && possibleWord.length()>0){
            possibleWord = possibleWord.toUpperCase();
            Character firstLetter = possibleWord.charAt(0);
            if(!DICTIONARY_MAP.containsKey(firstLetter)){
                return NO_MATCH;
            }
            TreeSet<String> existingWordsSet = DICTIONARY_MAP.get(firstLetter);
            if(existingWordsSet.contains(possibleWord)){
                //Word matches exactly with a word in dictionary
                return EXACT_MATCH;
            }
            for (Iterator iterator = existingWordsSet.iterator(); iterator
                    .hasNext();) {
                String existingWord = (String) iterator.next();
                if(existingWord.startsWith(possibleWord)){
                    //No exact match, but there are words in dictionary with starting characters as possible word
                    return START_MATCH;
                }
            }
            return NO_MATCH;
        }
        return NO_MATCH;
    }

    /*Method to add words to Dictionary in UpperCase under corresponding Start Letter*/
    private static void addWordsToDictionary(ArrayList<String> wordsList){
        for (Iterator iterator = wordsList.iterator(); iterator.hasNext();) {
            String word = ((String) iterator.next()).toUpperCase(Locale.ENGLISH);
            if(word != null && !word.equals("") && word.length()>0){
                Character firstLetter = word.charAt(0); //Get first letter
                if(DICTIONARY_MAP.containsKey(firstLetter)){
                    //already has entry for this character as first letter
                    TreeSet<String> existingWordsSet = DICTIONARY_MAP.get(firstLetter);
                    existingWordsSet.add(word);
                }
                else{
                    //Does not have an entry. Create one.
                    TreeSet<String> newWordsList = new TreeSet<String>(Arrays.asList(word));
                    DICTIONARY_MAP.put(firstLetter, newWordsList);
                }
            }
        }
    }

    //Method to check if a character is a punctuation mark
    private static String removePunctuation(String inputString){
        if(inputString != null && inputString.length()>0){
            inputString = inputString.replace(".", "");
            inputString = inputString.replace(",", "");
            inputString = inputString.replace(";", "");
            inputString = inputString.replace("-", "");
            inputString = inputString.replace("!", "");
            inputString = inputString.replace("?", "");
            inputString = inputString.replace(" ", "");
        }
        return inputString;
    }

}