package data_model;

public enum PlayerType {
    WHITE("white", "W"),  // الكمبيوتر
    BLACK("black", "B");   // الإنسان
    
    private final String symbol;
    
    PlayerType(String arabicName, String symbol) {
        this.symbol = symbol;
    }

    
    public String getSymbol() {
        return symbol;
    }
    
    public PlayerType getOpponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
