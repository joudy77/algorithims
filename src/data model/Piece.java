
public class Piece {
    private final int id;
    private final PlayerType owner;
    private int position;
    private PieceState state;
    private Integer needsExactRoll;
    
    public Piece(int id, PlayerType owner, int position) {
        this.id = id;
        this.owner = owner;
        this.position = position;
        this.state = PieceState.ON_BOARD;
        this.needsExactRoll = null;
    }
    

    public Piece(Piece other) {
        this.id = other.id;
        this.owner = other.owner;
        this.position = other.position;
        this.state = other.state;
        this.needsExactRoll = other.needsExactRoll;
    }
    

    public int getId() { return id; }
    public PlayerType getOwner() { return owner; }
    public int getPosition() { return position; }
    public PieceState getState() { return state; }
    public Integer getNeedsExactRoll() { return needsExactRoll; }
    

    public void setPosition(int position) { 
        this.position = position; 
    }
    
    public void setState(PieceState state) { 
        this.state = state; 
    }
    
    public void setNeedsExactRoll(Integer roll) { 
        this.needsExactRoll = roll; 
    }
    
    public boolean isFinished() {
        return state == PieceState.FINISHED;
    }
    
    public boolean isOnBoard() {
        return state == PieceState.ON_BOARD;
    }
    
    @Override
    public String toString() {
        return owner.getSymbol() + id;
    }
    
    public String getDetailedInfo() {
        String posStr = position == 0 ? "out of the game" : "square " + position;
        String rollInfo = needsExactRoll != null ? 
            " ( need roll " + needsExactRoll + ")" : "";
        return toString() + " - " + posStr + rollInfo;
    }
}