
package AI;

import java.util.*;
import data_model.*;
import game_logic.Board;
import game_logic.Dice;
import game_logic.GameState;


public class SenetAI {
    private int maxDepth;
    private int nodesVisited;
    private boolean verboseMode;
    private PlayerType aiPlayer;
    

    private List<MoveEvaluation> moveEvaluations;
    private static final double PROGRESS_WEIGHT = 2.0;
    private static final double OPPONENT_PROGRESS_WEIGHT = 1.5;
    private static final double FINISHED_BONUS = 50.0;
    private static final double SPECIAL_CELL_BONUS = 10.0;
    private static final double SAFETY_BONUS = 5.0;
    
    public SenetAI(int depth, boolean verbose, PlayerType aiPlayer) {
        this.maxDepth = depth;
        this.verboseMode = verbose;
        this.aiPlayer = aiPlayer;
        this.nodesVisited = 0;
        this.moveEvaluations = new ArrayList<>();
    }

    public Move getBestMove(GameState state) {
        nodesVisited = 0;
        moveEvaluations.clear();
        long startTime = System.currentTimeMillis();
        
        if (verboseMode) {
            printAlgorithmHeader();
        }
        
        List<Move> validMoves = state.getValidMoves();
        
        if (validMoves.isEmpty()) {
            return null;
        }
        
        Move bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        

        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move, true);
            double value = expectiminimax(nextState, maxDepth - 1, NodeType.CHANCE, 0);
            moveEvaluations.add(new MoveEvaluation(move, value));
            
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        if (verboseMode) {
            printDetailedResults(bestMove, bestValue, endTime - startTime);
        }
        
        return bestMove;
    }

    private double expectiminimax(GameState state, int depth, NodeType nodeType, int level) {
        nodesVisited++;

        boolean shouldPrint = verboseMode && level < 2;
        
        if (shouldPrint) {
            printNodeInfo(nodeType, depth, level);
        }
        

        if (depth == 0 || state.isGameOver()) {
            double value = heuristic(state);
            if (shouldPrint) {
                printLeafValue(value, level);
            }
            return value;
        }

        if (nodeType == NodeType.CHANCE) {
            return chanceNode(state, depth, level, shouldPrint);
        }

        if (nodeType == NodeType.MAX) {
            return maxNode(state, depth, level, shouldPrint);
        }

        return minNode(state, depth, level, shouldPrint);
    }

    private double chanceNode(GameState state, int depth, int level, boolean print) {
        double expectedValue = 0.0;
        int[] possibleRolls = Dice.getPossibleRolls();
        
        if (print) {
            System.out.println(indent(level) + "├─ CHANCE: Computing expected value over " 
                + possibleRolls.length + " possible rolls");
        }
        
        for (int roll : possibleRolls) {
            double probability = Dice.getProbability(roll);
            
            if (probability == 0.0) continue;
            
            GameState nextState = state.clone();
            nextState.setDiceValue(roll);
            
            NodeType nextNodeType = (nextState.getCurrentPlayer() == aiPlayer) ? 
                NodeType.MAX : NodeType.MIN;
            
            double value = expectiminimax(nextState, depth, nextNodeType, level + 1);
            expectedValue += probability * value;
            
            if (print && level < 1) {
                System.out.printf("%s│  ├─ Roll %d (prob=%.3f): value=%.2f\n", 
                    indent(level), roll, probability, value);
            }
        }
        
        if (print) {
            System.out.printf("%s└─ Expected value: %.2f\n", indent(level), expectedValue);
        }
        
        return expectedValue;
    }

    private double maxNode(GameState state, int depth, int level, boolean print) {
        List<Move> validMoves = state.getValidMoves();
        
        if (validMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.changeTurn();
            return expectiminimax(nextState, depth - 1, NodeType.CHANCE, level);
        }
        
        double maxValue = Double.NEGATIVE_INFINITY;
        
        if (print) {
            System.out.println(indent(level) + "├─ MAX: Evaluating " 
                + validMoves.size() + " moves");
        }
        
        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move, true);
            nextState.changeTurn();
            
            double value = expectiminimax(nextState, depth - 1, NodeType.CHANCE, level + 1);
            maxValue = Math.max(maxValue, value);
            
            if (print && level < 1) {
                System.out.printf("%s│  ├─ Move %s: value=%.2f\n", 
                    indent(level), move.toString(), value);
            }
        }
        
        if (print) {
            System.out.printf("%s└─ MAX returns: %.2f\n", indent(level), maxValue);
        }
        
        return maxValue;
    }

    private double minNode(GameState state, int depth, int level, boolean print) {
        List<Move> validMoves = state.getValidMoves();
        
        if (validMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.changeTurn();
            return expectiminimax(nextState, depth - 1, NodeType.CHANCE, level);
        }
        
        double minValue = Double.POSITIVE_INFINITY;
        
        if (print) {
            System.out.println(indent(level) + "├─ MIN: Evaluating " 
                + validMoves.size() + " moves");
        }
        
        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move, true);
            nextState.changeTurn();
            
            double value = expectiminimax(nextState, depth - 1, NodeType.CHANCE, level + 1);
            minValue = Math.min(minValue, value);
            
            if (print && level < 1) {
                System.out.printf("%s│  ├─ Move %s: value=%.2f\n", 
                    indent(level), move.toString(), value);
            }
        }
        
        if (print) {
            System.out.printf("%s└─ MIN returns: %.2f\n", indent(level), minValue);
        }
        
        return minValue;
    }

    private double heuristic(GameState state) {
        Board board = state.getBoard();

        if (state.isGameOver()) {
            PlayerType winner = state.getWinner();
            return (winner == aiPlayer) ? 10000.0 : -10000.0;
        }
        
        double score = 0.0;
        int aiProgress = board.calculateProgress(aiPlayer);
        int opponentProgress = board.calculateProgress(aiPlayer.getOpponent());
        
        score += PROGRESS_WEIGHT * aiProgress;
        score -= OPPONENT_PROGRESS_WEIGHT * opponentProgress;

        List<Piece> aiPieces = board.getPs(aiPlayer);
        List<Piece> opponentPieces = board.getPs(aiPlayer.getOpponent());
        
        for (Piece p : aiPieces) {
            if (p.isFinished()) {
                score += FINISHED_BONUS;
            } else if (p.getPosition() >= 26) {
                score += SPECIAL_CELL_BONUS * (p.getPosition() - 25);
            }
        }
        
        for (Piece p : opponentPieces) {
            if (p.isFinished()) {
                score -= FINISHED_BONUS;
            } else if (p.getPosition() >= 26) {
                score -= SPECIAL_CELL_BONUS * (p.getPosition() - 25);
            }
        }

        for (Piece p : aiPieces) {
            if (p.isOnBoard() && isSafePosition(board, p)) {
                score += SAFETY_BONUS;
            }
        }

        for (Piece p : aiPieces) {
            if (p.getPosition() == 27) score -= 20.0;
        }
        
        for (Piece p : opponentPieces) {
            if (p.getPosition() == 27) score += 20.0;
        }
        
        return score;
    }

    private boolean isSafePosition(Board board, Piece piece) {
        int pos = piece.getPosition();
        
        CellType cellType = board.getC(pos).getType();
        if (cellType == CellType.HAPPINESS ||
            cellType == CellType.THREE_TRUTHS ||
            cellType == CellType.RE_ATOUM || 
            cellType == CellType.HORUS) {
            return true;
        }
        
        List<Piece> opponentPieces = board.getPs(piece.getOwner().getOpponent());
        
        for (Piece opponent : opponentPieces) {
            if (opponent.isOnBoard() && opponent.getPosition() < pos) {
                int distance = pos - opponent.getPosition();
                if (distance <= 5) {
                    return false;
                }
            }
        }
        
        return true;
    }

    
    private void printAlgorithmHeader() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              Expectiminimax Algorithm Started               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Max Depth: %-50d║\n", maxDepth);
        System.out.printf("║  AI Player: %-50s║\n", aiPlayer.name());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
    }
    
    private void printDetailedResults(Move bestMove, double bestValue, long timeMs) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Algorithm Results                          ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");

        System.out.printf("║  Nodes Visited: %-46d║\n", nodesVisited);
        System.out.printf("║  Best Move Evaluation: %-39.2f║\n", bestValue);

        System.out.printf("║  Time Elapsed: %-45d ms║\n", timeMs);
        
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║              All Moves Evaluation Summary                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < moveEvaluations.size(); i++) {
            MoveEvaluation eval = moveEvaluations.get(i);
            String marker = eval.move.equals(bestMove) ? " ★ BEST" : "";
            System.out.printf("║  [%d] %-45s ║\n", i + 1, eval.move.toString());
            System.out.printf("║      Evaluation: %-36.2f%s ║\n", 
                eval.value, marker);
        }
        
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Selected Move: %-46s║\n", 
            bestMove != null ? bestMove.toString() : "None");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
    }
    
    private void printNodeInfo(NodeType type, int depth, int level) {
        System.out.printf("%s[%s Node] Depth=%d, Level=%d\n", 
            indent(level), type.name(), depth, level);
    }
    
    private void printLeafValue(double value, int level) {
        System.out.printf("%s└─ Leaf evaluation: %.2f\n", indent(level), value);
    }
    
    private String indent(int level) {
        return "  ".repeat(level);
    }
    
    private static class MoveEvaluation {
        Move move;
        double value;
        
        MoveEvaluation(Move move, double value) {
            this.move = move;
            this.value = value;
        }
    }

    public int getNodesVisited() { return nodesVisited; }
    public void setVerboseMode(boolean verbose) { this.verboseMode = verbose; }
    public void setMaxDepth(int depth) { this.maxDepth = depth; }
}