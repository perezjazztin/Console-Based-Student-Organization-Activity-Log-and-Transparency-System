import java.io.Serializable;
import java.time.LocalDateTime;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/*
 Activity holds proposed budget, planned expenses (unapproved) and later approved expenses.
*/
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int COUNTER = 1000;

    private int id;
    private String title;
    private String date; // MM-DD-YYYY
    private String venue;
    private String objectives;
    private String officerName;
    private String orgName;
    private String status; // Pending, Approved, Needs Clarification
    private double proposedBudget;
    private ArrayList<Expense> expenses = new ArrayList<>();
    private LocalDateTime timestamp;

    public Activity(String title, String date, String venue, String objectives,
                    String officerName, String orgName, double proposedBudget) {
        this.id = COUNTER++;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.objectives = objectives;
        this.officerName = officerName;
        this.orgName = orgName;
        this.status = "Pending";
        this.proposedBudget = proposedBudget;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getOfficerName() { return officerName; }
    public String getOrgName() { return orgName; }
    public ArrayList<Expense> getExpenses() { return expenses; }

    public void setStatus(String s) { status = s; }

    public void addExpense(Expense e) { expenses.add(e); }

    public double getPlannedExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public double getApprovedExpenses() {
        return expenses.stream().filter(Expense::isApproved).mapToDouble(Expense::getAmount).sum();
    }

    public double getProposedBudget() { return proposedBudget; }
    public void setProposedBudget(double b) { proposedBudget = b; }

    public void showSummary() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        System.out.printf("ID:%d - %s [%s] - Planned: %s - Approved: %s - Proposed Budget: %s%n",
                id, title, status, fmt.format(getPlannedExpenses()), fmt.format(getApprovedExpenses()), fmt.format(proposedBudget));
    }

    public void showDetails() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        System.out.println("\n=== Activity ID " + id + " ===");
        System.out.println("Title: " + title);
        System.out.println("Organization: " + orgName);
        System.out.println("Officer: " + officerName);
        System.out.println("Date: " + date + " / Venue: " + venue);
        System.out.println("Status: " + status);
        System.out.println("Objectives: " + objectives);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Proposed Budget: " + fmt.format(proposedBudget));
        System.out.println("Planned Expenses: " + fmt.format(getPlannedExpenses()));
        System.out.println("Approved Expenses: " + fmt.format(getApprovedExpenses()));
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("Expenses:");
            for (Expense e : expenses) e.show();
        }
    }
}
