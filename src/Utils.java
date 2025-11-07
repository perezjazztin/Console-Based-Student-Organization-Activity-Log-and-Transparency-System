import java.util.Scanner;
import java.util.regex.Pattern;

/*
 Utility input helpers (validated integer, double, date format MM-DD-YYYY)
*/
public class Utils {
    private static final Pattern DATE_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])\\-(0[1-9]|[12][0-9]|3[01])\\-\\d{4}$");

    public static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    public static int readInt(Scanner sc, int min, int max) {
        int v;
        do {
            v = readInt(sc);
            if (v < min || v > max) System.out.print("Enter a number between " + min + " and " + max + ": ");
        } while (v < min || v > max);
        return v;
    }

    public static double readDouble(Scanner sc, double min, double max) {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        double v = sc.nextDouble();
        sc.nextLine();
        if (v < min) v = min;
        return v;
    }

    // enforce MM-DD-YYYY format
    public static String readDate(Scanner sc) {
        String date;
        do {
            date = sc.nextLine().trim();
            if (!DATE_PATTERN.matcher(date).matches()) {
                System.out.print("Please enter date in MM-DD-YYYY format: ");
            } else break;
        } while (true);
        return date;
    }
}
