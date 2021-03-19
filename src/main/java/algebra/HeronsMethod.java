package algebra;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

import static java.math.RoundingMode.HALF_UP;

public class HeronsMethod {

    private static final Scanner input = new Scanner(System.in);
    private static boolean active = true;

    public static void main(String[] args) {
        System.out.println("Calculating of square root using Heron's method/Babylonian method");
        while (active) {
            try {
                Option choice = writeMainMenuAndReturnTheChoice();
                switch (choice) {
                    case EXIT: {
                        finishApplication();
                        break;
                    }
                    case FIND_BY_ITERATION: {
                        var result = findByIterations();
                        System.out.println("Result is " + result);
                        break;
                    }
                    case FIND_BY_PRECISION: {
                        var result = findByPrecision();
                        System.out.println("The number of iterations is " + result);
                        break;
                    }
                }
            } catch (IllegalStateException e) {
                System.out.println("Oops, I cannot understand this option. Try again.");
//            } catch (Exception e) {
//                System.out.println("Something goes wrong:");
//                System.out.println(e.toString());
            }
        }
        System.out.println(findApproximationByIterations(BigDecimal.valueOf(2), BigDecimal.valueOf(1), 10, 10));
    }

    private static Option writeMainMenuAndReturnTheChoice() {
        System.out.println("Choose the option:");
        System.out.println("0 - Close the application.");
        System.out.println("1 - Found the approximation of a number n (must give initial guess, number of iterations and precision).");
        System.out.println("2 - Found the number of iterations to achieve a certain precision for the square root of n (must give initial guess and precision).");
        System.out.print("Your choice: ");
        int result = input.nextInt();
        input.nextLine();
        return Option.valueOf(result);
    }

    private static void finishApplication() {
        System.out.println("Thank you for using my application!");
        active = false;
    }

    private static int findByPrecision() {
        System.out.println("Enter the values separated by whitespaces: number, initial guess, precision (number of decimal digits that must be the same):");
        while (true) {
            try {
                var number = input.nextBigDecimal();
                var initialGuess = input.nextBigDecimal();
                var precision = input.nextInt();
                return findNumberOfIterations(number, initialGuess, precision);
            } catch (NumberFormatException e) {
                System.out.println("One of the values is incorrect. Try again!");
            }
        }
    }

    private static BigDecimal findByIterations() {
        System.out.println("Enter the values separated by whitespaces: number, initial guess, number of iterations, precision (number of decimal digits):");
        while (true) {
            try {
                var number = input.nextBigDecimal();
                var initialGuess = input.nextBigDecimal();
                var numberOfIterations = input.nextInt();
                var precision = input.nextInt();
                return findApproximationByIterations(number, initialGuess, numberOfIterations, precision);
            } catch (NumberFormatException e) {
                System.out.println("One of the values is incorrect. Try again!");
            }
        }
    }

    private static int findNumberOfIterations(BigDecimal number, BigDecimal initialGuess, int precision) {
        BigDecimal standard = number.sqrt(MathContext.DECIMAL128);
        BigDecimal result = initialGuess;
        int iterations = 0;
        BigDecimal precisionBorder = BigDecimal.TEN.scaleByPowerOfTen(-precision);
        while (result.subtract(standard).abs().compareTo(precisionBorder) > 0) {
            iterations++;
            result = applyFormula(number, result, precision * 2);
        }
        System.out.println("The found number is " + result);
        return iterations;
    }

    private static BigDecimal findApproximationByIterations(BigDecimal number, BigDecimal initialGuess, int iterations, int precision) {
        BigDecimal result = initialGuess;
        for (int i = 0; i < iterations; i++)
            result = applyFormula(number, result, precision);
        return result;
    }

    private static final BigDecimal valueTwo = BigDecimal.valueOf(2);

    private static BigDecimal applyFormula(BigDecimal number, BigDecimal guess, int precision) {
        return number.divide(guess, precision, HALF_UP).add(guess).divide(valueTwo, precision, HALF_UP);
    }

    private enum Option {
        EXIT (0),
        FIND_BY_ITERATION(1),
        FIND_BY_PRECISION(2);

        private int number;

        Option(int number) {
            this.number = number;
        }

        // TODO: Rewrite in normal way
        public static Option valueOf(int number) {
            for (Option value: Option.values()) if (value.number == number) return value;
            throw new IllegalStateException("No option found for the number " + number + ".");
        }
    }
}
