import java.util.Scanner;
import java.util.List;

/*
 Admin: can approve activities, approve officer accounts, view transparency and balances
*/
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String username, String password) {
        super("Administrator", username, password);
    }

    @Override
    public void showMenu(Scanner scanner, DataStore ds) {
        int opt;
        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View all activities (all orgs)");
            System.out.println("2. Approve / Request Clarification for an activity");
            System.out.println("3. View transparency reports (per org)");
            System.out.println("4. View organization balances");
            System.out.println("5. Approve Officer Registrations");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            opt = Utils.readInt(scanner, 1, 6);

            switch (opt) {
                case 1 -> ds.printAllActivitiesWithIds();
                case 2 -> {
                    ds.printAllActivitiesWithIds();
                    System.out.print("Enter activity ID to process: ");
                    int id = Utils.readInt(scanner);
                    Activity a = ds.findActivityById(id);
                    if (a == null) {
                        System.out.println("Activity not found.");
                    } else {
                        System.out.print("Approve? (Y = approve, N = request clarification): ");
                        boolean approve = scanner.nextLine().trim().equalsIgnoreCase("Y");
                        if (approve) {
                            a.setStatus("Approved");
                            // approve expenses and deduct org funds for OrgFund expenses
                            for (Expense e : a.getExpenses()) {
                                if (!e.isApproved()) {
                                    e.setApproved(true);
                                    if (e.getSource().equalsIgnoreCase("OrgFund")) {
                                        Organization org = ds.findOrganization(a.getOrgName());
                                        if (org != null) org.deduct(e.getAmount());
                                    }
                                }
                            }
                            System.out.println("Activity approved and expenses finalized.");
                        } else {
                            a.setStatus("Needs Clarification");
                            System.out.println("Activity marked as 'Needs Clarification'. Officer must revise.");
                        }
                        ds.save(); // silent
                    }
                }
                case 3 -> {
                    ds.printAllOrganizations();
                    System.out.print("Enter organization name to view report (or blank to cancel): ");
                    String org = scanner.nextLine().trim();
                    if (!org.isEmpty()) ds.printTransparencyForOrg(org);
                }
                case 4 -> ds.printOrganizationBalances();
                case 5 -> {
                    List<Officer> pending = ds.getPendingOfficers();
                    if (pending.isEmpty()) {
                        System.out.println("No pending officer registrations.");
                    } else {
                        System.out.println("\nPending Officers:");
                        for (int i = 0; i < pending.size(); i++) {
                            Officer of = pending.get(i);
                            System.out.printf("%d) %s - Org: %s - Username: %s%n",
                                    i+1, of.getDisplayName(), of.getOrganization().getName(), of.getUsername());
                        }
                        System.out.print("Enter index to approve (0 to cancel): ");
                        int idx = Utils.readInt(scanner, 0, pending.size());
                        if (idx != 0) {
                            Officer selected = pending.get(idx - 1);
                            selected.setVerified(true);
                            System.out.println("Officer '" + selected.getDisplayName() + "' has been verified.");
                            ds.save(); // silent
                        }
                    }
                }
            }
        } while (opt != 6);
    }
}
