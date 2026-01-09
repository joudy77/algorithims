// ==================== PieceState.java ====================
/**
 * تعداد يمثل حالة الحجر
 */
public enum PieceState {
    ON_BOARD("on the board"),
    FINISHED("out of the game"),
    //WAITING_EXACT_ROLL("waiting for specifice shot")
    ;
    
    private final String arabicName;
    
    PieceState(String arabicName) {
        this.arabicName = arabicName;
    }
    
    public String getArabicName() {
        return arabicName;
    }
}
