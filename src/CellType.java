// ==================== CellType.java ====================
/**
 * Enum representing the special cell types on the board
 */
public enum CellType {
    NORMAL(0, "Normal"),
    REBIRTH(15, "House of Rebirth"),
    HAPPINESS(26, "House of Happiness"),
    WATER(27, "House of Water"),
    THREE_TRUTHS(28, "House of Three Truths"),
    RE_ATOUM(29, "House of Re-Atoum"),
    HORUS(30, "House of Horus");
    
    private final int position;
    private final String englishName;
    
    CellType(int position, String englishName) {
        this.position = position;
        this.englishName = englishName;
    }
    
    public int getPosition() {
        return position;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public static CellType getTypeForPosition(int pos) {
        for (CellType type : values()) {
            if (type.position == pos && type != NORMAL) {
                return type;
            }
        }
        return NORMAL;
    }
}
