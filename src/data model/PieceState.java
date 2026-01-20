
public enum PieceState {
    ON_BOARD("on the board"),
    FINISHED("out of the game"),

    ;
    
    private final String arabicName;
    
    PieceState(String arabicName) {
        this.arabicName = arabicName;
    }
    
    public String getArabicName() {
        return arabicName;
    }
}
