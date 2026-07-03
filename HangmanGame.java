import java.util.ArrayList;
import java.util.Scanner;

public class HangmanGame {

    // ─── ANSI Colors ──────────────────────────────────────────────
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String RED    = "\u001B[31m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN   = "\u001B[36m";
    static final String GRAY   = "\u001B[90m";

    // ─── Word Bank ────────────────────────────────────────────────
    static String[] wordBank = {
            "JAVA", "OBJECT", "CLASS", "METHOD", "ARRAY",
            "LOOP", "STRING", "STACK", "QUEUE", "INHERITANCE",
            "POLYMORPHISM", "ENCAPSULATION", "CONSTRUCTOR", "INTERFACE", "PACKAGE",
            "BOOLEAN", "INTEGER", "EXCEPTION", "COMPILE", "VARIABLE"
    };

    // ─── Game State ───────────────────────────────────────────────
    static String secretWord;
    static char[] guessedWord;
    static ArrayList<Character> wrongGuesses;
    static ArrayList<Character> correctGuesses;
    static int balloonsLeft;

    // ─── Main ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playAgain = "y";

        while (playAgain.equalsIgnoreCase("y")) {
            initGame();
            playGame(scanner);
            System.out.print("\n" + CYAN + "  Play again? (y/n): " + RESET);
            playAgain = scanner.next();
        }

        System.out.println(YELLOW + "\n  Thanks for playing! Goodbye.\n" + RESET);
        scanner.close();
    }

    // ─── Initialize Game ──────────────────────────────────────────
    static void initGame() {
        int randomIndex = (int)(Math.random() * wordBank.length);
        secretWord = wordBank[randomIndex];

        guessedWord = new char[secretWord.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }

        wrongGuesses  = new ArrayList<Character>();
        correctGuesses = new ArrayList<Character>();
        balloonsLeft  = 6;
    }

    // ─── Main Game Loop ───────────────────────────────────────────
    static void playGame(Scanner scanner) {
        while (balloonsLeft > 0 && !checkWin()) {
            printGameState();

            System.out.print(CYAN + "  Guess a letter: " + RESET);
            String input = scanner.next().toUpperCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println(RED + "  Enter a single letter only." + RESET);
                continue;
            }

            char letter = input.charAt(0);

            if (alreadyGuessed(letter)) {
                System.out.println(YELLOW + "  Already guessed '" + letter + "'. Try another." + RESET);
                continue;
            }

            if (secretWord.indexOf(letter) >= 0) {
                System.out.println(GREEN + "  Nice! '" + letter + "' is in the word." + RESET);
                revealLetter(letter);
                correctGuesses.add(letter);
            } else {
                wrongGuesses.add(letter);
                balloonsLeft--;
                System.out.println(RED + "  Wrong! A balloon popped. " + balloonsLeft + " left." + RESET);
            }
        }

        printGameState();

        if (checkWin()) {
            System.out.println(BOLD + GREEN + "  ╔══════════════════════════════╗" + RESET);
            System.out.println(BOLD + GREEN + "  ║  YOU WIN! Congratulations!  ║" + RESET);
            System.out.println(BOLD + GREEN + "  ║  Word: " + secretWord + spacePad(secretWord) + "  ║" + RESET);
            System.out.println(BOLD + GREEN + "  ╚══════════════════════════════╝" + RESET);
        } else {
            System.out.println(BOLD + RED   + "  ╔══════════════════════════════╗" + RESET);
            System.out.println(BOLD + RED   + "  ║  GAME OVER! You fell down!  ║" + RESET);
            System.out.println(BOLD + RED   + "  ║  Word: " + secretWord + spacePad(secretWord) + "  ║" + RESET);
            System.out.println(BOLD + RED   + "  ╚══════════════════════════════╝" + RESET);
        }
    }

    // helper to pad the win/lose box neatly
    static String spacePad(String word) {
        int spaces = 16 - word.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) sb.append(" ");
        return sb.toString();
    }

    // ─── Print Full Game State ────────────────────────────────────
    static void printGameState() {
        System.out.println();
        printBalloons();
        System.out.println();
        printAlphabetBox();
        System.out.println();
        System.out.println(BOLD + "  Word    : " + RESET + BOLD + CYAN  + getDisplayWord()  + RESET);
        System.out.println(BOLD + "  Wrong   : " + RESET + BOLD + RED   + getWrongGuesses() + RESET);
        System.out.println(BOLD + "  Balloons: " + RESET + BOLD + YELLOW + balloonsLeft + " / 6" + RESET);
        System.out.println();
    }

    // ─── Print Balloon Row ────────────────────────────────────────
    static void printBalloons() {
        String[] balloonColors = { RED, YELLOW, GREEN, CYAN, "\u001B[34m", "\u001B[35m" };

        System.out.print("  Balloons: ");
        for (int i = 0; i < 6; i++) {
            if (i < balloonsLeft) {
                System.out.print(balloonColors[i] + "● " + RESET);
            } else {
                System.out.print(GRAY + "✕ " + RESET);
            }
        }
        System.out.println();
    }

    // ─── Print Alphabet Box ───────────────────────────────────────
    static void printAlphabetBox() {
        // Top border
        System.out.println("  ╔═══════════════════════════════════════════════════════╗");

        // Row 1: A - M
        System.out.print("  ║  ");
        for (char c = 'A'; c <= 'M'; c++) {
            printLetter(c);
        }
        System.out.println("  ║");

        // Row 2: N - Z
        System.out.print("  ║  ");
        for (char c = 'N'; c <= 'Z'; c++) {
            printLetter(c);
        }
        // pad to align the right border (Z is 13 letters, A-M is 13 letters, same)
        System.out.println("  ║");

        // Bottom border
        System.out.println("  ╚═══════════════════════════════════════════════════════╝");
    }

    // print one letter: green if correct, red+strikethrough if wrong, white if unused
    static void printLetter(char c) {
        if (correctGuesses.contains(c)) {
            System.out.print(BOLD + GREEN + c + RESET + "  ");
        } else if (wrongGuesses.contains(c)) {
            System.out.print(BOLD + RED + "-" + RESET + "  ");  // replaced with dash = used wrong
        } else {
            System.out.print(BOLD + c + RESET + "  ");
        }
    }

    // ─── Reveal Correctly Guessed Letters ────────────────────────
    static void revealLetter(char letter) {
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                guessedWord[i] = letter;
            }
        }
    }

    // ─── Check Win ────────────────────────────────────────────────
    static boolean checkWin() {
        for (char c : guessedWord) {
            if (c == '_') return false;
        }
        return true;
    }

    // ─── Already Guessed? ─────────────────────────────────────────
    static boolean alreadyGuessed(char letter) {
        for (char c : wrongGuesses)   if (c == letter) return true;
        for (char c : correctGuesses) if (c == letter) return true;
        return false;
    }

    // ─── Display Word ─────────────────────────────────────────────
    static String getDisplayWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < guessedWord.length; i++) {
            sb.append(guessedWord[i]);
            if (i < guessedWord.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    // ─── Display Wrong Guesses ────────────────────────────────────
    static String getWrongGuesses() {
        if (wrongGuesses.isEmpty()) return "none";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wrongGuesses.size(); i++) {
            sb.append(wrongGuesses.get(i));
            if (i < wrongGuesses.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}