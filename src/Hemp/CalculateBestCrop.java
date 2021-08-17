package Hemp;

import java.util.ArrayList;

/**
 * Author: Tristan Boissy
 * Date: 2021-04-23
 * Version: 1.0
 */


/**
 * This class calculates all the possible crop and try to match it with
 * the wanted crop the user specified
 * This class uses a brute force method and try to match every result with
 * the wanted one. A percentage is also calculated depending on the amount of
 * 1/2 and 1/3 results. All of the matching results are sent back to the
 * user even if they are not optimal.
 */

/**
 * The next version will let the user add more than 8 plants so he doesnt
 * have to restart the program everytime and retype each hemp to replace only
 * one of them
 */

public class CalculateBestCrop {

    private final static char GROWTH_RATE = 'G';
    private final static char INCREASED_YIELD = 'Y';
    private final static char INCREASED_HARDINESS = 'H';
    private final static char INCREASED_WATER_INTAKE = 'W';
    private final static char EMPTY = 'X';

    private final static int MAXIMUM_NUMBER_OF_HEMP = 8;
    private final static int MAXIMUM_NUMBER_OF_GENES = 6;

    private static ArrayList<HempModel> hempList = new ArrayList<>();
    private static Object[][] genes;
    private static char[][] genesResult =
            new char[MAXIMUM_NUMBER_OF_GENES][3];
    private static ArrayList<char[]> results = new ArrayList<>();
    private static HempModel wantedHemp;
    private static double possiblePercentage;


    public CalculateBestCrop(Object[] wantedGenes){
        results.clear();
        wantedHemp = new HempModel(wantedGenes);
        int index = checkIfDesiredAlreadyExists();
        if(index != -1){
            System.out.println(index);
            return;
        }
        tryEveryPossibleOutcome(wantedGenes);
    }

    public static int getHempListSize(){return hempList.size();}

    public static HempModel getHempModelFromList(int index){return hempList.get(index);}

    public static void clearHempList(){hempList.clear();}

    /**
     * We add each hemp to a list here so we can use them easily
     * @param newHemp New HempModel to add to the list
     */
    public static void addHempToList(HempModel newHemp){
        hempList.add(newHemp);
    }

    /**
     * Create a matrice containing all the genes of each hemp
     * @param hempListSize Number of hemp in the list
     */
    private static void createGenesMatrice(int hempListSize){
        genes = new Object[MAXIMUM_NUMBER_OF_GENES][hempListSize];
    }

    /**
     * Function returning the size of a list [] that has an end point as -1
     * If -1 is found, we return the number of iteration done
     * @param list that we wish to count the size
     * @return The size of the list (int)
     */
    private static int getListSize(int[] list){
        int i = 0;
        while(list[i] != -1){
            i++;
        }
        return i;
    }

    private int checkIfDesiredAlreadyExists(){
        for(int i = 0; i < hempList.size(); i++){
            if(hempList.get(i).getAmountOfG_Genes() == wantedHemp.getAmountOfG_Genes() &&
                    hempList.get(i).getAmountOfY_Genes() == wantedHemp.getAmountOfY_Genes() &&
                    hempList.get(i).getAmountOfH_Genes() == wantedHemp.getAmountOfH_Genes() &&
                    hempList.get(i).getAmountOfW_Genes() == wantedHemp.getAmountOfW_Genes() &&
                    hempList.get(i).getAmountOfX_Genes() == wantedHemp.getAmountOfX_Genes()){
                return i;
            }
        }
        return -1;
    }


    /**
     * Check if the letter received is part of the gene list
     * @param gene Letter referencing a specific gene (G,Y,H,W,X)
     * @return true if the gene does exist
     */
    private static boolean isAGene(char gene){

        if(gene != GROWTH_RATE &&
                gene != INCREASED_YIELD &&
                gene != INCREASED_HARDINESS &&
                gene != INCREASED_WATER_INTAKE &&
                gene != EMPTY){
            return false;
        }
        return true;
    }

    /**
     * Calculates all the possible outcome of coupling a specific list of hemp
     * @param listToTry List of hemp to try (needs to be in the hempList)
     *                  containing numbers referencing the index of each hemp
     *                  in the hempList
     */
    public static void getCouplingResult(int[] listToTry){

        results.clear();
        possiblePercentage = 100;
        int size = getListSize(listToTry);
        createGenesMatrice(size);


        for(int i = 0; i < MAXIMUM_NUMBER_OF_GENES; i++){
            int j = 0;
            while(listToTry[j] != -1){
                genes[i][j] = hempList.get(listToTry[j]).getGeneFromList(i);
                j++;
            }

            char[] couplingResult = new char[3];
            couplingResult = getCouplingResultOfOneGene(genes[i]);

            for(int z = 0; z < 3; z++){
                genesResult[i][z] = couplingResult[z];
            }
        }

        writePossibleOutcome(genesResult);
    }


    /**
     * Verify if the outcome match the wanted one
     * @return true if it is a match
     */
    private static boolean verifyOutcomes(){

        int[] genesResultCount;

        //Looks into every result calculated in the list of results
        for(int i = 0; i < results.size(); i++){
            genesResultCount = countGenesFromResult(i);

            if(genesResultCount[0] == wantedHemp.getAmountOfG_Genes()
            && genesResultCount[1] == wantedHemp.getAmountOfY_Genes()
            && genesResultCount[2] == wantedHemp.getAmountOfH_Genes()
            && genesResultCount[3] == wantedHemp.getAmountOfW_Genes()
            && genesResultCount[4] == wantedHemp.getAmountOfX_Genes()){
                return true;
            }
        }

        return false;
    }


    /**
     * Writes all the possible results of a specific matching case.
     * It writes the results that has been calculating previously and passed
     * in as a parameter
     * @param genesResult Matrice contaning the result calculated
     */
    private static void writePossibleOutcome(char[][] genesResult){

        int j;

        for(int i = 0; i < 6; i++){

            //A coupled gene can have 3 possible outcome, so we iterate 3 times
            for(j = 0; j < 3; j++){

                //If it isnt a gene, we wrote the possible outcome of this gene
                if(!isAGene(genesResult[i][j])){
                    break;
                }

                //Only for the first gene, we create a temporary variable
                // that will store the result of the first gene at the first
                // index as a starting point for each possible outcome.
                //We can have up to 3 starting point depending on the coupled
                // hemp
                if(i == 0){
                    char[] temp = new char[6];
                    temp[i] = genesResult[i][j];
                    results.add(temp);
                }
            }

            //If there is only one outcome for the gene in the result, we
            // just add the new gene to the already existing results
            if(j == 1 && i != 0){
                addNewGeneToResults(0, results.size(),i,0);
            }
            //If the are two outcomes for the gene, we double the results list
            //to create 2 new different outcomes for the existing results
            else if(j == 2 && i != 0){
                possiblePercentage /= 2;
                doubleResultsArrayList();
                addNewGeneToResults(0,results.size()/2,i,0);
                addNewGeneToResults(results.size()/2,results.size(),i,1);
            }
            //If the are three outcomes for the gene, we triple the results
            // list to create 3 new different outcomes for the existing results
            else if(j == 3 && i != 0){
                possiblePercentage /= 3;
                tripleResultsArrayList();
                addNewGeneToResults(0,results.size()/3,i,0);
                addNewGeneToResults(results.size()/3,results.size()/3*2,i,1);
                addNewGeneToResults(results.size()/3*2,results.size(),i,2);
            }
        }
    }


    /**
     * Adds a new gene to a specific area of the results list being built
     * @param start Starting point where we wish to add the gene
     * @param finish Finishing point where we wish to stop adding the gene
     * @param geneLine The line where the gene is located in the genesResult
     *                 matrice
     * @param geneIndex The index where of the gene in the genesResult matrice
     */
    private static void addNewGeneToResults(int start, int finish, int geneLine,
                                            int geneIndex){

        //Goes through the specified area of the list
        for(int i = start; i < finish; i++){

            char[] temp = results.get(i);
            char[] newResult = new char[6];

            //Iterate through all the genes
            for(int j = 0; j < 6; j++){

                newResult[j] = temp[j];

                //When we dont find a gene in the new result hemp, we add the
                // new specified one and we replace the old result with the
                // the new updated result in the list
                if(!isAGene(temp[j])){
                    newResult[j] = genesResult[geneLine][geneIndex];
                    results.set(i,newResult);
                    break;
                }
            }
        }
    }

    /**
     * Counts the number of genes in a specific result
     * @param index of the result in the list of result
     * @return Array containing a number for each genes in order
     */
    private static int[] countGenesFromResult(int index){

        int amountOfG_Genes = 0;
        int amountOfY_Genes = 0;
        int amountOfH_Genes = 0;
        int amountOfW_Genes = 0;
        int amountOfX_Genes = 0;
        int[] genes = new int[5];

        //Check each genes of the result
        for(int i = 0; i < 6; i++){
            switch(results.get(index)[i]){
                case GROWTH_RATE: amountOfG_Genes++; break;
                case INCREASED_YIELD: amountOfY_Genes++; break;
                case INCREASED_HARDINESS: amountOfH_Genes++; break;
                case INCREASED_WATER_INTAKE: amountOfW_Genes++; break;
                case EMPTY: amountOfX_Genes++; break;
            }
        }

        //This is how i formated the result, if an index is wrong, the whole
        // count will also be
        genes[0] = amountOfG_Genes;
        genes[1] = amountOfY_Genes;
        genes[2] = amountOfH_Genes;
        genes[3] = amountOfW_Genes;
        genes[4] = amountOfX_Genes;

        return genes;
    }

    /**
     * Doubles the array of results
     */
    private static void doubleResultsArrayList(){

        int size = results.size();

        for(int i = 0; i < size; i++){
            results.add(results.get(i));
        }
    }

    /**
     * Triples the array of results
     */
    private static void tripleResultsArrayList() {

        doubleResultsArrayList();

        for(int i = 0; i < results.size()/2; i++){
            results.add(results.get(i));
        }
    }

    /**
     * Function trying every possible outcome up to 8
     * Goes through all the possible ways of coupling each hemp
     * @param expectedOutcome is the wanted outcome the user which to get at
     *                        the end
     */
    public static void tryEveryPossibleOutcome(Object[] expectedOutcome){

        int listSize = hempList.size();
        int[] listToTry = new int[9];


        for(int i = 0; i < listSize; i++){
            listToTry[0] = i;

            if(i < 7){
                for(int j = i+1; j < listSize; j++){
                    if(j == listSize){break;}
                    listToTry[1] = j;
                    listToTry[2] = -1;
                    getCouplingResult(listToTry);
                    if(verifyOutcomes()){
                        System.out.println(i + " " + j + " with " +
                                possiblePercentage + "%");
                    }

                    if(listSize > 2 && i < 6){
                        for(int k = j+1; k < listSize; k++){
                            if(k == listSize){break;}
                            listToTry[2] = k;
                            listToTry[3] = -1;
                            getCouplingResult(listToTry);
                            if(verifyOutcomes()){
                                System.out.println(i + " " + j + " " + k + " with " + possiblePercentage + "%");
                            }

                            if(listSize > 3 && i < 5){
                                for(int a = k+1; a < listSize; a++){
                                    if(a == listSize){break;}
                                    listToTry[3] = a;
                                    listToTry[4] = -1;
                                    getCouplingResult(listToTry);
                                    if(verifyOutcomes()) {
                                        System.out.println(i + " " + j + " " + k + " " + a + " with " + possiblePercentage + "%");
                                    }
                                    if(listSize > 4 && i < 4){
                                        for(int l = a+1; l < listSize; l++){
                                            if(l == listSize){break;}
                                            listToTry[4] = l;
                                            listToTry[5] = -1;
                                            getCouplingResult(listToTry);
                                            if(verifyOutcomes()) {
                                                System.out.println(i + " " + j +
                                                        " " + k + " " + a + " " + l + " with " + possiblePercentage + "%");
                                            }

                                            if(listSize > 5 && i < 3){
                                                for(int m = l+1; m < listSize; m++){
                                                    if(m == listSize){break;}
                                                    listToTry[5] = m;
                                                    listToTry[6] = -1;
                                                    getCouplingResult(listToTry);
                                                    if(verifyOutcomes()) {
                                                        System.out.println(i + " " + j +
                                                                " " + k + " " + a + " " + l + " " + m + " with " + possiblePercentage + "%");
                                                    }

                                                    if (listSize > 6 && i < 2){
                                                        for(int n = m +1; m < listSize; n++){
                                                            if(n == listSize){break;}
                                                            listToTry[6] = n;
                                                            listToTry[7] = -1;
                                                            getCouplingResult(listToTry);
                                                            if(verifyOutcomes()) {
                                                                System.out.println(i + " " + j +
                                                                        " " + k + " " + a + " " + l + " " + m + " " + n + " with " + possiblePercentage + "%");
                                                            }

                                                            if(listSize > 7 && i < 1 && n != 7){
                                                                listToTry[7] = 7;
                                                                listToTry[8] = -1;
                                                                getCouplingResult(listToTry);
                                                                if(verifyOutcomes()) {
                                                                    System.out.println(i + " " + j +
                                                                            " " + k + " " + a + " " + l + " " + m + " " + n + " " + 7 + " with " + possiblePercentage + "%");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Calculates the outcomes of matching the same gene of multiple hemp
     * together
     * @param Genes List of genes that we wish to match
     * @return The results in a list, containing the possible genes
     */
    public static char[] getCouplingResultOfOneGene(Object[] Genes){
        /*
        * Strategy: Check every possible ways a gene can match starting with
        * the dominant genes.
         */

        int G_gene = 0;
        int Y_gene = 0;
        int H_gene = 0;
        int W_gene = 0;
        int X_gene = 0;

        char[] result = new char[3];

        //Counts every genes
        for(int i = 0; i < Genes.length; i++){
            switch((char) Genes[i]){
                case GROWTH_RATE: G_gene++; break;
                case INCREASED_YIELD: Y_gene++; break;
                case INCREASED_HARDINESS: H_gene++; break;
                case INCREASED_WATER_INTAKE: W_gene++; break;
                case EMPTY: X_gene++; break;
            }
        }

        //The W gene has the biggest count or equal to the recessive genes
        if(W_gene >= G_gene &&
                W_gene >= Y_gene &&
                W_gene >= H_gene &&
                W_gene > X_gene ){

            result[0] = INCREASED_WATER_INTAKE;
        }
        //The X gene has the biggest count or equal to the recessive genes
        else if(X_gene >= G_gene &&
                X_gene >= Y_gene &&
                X_gene >= H_gene &&
                X_gene > W_gene ){

            result[0] = EMPTY;
        }
        //If we have the same amount of W and X genes, and that this amount
        // is bigger or equal to the amount of the other genes
        else if(W_gene == X_gene &&
                W_gene >= H_gene &&
                W_gene >= Y_gene &&
                W_gene >= G_gene){

            result[0] = INCREASED_WATER_INTAKE;
            result[1] = EMPTY;
        }
        //The G gene has the biggest count
        else if(G_gene > Y_gene &&
                G_gene > H_gene &&
                G_gene > W_gene &&
                G_gene > X_gene ){

            result[0] = GROWTH_RATE;
        }
        //The Y gene has the biggest count
        else if(Y_gene > G_gene &&
                Y_gene > H_gene &&
                Y_gene > W_gene &&
                Y_gene > X_gene ){

            result[0] = INCREASED_YIELD;
        }
        //The H gene has the biggest count
        else if(H_gene > G_gene &&
                H_gene > Y_gene &&
                H_gene > W_gene &&
                H_gene > X_gene ){

            result[0] = INCREASED_HARDINESS;
        }
        //All 3 recessive genes has the same count
        else if(G_gene == Y_gene && G_gene == H_gene){
            result[0] = GROWTH_RATE;
            result[1] = INCREASED_HARDINESS;
            result[2] = INCREASED_YIELD;

        }
        //G and H gene have the same count
        else if(G_gene == H_gene){
            result[0] = GROWTH_RATE;
            result[1] = INCREASED_HARDINESS;
        }
        //H and Y gene have the same count
        else if(H_gene == Y_gene){
            result[0] = INCREASED_YIELD;
            result[1] = INCREASED_HARDINESS;
        }
        //Y and G gene have the same count
        else if(Y_gene == G_gene){
            result[0] = INCREASED_YIELD;
            result[1] = GROWTH_RATE;
        }

        return result;
    }
}
