package Tests;
import Hemp.CalculateBestCrop;
import Hemp.HempModel;

public class CouplingTests {
    public static void main(String[] args) {
        Object[] couplingTest = new Object[8];
        char[] result;

        couplingTest[0] = 'G';
        couplingTest[1] = 'Y';
        couplingTest[2] = 'G';
        couplingTest[3] = 'H';
        couplingTest[4] = 'Y';
        couplingTest[5] = 'H';
        couplingTest[6] = 'X';
        couplingTest[7] = 'X';

        //result = CalculateBestCrop.getCouplingResultOfOneGene(couplingTest);
        //System.out.println(result);

        Object[] Hemp1 = new Object[6];
        Hemp1[0] = 'G';
        Hemp1[1] = 'W';
        Hemp1[2] = 'H';
        Hemp1[3] = 'Y';
        Hemp1[4] = 'H';
        Hemp1[5] = 'H';

        Object[] Hemp2 = new Object[6];
        Hemp2[0] = 'G';
        Hemp2[1] = 'W';
        Hemp2[2] = 'H';
        Hemp2[3] = 'H';
        Hemp2[4] = 'H';
        Hemp2[5] = 'W';

        Object[] Hemp3 = new Object[6];
        Hemp3[0] = 'G';
        Hemp3[1] = 'W';
        Hemp3[2] = 'Y';
        Hemp3[3] = 'H';
        Hemp3[4] = 'H';
        Hemp3[5] = 'W';

        Object[] Hemp4 = new Object[6];
        Hemp4[0] = 'H';
        Hemp4[1] = 'X';
        Hemp4[2] = 'Y';
        Hemp4[3] = 'H';
        Hemp4[4] = 'G';
        Hemp4[5] = 'W';

        Object[] Hemp5 = new Object[6];
        Hemp5[0] = 'Y';
        Hemp5[1] = 'X';
        Hemp5[2] = 'G';
        Hemp5[3] = 'G';
        Hemp5[4] = 'G';
        Hemp5[5] = 'W';

        Object[] Hemp6 = new Object[6];
        Hemp6[0] = 'H';
        Hemp6[1] = 'X';
        Hemp6[2] = 'G';
        Hemp6[3] = 'H';
        Hemp6[4] = 'G';
        Hemp6[5] = 'W';

        Object[] Hemp7 = new Object[6];
        Hemp7[0] = 'G';
        Hemp7[1] = 'X';
        Hemp7[2] = 'G';
        Hemp7[3] = 'H';
        Hemp7[4] = 'G';
        Hemp7[5] = 'W';

        Object[] Hemp8 = new Object[6];
        Hemp8[0] = 'H';
        Hemp8[1] = 'H';
        Hemp8[2] = 'X';
        Hemp8[3] = 'H';
        Hemp8[4] = 'G';
        Hemp8[5] = 'X';


        HempModel hemp1 = new HempModel(Hemp1);
        HempModel hemp2 = new HempModel(Hemp2);
        HempModel hemp3 = new HempModel(Hemp3);
        HempModel hemp4 = new HempModel(Hemp4);
        HempModel hemp5 = new HempModel(Hemp5);
        HempModel hemp6 = new HempModel(Hemp6);
        HempModel hemp7 = new HempModel(Hemp7);
        HempModel hemp8 = new HempModel(Hemp8);


        CalculateBestCrop.addHempToList(hemp1);
        CalculateBestCrop.addHempToList(hemp2);
        CalculateBestCrop.addHempToList(hemp3);
        CalculateBestCrop.addHempToList(hemp4);
        CalculateBestCrop.addHempToList(hemp5);
        CalculateBestCrop.addHempToList(hemp6);
        CalculateBestCrop.addHempToList(hemp7);
        CalculateBestCrop.addHempToList(hemp8);

        int[] cha = new int[6];
        cha[0]= 0;
        cha[1]= 1;
        cha[2]= 3;
        cha[3]= -1;

        Object[] expectedOutcome = new Object[6];
        expectedOutcome[0] = 'Y';
        expectedOutcome[1] = 'X';
        expectedOutcome[2] = 'G';
        expectedOutcome[3] = 'G';
        expectedOutcome[4] = 'G';
        expectedOutcome[5] = 'W';

        new CalculateBestCrop(expectedOutcome);
        //CalculateBestCrop.tryEveryPossibleOutcome(expectedOutcome);

    }
}
