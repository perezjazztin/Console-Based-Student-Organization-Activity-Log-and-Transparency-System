import java.io.Serializable;
import java.time.LocalDateTime;
import java.text.NumberFormat;
import java.util.Locale;

/*
 Expense: planned by officer (approved flag false). When admin approves the activity,
 expenses become approved. Additional expenses added after approval will be auto-approved.
*/
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;
    private double amount;
    private String source;
    private LocalDateTime timestamp;
    private boolean approved = false;

    public Expense(String description, double amount, String source) {
        this.description = description;
        this.amount = amount;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    public double getAmount() { return amount; }
    public String getSource() { return source; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean val) { approved = val; }

    public void show() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        System.out.printf(" - %s: %s (Source: %s) Approved: %s Time: %s%n",
                description, fmt.format(amount), source, approved ? "Yes" : "No", timestamp);
    }
}
