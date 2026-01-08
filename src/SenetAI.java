// ==================== SenetAI.java ====================
import java.util.*;

/**
 * فئة الذكاء الاصطناعي - تطبيق خوارزمية Expectiminimax
 */
public class SenetAI {
    private int maxDepth;
    private int nodesVisited;
    private boolean verboseMode;
    private PlayerType aiPlayer;
    
    // أوزان دالة التقييم
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
    }
    
    /**
     * الحصول على أفضل حركة باستخدام Expectiminimax
     */
    public Move getBestMove(GameState state) {
        nodesVisited = 0;
        long startTime = System.currentTimeMillis();
        
        if (verboseMode) {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║         algorithme start Expectiminimax                    ║");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.printf("║  max depth: %d                                      ║\n", maxDepth);
            System.out.printf("║ player: %s                                         ║\n", 
                aiPlayer.getArabicName());
            System.out.println("╚════════════════════════════════════════════════════════╝");
        }
        
        List<Move> validMoves = state.getValidMoves();
        
        if (validMoves.isEmpty()) {
            return null;
        }
        
        Move bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        
        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move);
            
            // بعد الحركة، يأتي دور رمي العصي للاعب التالي
            double value = expectiminimax(nextState, maxDepth - 1, NodeType.CHANCE);
            
            if (verboseMode) {
                System.out.printf("\n├─  *** moves: %s\n", move.getDetailedDescription());
                System.out.printf("│  value: %.2f\n", value);
            }
            
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        if (verboseMode) {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║               algorithme result                         ║");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.printf("║  best move: %s\n", 
                bestMove != null ? bestMove.toString() : "nothing ");
            System.out.printf("║ value : %.2f                                          ║\n", bestValue);
            System.out.printf("║   number of visited nodes: %d                                ║\n", nodesVisited);
            System.out.printf("║  time spend: %d ms                                ║\n", 
                endTime - startTime);
            System.out.println("╚════════════════════════════════════════════════════════╝");
        }
        
        return bestMove;
    }
    
    /**
     * خوارزمية Expectiminimax العودية
     */
    private double expectiminimax(GameState state, int depth, NodeType nodeType) {
        nodesVisited++;
        
        if (verboseMode && depth == maxDepth - 1) {
            System.out.printf("│  └─ [%s] depth: %d\n", 
                nodeType.getArabicName(), maxDepth - depth);
        }
        
        // حالات التوقف
        if (depth == 0 || state.isGameOver()) {
            double value = heuristic(state);
            if (verboseMode && depth == 0) {
                System.out.printf("│     └─ paper: *** = %.2f\n", value);
            }
            return value;
        }
        
        // عقدة الحظ (Chance Node)
        if (nodeType == NodeType.CHANCE) {
            return chanceNode(state, depth);
        }
        
        // عقدة التعظيم (Max Node) - الكمبيوتر
        if (nodeType == NodeType.MAX) {
            return maxNode(state, depth);
        }
        
        // عقدة التصغير (Min Node) - الإنسان
        return minNode(state, depth);
    }

    /**
     * معالجة عقدة الحظ - حساب القيمة المتوقعة
     */
    private double chanceNode(GameState state, int depth) {
        double expectedValue = 0.0;
        int[] possibleRolls = Dice.getPossibleRolls();
        
        for (int roll : possibleRolls) {
            double probability = Dice.getProbability(roll);
            
            if (probability == 0.0) continue;
            
            // إنشاء حالة جديدة مع الرمية
            GameState nextState = state.clone();
            nextState.setDiceValue(roll);
            
            // تحديد نوع العقدة التالية بناءً على اللاعب الحالي
            NodeType nextNodeType = (nextState.getCurrentPlayer() == aiPlayer) ? 
                NodeType.MAX : NodeType.MIN;
            
            double value = expectiminimax(nextState, depth, nextNodeType);
            expectedValue += probability * value;
        }
        
        return expectedValue;
    }
    
    /**
     * معالجة عقدة التعظيم (الكمبيوتر)
     */
    private double maxNode(GameState state, int depth) {
        List<Move> validMoves = state.getValidMoves();
        
        // إذا لم يكن هناك حركات، انتقل للاعب التالي
        if (validMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.changeTurn();
            return expectiminimax(nextState, depth - 1, NodeType.CHANCE);
        }
        
        double maxValue = Double.NEGATIVE_INFINITY;
        
        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move);
            nextState.changeTurn();
            
            double value = expectiminimax(nextState, depth - 1, NodeType.CHANCE);
            maxValue = Math.max(maxValue, value);
        }
        
        return maxValue;
    }
    
    /**
     * معالجة عقدة التصغير (الإنسان)
     */
    private double minNode(GameState state, int depth) {
        List<Move> validMoves = state.getValidMoves();
        
        // إذا لم يكن هناك حركات، انتقل للاعب التالي
        if (validMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.changeTurn();
            return expectiminimax(nextState, depth - 1, NodeType.CHANCE);
        }
        
        double minValue = Double.POSITIVE_INFINITY;
        
        for (Move move : validMoves) {
            GameState nextState = state.clone();
            nextState.applyMove(move);
            nextState.changeTurn();
            
            double value = expectiminimax(nextState, depth - 1, NodeType.CHANCE);
            minValue = Math.min(minValue, value);
        }
        
        return minValue;
    }
    
    /**
     * دالة التقييم (Heuristic Function)
     * تقيّم الرقعة من وجهة نظر الكمبيوتر
     */
    private double heuristic(GameState state) {
        Board board = state.getBoard();
        
        // إذا انتهت اللعبة
        if (state.isGameOver()) {
            PlayerType winner = state.getWinner();
            if (winner == aiPlayer) {
                return 10000.0;  // فوز الكمبيوتر
            } else {
                return -10000.0;  // فوز الإنسان
            }
        }
        
        double score = 0.0;
        
        // 1. تقدم الأحجار
        int aiProgress = board.calculateProgress(aiPlayer);
        int opponentProgress = board.calculateProgress(aiPlayer.getOpponent());
        
        score += PROGRESS_WEIGHT * aiProgress;
        score -= OPPONENT_PROGRESS_WEIGHT * opponentProgress;
        
        // 2. مكافأة الأحجار المنتهية
        List<Piece> aiPieces = board.getPieces(aiPlayer);
        List<Piece> opponentPieces = board.getPieces(aiPlayer.getOpponent());
        
        for (Piece p : aiPieces) {
            if (p.isFinished()) {
                score += FINISHED_BONUS;
            } else if (p.getPosition() >= 26) {
                // مكافأة إضافية للأحجار القريبة من النهاية
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
        
        // 3. مكافأة المواقع الآمنة (لا يوجد حجر خصم يمكنه الوصول)
        for (Piece p : aiPieces) {
            if (p.isOnBoard() && isSafePosition(board, p)) {
                score += SAFETY_BONUS;
            }
        }
        
        // 4. عقوبة للأحجار في مواقع خطرة (بيت الماء 27)
        for (Piece p : aiPieces) {
            if (p.getPosition() == 27) {
                score -= 20.0;
            }
        }
        
        for (Piece p : opponentPieces) {
            if (p.getPosition() == 27) {
                score += 20.0;
            }
        }
        
        return score;
    }
    
    /**
     * التحقق من أن الحجر في موقع آمن
     */
    private boolean isSafePosition(Board board, Piece piece) {
        int pos = piece.getPosition();
        
        // الأحجار في المربعات الخاصة نعتبرها آمنة نسبياً
        CellType cellType = board.getCell(pos).getType();
        if (cellType == CellType.HAPPINESS || 
            cellType == CellType.THREE_TRUTHS ||
            cellType == CellType.RE_ATOUM || 
            cellType == CellType.HORUS) {
            return true;
        }
        
        // التحقق من عدم إمكانية وصول حجر خصم
        List<Piece> opponentPieces = board.getPieces(piece.getOwner().getOpponent());
        
        for (Piece opponent : opponentPieces) {
            if (opponent.isOnBoard() && opponent.getPosition() < pos) {
                int distance = pos - opponent.getPosition();
                if (distance <= 5) {  // أقصى رمية هي 5
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // Getters
    public int getNodesVisited() { return nodesVisited; }
    public void setVerboseMode(boolean verbose) { this.verboseMode = verbose; }
    public void setMaxDepth(int depth) { this.maxDepth = depth; }
}