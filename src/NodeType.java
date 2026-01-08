// ==================== 4. NodeType.java ====================
/**
 * تعداد يمثل نوع العقدة في شجرة Expectiminimax
 */
public enum NodeType {
    MAX("maxxer node"),
    MIN("minner node"),
    CHANCE("chance node ");
    
    private final String arabicName;
    
    NodeType(String arabicName) {
        this.arabicName = arabicName;
    }
    
    public String getArabicName() {
        return arabicName;
    }
}