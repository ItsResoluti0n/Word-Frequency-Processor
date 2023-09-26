package com.example.wordfrequencyprocessor;
import java.io.*;
import java.util.Arrays;

public class WordsList {
    Words[] allWords = new Words[99999];
    int position = 0;

    public String getFileLocation(String fileName){
        try {
            String jarFilePath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String jarDirectory = new File(jarFilePath).getParent();
            String filePath = jarDirectory + File.separator + "TextFiles" + File.separator + fileName;
            File file = new File(filePath);
            return filePath;

        } catch(Exception e) {

        }
        return null;
    }

    public void addWordToList(Words tempWord) {
        allWords[position] = tempWord;
        position++;
    }

    public void writeWordsToFile(String fileName) {
        try {
            FileWriter fw = new FileWriter(getFileLocation(fileName + ".txt"), false);
            for(int i = 0; i<position; i++) {
                Words currentWord = allWords[i];
                fw.write(currentWord.word + ",");
                fw.write(currentWord.frequency + ",");
                fw.write(Boolean.toString(currentWord.known));
                fw.write("\r\n");
            }
            fw.close();
        }
        catch(Exception e) {
            //System.out.println("Error when reading from file");
        }
    }

    public void readWordsToArray(String fileName) {
        position = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileLocation(fileName + ".txt")));
            String currentLine = br.readLine();

            while(currentLine != null)
            {
                String[] splitData = currentLine.split(",");

                Words currentWord = new Words();
                currentWord.word = splitData[0];
                currentWord.frequency = Integer.parseInt(splitData[1]);
                currentWord.known = Boolean.parseBoolean(splitData[2]);

                addWordToList(currentWord);
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(Exception e) {
            //System.out.println("Error when reading from file");
        }
    }

    public int compareWord(String theWord) {
        int tempPos = 0;
        boolean found = false;
        while(allWords[tempPos] != null) {
            if(allWords[tempPos].word.equals(theWord)) {
                found = true;
                break;
            } else {
                tempPos++;
            }
        }
        if(!found) {
            return -1;
        } else {
            return tempPos;
        }
    }

    public void createTempList(String[] words) {
        clearArray();
        int tempPos = 0;

        for(int i = 0; i<words.length; i++) {
            int compResult = compareWord(words[i]);
            Words tempWord = new Words();
            tempWord.word = words[i];
            if (compResult == -1) {
                tempWord.frequency = 1;
                tempWord.known = false;
                allWords[tempPos] = tempWord;
                tempPos++;
            } else {
                allWords[compResult].frequency = allWords[compResult].frequency+1;
                tempWord.known = true;
            }
        }
        position = tempPos;
    }

    public void updateTotalArray() {
        Words[] tempWords = Arrays.copyOf(allWords, position);
        int posNewWords = position;
        clearArray();
        readWordsToArray("wordsTotal");
        for(int i = 0; i<posNewWords-1; i++) {
            //If the word in the tempWords array can not be found in the wordsTotal array then add it. If already present then update the existing frequency
            //Position holds the length of total words
            //Position new words originally held length of the new words but is now used to track

            int compResult = compareWord(tempWords[i].word);
            if (compResult == -1) {
                allWords[position] = tempWords[i];
                position++;
            } else {
                allWords[compResult].frequency = allWords[compResult].frequency + tempWords[i].frequency;
            }
        }
    }

    public void checkKnown() {
        //Check the read contents against the main file. If the word exists then copy the known status, else default to false

        Words[] tempWords = Arrays.copyOf(allWords, position);
        int posNewWords = position;
        clearArray();
        readWordsToArray("wordsTotal");

        for(int i = 0; i<posNewWords; i++) {
            int compResult = compareWord(tempWords[i].word);
            if (compResult == -1) {
                tempWords[i].known = false;
            } else {
                tempWords[i].known = allWords[compResult].known;
            }
        }
        clearArray();
        System.arraycopy(tempWords, 0, allWords, 0, posNewWords);
        position = posNewWords;
    }

    public int[] gatherKnownPercentage() {
        int total = 0;
        int known = 0;

        int[] numbers = new int[2];

        for(int i = 0; i<position; i++) {
            if(allWords[i].known) {
                total += allWords[i].frequency;
                known += allWords[i].frequency;
            } else {
                total += allWords[i].frequency;
            }
        }
        numbers[0] = known;
        numbers[1] = total;
        return numbers;
    }

    public void sortArray() {
        int j;
        //Perform insertion sort
        for (int i = 1; i<position; i++)
        {
            Words currentWord = allWords[i];
            j = i - 1;
            while ((j >= 0) && (currentWord.frequency > allWords[j].frequency))
            {
                allWords[j+1] = allWords[j];
                j = j - 1;
            }
            allWords[j + 1] = currentWord;
        }
    }

    public int searchForWord(String searchTerm, String deckName) {
        readWordsToArray(deckName);
        for(int i = 0; i<position; i++) {
            if (searchTerm.equals(allWords[i].word)) {
                return i;
            }
        }
        return -1;
    }

    public Words[] searchMultipleWords(String searchTerm, String deckName) {
        readWordsToArray(deckName);
        Words[] allMatchingWords = new Words[position];
        int tempArrPos = 0;
        for (int i=0; i<position; i++)
        {
            Words currentWord = allWords[i];
            //If equal then add the current card to the array to return
            if (currentWord.word.contains(searchTerm))
            {
                allMatchingWords[tempArrPos] = currentWord;
                tempArrPos++;
            }
        }
        return allMatchingWords;
    }

    public void printArray() {
        for(int i = 0; i<position; i++) {
            System.out.println(allWords[i]);
        }
    }

    public void clearArray() {
        for(int i = 0; i<position; i++) {
            allWords[i] = null;
        }
        position = 0;
    }

    public void clearTextFile() {
        try {
            FileWriter fw = new FileWriter(getFileLocation("wordsTotal.txt"));
            fw.close();
        }
        catch(Exception e) {
            //System.out.println("Error when reading from file");
        }
    }
}
