// ==================== SenetGame.java ====================
import java.util.Scanner;

/**
 * الفئة الرئيسية لتشغيل لعبة السينت
 * 
 * مشروع خوارزميات البحث الذكية
 * جامعة دمشق - كلية الهندسة المعلوماتية
 * قسم الذكاء الصنعي
 * 
 * تطبيق لعبة السينت المصرية القديمة باستخدام:
 * - البرمجة كائنية التوجه (OOP)
 * - خوارزمية Expectiminimax للذكاء الاصطناعي
 * - دالة تقييم ذكية (Heuristic Function)
 */
public class SenetGame {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // عرض قائمة البداية
        displayMainMenu();
        
        // اختيار الوضع
        System.out.print("choose player mode ( 1 - 4 ) :");
        int choice = getIntInput(scanner, 1, 4);
        
        switch (choice) {
            case 1:
                // لعب عادي
                playGame(3, false, scanner);
                break;
            case 2:
                // لعب مع تفاصيل AI
                playGame(3, true, scanner);
                break;
            case 3:
                // اختيار مستوى الصعوبة
                selectDifficulty(scanner);
                break;
            case 4:
                // عرض معلومات الاحتمالات
                Dice.printProbabilityTable();
                System.out.print("press  Enter to return  ...");
                scanner.nextLine();
                main(args);  // العودة للقائمة الرئيسية
                break;
        }
        
        scanner.close();
    }
    
    /**
     * عرض القائمة الرئيسية
     */
   private static void displayMainMenu() {
    System.out.println("\n");
    System.out.println("╔═══════════════════════════════════════════════════════════════╗");
    System.out.println("║                                                               ║");
    System.out.println("║                     Senet Game                               ║");
    System.out.println("║              The Ancient Egyptian Board Game                 ║");
    System.out.println("║                                                               ║");
    System.out.println("╠═══════════════════════════════════════════════════════════════╣");
    System.out.println("║                                                               ║");
    System.out.println("║  Artificial Intelligence Search Algorithms Project            ║");
    System.out.println("║  Damascus University - Faculty of Information Technology      ║");
    System.out.println("║  Artificial Intelligence Department                           ║");
    System.out.println("║                                                               ║");
    System.out.println("╠═══════════════════════════════════════════════════════════════╣");
    System.out.println("║                     Select Game Mode:                         ║");
    System.out.println("║                                                               ║");
    System.out.println("║  1. Normal Play (Medium Difficulty)                           ║");
    System.out.println("║  2. Play with AI Details                                      ║");
    System.out.println("║  3. Select Difficulty Level                                   ║");
    System.out.println("║  4. Show Stick Roll Probability Table                         ║");
    System.out.println("║                                                               ║");
    System.out.println("╚═══════════════════════════════════════════════════════════════╝");
}

    
    /**
     * اختيار مستوى الصعوبة
     */
   /**
 * Select difficulty level
 */
private static void selectDifficulty(Scanner scanner) {
    System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
    System.out.println("║                   Select Difficulty Level                    ║");
    System.out.println("╠═══════════════════════════════════════════════════════════════╣");
    System.out.println("║  1. Easy    (Depth = 2)                                       ║");
    System.out.println("║  2. Medium  (Depth = 3)                                       ║");
    System.out.println("║  3. Hard    (Depth = 4)                                       ║");
    System.out.println("║  4. Expert  (Depth = 5)                                       ║");
    System.out.println("╚═══════════════════════════════════════════════════════════════╝");

    System.out.print("\nSelect level (1-4): ");
    int level = getIntInput(scanner, 1, 4);

    int depth = level + 1;  // 2, 3, 4, 5

    System.out.print("Show AI details? (y/n): ");
    String showDetails = scanner.nextLine().trim().toLowerCase();
    boolean verbose = showDetails.equals("y") || showDetails.equals("yes");

    playGame(depth, verbose, scanner);
}

    /**
     * بدء اللعبة
     */
    private static void playGame(int depth, boolean showAIDetails, Scanner scanner) {
        GameController controller = new GameController(depth, showAIDetails);
        controller.startGame();
        
        // سؤال اللاعب إذا أراد اللعب مرة أخرى
        System.out.print("do you want to play again :(y/n): ");
        String playAgain = scanner.nextLine().trim().toLowerCase();
        
        if (playAgain.equals("y") || playAgain.equals("yes")) {
            main(new String[0]);  
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                               ║");
            System.out.println("║                   !thanks salve goodbaye  👋                       ║");
            System.out.println("║                                                               ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        }
        
        controller.close();
    }
    
    /**
     * قراءة إدخال صحيح من المستخدم
     */
    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("❌ press enter number between : %d و %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print("❌  please enter showen number   : ");
            }
        }
    }
    

}