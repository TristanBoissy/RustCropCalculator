package Views;


import Hemp.CalculateBestCrop;
import Hemp.HempModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

/**
 * Author: Tristan Boissy
 * Creation date: 2021-04-23
 * Version: 1.2
 */


public class MainPanel extends JFrame implements Runnable {

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    static ArrayList<CropPanel> entryPanel = new ArrayList<>();
    private static ArrayList<CropPanel> backUpEntryPanel = new ArrayList<>();

    private final char GROWTH_RATE = 'G';
    private final char INCREASED_YIELD = 'Y';
    private final char INCREASED_HARDINESS = 'H';
    private final char INCREASED_WATER_INTAKE = 'W';
    private final char EMPTY = 'X';

    private static Object[] GENES_LIST = new Object[5];

    final private int SCREEN_SIZE_X = 800;
    final private int SCREEN_SIZE_Y = 800;
    final private int HORIZONTALE_PANEL_OFFSET = 45;
    private final static int NUMBER_OF_GENES = 6;

    //To keep track of the size of all the panels
    private int ScreenTotalSize = 0;
    private int AmountOfCrops = 0;

    private static MainPanel instance = new MainPanel();

    //I made it a general variable for all the methods to be able to use it
    // freely to make my life easier
    private JTextField CropTextField = new JTextField("Add a Crop");
    private EntryPanel DesiredHempPanel = new EntryPanel("Desired",GENES_LIST,
            Color.LIGHT_GRAY);
    private int PanelVerticalValue = 60;

    /**
     * Runs the interface
     */
    @Override
    public void run() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(entryPanel.size() != backUpEntryPanel.size()){
                    rebuildCropPanels();
                    revalidateFrame();
                    repaintFrame();
                }

            }
        }, 0,100);
        initFrame();
        this.getContentPane().setBackground(Color.DARK_GRAY);
        initGenesList();
        initAddCropPanel();
        //initPanels(6);
        initDesiredHempPanel();
        initCalculationButton();

        setVisible(true);
    }


    /*************
     * ACCESSORS *
     *************/
   public static int getNumberOfPanels(){
        return entryPanel.size();
   }

   public static MainPanel getInstance(){return instance;}

    /**
     * Returns a gene
     * @param index
     * @return The gene contained in the list at the specified index
     */
    public static Object getGenes(int index){

        return GENES_LIST[index];
    }


    /***********
     * METHODS *
     ***********/

    /**
     * Initiliaze the main Frame for the interface
     */
    private void initFrame(){

        this.setLayout(null);
        this.setSize(SCREEN_SIZE_X, SCREEN_SIZE_Y);
        this.setLocation(dim.width/2-this.getSize().width/2,
                            dim.height/2-this.getSize().height/2);

        // ajoute une gestion du EXIT par confirmation pop-up
        this.addWindowListener(new WindowAdapter() {

            // gestionnaire d'événement pour la fermeture
            public void windowClosing(WindowEvent we) {

                // ajoute une demande de confirmation
                int result = JOptionPane.showConfirmDialog(null,
                        "Do you want to quit?",
                        "Exit confirmation: ",
                        JOptionPane.YES_NO_OPTION);

                // si la réponse est oui
                if (result == JOptionPane.YES_OPTION){
                    //ferme la fenêtre en activant la gestion de l'événement
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else if (result == JOptionPane.NO_OPTION){
                    // sinon, désactive la gestion de l'événement
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    /**
     * Initiliaze the last hemp panel which is the desired one the user wants
     */
    private void initDesiredHempPanel() {
        DesiredHempPanel.setLocation(HORIZONTALE_PANEL_OFFSET,ScreenTotalSize);
        this.getContentPane().add(DesiredHempPanel);
        ScreenTotalSize += EntryPanel.getPANEL_SIZE_Y();
    }

    /**
     * Initiate the first panel of the frame, it adds a JTextField and a
     * button to add crops to the program
     */
    private void initAddCropPanel(){
        //Constants
        Rectangle JPanelBounds = new Rectangle(0,0,SCREEN_SIZE_X,PanelVerticalValue);
        Rectangle JTextFieldBounds = new Rectangle(75,10,200,40);

        //Creation of a JPanel and a JFieldText
        JPanel newCropPanel = new JPanel();

        //Setting the parameters for the TextField
        CropTextField.setSelectedTextColor(Color.GRAY);
        CropTextField.setHorizontalAlignment(JTextField.CENTER);
        CropTextField.setSize(200,10);
        CropTextField.setBounds(JTextFieldBounds);

        //Setting the parameters for the Panel
        newCropPanel.setLayout(null);
        newCropPanel.setBackground(Color.DARK_GRAY);
        newCropPanel.setBounds(JPanelBounds);
        newCropPanel.add(CropTextField);

        //Focus Listener for the panel that removes the text when clicked for
        // the first time
        CropTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                CropTextField.setText(null);
            }
            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        //Checks if the user input is a gene from the list allowed, if
        // another letter is pressed, a pop-up shows up that prevents the
        // user from typing the letter
        //It also checks for extra character, when above 6, a pop-up tells
        // the user the amount of genes has been exceeded
        CropTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String JTextFieldInput = getCropTextField();
                char JTextFieldLastInput =
                        JTextFieldInput.charAt(JTextFieldInput.length()-1);
                int asciiOffshift = 32;

                //Each inputs needs to be a good letter
                if(JTextFieldLastInput != GROWTH_RATE &&
                        JTextFieldLastInput != INCREASED_YIELD &&
                        JTextFieldLastInput != INCREASED_HARDINESS &&
                        JTextFieldLastInput != INCREASED_WATER_INTAKE &&
                        JTextFieldLastInput != EMPTY &&
                        JTextFieldLastInput != GROWTH_RATE + asciiOffshift &&
                        JTextFieldLastInput != INCREASED_YIELD + asciiOffshift &&
                        JTextFieldLastInput != INCREASED_HARDINESS + asciiOffshift &&
                        JTextFieldLastInput != INCREASED_WATER_INTAKE + asciiOffshift &&
                        JTextFieldLastInput != EMPTY + asciiOffshift
                ){
                    JOptionPane.showConfirmDialog(null,"Enter a gene with " +
                            "the right letters", "Crop alert:", JOptionPane.CLOSED_OPTION);
                }

                //You cannot exceed 6 genes
                if(JTextFieldInput.length() > NUMBER_OF_GENES){
                    JOptionPane.showConfirmDialog(null,
                            "Too many genes added to the crop",
                            "Crop alert: ",
                            JOptionPane.CLOSED_OPTION);
                }
            }
        });

        //Add components to the panel
        newCropPanel.add(initAddCropButton());
        newCropPanel.add(initCalculationButton());
        //Add the panel to the frame
        this.getContentPane().add(newCropPanel);

        ScreenTotalSize += PanelVerticalValue;
    }

    /**
     * Initiate the add button to add a crop to the created list
     * @return JButton
     */
    private JButton initAddCropButton() {

        //Setting up the button
        JButton AddCropButton = new JButton("Add Crop");
        AddCropButton.setSize(100, 50);
        AddCropButton.setBounds(300, 10, 100,40);

        //When the button is pressed, we verify if the requirement are met
        //It needs to have 6 correct genes
        //If there is less than 6, a pop-up tells the user that genes are
        // missing
        AddCropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(entryPanel.size() == 8){
                    JOptionPane.showConfirmDialog(null,"Maximum amount of " +
                                    "crops reached",
                            "Crop alert: ", JOptionPane.CLOSED_OPTION);
                    return;
                }
                else if(CropTextField.getText().length() < 6){
                    JOptionPane.showConfirmDialog(null,"Missing genes",
                            "Crop alert: ", JOptionPane.CLOSED_OPTION);
                }
                else if(CropTextField.getText().length() == 6){
                    createNewCropPanel(getCharArrayFromString(getCropTextField().toUpperCase()));
                }
            }
        });
        return AddCropButton;
    }

    /**
     * Creates a new Panel containing a list of 6 genes and add it the the frame
     * @param geneList
     */
    private void createNewCropPanel(char[] geneList){

        if(!verifyIfCropExists(geneList)){
            AmountOfCrops++;
            CropPanel newPanel = new CropPanel(AmountOfCrops,geneList,
                    ScreenTotalSize, AmountOfCrops);
            newPanel.setLocation(HORIZONTALE_PANEL_OFFSET,ScreenTotalSize);

            this.getContentPane().add(newPanel);
            revalidateFrame();
            repaintFrame();

            entryPanel.add(newPanel);
            updateBackUpEntryPanel();
            ScreenTotalSize += PanelVerticalValue;
        }
    }

    /**
     * Verify if a crop exists in the panel by comparing each gene
     * @param newCrop The new crop to add to the list and compare to the
     *                existing ones
     * @return true if it exists
     */
    private boolean verifyIfCropExists(char[] newCrop){
        for (int i = 0; i < entryPanel.size(); i++){
            for(int j = 0; j < NUMBER_OF_GENES; j++){
                if(entryPanel.get(i).getGene(j) != newCrop[j]){
                    break;
                }
                if(j == 5){
                    JOptionPane.showConfirmDialog(null,"Crop already exists",
                            "Crop alert:", JOptionPane.CLOSED_OPTION);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyIfSameCrop(Object[] newCrop, Object[] comparisonCrop){
        for(int j = 0; j < NUMBER_OF_GENES; j++){
            if(comparisonCrop[j] != newCrop[j]){
                break;
            }
            if(j == 5){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the genes contained inside the crop
     * It is always called from a listener of a crop, you dont have to tell
     * the method which one it is
     * @return
     */
    private String getCropTextField(){
        return CropTextField.getText();
    }

    /**
     * Returns each letter of a string in a char arraylist
     * @param string to convert into an arraylist
     * @return char list
     */
    private char[] getCharArrayFromString(String string){

        //Will usually be a 6 letter string, since it will be used for the genes
        //but i made sure i had more just in case
        char[] tempo = new char[100];

        for(int i = 0; i < string.length();i++){
            tempo[i] = string.charAt(i);
        }
        return tempo;
    }

    /**
     * Initiliaze all the panels the user wanted
     * *******************************************
     * This has been removed because it takes too long for the user to add
     * crops when starting the program. Used to be a pain to create crops

    private void initPanels(int numberOfEntry){

        for(int i = 0; i < numberOfEntry; i++){

            entryPanel.add(new EntryPanel(i,GENES_LIST));
            this.getContentPane().add(entryPanel.get(i));
            ScreenTotalSize += EntryPanel.getPANEL_SIZE_Y();
        }
    }
     */


    /**
     * Rebuilds the crop panels on the frame by deleting them all and adding
     * them back
     * Used to update the GUI from the user deleting a crop. It uses the
     * entryPanelBackUp to remove all the old panels and adds the entryPanel
     * which has already removed the panel by the user
     */
    private void rebuildCropPanels(){
        removeCropPanels();
        addCropPanels();
        updateBackUpEntryPanel();
    }

    /**
     * Removes all the panels once added to the frame
     */
    private void removeCropPanels(){
        for(int i = 0; i < backUpEntryPanel.size(); i++){
            this.getContentPane().remove(backUpEntryPanel.get(i));
            this.ScreenTotalSize -= PanelVerticalValue;
        }
    }

    /**
     * Adds all the panels remaining to the frame
     */
    private void addCropPanels(){
        AmountOfCrops = entryPanel.size();
        for(int i = 0; i < entryPanel.size(); i++){
            entryPanel.get(i).setCropNumber(i+1);
            System.out.println(entryPanel.get(i).getCropNumber());
            entryPanel.get(i).setVerticalLocation(ScreenTotalSize);
            this.getContentPane().add(entryPanel.get(i));
            ScreenTotalSize += PanelVerticalValue;
        }
    }

    /**
     * Update the backUp with the new entryPanel
     */
    private void updateBackUpEntryPanel(){
        backUpEntryPanel.clear();
        backUpEntryPanel.addAll(entryPanel);
    }

    /**
     * Removes a crop from the entryPanel list
     * @param index
     */
    public static void removeCropFromList(int index){
        entryPanel.remove(index-1);
    }

    /**
     * Revalidates the frame
     */
    private void revalidateFrame(){
        this.revalidate();

    }

    /**
     * Repaint the frame
     */
    private void repaintFrame(){
        this.repaint();
    }



    /**
     * Initiliaze the button that will start the algorithm and places it at
     * right after all the panels
     */
    private JButton initCalculationButton() {

        //Setting up the button
        JButton CalculateButton = new JButton("Calculate");
        CalculateButton.setSize(100,40);
        CalculateButton.setLocation(550,10);

        //When the button is pressed, it initiate the calculation by calling
        // the function that initate the crops
        CalculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initiateCalculation();
            }
        });
        return CalculateButton;
    }


    /**
     * When this function is called, it means that the button has been
     * pressed and we start gattering each box information to create a new
     * hemp model for each panel and we calculate the best outcome from those
     * crops
     */
    private void initiateCalculation(){

        if(entryPanel.size() < 2){
            JOptionPane.showConfirmDialog(null,"Not enough crop to compare " +
                            "anything",
                    "Crop alert:", JOptionPane.CLOSED_OPTION);
            return;
        }
        Object[] genes = new Object[6];
        CalculateBestCrop.clearHempList();
        //Get all the genes from the list and create a new hemp for each one
        for(int i = 0; i < entryPanel.size();i++){
            for(int j = 0; j < 6; j++){
                genes[j] = entryPanel.get(i).getGene(j);
            }

            CalculateBestCrop.addHempToList(new HempModel(genes));
            genes = new Object[6];
        }

        //Get the wanted result from the desired section and initiate the
        // calculation around it
        for(int j = 0; j < EntryPanel.getNUMBER_OF_GENES();j++){
            genes[j] = DesiredHempPanel.getValueFromSpinner(j);
        }
        new CalculateBestCrop(genes);
    }

    /**
     * Initiliaze the list of genes for the game
     */
    private void initGenesList(){

        GENES_LIST[0] = EMPTY;
        GENES_LIST[1] = GROWTH_RATE;
        GENES_LIST[2] = INCREASED_HARDINESS;
        GENES_LIST[3] = INCREASED_WATER_INTAKE;
        GENES_LIST[4] = INCREASED_YIELD;
    }





    /**********************
     *     CROP PANEL     *
     *********************/

    /**
     * Author: Tristan Boissy
     * Date: 2021-06-10
     * Version: 1.1
     */

    /**
     * Subclass that creates a crop Panel everytime a new crop is added to
     * the list
     */
    public static class CropPanel extends JPanel {

        /*************
         * CONSTANTS *
         ************/

        private final static int PANEL_SIZE_X = 700;
        private final static int PANEL_SIZE_Y = 60;
        private final static int NUMBER_OF_GENES = 6;

        private final char GROWTH_RATE = 'G';
        private final char INCREASED_YIELD = 'Y';
        private final char INCREASED_HARDINESS = 'H';
        private final char INCREASED_WATER_INTAKE = 'W';
        private final char EMPTY = 'X';

        private JLabel HEMP_TITLE = new JLabel();
        private char[] GENES_LIST = new char[6];
        private int VerticalLocation;
        private int CROP_NUMBER;

        /***************
         * CONSTRUCTOR *
         ***************/

        public CropPanel(int cropNumber, char[] genesList, int verticalLocation,
                         int index){

            HEMP_TITLE.setText("Hemp: " + cropNumber);
            HEMP_TITLE.setForeground(Color.WHITE);
            GENES_LIST = genesList;
            VerticalLocation = verticalLocation;
            CROP_NUMBER = index;

            initPanel();
            initComponents(genesList);
            this.setVisible(true);

        }

        /*************
         * ACCESSORS *
         *************/

        public char getGene(int index){return GENES_LIST[index];}
        public char[] getGenes(){return GENES_LIST;}
        public int getCropNumber(){return CROP_NUMBER;}
        public int getVerticalLocation(){return VerticalLocation;}

        public void setCropNumber(int number){
            this.CROP_NUMBER = number;
            this.HEMP_TITLE.setText("Hemp: " + number);
        }
        public void setVerticalLocation(int newLocation){
            this.setLocation(45,newLocation);
            this.VerticalLocation = newLocation;
        }


        private void initPanel(){

            this.setLayout(new GridLayout(1,8));
            this.setSize(PANEL_SIZE_X, PANEL_SIZE_Y);
            this.setBackground(Color.DARK_GRAY);
            this.setBorder(BorderFactory.createEtchedBorder());

            this.add(HEMP_TITLE);
        }

        /***********
         * METHODS *
         ***********/

        /**
         * Initiate the components required for this panel
         * @param genesList
         */
        private void initComponents(char[] genesList){

            for(int i = 0; i < 6; i++){
                JLabel tempo = new JLabel();
                tempo.setBackground(Color.DARK_GRAY);
                tempo.setText(String.valueOf(genesList[i]).toUpperCase());
                int asciiOffshift = 32;

                if(genesList[i] == GROWTH_RATE ||
                        genesList[i] == INCREASED_YIELD ||
                        genesList[i] == INCREASED_HARDINESS ||
                        genesList[i] == GROWTH_RATE + asciiOffshift ||
                        genesList[i] == INCREASED_YIELD + asciiOffshift ||
                        genesList[i] == INCREASED_HARDINESS + asciiOffshift){
                    tempo.setForeground(Color.GREEN);
                }
                else{
                    tempo.setForeground(Color.RED);
                }

                this.add(tempo);
            }
            initRemovePanelButton();

        }

        /**
         * Initiate the button that allows a panel to be removed
         */
        private void initRemovePanelButton(){
            int buttonSize = 50;
            JPanel exitPanel = new JPanel();
            exitPanel.setBackground(Color.DARK_GRAY);
            exitPanel.setLayout(null);
            JButton removePanelButton = new JButton("X");
            removePanelButton.setBackground(Color.DARK_GRAY);
            removePanelButton.setBorder(null);
            removePanelButton.setForeground(Color.LIGHT_GRAY);
            removePanelButton.setContentAreaFilled(false);
            removePanelButton.setSize(45,40);
            removePanelButton.setLocation(5, 7);

            removePanelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeCropFromList(CROP_NUMBER);
                }
            });

            exitPanel.add(removePanelButton);
            this.add(exitPanel);

        }

        /**
         * Compare an object to this one and return true if it is the same
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CropPanel cropPanel = (CropPanel) o;
            return VerticalLocation == cropPanel.VerticalLocation && CROP_NUMBER == cropPanel.CROP_NUMBER && GROWTH_RATE == cropPanel.GROWTH_RATE && INCREASED_YIELD == cropPanel.INCREASED_YIELD && INCREASED_HARDINESS == cropPanel.INCREASED_HARDINESS && INCREASED_WATER_INTAKE == cropPanel.INCREASED_WATER_INTAKE && EMPTY == cropPanel.EMPTY && Objects.equals(HEMP_TITLE, cropPanel.HEMP_TITLE) && Arrays.equals(GENES_LIST, cropPanel.GENES_LIST);
        }

    }




    /**********************
     *     ENTRY PANEL    *
     *********************/

    /**
     * Author: Tristan Boissy
     * Date: 2021-04-23
     * Version: 1.0
     */

    /**
     * subclass created for the desired gene with a spinner
     */

    public static class EntryPanel extends JPanel {

        private final static int PANEL_SIZE_X = 700;
        private final static int PANEL_SIZE_Y = 60;
        private final static int NUMBER_OF_GENES = 6;
        private JLabel HEMP_TITLE = new JLabel();


        private ArrayList<JSpinner> genes = new ArrayList<>();

        /**
         * Creates a new hemp panel
         * @param number Hemp number in the list
         * @param genesList Contains a list of 5 genes
         */
        public EntryPanel(int number, Object[] genesList){

            HEMP_TITLE.setText("Hemp: " + (1 + number));
            HEMP_TITLE.setForeground(Color.WHITE);

            initPanel(number);
            initComponents(genesList);
            this.setVisible(true);
        }

        /**
         * Creates a new hemp panel
         * @param text Is the title of the hemp
         * @param genesList Contains a list of 5 genes
         * @param color Color of the border
         */
        public EntryPanel(String text, Object[] genesList, Color color){

            HEMP_TITLE.setText(text);
            HEMP_TITLE.setForeground(Color.WHITE);

            initPanel(getNumberOfPanels());
            initComponents(genesList);
            this.setBorder(null);
            this.setVisible(true);
        }

        /*************
         * ACCESSORS *
         *************/

        public static int getPANEL_SIZE_X(){
            return PANEL_SIZE_X;
        }

        public static int getPANEL_SIZE_Y(){
            return PANEL_SIZE_Y;
        }

        public static int getNUMBER_OF_GENES(){
            return NUMBER_OF_GENES;
        }

        /***********
         * METHODS *
         ***********/

        /**
         * Initialize the panel and sets the location depending on the amount of
         * panel already existing
         * @param numberOfEntry
         */
        private void initPanel(int numberOfEntry){

            this.setLayout(null);
            this.setSize(PANEL_SIZE_X, PANEL_SIZE_Y);
            this.setLocation(50,numberOfEntry* PANEL_SIZE_Y);
            this.setBackground(Color.DARK_GRAY);
            this.setBorder(BorderFactory.createEtchedBorder());
        }

        /**
         * Initiliaze all the components needed for a hemp panel
         * @param SpinnerList
         */
        private void initComponents(Object[] SpinnerList){

            HEMP_TITLE.setSize(50,50);
            HEMP_TITLE.setLocation(5,5);

            this.add(HEMP_TITLE);

            for(int i = 0; i < NUMBER_OF_GENES; i++){

                JSpinner spinner = new JSpinner(new CustomSpinner(SpinnerList));
                spinner.setBounds(0,0,50,40);
                spinner.setLocation(PANEL_SIZE_X / NUMBER_OF_GENES *i+60,10);

                this.add(spinner);

                genes.add(spinner);
            }
        }


        /**
         * Returns the value inside of a spinner box for this panel
         * @param index of the box
         * @return Gene letter
         */
        public Object getValueFromSpinner(int index){
            return genes.get(index).getValue();
        }


        /**
         * Custom class found on internet to create a custom spinner list
         * Adapted to cycle through all 5 genes starting from X
         * http://www.java2s.com/Tutorials/Java/Swing_How_to/JSpinner/Create_Custom_Cycling_Spinner_Model.htm
         */
        public static class CustomSpinner extends SpinnerListModel {

            SpinnerModel linkedModel = null;

            public CustomSpinner(Object[] genesList){
                super(genesList);
            }


            public Object getNextValue() {

                Object value = super.getNextValue();

                if (value == null) {
                    value = getGenes(0);

                    if (linkedModel != null) {
                        linkedModel.setValue(linkedModel.getNextValue());
                    }
                }
                return value;
            }

            public Object getPreviousValue() {
                Object value = super.getPreviousValue();

                if (value == null) {
                    value = getGenes(4);

                    if (linkedModel != null) {
                        linkedModel.setValue(linkedModel.getPreviousValue());
                    }
                }
                return value;
            }
        }
    }
}
