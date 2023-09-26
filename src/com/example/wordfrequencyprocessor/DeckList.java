package com.example.wordfrequencyprocessor;
import java.io.*;

public class DeckList {
    String[] allDecks = new String[9999];
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

    public void addDeckToList(String tempDeck) {
        allDecks[position] = tempDeck;
        position++;
    }

    public void writeDecksToFile() {
        try {
            FileWriter fw = new FileWriter(getFileLocation("decks.txt"), true);
            for(int i = 0; i<position; i++) {
                String currentDeck = allDecks[i];
                fw.write(currentDeck);
                fw.write("\r\n");
            }
            fw.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void readDecksToArray() {
        position = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileLocation("decks.txt")));
            String currentDeck = br.readLine();
            while(currentDeck != null)
            {
                addDeckToList(currentDeck);
                currentDeck = br.readLine();
            }
            br.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public int searchForDeck(String searchTerm) {
        readDecksToArray();
        for(int i = 0; i<position; i++) {
             if (searchTerm.equals(allDecks[i])) {
                 return i;
             }
        }
        return -1;
    }

    public void clearArray() {
        for(int i = 0; i<position; i++) {
            allDecks[i] = null;
        }
        position = 0;
    }
}
