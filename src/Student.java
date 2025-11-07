import java.util.List;

/*
 Student viewer: asks which org to view (does not show all orgs by default)
*/
public class Student extends User {
    private static final long serialVersionUID = 1L;

    public Student(String displayName) {
        super(displayName, "viewer", "");
    }

    @Override
    public void showMenu(java.util.Scanner scanner, DataStore ds) {
        int opt;
        do {
            System.out.println("\n--- STUDENT VIEW ---");
            System.out.println("1. View approved activities by organization");
            System.out.println("2. View transparency (approved expenses & balances) by organization");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            opt = Utils.readInt(scanner, 1, 3);

            switch (opt) {
                case 1 -> {
                    ds.printAllOrganizations();
                    System.out.print("Enter organization name to view approved activities (or blank to cancel): ");
                    String org = scanner.nextLine().trim();
                    if (!org.isEmpty()) ds.printApprovedActivitiesForOrg(org);
                }
                case 2 -> {
                    ds.printAllOrganizations();
                    System.out.print("Enter organization name to view transparency (or blank to cancel): ");
                    String org = scanner.nextLine().trim();
                    if (!org.isEmpty()) ds.printTransparencyForOrg(org);
                }
            }
        } while (opt != 3);
    }
}
