package data_model;

public class Cell {

    private final int index;
    private final CellType type;
    private Piece occupant;


    public Cell(int index) {
        this.index = index;
        this.type = CellType.getTypeForPosition(index);
        this.occupant = null;
    }


    public Cell(Cell other) {
        this.index = other.index;
        this.type = other.type;
        this.occupant = null;
    }


    public int getIndex() {
        return index;
    }

    public CellType getType() {
        return type;
    }

    public Piece getOccupant() {
        return occupant;
    }

    public void setOccupant(Piece piece) {
        this.occupant = piece;
    }


    public boolean isEmpty() {
        return occupant == null;
    }


    public boolean isOccupiedBy(PlayerType player) {
        return occupant != null && occupant.getOwner() == player;
    }

    public boolean isSpecialCell() {
        return type != CellType.NORMAL;
    }

    public String getCSymbol() {
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
            return "[" + getCSymbol() + "]";
        }
        return "[" + occupant + "]";
    }
}
