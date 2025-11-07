import java.util.*;
/*
 Officer: create activities, log planned expenses, view and edit proposed budget.
 Officer accounts must be verified by Admin before they can login.
*/
public class Officer extends User {
    private static final long serialVersionUID = 1L;

    private Organization organization;
    private List<Integer> myActivityIds = new ArrayList<>();
    private boolean verified = false;

    public Officer(String displayName, String username, String password, Organization organization) {
        super(displayName, username, password);
        this.organization = organization;
    }

    public Organization getOrganization() { return organization; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean val) { verified = val; }

    @Override
    public void showMenu(Scanner scanner, DataStore ds) {
        int opt;
        do {
            System.out.println("\n--- OFFICER MENU (" + organization.getName() + ") ---");
            System.out.println("1. Create new activity (Pending)");
            System.out.println("2. Log planned expense (will not deduct until approved)");
            System.out.println("3. View my submitted activities");
            System.out.println("4. Edit activity proposed budget / add additional expense");
            System.out.println("5. View organization remaining funds");
            System.out.println("6. Back");
            System.out.print("Choice: ");
            opt = Utils.readInt(scanner, 1, 6);

            switch (opt) {
                case 1 -> {
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Date (MM-DD-YYYY): ");
                    String date = Utils.readDate(scanner);
                    System.out.print("Venue: ");
                    String venue = scanner.nextLine();
                    System.out.print("Objectives: ");
                    String obj = scanner.nextLine();
                    System.out.print("Proposed budget (₱): ");
                    double budget = Utils.readDouble(scanner, 0, Double.MAX_VALUE);

                    Activity a = new Activity(title, date, venue, obj, getDisplayName(), organization.getName(), budget);
                    ds.addActivity(a);
                    myActivityIds.add(a.getId());
                    organization.addActivityId(a.getId());
                    ds.save(); // silent
                    System.out.println("Activity created (Pending). ID: " + a.getId());
                }
                case 2 -> {
                    List<Activity> list = ds.findActivitiesByOfficer(getDisplayName());
                    if (list.isEmpty()) {
                        System.out.println("You have no activities yet.");
                        break;
                    }
                    System.out.println("\n-- My Activities --");
                    for (Activity a : list) a.showSummary();
                    System.out.print("Enter activity ID to log planned expense: ");
                    int id = Utils.readInt(scanner);
                    Activity chosen = ds.findActivityById(id);
                    if (chosen == null || !chosen.getOfficerName().equals(getDisplayName())) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    if (!chosen.getStatus().equalsIgnoreCase("Pending")) {
                        System.out.println("You can still add extra expenses even if activity is approved (they will be treated as additional expenses).");
                    }
                    System.out.print("Expense description: ");
                    String desc = scanner.nextLine();
                    System.out.print("Amount (₱): ");
                    double amt = Utils.readDouble(scanner, 0, Double.MAX_VALUE);
                    System.out.print("Source (OrgFund / Sponsorship): ");
                    String src = scanner.nextLine();

                    Expense e = new Expense(desc, amt, src);
                    chosen.addExpense(e);
                    // if activity already approved, auto-approve expense and deduct if OrgFund
                    if (chosen.getStatus().equalsIgnoreCase("Approved")) {
                        e.setApproved(true);
                        if (src.equalsIgnoreCase("OrgFund")) organization.deduct(amt);
                    }
                    ds.save(); // silent
                    System.out.println("Planned expense added.");
                }
                case 3 -> {
                    List<Activity> list = ds.findActivitiesByOfficer(getDisplayName());
                    if (list.isEmpty()) {
                        System.out.println("You have no submitted activities.");
                    } else {
                        for (Activity a : list) a.showSummary();
                    }
                }
                case 4 -> {
                    System.out.println("\nEdit proposed budget or add extra expense:");
                    List<Activity> list = ds.findActivitiesByOfficer(getDisplayName());
                    if (list.isEmpty()) { System.out.println("No activities."); break; }
                    for (Activity a : list) a.showSummary();
                    System.out.print("Enter activity ID to edit: ");
                    int id = Utils.readInt(scanner);
                    Activity chosen = ds.findActivityById(id);
                    if (chosen == null || !chosen.getOfficerName().equals(getDisplayName())) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    System.out.println("1. Edit proposed budget");
                    System.out.println("2. Add additional expense (will be approved immediately if activity is Approved)");
                    System.out.print("Choice: ");
                    int sub = Utils.readInt(scanner, 1, 2);
                    if (sub == 1) {
                        System.out.print("New proposed budget (₱): ");
                        double newBudget = Utils.readDouble(scanner, 0, Double.MAX_VALUE);
                        chosen.setProposedBudget(newBudget);
                        ds.save();
                        System.out.println("Proposed budget updated.");
                    } else {
                        System.out.print("Expense description: ");
                        String desc = scanner.nextLine();
                        System.out.print("Amount (₱): ");
                        double amt = Utils.readDouble(scanner, 0, Double.MAX_VALUE);
                        System.out.print("Source (OrgFund / Sponsorship): ");
                        String src = scanner.nextLine();
                        Expense e = new Expense(desc, amt, src);
                        chosen.addExpense(e);
                        if (chosen.getStatus().equalsIgnoreCase("Approved")) {
                            e.setApproved(true);
                            if (src.equalsIgnoreCase("OrgFund")) organization.deduct(amt);
                        }
                        ds.save();
                        System.out.println("Additional expense recorded.");
                    }
                }
                case 5 -> organization.printFunds();
            }
        } while (opt != 6);
    }
}
