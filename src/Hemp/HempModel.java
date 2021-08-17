package Hemp;

/**
 * Author: Tristan Boissy
 * Date: 2021-04-23
 * Version: 1.0
 */

public class HempModel {

    private Object[] genesList = new Object[6];

    private final static char GROWTH_RATE = 'G';
    private final static char INCREASED_YIELD = 'Y';
    private final static char INCREASED_HARDINESS = 'H';
    private final static char INCREASED_WATER_INTAKE = 'W';
    private final static char EMPTY = 'X';

    private int amountOfG_Genes = 0;
    private int amountOfY_Genes = 0;
    private int amountOfH_Genes = 0;
    private int amountOfW_Genes = 0;
    private int amountOfX_Genes = 0;


    /**
     * Constructor
     * @param genes List of genes for the hemp
     */
    public HempModel(Object[] genes){

        genesList = genes;
        updateCountGenes();
    }

    /**
     * Returns the gene for the specified index
     * @param index
     * @return A gene as a letter
     */
    public char getGeneFromList(int index){
        return (char) genesList[index];
    }

    public Object[] getGenes(){return genesList;}





    public static boolean isDominantGene(Object gene){
        if((char) gene == 'W' || (char) gene == 'X') {return true;}

        return false;
    }

    public void updateCountGenes(){
        for(int i = 0; i < genesList.length; i++){
            switch((char) genesList[i]){
                case GROWTH_RATE: amountOfG_Genes++; break;
                case INCREASED_YIELD: amountOfY_Genes++; break;
                case INCREASED_HARDINESS: amountOfH_Genes++; break;
                case INCREASED_WATER_INTAKE: amountOfW_Genes++; break;
                case EMPTY: amountOfX_Genes++; break;
            }
        }
    }

    public void setAmountOfG_Genes(int number){amountOfG_Genes = number;}
    public int getAmountOfG_Genes(){return amountOfG_Genes;}

    public void setAmountOfH_Genes(int number){amountOfH_Genes = number;}
    public int getAmountOfH_Genes(){return amountOfH_Genes;}

    public void setAmountOfY_Genes(int number){amountOfY_Genes = number;}
    public int getAmountOfY_Genes(){return amountOfY_Genes;}

    public void setAmountOfW_Genes(int number){amountOfW_Genes = number;}
    public int getAmountOfW_Genes(){return amountOfW_Genes;}

    public void setAmountOfX_Genes(int number){amountOfX_Genes = number;}
    public int getAmountOfX_Genes(){return amountOfX_Genes; }


}


