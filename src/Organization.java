import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;

/*
 Organization stores funds and activity IDs linked to it
*/
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double totalFunds;
    private double remainingFunds;
    private ArrayList<Integer> activityIds = new ArrayList<>();

    public Organization(String name, double initialFunds) {
        this.name = name;
        this.totalFunds = initialFunds;
        this.remainingFunds = initialFunds;
    }

    public String getName() { return name; }
    public double getRemainingFunds() { return remainingFunds; }
    public double getTotalFunds() { return totalFunds; }

    public boolean deduct(double amount) {
        // deduct and allow negative (but return whether there was enough before deduction)
        boolean enough = remainingFunds >= amount;
        remainingFunds -= amount;
        return enough;
    }

    public void addFunds(double amount) {
        totalFunds += amount;
        remainingFunds += amount;
    }

    public void addActivityId(int id) { activityIds.add(id); }

    public void printFunds() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        System.out.println("\n--- Organization: " + name + " ---");
        System.out.println("Total Funds: " + fmt.format(totalFunds));
        System.out.println("Remaining Funds: " + fmt.format(remainingFunds));
    }
}
