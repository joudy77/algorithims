// ==================== GameState.java ====================
/**
 * Represents the game state at a specific moment
 * Used by the Expectiminimax algorithm
 */
public class GameState {

    private Board board;
    private PlayerType currentPlayer;
    private Integer diceValue;   // null if sticks not thrown yet
    private int turnNumber;

    public GameState(Board board, PlayerType currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.diceValue = null;
        this.turnNumber = 1;
    }

    /**
     * Copy constructor (deep copy)
     */
    public GameState(GameState other) {
        this.board = new Board(other.board);
        this.currentPlayer = other.currentPlayer;
        this.diceValue = other.diceValue;
        this.turnNumber = other.turnNumber;
    }

    // ===== Getters =====
    public Board getBoard() {
        return board;
    }

    public PlayerType getCurrentPlayer() {
        return currentPlayer;
    }

    public Integer getDiceValue() {
        return diceValue;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    // ===== Setters =====
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayer(PlayerType player) {
        this.currentPlayer = player;
    }

    public void setDiceValue(Integer value) {
        this.diceValue = value;
    }

    public void setTurnNumber(int turn) {
        this.turnNumber = turn;
    }

    /**
     * Check if the game is over
     */
    public boolean isGameOver() {
        return board.isGameOver();
    }

    /**
     * Get the winner (if any)
     */
    public PlayerType getWinner() {
        return board.getWinner();
    }

    /**
     * Switch turn to the next player
     */
    public void changeTurn() {
        currentPlayer = currentPlayer.getOpponent();
        diceValue = null;
        turnNumber++;
    }

    /**
     * Create a deep copy of this game state
     */
    @Override
    public GameState clone() {
        return new GameState(this);
    }

    /**
     * Apply a move to the current state
     */
    /**
 * Apply a move to the current state
 */
public void applyMove(Move move) {
    // Find the corresponding piece in THIS board
    Piece pieceInThisBoard = board.findPiece(
        move.getPiece().getId(), 
        move.getPiece().getOwner()
    );
    
    if (pieceInThisBoard == null) {
        // Fallback to original method
        Rules.applyMove(board, move);
        return;
    }
    
    // Create a new move with the piece from this board
    Move correctedMove = new Move(
        pieceInThisBoard,
        move.getFromPosition(),
        move.getToPosition(),
        move.getSteps(),
        move.isSwap(),
        move.isExit()
    );
    
    Rules.applyMove(board, correctedMove);
}

    /**
     * Get all valid moves for the current player
     */
    public java.util.List<Move> getValidMoves() {
        if (diceValue == null) {
            return new java.util.ArrayList<>();
        }
        return Rules.getValidMoves(board, currentPlayer, diceValue);
    }

    /**
     * Print current game state information
     */
    public void printState() {
        System.out.println("\n┌───────────────────────────────────────┐");
        System.out.printf(
            "│ Turn #%d - Player: %s %s           │\n",
            turnNumber,
            currentPlayer.name(),
            currentPlayer == PlayerType.WHITE ? "(●)" : "(○)"
        );

        if (diceValue != null) {
            System.out.printf("│ Roll: %d                               │\n", diceValue);
        }

        System.out.println("└───────────────────────────────────────┘");
    }

    @Override
    public String toString() {
        return String.format(
            "GameState[Turn=%d, Player=%s, Dice=%s]",
            turnNumber,
            currentPlayer.name(),
            diceValue == null ? "?" : diceValue
        );
    }
}
