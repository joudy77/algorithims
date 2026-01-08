// ==================== PlayerType.java ====================
/**
 * تعداد يمثل نوع اللاعب
 */
public enum PlayerType {
    WHITE("white", "W"),  // الكمبيوتر
    BLACK("black", "B");   // الإنسان
    
    private final String arabicName;
    private final String symbol;
    
    PlayerType(String arabicName, String symbol) {
        this.arabicName = arabicName;
        this.symbol = symbol;
    }
    
    public String getArabicName() {
        return arabicName;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public PlayerType getOpponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
