// ==================== Dice.java ====================
import java.util.*;

/**
 * Class representing the four throwing sticks and their rolling logic
 * Rules:
 * - 4 sticks
 * - Each stick has a dark side (1) and a light side (0)
 * - Result:
 *   0 light sides  -> 5 points
 *   1–3 light sides -> same value
 *   4 light sides  -> 5 points
 */
public class Dice {
    private static final Random random = new Random();
    
    // Mathematical probabilities for each result
    // Calculated using binomial distribution: C(4,k) / 16
    private static final Map<Integer, Double> PROBABILITIES = new HashMap<>();
    private static final int[] POSSIBLE_ROLLS = {1, 2, 3, 4, 5};
    
    static {
        // Mathematical explanation:
        // 0 light (4 dark) -> 4C0 = 1/16 -> gives 5 points
        // 1 light (3 dark) -> 4C1 = 4/16 -> gives 1 point
        // 2 light (2 dark) -> 4C2 = 6/16 -> gives 2 points
        // 3 light (1 dark) -> 4C3 = 4/16 -> gives 3 points
        // 4 light (0 dark) -> 4C4 = 1/16 -> gives 5 points
        
        PROBABILITIES.put(1, 4.0 / 16.0);   // 0.25
        PROBABILITIES.put(2, 6.0 / 16.0);   // 0.375
        PROBABILITIES.put(3, 4.0 / 16.0);   // 0.25
        PROBABILITIES.put(4, 0.0);          // Not a valid roll
        PROBABILITIES.put(5, 2.0 / 16.0);   // 0.125 (two cases)
    }
    
    /**
     * Throw the four sticks and return the result (1–5)
     */
    public static int throwSticks() {
        int lightSide = 0;  // Number of light sides
        
        // Throw 4 sticks
        for (int i = 0; i < 4; i++) {
            if (random.nextBoolean()) {  // 50% light, 50% dark
                lightSide++;
            }
        }
        
        // Calculate result according to rules
        if (lightSide == 0 || lightSide == 4) {
            return 5;
        }
        return lightSide;
    }
    
    /**
     * Get the probability of a specific roll
     */
    public static double getProbability(int roll) {
        return PROBABILITIES.getOrDefault(roll, 0.0);
    }
    
    /**
     * Get all possible rolls
     */
    public static int[] getPossibleRolls() {
        return POSSIBLE_ROLLS;
    }
    
    /**
     * Get a map of all probabilities
     */
    public static Map<Integer, Double> getAllProbabilities() {
        return new HashMap<>(PROBABILITIES);
    }
    
    /**
     * Print probability table (for reports)
     */
    public static void printProbabilityTable() {
        System.out.println("\n=== Stick Throw Probability Table ===");
        System.out.println("Roll\t| Probability\t| Distribution");
        System.out.println("--------|---------------|------------");
        
        for (int roll : POSSIBLE_ROLLS) {
            double prob = getProbability(roll);
            int percentage = (int)(prob * 100);
            String bar = "█".repeat(percentage / 5);
            System.out.printf("%d\t| %.4f (%.1f%%)\t| %s\n",
                roll, prob, prob * 100, bar);
        }
        
        System.out.println("\nExplanation:");
        System.out.println("- Roll 1: 4 cases (1 light, 3 dark) = 4/16 = 25%");
        System.out.println("- Roll 2: 6 cases (2 light, 2 dark) = 6/16 = 37.5%");
        System.out.println("- Roll 3: 4 cases (3 light, 1 dark) = 4/16 = 25%");
        System.out.println("- Roll 5: 2 cases (0 or 4 light) = 2/16 = 12.5%");
    }
    
    /**
     * Visually simulate throwing the sticks
     */
    public static int throwSticksWithVisualization() {
        System.out.print("\nThrowing sticks: ");
        int lightSide = 0;
        
        for (int i = 0; i < 4; i++) {
            boolean isLight = random.nextBoolean();
            if (isLight) {
                System.out.print("○ ");  // Light side
                lightSide++;
            } else {
                System.out.print("● ");  // Dark side
            }
        }
        
        int result = (lightSide == 0 || lightSide == 4) ? 5 : lightSide;
        System.out.printf("-> Result: %d\n", result);
        return result;
    }
}
