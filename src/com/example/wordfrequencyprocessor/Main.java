package com.example.wordfrequencyprocessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.DecimalFormat;
public class Main {

    Scanner inputScanner = new Scanner(System.in);

    WordsList wordsList = new WordsList();
    Words words = new Words();

    DeckList deckList = new DeckList();
    Deck deck = new Deck();

    Color backgroundColour = Color.decode("#181818");
    Color foregroundColour = Color.decode("#DCDCDC");
    Color accentColour = Color.decode("#3B7DEF");

    Font fontButton = new Font("Montserrat", Font.PLAIN, 24);
    Font fontLabel = new Font("Montserrat", Font.PLAIN, 20);

    JFrame myFrame = new JFrame();

    JPanel pnlMainMenu = new JPanel(null);
    JPanel pnlViewAll = new JPanel(null);
    JPanel pnlViewDeck = new JPanel(null);
    JPanel pnlAddAll = new JPanel(null);
    JPanel pnlAddDeck = new JPanel(null);
    JPanel pnlCompareKnown = new JPanel(null);

    //Main buttons
    JButton btnViewAll = new JButton("View All");
    JButton btnViewDeck = new JButton("View Deck");
    JButton btnAddDeck = new JButton("Add Deck");
    JButton btnAddAll = new JButton("Add All");
    JButton btnKnownPercent = new JButton("Estimate Known Words");

    //View components
    String[] tableHeadingsView = {"Word", "Frequency", "Known?"};
    DefaultTableModel theTableModel = new DefaultTableModel(tableHeadingsView, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable tableView = new JTable(theTableModel);
    JScrollPane scrollPaneTableView = new JScrollPane(tableView);
    JTextField tfSearch = new JTextField();
    JButton btnKnownToggle = new JButton("Hide");
    JComboBox cbxDecks = new JComboBox();

    //Add Components
    JTextArea taEnter = new JTextArea(6,30);
    JScrollPane scrollPaneEnter = new JScrollPane(taEnter);
    JButton btnAddAllConfirm = new JButton("Process the following text");
    JButton btnAddDeckConfirm = new JButton("Process");
    JLabel lblDeckName = new JLabel("Deck Name:");
    JTextField tfDeckName = new JTextField();

    JButton btnAnalyse = new JButton("Analyse");
    JTextArea taAnalyse = new JTextArea(6,30);
    JScrollPane scrollPaneAnalyse = new JScrollPane(taAnalyse);
    JComboBox cbxAnalyseDecks = new JComboBox();

    public void initFrame() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(900,900);
        myFrame.setLocation(1240,240);
        myFrame.setResizable(false);
        myFrame.setTitle("Word Frequency Processor");
        myFrame.setLayout(null);

        pnlMainMenu.setBackground(backgroundColour);
        initComponentsMain();
        initComponentsView();
        initComponentsAdd();
        initComponentsCompare();

        pnlMainMenu.setBounds(0,0,900,260);
        pnlViewAll.setBounds(0,240,900,660);
        pnlViewDeck.setBounds(0,240,900,660);
        pnlAddAll.setBounds(0,240,900,660);
        pnlAddDeck.setBounds(0,240,900,660);
        pnlCompareKnown.setBounds(0,240,900,660);

        myFrame.setVisible(true);
        myFrame.add(pnlMainMenu);
        myFrame.add(pnlViewAll);
    }

    public void initComponentsMain() {
        btnViewAll.setSize(200, 50);
        btnViewAll.setBackground(backgroundColour);
        btnViewAll.setLocation(225, 50);
        btnViewAll.setForeground(accentColour);
        btnViewAll.setFont(fontButton);
        btnViewAll.addActionListener( AL->btnView_All() );
        pnlMainMenu.add(btnViewAll);

        btnViewDeck.setSize(200, 50);
        btnViewDeck.setBackground(backgroundColour);
        btnViewDeck.setLocation(225, 125);
        btnViewDeck.setForeground(accentColour);
        btnViewDeck.setFont(fontButton);
        btnViewDeck.addActionListener( AL->btnView_Deck() );
        pnlMainMenu.add(btnViewDeck);

        btnAddAll.setSize(200, 50);
        btnAddAll.setBackground(backgroundColour);
        btnAddAll.setLocation(475, 50);
        btnAddAll.setForeground(accentColour);
        btnAddAll.setFont(fontButton);
        btnAddAll.addActionListener( AL->btnAdd_All_Screen() );
        pnlMainMenu.add(btnAddAll);

        btnAddDeck.setSize(200, 50);
        btnAddDeck.setBackground(backgroundColour);
        btnAddDeck.setLocation(475, 125);
        btnAddDeck.setForeground(accentColour);
        btnAddDeck.setFont(fontButton);
        btnAddDeck.addActionListener( AL->btnAdd_Deck_Screen() );
        pnlMainMenu.add(btnAddDeck);

        btnKnownPercent.setSize(450, 50);
        btnKnownPercent.setBackground(backgroundColour);
        btnKnownPercent.setLocation(225, 200);
        btnKnownPercent.setForeground(accentColour);
        btnKnownPercent.setFont(fontButton);
        btnKnownPercent.addActionListener( AL->btnCompare_Screen() );
        pnlMainMenu.add(btnKnownPercent);

        deckList.readDecksToArray();
    }

    public void initComponentsView() {
        pnlViewAll.setBackground(backgroundColour);

        cbxDecks.setSize(300,50);
        cbxDecks.setLocation(100,50);
        cbxDecks.setFont(fontLabel);
        cbxDecks.setForeground(accentColour);
        cbxDecks.setBackground(backgroundColour);
        cbxDecks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cbxDecks.getSelectedItem() != null) {
                    String deckName = cbxDecks.getSelectedItem().toString();
                    if (tfSearch.getText() == null) {
                        addAllTableRows(deckName);
                    } else {
                        tableSearch();
                    }
                    btnKnownToggle.setText("Hide");
                }
            }
        });
        cbxDecks.addItem("all");

        tfSearch.setSize(575,50);
        tfSearch.setLocation(100, 50);
        tfSearch.setFont(fontLabel);
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e){

            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                tableSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tableSearch();
            }
        });
        pnlViewAll.add(tfSearch);

        btnKnownToggle.setSize(100, 50);
        btnKnownToggle.setLocation(700, 50);
        btnKnownToggle.setFont(fontButton);
        btnKnownToggle.setForeground(accentColour);
        btnKnownToggle.setBackground(backgroundColour);
        btnKnownToggle.addActionListener( AL->btnHide_Show_Toggle() );
        pnlViewAll.add(btnKnownToggle);

        scrollPaneTableView.setSize(700, 475);
        tableView.setFont(fontLabel);
        scrollPaneTableView.setLocation(100, 125);
        tableView.setRowHeight(tableView.getRowHeight() + 20);
        pnlViewAll.add(scrollPaneTableView);

        tableView.getColumnModel().getColumn(2).setCellRenderer(new trueFalseTableCellRenderer());
        tableView.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableView.rowAtPoint(evt.getPoint());
                int col = tableView.columnAtPoint(evt.getPoint());
                String selected = (tableView.getValueAt(row, col)).toString();
                if (col == 2) {
                    if (selected.equals("true")) {
                        tableView.setValueAt("false", row, col);
                    } else if (selected.equals("false")) {
                        tableView.setValueAt("true", row, col);
                    }
                    refreshKnown(Boolean.parseBoolean(tableView.getValueAt(row, col).toString()), tableView.getValueAt(row, 0).toString());
                    if(btnKnownToggle.getText().equals("Show")) {
                        ((DefaultTableModel)tableView.getModel()).removeRow(row);
                    }
                }
            }
        });

        addAllTableRows("wordsTotal");
    }

    public void initComponentsAdd() {
        pnlAddAll.setBackground(backgroundColour);

        btnAddAllConfirm.setSize(700, 50);
        btnAddAllConfirm.setBackground(backgroundColour);
        btnAddAllConfirm.setLocation(100,50);
        btnAddAllConfirm.setForeground(accentColour);
        btnAddAllConfirm.setFont(fontButton);
        btnAddAllConfirm.addActionListener( AL->btnProcess_Text_All() );

        btnAddDeckConfirm.setSize(200,50);
        btnAddDeckConfirm.setBackground(backgroundColour);
        btnAddDeckConfirm.setLocation(600,50);
        btnAddDeckConfirm.setForeground(accentColour);
        btnAddDeckConfirm.setFont(fontButton);
        btnAddDeckConfirm.addActionListener( AL->btnProcess_Text_Deck() );

        lblDeckName.setSize(150, 50);
        lblDeckName.setLocation(100,0);
        lblDeckName.setForeground(foregroundColour);
        lblDeckName.setFont(fontLabel);

        tfDeckName.setSize(450,50);
        tfDeckName.setLocation(100, 50);
        tfDeckName.setFont(fontLabel);
        tfDeckName.setCaretColor(foregroundColour);

        taEnter.setForeground(foregroundColour);
        taEnter.setBackground(backgroundColour);
        taEnter.setFont(fontLabel);
        taEnter.setCaretColor(foregroundColour);
        taEnter.setLineWrap(true);
        taEnter.setWrapStyleWord(true);
        scrollPaneEnter.setLocation(100, 125);
        scrollPaneEnter.setSize(700, 475);
        pnlAddAll.add(scrollPaneEnter);
    }

    public void initComponentsCompare() {
        pnlCompareKnown.setBackground(backgroundColour);

        btnAnalyse.setSize(250, 50);
        btnAnalyse.setBackground(backgroundColour);
        btnAnalyse.setLocation(550,50);
        btnAnalyse.setForeground(accentColour);
        btnAnalyse.setFont(fontButton);
        btnAnalyse.addActionListener( AL->btnAnalyse() );
        pnlCompareKnown.add(btnAnalyse);

        taAnalyse.setForeground(foregroundColour);
        taAnalyse.setBackground(backgroundColour);
        taAnalyse.setFont(fontLabel);
        taAnalyse.setCaretColor(foregroundColour);
        taAnalyse.setLineWrap(true);
        taAnalyse.setWrapStyleWord(true);
        scrollPaneAnalyse.setLocation(100, 125);
        scrollPaneAnalyse.setSize(700, 475);
        pnlCompareKnown.add(scrollPaneAnalyse);

        cbxAnalyseDecks.setSize(400,50);
        cbxAnalyseDecks.setLocation(100,50);
        cbxAnalyseDecks.setFont(fontLabel);
        cbxAnalyseDecks.setForeground(accentColour);
        cbxAnalyseDecks.setBackground(backgroundColour);
        cbxAnalyseDecks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cbxAnalyseDecks.getSelectedItem() != null) {
                    if(cbxAnalyseDecks.getSelectedItem().equals("Input")) {
                        taAnalyse.setBackground(backgroundColour);
                        taAnalyse.enable();
                    } else {
                        taAnalyse.setBackground(Color.decode("#121212"));
                        taAnalyse.disable();
                    }
                }
            }
        });
        pnlCompareKnown.add(cbxAnalyseDecks);

        fillComboBoxAnalyse();
    }

    public void btnView_All() {
        removePanel();

        tfSearch.setSize(575,50);
        tfSearch.setLocation(100, 50);

        cbxDecks.removeAllItems();
        cbxDecks.addItem("all");

        btnKnownToggle.setText("Hide");
        addAllTableRows("wordsTotal");

        pnlViewAll.remove(cbxDecks);

        pnlViewAll.show();
        myFrame.add(pnlViewAll);
        pnlViewAll.revalidate();
        myFrame.repaint();
    }

    public void btnView_Deck() {
        removePanel();

        cbxDecks.removeAllItems();
        fillComboBox();

        pnlViewAll.add(cbxDecks);

        tfSearch.setSize(225,50);
        tfSearch.setLocation(450, 50);

        pnlViewAll.show();
        myFrame.add(pnlViewAll);
        pnlViewAll.revalidate();
        myFrame.repaint();
    }

    public void btnAdd_All_Screen() {
        removePanel();

        btnAddAllConfirm.setSize(700, 50);
        btnAddAllConfirm.setLocation(100,50);

        pnlAddAll.add(btnAddAllConfirm);

        pnlAddAll.remove(lblDeckName);
        pnlAddAll.remove(tfDeckName);
        pnlAddAll.remove(btnAddDeckConfirm);

        addAllTableRows("wordsTotal");

        pnlAddAll.show();
        myFrame.add(pnlAddAll);
        pnlAddAll.revalidate();
        myFrame.repaint();
    }

    public void btnAdd_Deck_Screen() {
        removePanel();
        btnAddAllConfirm.setSize(200,50);
        btnAddAllConfirm.setLocation(600,50);

        pnlAddAll.remove(btnAddAllConfirm);

        pnlAddAll.add(lblDeckName);
        pnlAddAll.add(tfDeckName);
        pnlAddAll.add(btnAddDeckConfirm);

        pnlAddAll.show();
        myFrame.add(pnlAddAll);
        pnlAddAll.revalidate();
        myFrame.repaint();
    }

    public void btnCompare_Screen() {
        removePanel();

        pnlCompareKnown.show();
        myFrame.add(pnlCompareKnown);
        pnlCompareKnown.revalidate();
        myFrame.repaint();
    }

    public void removePanel() {
        Component[] components = myFrame.getContentPane().getComponents();

        components[1].hide();
        myFrame.remove(components[1]);
        myFrame.repaint();
    }

    public void btnProcess_Text_All() {
        wordsList.createTempList(analyseText(taEnter));
        wordsList.updateTotalArray();
        wordsList.sortArray();
        wordsList.writeWordsToFile("wordsTotal");
        taEnter.setText("");
    }

    public void btnProcess_Text_Deck() {
        String deckName = tfDeckName.getText();

        wordsList.createTempList(analyseText(taEnter));
        wordsList.sortArray();
        wordsList.checkKnown();
        wordsList.writeWordsToFile(deckName);
        wordsList.updateTotalArray();
        wordsList.sortArray();
        wordsList.writeWordsToFile("wordsTotal");
        deckList.clearArray();
        deckList.addDeckToList(deckName);

        deckList.writeDecksToFile();
        taEnter.setText("");
        tfDeckName.setText("");
    }

    public void btnAnalyse() {
        String selection = cbxAnalyseDecks.getSelectedItem().toString();
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        int[] numbers;
        if (selection.equals("Input")) {
            wordsList.createTempList(analyseText(taAnalyse));
            wordsList.checkKnown();
            numbers = wordsList.gatherKnownPercentage();
        } else if (selection.equals("Total")){
            wordsList.readWordsToArray("wordsTotal");
            numbers = wordsList.gatherKnownPercentage();
        } else {
            wordsList.readWordsToArray(selection);
            numbers = wordsList.gatherKnownPercentage();
        }
        float percentage = (float)numbers[0] / (float)numbers[1];
        JOptionPane.showMessageDialog(myFrame, "You know approximately " + twoDForm.format(percentage*100) + "% of your chosen area");
    }

    public String[] analyseText(JTextArea theTA) {
        String text = theTA.getText();
        //Get text, sanitise the input, split into each word, compare
        //each word with a list of words already inputted, if there is
        //a match then increment the frequency, if not then add the word
        //to the end of the list then set the frequency to 1
        text = text.toLowerCase().replace(",","").replace(".","").replace("/","").replace("'","").replace(";","").replace(":","").replace("+","").replace("=","").replace("_","").replace("2","").replace("1","").replace("!","")
        .replace("?","").replace(")","").replace("(","").replace("…","").replace("{","").replace("{","").replace("[","").replace("]","").replace("*","").replace("&","").replace("^","").replace("%","").replace("$","")
        .replace("£","").replace("`","").replace("~","").replace("#","").replace("\"","").replace("“","").replace("”","").replace("\n"," ").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "")
        .replace("6", "").replace("7", "").replace("8", "").replace("9", "").replace("0", "").replace("°","").replace("²","").replace("\t"," ").replace("—","").replace("‘","").replace("‘","");

        return text.split(" ");
    }

    public void addAllTableRows(String deckName) {
        wordsList.readWordsToArray(deckName);
        theTableModel.setRowCount(0);
        int myPosition = wordsList.position;
        for (int i = 0; i<myPosition; i++) {
            Words tempWord = wordsList.allWords[i];

            String[] rowData = new String[3];

            rowData[0] = tempWord.word;
            rowData[1] = Integer.toString(tempWord.frequency);
            rowData[2] = Boolean.toString(tempWord.known);

            theTableModel.addRow(rowData);
        }
        wordsList.allWords = new Words[99999];
    }

    public void fillComboBox() {
        deckList.readDecksToArray();
        for(int i = 0; i<deckList.position; i++) {
            cbxDecks.addItem(deckList.allDecks[i]);
        }
    }

    public void fillComboBoxAnalyse() {
        deckList.readDecksToArray();
        cbxAnalyseDecks.addItem("Input");
        cbxAnalyseDecks.addItem("Total");
        for(int i = 0; i<deckList.position; i++) {
            cbxAnalyseDecks.addItem(deckList.allDecks[i]);
        }
    }

    public void btnHide_Show_Toggle() {
        String state = btnKnownToggle.getText();
        String deckName = cbxDecks.getSelectedItem().toString();
        Words[] matchingWords;
        String searchTerm = tfSearch.getText();
        if (deckName.equals("all")) {
            deckName = "wordsTotal";
        }
        wordsList.readWordsToArray(deckName);
        if (searchTerm.isEmpty()) {
            if (state.equals("Hide")) {
                addAllTableRowsUnknown(wordsList.allWords, wordsList.position);
                btnKnownToggle.setText("Show");
            } else {
                addAllTableRows(deckName);
                btnKnownToggle.setText("Hide");
            }
        }
        else {
            if (state.equals("Hide")) {
                btnKnownToggle.setText("Show");
                matchingWords = wordsList.searchMultipleWords(searchTerm, deckName);
                addAllTableRowsUnknown(matchingWords, matchingWords.length);
            } else {
                btnKnownToggle.setText("Hide");
                tableSearch();
            }
        }
    }

    public void addAllTableRowsUnknown(Words[] words, int myPosition) {
        theTableModel.setRowCount(0);
        for (int i = 0; i<myPosition; i++) {
            Words tempWord = words[i];
            if (tempWord != null) {
                if(!tempWord.known) {
                    String[] rowData = new String[3];

                    rowData[0] = tempWord.word;
                    rowData[1] = Integer.toString(tempWord.frequency);
                    rowData[2] = "false";

                    theTableModel.addRow(rowData);
                }
            }
        }
        wordsList.allWords = new Words[99999];
    }

    public void refreshKnown(Boolean newKnown, String word) {
        //Always update the main file as if a word is in a deck it definitely exists here
        int pos = wordsList.searchForWord(word, "wordsTotal");
        wordsList.allWords[pos].known = newKnown;
        wordsList.writeWordsToFile("wordsTotal");
        deckList.readDecksToArray();
        //Check each deck to see if the changed word is there and if it is make the appropriate change to the known status
        for(int i = 0; i<deckList.position; i++) {
            pos = wordsList.searchForWord(word, deckList.allDecks[i]);
            if(pos != -1) {
                wordsList.allWords[pos].known = newKnown;
                wordsList.writeWordsToFile(deckList.allDecks[i]);
            }
        }
    }

    public void tableSearch() {
        String searchTerm = tfSearch.getText();
        String deckName = cbxDecks.getSelectedItem().toString();
        Words[] matchingWords;
        theTableModel.setRowCount(0);
        if(searchTerm.isEmpty()) {
            addAllTableRows("wordsTotal");
        }
        else {
            if (deckName.equals("all")) {
                matchingWords = wordsList.searchMultipleWords(searchTerm, "wordsTotal");
            } else {
                matchingWords = wordsList.searchMultipleWords(searchTerm, deckName);
            }
            int tempPos = 0;
            while (matchingWords[tempPos] != null) {
                Words tempWord = matchingWords[tempPos];

                String[] rowData = new String[3];

                rowData[0] = tempWord.word;
                rowData[1] = Integer.toString(tempWord.frequency);
                rowData[2] = Boolean.toString(tempWord.known);

                theTableModel.addRow(rowData);
                tempPos++;
            }
        }
    }

    public void createTextDirectory(){
        try {
            String jarFilePath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String jarDirectory = new File(jarFilePath).getParent();
            String directoryPath = jarDirectory + File.separator + "TextFiles";
            File newDirectory = new File(directoryPath);
            if (newDirectory.mkdir()) {
                System.out.println("Directory has been created successfully");
            }
            else {
                System.out.println("Directory cannot be created");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.createTextDirectory();
        //main.startProgram();
        main.initFrame();
    }
}