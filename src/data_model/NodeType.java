package data_model;

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