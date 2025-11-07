import java.util.Scanner;

/*
 Main application entry and top-level menu
*/
public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static DataStore ds;

    public static void main(String[] args) {
        ds = DataStore.load();

        // ensure admin exists
        if (ds.getAdmin() == null) {
            ds.setAdmin(new Admin("admin", "osas123")); // default admin
            ds.save(); // silent save
        }

        System.out.println("Welcome — Student Organization Activity Log & Transparency System");
        mainLoop();
        scanner.close();

        ds.save(); // final save
        System.out.println("Data saved successfully. Goodbye!");
    }

    private static void mainLoop() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Admin");
            System.out.println("2. Organization Officer");
            System.out.println("3. Student Viewer");
            System.out.println("4. Exit");
            System.out.print("Choose role: ");
            int choice = Utils.readInt(scanner, 1, 4);

            switch (choice) {
                case 1 -> adminPath();
                case 2 -> officerPath();
                case 3 -> studentPath();
                case 4 -> { return; }
            }
        }
    }

    // Admin login & path
    private static void adminPath() {
        System.out.print("Admin username: ");
        String u = scanner.nextLine().trim();
        System.out.print("Admin password: ");
        String p = scanner.nextLine().trim();

        Admin admin = ds.getAdmin();
        if (admin != null && admin.login(u, p)) {
            admin.showMenu(scanner, ds);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    // Officer login or registration
    private static void officerPath() {
        System.out.println("\n--- Officer Access ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choice: ");
        int c = Utils.readInt(scanner, 1, 2);

        if (c == 1) {
            System.out.print("Organization name: ");
            String org = scanner.nextLine().trim();
            System.out.print("Officer username: ");
            String user = scanner.nextLine().trim();
            System.out.print("Officer password: ");
            String pass = scanner.nextLine().trim();

            Officer o = ds.findOfficer(org, user);
            if (o != null) {
                if (!o.isVerified()) {
                    System.out.println("Your officer account is not yet verified by Admin. Please wait for approval.");
                    return;
                }
                if (o.login(user, pass)) {
                    o.showMenu(scanner, ds);
                } else {
                    System.out.println("Login failed. Check username/password.");
                }
            } else {
                System.out.println("Officer account not found. If you just registered, wait for Admin verification.");
            }
        } else {
            // Register: creates org if needed; officer starts unverified
            System.out.print("Full name (display name): ");
            String name = scanner.nextLine().trim();
            System.out.print("Organization name: ");
            String orgName = scanner.nextLine().trim();

            Organization org = ds.findOrganization(orgName);
            if (org == null) {
                System.out.print("Initial fund for new organization (₱): ");
                double fund = Utils.readDouble(scanner, 0, Double.MAX_VALUE);
                org = new Organization(orgName, fund);
                ds.addOrganization(org);
                // no message about data file; silent save done internally
            }

            String username;
            do {
                System.out.print("Choose username (min 5 chars): ");
                username = scanner.nextLine().trim();
            } while (username.length() < 5);

            String password;
            do {
                System.out.print("Choose password (min 5 chars): ");
                password = scanner.nextLine();
            } while (password.length() < 5);

            Officer newOfficer = new Officer(name, username, password, org);
            newOfficer.setVerified(false); // starts unverified
            ds.addOfficer(newOfficer);
            ds.save(); // silent
            System.out.println("Officer registration submitted. Admin will verify your account.");
        }
    }

    // Student viewer (no login)
    private static void studentPath() {
        Student s = new Student("StudentViewer");
        s.showMenu(scanner, ds);
    }
}
