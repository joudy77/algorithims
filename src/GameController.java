// ==================== GameController.java ====================
import java.util.*;

/**
 * Controls the game flow and coordinates between game components
 */
public class GameController {

    private GameState gameState;
    private SenetAI ai;
    private Scanner scanner;
    private boolean showAIDetails;

    public GameController(int aiDepth, boolean showAIDetails) {
        Board board = new Board();
        this.gameState = new GameState(board, PlayerType.BLACK); // Human starts
        this.ai = new SenetAI(aiDepth, showAIDetails, PlayerType.WHITE);
        this.scanner = new Scanner(System.in);
        this.showAIDetails = showAIDetails;
    }

    /**
     * Start the game loop
     */
    public void startGame() {
        printWelcomeMessage();

        while (!gameState.isGameOver()) {
            gameState.printState();
            gameState.getBoard().printBoard();

            if (gameState.getCurrentPlayer() == PlayerType.BLACK) {
                playHumanTurn();
            } else {
                playAITurn();
            }

            // Visual separator
            System.out.println("\n" + "═".repeat(70) + "\n");

            // Small delay for better readability
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        printGameOver();
    }

    // /**
    //  * Human player's turn
    //  */
    // private void playHumanTurn() {
    //     System.out.println("\n┌───────────────────────────────────────┐");
    //     System.out.println(  "│        Your Turn! (Black Player)      │");
    //     System.out.println(  "└───────────────────────────────────────┘");
    // Rules.checkAndPenalizeSpecialHouses(gameState.getBoard(), gameState.getCurrentPlayer());

    //     // Roll sticks
    //     System.out.print("\nPress Enter to throw the sticks...");
    //     scanner.nextLine();

    //     int diceValue = Dice.throwSticksWithVisualization();
    //     gameState.setDiceValue(diceValue);

    //     List<Move> validMoves = gameState.getValidMoves();

    //     if (validMoves.isEmpty()) {
    //         // Before changing turn, check special house penalties
    //         //Rules.checkSpecialHousePenalties(gameState.getBoard(), gameState.getCurrentPlayer());
    //         System.out.println("\n❌ No valid moves available. Turn skipped.");
    //         gameState.changeTurn();
    //         return;
    //     }

    //     // Show valid moves
    //     Rules.printValidMoves(validMoves);

    //     // Select move
    //     Move selectedMove = null;
    //     while (selectedMove == null) {
    //         System.out.print("\nChoose a move (1-" + validMoves.size() + "): ");
    //         try {
    //             int choice = Integer.parseInt(scanner.nextLine());
    //             if (choice >= 1 && choice <= validMoves.size()) {
    //                 selectedMove = validMoves.get(choice - 1);
    //             } else {
    //                 System.out.println("❌ Invalid number. Try again.");
    //             }
    //         } catch (NumberFormatException e) {
    //             System.out.println("❌ Please enter a valid number.");
    //         }
    //     }

    //     // Apply move
    //     System.out.println("\n✓ Applied move: " + selectedMove.getDetailedDescription());
    //     gameState.applyMove(selectedMove);
    //     gameState.changeTurn();
    // }
private void playHumanTurn() {
    System.out.println("\n┌───────────────────────────────────────┐");
    System.out.println(  "│        Your Turn! (Black Player)      │");
    System.out.println(  "└───────────────────────────────────────┘");

    // Roll sticks
    System.out.print("\nPress Enter to throw the sticks...");
    scanner.nextLine();

    int diceValue = Dice.throwSticksWithVisualization();
    gameState.setDiceValue(diceValue);

    List<Move> validMoves = gameState.getValidMoves();

    if (validMoves.isEmpty()) {
        System.out.println("\n❌ No valid moves available. Turn skipped.");
        
        // معاقبة القطع في البيوت الخاصة (لم يتحرك أي شيء)
        Rules.penalizeSpecialHouses(
            gameState.getBoard(), 
            gameState.getCurrentPlayer(), 
            null
        );
        
        gameState.changeTurn();
        return;
    }

    // Show valid moves
    Rules.printValidMoves(validMoves);

    // Select move
    Move selectedMove = null;
    while (selectedMove == null) {
        System.out.print("\nChoose a move (1-" + validMoves.size() + "): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= validMoves.size()) {
                selectedMove = validMoves.get(choice - 1);
            } else {
                System.out.println("❌ Invalid number. Try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
        }
    }

    // Apply move
    System.out.println("\n✓ Applied move: " + selectedMove.getDetailedDescription());
    gameState.applyMove(selectedMove);
    
    // معاقبة القطع الأخرى في البيوت الخاصة
    Rules.penalizeSpecialHouses(
        gameState.getBoard(), 
        gameState.getCurrentPlayer(), 
        selectedMove
    );
    
    gameState.changeTurn();
}
//     /**
//      * AI player's turn
//      */
//     private void playAITurn() {
//         System.out.println("\n┌───────────────────────────────────────┐");
//         System.out.println(  "│      Computer Turn (White Player)     │");
//         System.out.println(  "└───────────────────────────────────────┘");
//  // Rules.checkAndPenalizeSpecialHouses(gameState.getBoard(), gameState.getCurrentPlayer());

//         System.out.println("\nComputer is throwing the sticks...");
//         try {
//             Thread.sleep(500);
//         } catch (InterruptedException ignored) {
//         }

//         int diceValue = Dice.throwSticksWithVisualization();
//         gameState.setDiceValue(diceValue);

//         List<Move> validMoves = gameState.getValidMoves();

//         if (validMoves.isEmpty()) {
// //Rules.checkSpecialHousePenalties(gameState.getBoard(), gameState.getCurrentPlayer());
//             System.out.println("\n❌ Computer has no valid moves. Turn skipped.");
//             gameState.changeTurn();
//             return;
//         }

//         System.out.println("\nComputer is thinking...");
//         Move bestMove = ai.getBestMove(gameState);

//         if (bestMove == null) {
//             System.out.println("\n❌ Computer could not find a move.");
//             gameState.changeTurn();
//             return;
//         }

//         System.out.println("\n✓ Computer chose: " + bestMove.getDetailedDescription());
//         gameState.applyMove(bestMove);
//         gameState.changeTurn();
//     }


private void playAITurn() {
    System.out.println("\n┌───────────────────────────────────────┐");
    System.out.println(  "│      Computer Turn (White Player)     │");
    System.out.println(  "└───────────────────────────────────────┘");

    System.out.println("\nComputer is throwing the sticks...");
    try {
        Thread.sleep(500);
    } catch (InterruptedException ignored) {
    }

    int diceValue = Dice.throwSticksWithVisualization();
    gameState.setDiceValue(diceValue);

    List<Move> validMoves = gameState.getValidMoves();

    if (validMoves.isEmpty()) {
        System.out.println("\n❌ Computer has no valid moves. Turn skipped.");
        
        // معاقبة القطع في البيوت الخاصة
        Rules.penalizeSpecialHouses(
            gameState.getBoard(), 
            gameState.getCurrentPlayer(), 
            null
        );
        
        gameState.changeTurn();
        return;
    }

    System.out.println("\nComputer is thinking...");
    Move bestMove = ai.getBestMove(gameState);

    if (bestMove == null) {
        System.out.println("\n❌ Computer could not find a move.");
        
        Rules.penalizeSpecialHouses(
            gameState.getBoard(), 
            gameState.getCurrentPlayer(), 
            null
        );
        
        gameState.changeTurn();
        return;
    }

    System.out.println("\n✓ Computer chose: " + bestMove.getDetailedDescription());
    gameState.applyMove(bestMove);
    
    // معاقبة القطع الأخرى
    Rules.penalizeSpecialHouses(
        gameState.getBoard(), 
        gameState.getCurrentPlayer(), 
        bestMove
    );
    
    gameState.changeTurn();
}

    /**
     * Print welcome screen
     */
    private void printWelcomeMessage() {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                               ║");
        System.out.println("║              Welcome to the Senet Game                        ║");
        System.out.println("║              The Ancient Egyptian Board Game                  ║");
        System.out.println("║                                                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Rules:                                                       ║");
        System.out.println("║  • Goal: Move all your pieces off the board first             ║");
        System.out.println("║  • Each player has 7 pieces                                   ║");
        System.out.println("║  • Stick throw determines moves (1–5)                         ║");
        System.out.println("║  • Landing on opponent swaps positions                        ║");
        System.out.println("║  • Beware of special squares!                                 ║");
        System.out.println("║                                                               ║");
        System.out.println("║  You: Black Player (○)                                        ║");
        System.out.println("║  Computer: White Player (●)                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();

        if (showAIDetails) {
            System.out.println("⚠️  AI detail display mode is enabled");
        }

        System.out.print("\nPress Enter to start...");
        scanner.nextLine();
    }

    /**
     * Print game over screen
     */
    private void printGameOver() {
        gameState.getBoard().printBoard();

        PlayerType winner = gameState.getWinner();

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        Game Over!                             ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");

        if (winner == PlayerType.BLACK) {
            System.out.println("║                                                               ║");
            System.out.println("║                🎉 Congratulations! You Win! 🎉               ║");
            System.out.println("║                                                               ║");
        } else {
            System.out.println("║                                                               ║");
            System.out.println("║             💻 Computer Wins! Try Again 💻                   ║");
            System.out.println("║                                                               ║");
        }

        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Total Turns: %-46d║\n", gameState.getTurnNumber());
        System.out.printf("║  Winner: %-51s║\n", winner.name());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Close resources
     */
    public void close() {
        scanner.close();
    }
}
