import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class Algorithm {
    //public static double[][] A = new double[3][3];
    //public static double[][] B = new double[3][3];
    public static String[][] A = new String[3][3];
    public static String[][] B = new String[3][3];
    public static int[] E = new int[100];
    public static List<BigDecimal> T1 = new ArrayList<>(3);
    public static int[][] T2 = new int[100][3];
    public static int whichIsMax;
    public static int[] resultSequence = new int[100];
    public static BigDecimal resultProb;
    public static int[] test = new int[100];

    public static final void initFromFile(String fileName) throws Exception {
        String filePath = "./" + fileName;
        File file = new File(filePath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
    
        BufferedReader buffReader = new BufferedReader(reader);
        String line = "";
        String[] parseLine;
        int lineId = 0;
        while((line = buffReader.readLine()) != null) {
            if (line.startsWith("#")) continue;
            if (lineId == 0) {
                double probKeepSame = Double.parseDouble(line);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        //A[i][j] = (i == j) ? probKeepSame : (1-probKeepSame)/2;
                        A[i][j] = (i == j) ? new BigDecimal(String.format("%.8f", probKeepSame)).stripTrailingZeros().toString() : new BigDecimal(String.format("%.8f", (1-probKeepSame)/2)).stripTrailingZeros().toString(); // this is for output convenience.
                    }
                }
                lineId++;
            }
            else if (lineId <= 3) {
                parseLine = line.split(" ");
                //for (int i = 0; i < 3; i++) B[lineId - 1][i] = Double.parseDouble(parseLine[i]);
                for (int i = 0; i < 3; i++) B[lineId - 1][i] = parseLine[i];
                lineId++;
            }
            else if (lineId == 4) {
                parseLine = line.split(",");
                for (int i = 0; i < 100; i++) E[i] = Integer.parseInt(parseLine[i]);
                lineId++;
            }
            else if (lineId == 5) {
                parseLine = line.split(",");
                for (int i = 0; i < 100; i++) test[i] = Integer.parseInt(parseLine[i]);
                lineId++;
                return;
            }
        }
        buffReader.close();
    }

    public static void solve() {
        Double d = 1.0 / 3;
        BigDecimal Pi = new BigDecimal(d);
        BigDecimal temp0 = new BigDecimal(B[0][E[0] - 1]);
        BigDecimal temp1 = new BigDecimal(B[1][E[0] - 1]);
        BigDecimal temp2 = new BigDecimal(B[2][E[0] - 1]);
        T1.add(temp0.multiply(Pi));
        T1.add(temp1.multiply(Pi));
        T1.add(temp2.multiply(Pi));
        T2[0] = new int[]{0, 0, 0};
        for (int i = 1; i < 100; i++) {
            temp0 = maxBigDecimal(T1.get(0).multiply(new BigDecimal(A[0][0])), T1.get(1).multiply(new BigDecimal(A[1][0])), T1.get(2).multiply(new BigDecimal(A[2][0]))).multiply(new BigDecimal(B[0][E[i] - 1]));
            T2[i][0] = whichIsMax;
            temp1 = maxBigDecimal(T1.get(0).multiply(new BigDecimal(A[0][1])), T1.get(1).multiply(new BigDecimal(A[1][1])), T1.get(2).multiply(new BigDecimal(A[2][1]))).multiply(new BigDecimal(B[1][E[i] - 1]));
            T2[i][1] = whichIsMax;
            temp2 = maxBigDecimal(T1.get(0).multiply(new BigDecimal(A[0][2])), T1.get(1).multiply(new BigDecimal(A[1][2])), T1.get(2).multiply(new BigDecimal(A[2][2]))).multiply(new BigDecimal(B[2][E[i] - 1]));
            T2[i][2] = whichIsMax;
            T1.set(0, temp0);
            T1.set(1, temp1);
            T1.set(2, temp2);
        }
        resultProb = maxBigDecimal(T1.get(0), T1.get(1), T1.get(2));
        resultSequence[99] = whichIsMax;
        for (int i = 98; i >= 0; i--) {
            resultSequence[i] = T2[i + 1][resultSequence[i + 1]];
        }
        for (int i = 0; i < 100; i++) {
            resultSequence[i]++;
        }
    }

    public static BigDecimal maxBigDecimal(BigDecimal BD0, BigDecimal BD1, BigDecimal BD2) {
        BigDecimal temp;
        if (BD0.compareTo(BD1) >= 0) {
            temp = BD0;
            whichIsMax = 0;
        } else {
            temp = BD1;
            whichIsMax = 1;
        }
        if (temp.compareTo(BD2) >= 0) {
            return temp;
        } else {
            whichIsMax = 2;
            return BD2;
        }
    }

    public static void main(String[] args) throws Exception {
        Algorithm.initFromFile(args[0]);
        Algorithm.solve();
        System.out.println("States: {dice1, dice2, dice3}\n");
        //System.out.printf("               %f\t%f\t%f\n", A[0][0], A[0][1], A[0][2]);
        //System.out.printf("Transition A = %f\t%f\t%f\n", A[1][0], A[1][1], A[1][2]);
        //System.out.println(new BigDecimal(A[0][0]).stripTrailingZeros());
        System.out.printf("               %s\t%s\t%s\n", A[0][0], A[0][1], A[0][2]);
        System.out.printf("Transition A = %s\t%s\t%s\n", A[1][0], A[1][1], A[1][2]);
        System.out.printf("               %s\t%s\t%s\n", A[2][0], A[2][1], A[2][2]);
        System.out.println("");

        System.out.printf("               %s\t%s\t%s\n", B[0][0], B[0][1], B[0][2]);
        System.out.printf("Emision    B = %s\t%s\t%s\n", B[1][0], B[1][1], B[1][2]);
        System.out.printf("               %s\t%s\t%s\n", B[2][0], B[2][1], B[2][2]);
        System.out.println("");

        System.out.printf("Belief: %d", resultSequence[0]);
        for (int i = 1; i < 100; i++) {
            System.out.printf(",%d", resultSequence[i]);
        }
        System.out.println("\n");

        //System.out.println(resultProb.toString());
        System.out.println("Probability: " + new DecimalFormat("0.00000E0").format(resultProb));

        if (test[0] != 0) {
            int same = 0;
            for (int i = 0; i < 100; i++) {
                if (resultSequence[i] == test[i]) {
                    same++;
                }
            }
            System.out.println("");
            System.out.println("(Similarity: " + same + "%)");
        }
    }
}