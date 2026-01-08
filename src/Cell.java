// ==================== Cell.java ====================
/**
 * Class representing a single cell on the board
 */
public class Cell {

    private final int index;        // Cell index (1–30)
    private final CellType type;     // Cell type (normal or special)
    private Piece occupant;          // Piece occupying the cell (if any)

    /**
     * Create a new cell by index
     */
    public Cell(int index) {
        this.index = index;
        this.type = CellType.getTypeForPosition(index);
        this.occupant = null;
    }

    /**
     * Copy constructor (cell only, without copying occupant)
     * Occupants are reattached by Board
     */
    public Cell(Cell other) {
        this.index = other.index;
        this.type = other.type;
        this.occupant = null;
    }

    // ===== Getters =====
    public int getIndex() {
        return index;
    }

    public CellType getType() {
        return type;
    }

    public Piece getOccupant() {
        return occupant;
    }

    // ===== Setters =====
    public void setOccupant(Piece piece) {
        this.occupant = piece;
    }

    // ===== State checks =====
    public boolean isEmpty() {
        return occupant == null;
    }

    public boolean isOccupiedBy(PlayerType player) {
        return occupant != null && occupant.getOwner() == player;
    }

    public boolean isSpecialCell() {
        return type != CellType.NORMAL;
    }

    /**
     * Get a single-character symbol for board rendering
     */
    public String getCellSymbol() {
        if (isEmpty()) {
            switch (type) {
                case REBIRTH:        return "♆"; // House of Rebirth
                case HAPPINESS:      return "☺"; // House of Happiness
                case WATER:          return "≈"; // House of Water
                case THREE_TRUTHS:   return "⚔"; // House of Three Truths
                case RE_ATOUM:       return "☥"; // House of Re-Atoum
                case HORUS:          return "⊕"; // House of Horus
                default:             return "·"; // Normal cell
            }
        }
        return occupant.getOwner().getSymbol() + occupant.getId();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[" + getCellSymbol() + "]";
        }
        return "[" + occupant + "]";
    }
}
