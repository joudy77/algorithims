// ==================== Rules.java ====================
import java.util.*;

/**
 * فئة تحتوي على جميع قواعد اللعبة وحساب الحركات الممكنة
 */
public class Rules {
    
    /**
     * الحصول على جميع الحركات الممكنة للاعب في حالة معينة
     */
    public static List<Move> getValidMoves(Board board, PlayerType player, int diceValue) {
        List<Move> validMoves = new ArrayList<>();
        List<Piece> pieces = board.getPieces(player);
        
        for (Piece piece : pieces) {
            // تجاهل الأحجار المنتهية
            if (piece.isFinished()) {
                continue;
            }
            
            Move move = canMovePiece(board, piece, diceValue);
            if (move != null) {
                validMoves.add(move);
            }
        }
        
        return validMoves;
    }



// /**
//  * التحقق من إمكانية تحريك حجر معين
//  */
// public static Move canMovePiece(Board board, Piece piece, int steps) {
//     if (piece.isFinished()) {
//         return null;
//     }

//     int currentPos = piece.getPosition();
//     CellType currentCellType = board.getCell(currentPos).getType();

//     // ===== الخروج من البيوت الخاصة (28, 29, 30) =====

//     // بيت الحقائق الثلاث (28) → يحتاج 3
//     if (currentCellType == CellType.THREE_TRUTHS) {
//         if (steps == 3) {
//           // return new Move(piece, currentPos, 31, steps, false, true);
//           return new Move(piece, currentPos, currentPos + steps, steps, false, true);
//         }
//         return null;
//     }

//     // بيت إعادة أتوم (29) → يحتاج 2
//     if (currentCellType == CellType.RE_ATOUM) {
//         if (steps == 2) {
//            //   return new Move(piece, currentPos, 31, steps, false, true);
//             return new Move(piece, currentPos, currentPos + steps, steps, false, true);
//         }
//         return null;
//     }

//     // بيت حورس (30) → أي رمية
//     if (currentCellType == CellType.HORUS) {
//        // return new Move(piece, currentPos, currentPos + steps, steps, false, true);
//         return new Move(piece, currentPos, currentPos + steps, steps, false, true);
//     }

//     int targetPos = currentPos + steps;

//     // ===== الخروج العادي من اللعبة =====
//     // يجب أن يكون الحجر في المنطقة الأخيرة (26-30) ليتمكن من الخروج
//     if (targetPos > 30) {
//         if (currentPos >= 26) {  // في المنطقة الأخيرة
//             return new Move(piece, currentPos, targetPos, steps, false, true);
//         }
//         return null;  // لا يمكن الخروج من مكان بعيد
//     }

//     // ===== لا قفز فوق بيت السعادة (26) =====
//     if (currentPos < 26 && targetPos > 26) {
//         return null;
//     }

//     // ===== التحقق من أن الموقع المستهدف صالح =====
//     if (targetPos < 1) {
//         return null;  // لا يمكن الرجوع للخلف خارج اللوحة
//     }

//     // ===== فحص الخانة الهدف =====
//     Cell targetCell = board.getCell(targetPos);

//     if (targetCell.isEmpty()) {
//         // حركة عادية
//         return new Move(piece, currentPos, targetPos, steps, false, false);
//     } else {
//         Piece occupant = targetCell.getOccupant();

//         if (occupant.getOwner() == piece.getOwner()) {
//             // حجر من نفس اللاعب
//             return null;
//         } else {
//             // تبديل مع الخصم
//             return new Move(piece, currentPos, targetPos, steps, true, false);
//         }
//     }
// }



/**
 * التحقق من إمكانية تحريك حجر معين
 */
public static Move canMovePiece(Board board, Piece piece, int steps) {
    if (piece.isFinished()) {
        return null;
    }

    int currentPos = piece.getPosition();
    CellType currentCellType = board.getCell(currentPos).getType();

    // ===== الخروج من البيوت الخاصة (28, 29, 30) =====

    // بيت الحقائق الثلاث (28) → يحتاج 3 للخروج
    if (currentCellType == CellType.THREE_TRUTHS) {
        if (steps == 3) {
            // الخروج المباشر من اللعبة
            return new Move(piece, currentPos, 31, steps, false, true);
        }
        // رمية خاطئة - لا يمكن التحرك
        return null;
    }

    // بيت إعادة أتوم (29) → يحتاج 2 للخروج
    if (currentCellType == CellType.RE_ATOUM) {
        if (steps == 2) {
            // الخروج المباشر من اللعبة
            return new Move(piece, currentPos, 31, steps, false, true);
        }
        // رمية خاطئة - لا يمكن التحرك
        return null;
    }

    // بيت حورس (30) → أي رمية تخرج
    if (currentCellType == CellType.HORUS) {
        // أي رمية (1-5) تسمح بالخروج المباشر
        return new Move(piece, currentPos, 31, steps, false, true);
    }

    int targetPos = currentPos + steps;

    // ===== الخروج العادي من اللعبة =====
    // يجب أن يكون الحجر في المنطقة الأخيرة (26-30) ليتمكن من الخروج
    if (targetPos > 30) {
        if (currentPos >= 26) {
            // السماح بالخروج
            return new Move(piece, currentPos, targetPos, steps, false, true);
        }
        // لا يمكن الخروج من مكان بعيد
        return null;
    }

    // ===== لا قفز فوق بيت السعادة (26) =====
    if (currentPos < 26 && targetPos > 26) {
        return null;
    }

    // ===== التحقق من أن الموقع المستهدف صالح =====
    if (targetPos < 1) {
        return null;
    }

    // ===== فحص الخانة الهدف =====
    Cell targetCell = board.getCell(targetPos);

    if (targetCell.isEmpty()) {
        // حركة عادية
        return new Move(piece, currentPos, targetPos, steps, false, false);
    } else {
        Piece occupant = targetCell.getOccupant();

        if (occupant.getOwner() == piece.getOwner()) {
            // حجر من نفس اللاعب
            return null;
        } else {
            // تبديل مع الخصم
            return new Move(piece, currentPos, targetPos, steps, true, false);
        }
    }
}
/**
 * التحقق من القطع التي في المربعات الخاصة وإرجاعها للـ Rebirth إذا لم تحصل على الرمية الصحيحة
 * يتم استدعاء هذه الدالة في بداية دور اللاعب
 */
public static void checkAndPenalizeSpecialHouses(Board board, PlayerType player) {
    List<Piece> pieces = board.getPieces(player);
    
    for (Piece piece : pieces) {
        if (//piece.getState() == PieceState.WAITING_EXACT_ROLL && 
            piece.getNeedsExactRoll() != null) {
            
            int position = piece.getPosition();
            CellType cellType = board.getCell(position).getType();
            
            // القطعة في مربع خاص ولم تتحرك - يجب إرجاعها للـ Rebirth
            if (cellType == CellType.THREE_TRUTHS || 
                cellType == CellType.RE_ATOUM || 
                cellType == CellType.HORUS) {
                
                System.out.println("→ " + piece + " didn't get the correct roll, returning to rebirth house");
                returnToRebirth(board, piece);
            }
        }
    }
}
    /**
     * تطبيق حركة على الرقعة
     * @return true إذا نجحت الحركة
     */
  /**
 * تطبيق حركة على الرقعة
 * @return true إذا نجحت الحركة
 */
 public static boolean applyMove(Board board, Move move) {
        return applyMove(board, move, true);  // silent mode للمحاكاة
 }
public static boolean applyMove(Board board, Move move,boolean silent) {
    if (move == null) return false;

    Piece piece = move.getPiece();
    int fromPos = move.getFromPosition();

    // ===== حالة الخروج من اللعبة =====
    if (move.isExit()) {
        // إزالة الحجر من موقعه الحالي
        if (fromPos > 0 && fromPos <= 30) {
            board.getCell(fromPos).setOccupant(null);
        }

        piece.setPosition(0);
        piece.setState(PieceState.FINISHED);
        piece.setNeedsExactRoll(null);
        
         if (!silent) {
        System.out.println("→ " + piece + " finished and exited the board!");
         }
        return true;
    }

    int toPos = move.getToPosition();

    // ===== حالة التبديل =====
    if (move.isSwap()) {
        Piece opponent = board.getCell(toPos).getOccupant();

        // حماية إضافية
        if (opponent == null) return false;

        // إزالة القطعتين من مواقعهم
        board.getCell(fromPos).setOccupant(null);
        board.getCell(toPos).setOccupant(null);

        // تبديل المواقع
        piece.setPosition(toPos);
        opponent.setPosition(fromPos);

        // تنظيف حالة الرمية الخاصة للقطعة المتحركة فقط
        if (piece.getNeedsExactRoll() != null) {
            piece.setNeedsExactRoll(null);
            piece.setState(PieceState.ON_BOARD);
        }

        board.getCell(toPos).setOccupant(piece);
        board.getCell(fromPos).setOccupant(opponent);

        // تطبيق قواعد المربعات الخاصة
        applySpecialCellRules(board, piece, toPos,silent);
        applySpecialCellRules(board, opponent, fromPos,silent);

        return true;
    }

    // ===== حركة عادية =====

    // إزالة الحجر من موقعه القديم
    if (fromPos > 0 && fromPos <= 30) {
        board.getCell(fromPos).setOccupant(null);
    }

    piece.setPosition(toPos);

    // تنظيف حالة الرمية الخاصة إن وجدت
    if (piece.getNeedsExactRoll() != null) {
        piece.setNeedsExactRoll(null);
        piece.setState(PieceState.ON_BOARD);
    }

    board.getCell(toPos).setOccupant(piece);

    // تطبيق قواعد المربعات الخاصة
    applySpecialCellRules(board, piece, toPos,silent);

    return true;
}

    /**
     * تطبيق قواعد المربعات الخاصة
     */
/**
 * تطبيق قواعد المربعات الخاصة
 */
// private static void applySpecialCellRules(Board board, Piece piece, int position) {
private static void applySpecialCellRules(Board board, Piece piece, int position, boolean silent) {
    CellType cellType = board.getCell(position).getType();
    
    switch (cellType) {
        case WATER:  // بيت الماء (27)
            // العودة فوراً إلى بيت البعث
            returnToRebirth(board, piece);
             if (!silent) {
                System.out.println("→ " + piece + " fall in to the water, your block return to home");
             }
            break;
            
        case THREE_TRUTHS:  // بيت الحقائق الثلاث (28)
            // يحتاج رمية 3 بالضبط للخروج في الدور القادم
            // إذا لم يحصل على 3، سيعود للـ Rebirth
            piece.setNeedsExactRoll(3);
           
            piece.setState(PieceState.ON_BOARD);
            // piece.setState(PieceState.WAITING_EXACT_ROLL);
            if (!silent) {
            System.out.println("→ " + piece + " inside the fact house, need three moves to finish");
            }
            break;
            
            case RE_ATOUM:  // بيت إعادة أتوم (29)
            // يحتاج رمية 2 بالضبط للخروج في الدور القادم
            // إذا لم يحصل على 2، سيعود للـ Rebirth
            piece.setNeedsExactRoll(2);
            piece.setState(PieceState.ON_BOARD);
        //    piece.setState(PieceState.WAITING_EXACT_ROLL);
            if (!silent) {
            System.out.println("→ " + piece + " inside atom house, need two moves to finish");
            }
            break;
            
         case HORUS:  // بيت حورس (30)
            // يحتاج أي رمية للخروج في الدور القادم
            // إذا لم يُحرك في الدور القادم، سيعود للـ Rebirth
            piece.setNeedsExactRoll(1);  // أي رمية ستكون >= 1
            piece.setState(PieceState.ON_BOARD);
          //  piece.setState(PieceState.WAITING_EXACT_ROLL);
          if (!silent) {
            System.out.println("→ " + piece + " inside horus house, any move to finish");
          }
            break;
            
        case HAPPINESS:  // بيت السعادة (26)
        if (!silent) {
          System.out.println("→ " + piece + " inside happy house!");
         }
        break;
            
        case REBIRTH:  // بيت البعث (15)
         if (!silent) {
            System.out.println("→ " + piece + " inside the rebirth house");
         }
            break;
            
        default:
            // مربع عادي - لا توجد قواعد خاصة
            break;
    }
}
/**
 * التحقق من القطع التي تحتاج رمية محددة وإرجاعها للـ Rebirth إذا لم تتحرك
 * يتم استدعاء هذه الدالة في نهاية دور اللاعب
 */
public static void checkSpecialHousePenalties(Board board, PlayerType player) {
    List<Piece> pieces = board.getPieces(player);
    
    for (Piece piece : pieces) {
        if (//piece.getState() == PieceState.WAITING_EXACT_ROLL && 
            piece.getNeedsExactRoll() != null) {
            
            int position = piece.getPosition();
            
            // التحقق من أن القطعة لم تتحرك من موقعها الخاص
            if (position == 28 || position == 29 || position == 30) {
                // القطعة لم تتحرك، يجب إرجاعها للـ Rebirth
                System.out.println("→ " + piece + " didn't move from special house, returning to rebirth");
                returnToRebirth(board, piece);
            }
        }
    }
}
    /**
     * إعادة حجر إلى بيت البعث (المربع 15)
     */
    private static void returnToRebirth(Board board, Piece piece) {
        int currentPos = piece.getPosition();
        
        // إزالة الحجر من موقعه الحالي
        if (currentPos > 0 && currentPos <= 30) {
            board.getCell(currentPos).setOccupant(null);
        }
        
        // إيجاد أول مربع فارغ عند أو قبل المربع 15
        int targetPos = 15;
        while (targetPos > 0 && !board.getCell(targetPos).isEmpty()) {
            targetPos--;
        }
        
        if (targetPos > 0) {
            piece.setPosition(targetPos);
            piece.setState(PieceState.ON_BOARD);
            piece.setNeedsExactRoll(null);
            board.getCell(targetPos).setOccupant(piece);
        } else {
            // في حالة نادرة: جميع المربعات قبل 15 مشغولة
            piece.setPosition(1);
            piece.setState(PieceState.ON_BOARD);
            piece.setNeedsExactRoll(null);
            board.getCell(1).setOccupant(piece);
        }
    }
    
    /**
     * التحقق من صحة حركة على رقعة
     */
    public static boolean isValidMove(Board board, Move move) {
        if (move == null) return false;
        
        Piece piece = move.getPiece();
        List<Move> validMoves = getValidMoves(board, piece.getOwner(), move.getSteps());
        
        for (Move valid : validMoves) {
            if (valid.getPiece().getId() == piece.getId() &&
                valid.getFromPosition() == move.getFromPosition() &&
                valid.getToPosition() == move.getToPosition()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * نسخ الرقعة بشكل عميق
     */
    public static Board cloneBoard(Board board) {
        return new Board(board);
    }
    
    /**
     * تطبيق حركة على نسخة من الرقعة وإرجاع النسخة الجديدة
     */
public static Board applyMoveCloned(Board board, Move move) {
    Board newBoard = cloneBoard(board);

    Piece clonedPiece = newBoard.findPiece(
            move.getPiece().getId(),
            move.getPiece().getOwner()
    );

    Move clonedMove = new Move(
            clonedPiece,
            move.getFromPosition(),
            move.getToPosition(),
            move.getSteps(),
            move.isSwap(),
            move.isExit()
    );

    applyMove(newBoard, clonedMove);
    return newBoard;
}



    
    /**
     * طباعة جميع الحركات الممكنة
     */
    public static void printValidMoves(List<Move> moves) {
        if (moves.isEmpty()) {
            System.out.println("their isn't any avilable moves");
            return;
        }
        
        System.out.println("\n=== possible moves ===");
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, moves.get(i).getDetailedDescription());
        }
    }

    /**
 * معاقبة القطع في البيوت الخاصة إذا لم تتحرك
 * يُستدعى في نهاية الدور
 */
public static void penalizeSpecialHouses(Board board, PlayerType player, Move appliedMove,boolean silent) {
    List<Piece> pieces = board.getPieces(player);
    
    for (Piece piece : pieces) {
        if (piece.isFinished()) {
            continue;
        }
        
        // إذا كانت هذه القطعة هي التي تحركت، لا تعاقبها
        if (appliedMove != null && piece.getId() == appliedMove.getPiece().getId()) {
            continue;
        }
        
        int position = piece.getPosition();
        CellType cellType = board.getCell(position).getType();
        
        // معاقبة القطع التي لم تتحرك من البيوت الخاصة
        if (cellType == CellType.THREE_TRUTHS || 
            cellType == CellType.RE_ATOUM || 
            cellType == CellType.HORUS) {
            
    if (!silent) {  // أضف هذا الشرط
       System.out.println("→ " + piece + " didn't move from special house, returning to rebirth");
        }
           returnToRebirth(board, piece);
        }
    }
}
}