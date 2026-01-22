package data_model;

public class Move {

    private final Piece piece;
    private final int fromPosition;
    private final int toPosition;
    private final int steps;
    private final boolean isSwap;
    private final boolean isExit;

    public Move(Piece piece, int steps) {
        this.piece = piece;
        this.fromPosition = piece.getPosition();
        this.steps = steps;
        this.toPosition = fromPosition + steps;
        this.isSwap = false;
        this.isExit = toPosition > 30;
    }

    public Move(Piece piece, int fromPos, int toPos, int steps,
                boolean isSwap, boolean isExit) {
        this.piece = piece;
        this.fromPosition = fromPos;
        this.toPosition = toPos;
        this.steps = steps;
        this.isSwap = isSwap;
        this.isExit = isExit;
    }

    public Piece getPiece() { return piece; }
    public int getFromPosition() { return fromPosition; }
    public int getToPosition() { return toPosition; }
    public int getSteps() { return steps; }
    public boolean isSwap() { return isSwap; }
    public boolean isExit() { return isExit; }

    @Override
    public String toString() {
        if (isExit) {
            return String.format(
                "%s: %d -> EXIT",
                piece.toString(), fromPosition
            );
        }
        if (isSwap) {
            return String.format(
                "%s: %d ⇄ %d (SWAP)",
                piece.toString(), fromPosition, toPosition
            );
        }
        return String.format(
            "%s: %d -> %d",
            piece.toString(), fromPosition, toPosition
        );
    }
    public String getDetailedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Move: ").append(piece.toString());
        sb.append(" from cell ").append(fromPosition);

        if (isExit) {
            sb.append(" -> EXIT BOARD (").append(steps).append(" steps)");
        } else {
            sb.append(" -> cell ").append(toPosition);
            sb.append(" (").append(steps).append(" steps)");
            if (isSwap) {
                sb.append(" [SWAP WITH OPPONENT]");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Move other = (Move) obj;
        return piece.getId() == other.piece.getId()
                && piece.getOwner() == other.piece.getOwner()
                && fromPosition == other.fromPosition
                && toPosition == other.toPosition;
    }

    @Override
    public int hashCode() {
        return 31 * piece.getId() + fromPosition * 100 + toPosition;
    }
}
