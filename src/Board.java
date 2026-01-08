// ==================== Board.java ====================
import java.util.*;

/**
 * Class representing the game board (30 squares)
 * Path: 1–10 (right to left), 11–20 (left to right), 21–30 (right to left)
 */
public class Board {
    private Cell[] cells;  // 30 cells (index 0 unused, start from 1)
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    
    public Board() {
        // Create 31 cells (ignore index 0)
        cells = new Cell[31];
        for (int i = 1; i <= 30; i++) {
            cells[i] = new Cell(i);
        }
        
        // Create pieces
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        
        // Distribute pieces alternately on the first 14 squares
        for (int i = 0; i < 7; i++) {
            // White pieces on odd squares
            Piece whitePiece = new Piece(i + 1, PlayerType.WHITE, 2 * i + 1);
            whitePieces.add(whitePiece);
            cells[2 * i + 1].setOccupant(whitePiece);
            
            // Black pieces on even squares
            Piece blackPiece = new Piece(i + 1, PlayerType.BLACK, 2 * i + 2);
            blackPieces.add(blackPiece);
            cells[2 * i + 2].setOccupant(blackPiece);
        }
    }
    
    // Copy constructor for deep copy
    public Board(Board other) {
        cells = new Cell[31];
        for (int i = 1; i <= 30; i++) {
            cells[i] = new Cell(other.cells[i]);
        }
        
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        
        // Copy pieces and attach them to new cells
        for (Piece p : other.whitePieces) {
            Piece newPiece = new Piece(p);
            whitePieces.add(newPiece);
            if (newPiece.getPosition() > 0 && newPiece.getPosition() <= 30) {
                cells[newPiece.getPosition()].setOccupant(newPiece);
            }
        }
        
        for (Piece p : other.blackPieces) {
            Piece newPiece = new Piece(p);
            blackPieces.add(newPiece);
            if (newPiece.getPosition() > 0 && newPiece.getPosition() <= 30) {
                cells[newPiece.getPosition()].setOccupant(newPiece);
            }
        }
    }
    /**
 * Find piece by ID and owner in this board
 */
public Piece findPiece(int id, PlayerType owner) {
    List<Piece> pieces = owner == PlayerType.WHITE ? whitePieces : blackPieces;
    for (Piece p : pieces) {
        if (p.getId() == id) {
            return p;
        }
    }
    return null;
}
    
    // Getters
    public Cell getCell(int index) {
        if (index < 1 || index > 30) return null;
        return cells[index];
    }
    
    public Piece getPieceAt(int position) {
        if (position < 1 || position > 30) return null;
        return cells[position].getOccupant();
    }
    
    public List<Piece> getPieces(PlayerType player) {
        return player == PlayerType.WHITE ?
            new ArrayList<>(whitePieces) : new ArrayList<>(blackPieces);
    }
    
    public boolean isSquareOccupied(int position) {
        if (position < 1 || position > 30) return false;
        return !cells[position].isEmpty();
    }
    
    /**
     * Check if the game is over
     */
    public boolean isGameOver() {
        return isPlayerFinished(PlayerType.WHITE) ||
               isPlayerFinished(PlayerType.BLACK);
    }
    
    /**
     * Check if a specific player has finished all pieces
     */
    public boolean isPlayerFinished(PlayerType player) {
        List<Piece> pieces = getPieces(player);
        for (Piece p : pieces) {
            if (!p.isFinished()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get the winner
     */
    public PlayerType getWinner() {
        if (isPlayerFinished(PlayerType.WHITE)) return PlayerType.WHITE;
        if (isPlayerFinished(PlayerType.BLACK)) return PlayerType.BLACK;
        return null;
    }
    
    /**
     * Count pieces still on the board
     */
    public int countPiecesOnBoard(PlayerType player) {
        int count = 0;
        for (Piece p : getPieces(player)) {
            if (p.isOnBoard()) count++;
        }
        return count;
    }
    
    /**
     * Calculate total progress of a player's pieces
     */
    public int calculateProgress(PlayerType player) {
        int progress = 0;
        for (Piece p : getPieces(player)) {
            if (p.isFinished()) {
                progress += 35;  // Large bonus for finished pieces
            } else if (p.isOnBoard()) {
                progress += p.getPosition();
            }
        }
        return progress;
    }

    /**
     * Print the board in a readable format
     */
    public void printBoard() {
                            
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println(  "║                      Senet Game Board                        ║");
        System.out.println(  "╠══════════════════════════════════════════════════════════════╣");
        
        // First row (10–1) - right to left
        System.out.print("║ ");
        for (int i = 1; i <= 10; i++) {
            printCell(i);
        }
        System.out.println(" ║");
                         
        // First row numbers
        System.out.println(  "║                                                              ║");
        System.out.println(  "╠──────────────────────────────────────────────────────────────╣");
        
        // Second row (11–20) - left to right
        System.out.print("║ ");
        for (int i = 20; i >= 11; i--) {
            printCell(i);
        }
        System.out.println(" ║");
        
        // Second row numbers
       System.out.println(   "║                                                              ║");
        System.out.println(  "╠──────────────────────────────────────────────────────────────╣");
        
        // Third row (30–21) - right to left
        System.out.print("║ ");
        for (int i = 21; i <=30; i++) {
            printCell(i);
        }
        System.out.println(" ║");
        
        // Third row numbers
          System.out.println("║                                                              ║");
        
        System.out.println(  "╚══════════════════════════════════════════════════════════════╝");
        
        // Print statistics
        printStatistics();
        
        // Print special squares legend
       // printLegend();
    }
    
    private void printCell(int index) {
        Cell cell = cells[index];
        String content;
        
        if (cell.isEmpty()) {
            // Symbols for special squares
            CellType ct = cell.getType();
            if (ct == CellType.REBIRTH) {
                content = " ♆ ";  // House of Rebirth (15)
            } else if (ct == CellType.HAPPINESS) {
                content = " ☺ ";  // House of Happiness (26)
            } else if (ct == CellType.WATER) {
                content = " ≈ ";  // House of Water (27)
            } else if (ct == CellType.THREE_TRUTHS) {
                content = " ⚔ ";  // House of Three Truths (28)
            } else if (ct == CellType.RE_ATOUM) {
                content = " ☥ ";  // House of Re-Atoum (29)
            } else if (ct == CellType.HORUS) {
                content = " ⊕ ";  // House of Horus (30)
            } else {
                content = " · ";
            }
        } else {
            Piece p = cell.getOccupant();
            String symbol = p.getOwner() == PlayerType.WHITE ? " ●" : " ○";
            content = symbol  ;
            if (content.length() < 4) content += " ";
        }
        
        System.out.print(" [" + content + "]");
    }
    
    private void printStatistics() {
        int whiteOnBoard = countPiecesOnBoard(PlayerType.WHITE);
        int blackOnBoard = countPiecesOnBoard(PlayerType.BLACK);
        int whiteFinished = 7 - whiteOnBoard;
        int blackFinished = 7 - blackOnBoard;
        int whiteProgress = calculateProgress(PlayerType.WHITE);
        int blackProgress = calculateProgress(PlayerType.BLACK);
                              
        System.out.println(  "\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println(    "│                         Statistics                          │");
        System.out.println(    "├─────────────────────────────────────────────────────────────┤");
        System.out.printf("│ White Player (●): %d on board, %d finished, progress: %d      │\n",
            whiteOnBoard, whiteFinished, whiteProgress);
        System.out.printf("│ Black Player (○): %d on board, %d finished, progress: %d      │\n",
            blackOnBoard, blackFinished, blackProgress);
        System.out.println(    "└─────────────────────────────────────────────────────────────┘");
    }
    
    
    /**
     * Print detailed information about a player's pieces
     */
    public void printPiecesInfo(PlayerType player) {
        System.out.println("\n=== Piece Information for " + player.name() + " ===");
        List<Piece> pieces = getPieces(player);
        for (Piece p : pieces) {
            System.out.println(p.getDetailedInfo());
        }
    }
}
