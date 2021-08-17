package Tests;

import Hemp.HempModel;

/**
 * Author: Tristan Boissy
 * Date: 2021-04-23
 * Version: 1.0
 */

public class HempModelTest {

    private static final char GROWTH_RATE = 'G';
    private static final char INCREASED_YIELD = 'Y';
    private static final char INCREASED_HARDINESS = 'H';
    private static final char INCREASED_WATER_INTAKE = 'W';
    private static final char EMPTY = 'X';

    private static Object[] GENES_LIST = new Object[5];


    public static void main(String[] args) {

        initGenesList();

        HempModel newHemp = new HempModel(GENES_LIST);

        for(int i = 0; i < 5; i++){
            System.out.println(newHemp.getGeneFromList(i));
        }

    }


    /**
     * Initiliaze the list of genes for the game
     */
    private static void initGenesList(){

        GENES_LIST[0] = EMPTY;
        GENES_LIST[1] = INCREASED_YIELD;
        GENES_LIST[2] = INCREASED_HARDINESS;
        GENES_LIST[3] = INCREASED_WATER_INTAKE;
        GENES_LIST[4] = GROWTH_RATE;
    }
}
