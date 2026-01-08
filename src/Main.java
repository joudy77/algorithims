// ==================== Main.java ====================
import java.util.Scanner;

/**
 * Main class to run the Senet game
 *
 * Artificial Intelligence Search Algorithms Project
 * Damascus University - Faculty of Information Technology
 * Artificial Intelligence Department
 *
 * Implementation of the ancient Egyptian game Senet using:
 * - Object-Oriented Programming (OOP)
 * - Expectiminimax algorithm
 * - Heuristic Evaluation Function
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display main menu
        displayMainMenu();

        // Choose mode
        System.out.print("\nChoose game mode (1-4): ");
        int choice = getIntInput(scanner, 1, 4);

        switch (choice) {
            case 1:
                // Normal play
                playGame(3, false, scanner);
                break;
            case 2:
                // Play with AI details
                playGame(3, true, scanner);
                break;
            case 3:
                // Select difficulty
                selectDifficulty(scanner);
                break;
            case 4:
                // Show probability table
                Dice.printProbabilityTable();
                System.out.print("\nPress Enter to return...");
                scanner.nextLine();
                main(args);
                break;
        }

        scanner.close();
    }

    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                               ║");
        System.out.println("║                     Senet Game                                ║");
        System.out.println("║               Ancient Egyptian Board Game                     ║");
        System.out.println("║                                                               ║");
//        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
//        System.out.println("║                                                               ║");
//        System.out.println("║  Artificial Intelligence Search Algorithms Project            ║");
//        System.out.println("║  Damascus University - Faculty of IT                          ║");
//        System.out.println("║  Artificial Intelligence Department                           ║");
//        System.out.println("║                                                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║                     Choose Game Mode:                         ║");
        System.out.println("║                                                               ║");
        System.out.println("║  1. Normal Play (Medium difficulty)                           ║");
        System.out.println("║  2. Play with AI details                                      ║");
        System.out.println("║  3. Select difficulty                                         ║");
        System.out.println("║  4. Show stick throw probability table                        ║");
        System.out.println("║                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Select difficulty level
     */
    private static void selectDifficulty(Scanner scanner) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println(  "║                    Select Difficulty Level                    ║");
        System.out.println(  "╠═══════════════════════════════════════════════════════════════╣");
        System.out.println(  "║  1. Easy    (Depth = 2)                                       ║");
        System.out.println(  "║  2. Medium  (Depth = 3)                                       ║");
        System.out.println(  "║  3. Hard    (Depth = 4)                                       ║");
        System.out.println(  "║  4. Expert  (Depth = 5)                                       ║");
        System.out.println(  "╚═══════════════════════════════════════════════════════════════╝");

        System.out.print("\nChoose level (1-4): ");
        int level = getIntInput(scanner, 1, 4);

        int depth = level + 1; // 2,3,4,5

        System.out.print("Show AI details? (y/n): ");
        String showDetails = scanner.nextLine().trim().toLowerCase();
        boolean verbose = showDetails.equals("y") || showDetails.equals("yes");

        playGame(depth, verbose, scanner);
    }

    /**
     * Start the game
     */
    private static void playGame(int depth, boolean showAIDetails, Scanner scanner) {
        GameController controller = new GameController(depth, showAIDetails);
        controller.startGame();

        System.out.print("\nDo you want to play again? (y/n): ");
        String playAgain = scanner.nextLine().trim().toLowerCase();

        if (playAgain.equals("y") || playAgain.equals("yes")) {
            main(new String[0]);
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                               ║");
            System.out.println("║               Thank you for playing! Goodbye 👋              ║");
            System.out.println("║                                                               ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        }

        controller.close();
    }

    /**
     * Read valid integer input
     */
    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("❌ Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Please enter a valid number: ");
            }
        }
    }

//    /**
//     * Print project information
//     */
//    public static void printProjectInfo() {
//        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
//        System.out.println("║                     Project Information                      ║");
//        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
//        System.out.println("║  Course: Intelligent Search Algorithms                       ║");
//        System.out.println("║  University: Damascus University                              ║");
//        System.out.println("║  Faculty: Information Technology                              ║");
//        System.out.println("║  Department: Artificial Intelligence                          ║");
//        System.out.println("║                                                               ║");
//        System.out.println("║  Game: Senet                                                  ║");
//        System.out.println("║  Algorithm: Expectiminimax                                    ║");
//        System.out.println("║                                                               ║");
//        System.out.println("║  Components:                                                  ║");
//        System.out.println("║  • PlayerType.java                                           ║");
//        System.out.println("║  • CellType.java                                             ║");
//        System.out.println("║  • PieceState.java                                           ║");
//        System.out.println("║  • NodeType.java                                             ║");
//        System.out.println("║  • Piece.java                                                ║");
//        System.out.println("║  • Cell.java                                                 ║");
//        System.out.println("║  • Board.java                                                ║");
//        System.out.println("║  • Dice.java                                                 ║");
//        System.out.println("║  • Move.java                                                 ║");
//        System.out.println("║  • Rules.java                                                ║");
//        System.out.println("║  • GameState.java                                            ║");
//        System.out.println("║  • SenetAI.java                                              ║");
//        System.out.println("║  • GameController.java                                       ║");
//        System.out.println("║  • Main.java                                                 ║");
//        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
//    }
}
