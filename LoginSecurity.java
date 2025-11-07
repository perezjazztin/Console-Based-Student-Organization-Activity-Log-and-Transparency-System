public static Officer officerLogin(ArrayList<Officer> officers, Scanner sc) {
    System.out.print("Enter Organization Name: ");
    String orgName = sc.nextLine();

    System.out.print("Enter Officer Username: ");
    String username = sc.nextLine();

    System.out.print("Enter Officer Password: ");
    String password = sc.nextLine();

    for (Officer officer : officers) {
        if (officer.getOrganization().getOrgName().equalsIgnoreCase(orgName)
                && officer.username.equals(username)
                && officer.login(password)) {

            return officer; // SUCCESS ✔
        }
    }

    System.out.println("❌ Invalid Officer Credentials.");
    return null;
}
