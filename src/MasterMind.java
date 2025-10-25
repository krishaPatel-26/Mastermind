
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krisha Patel
 */
public class MasterMind {

    //colors
    private final String RESET = "\u001B[0m";
    private final String RED = "\u001B[31m";
    private final String GREEN = "\u001B[32m";
    private final String CYAN = "\u001B[36m";
    private final String BLUE = "\u001B[34m";
    private final String PURPLE = "\u001B[35m";
    private final String YELLOW = "\u001B[33m";

    //array of colors
    private static final String[] COLORS = {"R", "G", "B", "C", "P", "Y"};
    private String[][] guesses;
    private String[][] indications;

    private int attempts;
    private int numPegs;

    private Scanner scan = new Scanner(System.in);
    private String code;
    private String name;
    private int attemptsTookToGuess = 0;

    public MasterMind() {

        System.out.print("Please enter your name: ");
        name = scan.nextLine();

        numOfPegs();

        code = randomCode(numPegs);
      //System.out.println(code);
        guesses = new String[attempts][numPegs];
        indications = new String[attempts][numPegs];
        instr();
        playGame();

    }

    //how many pegs
    public void numOfPegs() {
        System.out.print("How many pegs do you want to play with (4, 5 or 6)? ");
        numPegs = scan.nextInt();
        scan.nextLine();
        while (numPegs != 4 && numPegs != 5 && numPegs != 6) {
            System.out.print("Please choose between 4, 5, or 6 pegs: ");
            numPegs = scan.nextInt();
            scan.nextLine();
        }

        if (numPegs == 4) {
            attempts = 10;
        }
        if (numPegs == 5) {
            attempts = 12;
        }
        if (numPegs == 6) {
            attempts = 14;
        }
    }

    //random color sequence of pegs
    private String randomCode(int length) {
        Random random = new Random();
        String s = "";
        while (s.length() < length) {
            int index = random.nextInt(COLORS.length);
            String color = COLORS[index];
            if (s.toString().indexOf(color) <0) {
                s += color;
            }
        }
        return s;
    }

    //how to play
    public void instr() {

        System.out.println("Welcome to Mastermind!");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Your goal is to guess the color sequence of the "
                + "pegs. You will start of by guessing 4 colors of pegs.\nThe "
                + "sequence will be a set of four different colors represented "
                + "by a letter.\n" + RED + "R - red" + RESET + GREEN
                + "\nG - green" + RESET + BLUE + "\nB - blue" + RESET + CYAN
                + "\nC - cyan" + RESET + YELLOW + "\nY - yellow" + RESET
                + PURPLE + "\nP - purple" + RESET + "\nQ - Quit \nL - Leaderboard");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Each time you guess, there will be a color response"
                + " of three options: \nBLACK, RED, WHITE \nBLACK = A color "
                + "you guessed isn't one of the color in the sequence. \n"
                + "WHITE = A color you guessed IS one of the color in the "
                + "sequence, but NOT in the place where you guessed it.\n"
                + "RED = A color you guessed IS one of the color in the "
                + "sequence, and IS where you guessed it. \n"
                + "The order of the colors is random. So even if you see "
                + "RED in the second spot, it does NOT mean \n"
                + "that your second guess is in the correct place.");
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Let's Play!!\n");

    }

    //player's guess
    private String makeGuess(int attemptNum, int length) {
        System.out.print("Guess " + attemptNum + ": Enter your guess (" + length
                + "-color code): ");
        String guess = scan.nextLine().toUpperCase();
        guess = guess.replaceAll(" ", "");

        if (guess.equals("Q") || guess.equals("q")) {
            System.out.println("Quitting the game...");
            System.exit(0); // Exit the program
        }
        if(guess.equals("L") || guess.equals("l"))
        {
            openFile();
        }
        while (!isValidCode(guess, length)) {
            System.out.print("Invalid guess.\nGuess " + attemptNum + ": Enter "
                    + "your guess (" + length + "-color code): ");
            guess = scan.nextLine().toUpperCase();
            guess = guess.replaceAll(" ", "");
            if (guess.equals("Q") || guess.equals("q")) {
                System.out.println("Quitting the game...");
                System.exit(0); // Exit the program
            }
            if(guess.equals("L") || guess.equals("l"))
        {
            openFile();
        }

        }
        return guess;
    }

    //is the code correct length or a letter??
    private boolean isValidCode(String code, int length) {
        if (code.length() != length) {
            return false;
        }
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (!isValidColor(Character.toString(c))) {
                return false;
            }
        }
        return true;
    }

    //is the code a color from the given options??
    private boolean isValidColor(String color) {
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i].equals(color)) {
                return true;
            }
        }
        return false;
    }

    //tell the player if their guess is
    //the correct color??
    //in the correct position?? 
    private void giveColorIndications(String guess, int attempt) {

        int correct = 0;
        int correctColor = 0;

        for (int i = 0; i < numPegs; i++) {
            if (code.charAt(i) == guess.charAt(i)) {
                correct++;
            } else if (code.indexOf(guess.charAt(i) - '0')>0) {
                correctColor++;
            }
        }
        for (int i = 0; i < numPegs; i++) {
            if (correct > 0) {
                indications[attempt][i] = "RED";
                correct--;
            } else if (correctColor > 0) {
                indications[attempt][i] = "WHITE";
                correctColor--;
            } else {
                indications[attempt][i] = "BLACK";

            }

        }
    }

    private void playGame() {
        for (int i = 0; i < attempts; i++) {
            String guess = makeGuess(i + 1, numPegs); // Pass attempt number
            if (guess == null) {
                System.out.println("Input is empty. Please try again.\n");
                i--; // Decrement i to stay at the same attempt
                continue; // Skip incrementing attempts
            }

            // Add each character of the guess string to the guesses array
            for (int j = 0; j < guess.length(); j++) {
                guesses[i][j] = String.valueOf(guess.charAt(j));
            }

            giveColorIndications(guess, i);
            if (checkGuess(guess)) {
                attemptsTookToGuess = i;
                System.out.println("");
                System.out.println("Congratulations " + name + "! You have "
                        + "guessed the code in " + (attemptsTookToGuess + 1)
                        + " attempts!");
                toFile();
                return;
            } else {
                System.out.println("");
                System.out.println("Previous Guesses:");
                for (int k = 0; k <= i; k++) {
                    System.out.println("Guess " + (k + 1) + ": " + Arrays.toString(guesses[k])
                            + " --- " + Arrays.toString(indications[k]));
                }
                System.out.println("");
            }
        }
        System.out.println("Out of attempts. The secret code was: " + code);
    }
    
    private void toFile(){
        try {
            FileWriter fw = new FileWriter("Leaderboard.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Winner: " + name + " --- Attempts took to win: "+ (attemptsTookToGuess+1));
            bw.newLine();
            bw.close();
            
            
        } catch (IOException ex) {
            Logger.getLogger(MasterMind.class.getName()).log(Level.SEVERE, null, ex);
        }
        openFile();
    }
    
    private void openFile(){
        File f = new File("Leaderboard.txt");
        if(f.exists()){
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {
                Logger.getLogger(MasterMind.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    private boolean checkGuess(String guess) {
        return code.equals(guess);
    }

}
