import java.io.*;
import java.util.*;

/*
 DataStore: holds admin, organizations, officers, and activities.
 Uses Java serialization. Saves silently; MainApp prints friendly save on exit.
*/
public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE = "data.ser";

    private Admin admin;
    private List<Organization> organizations = new ArrayList<>();
    private List<Officer> officers = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();

    // admin
    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin a) { admin = a; }

    // organizations
    public void addOrganization(Organization o) { organizations.add(o); }
    public Organization findOrganization(String name) {
        for (Organization o : organizations) if (o.getName().equalsIgnoreCase(name)) return o;
        return null;
    }
    public void printAllOrganizations() {
        System.out.println("\n--- Organizations ---");
        if (organizations.isEmpty()) System.out.println("No organizations registered.");
        for (Organization o : organizations) System.out.println(" - " + o.getName());
    }

    // officers
    public void addOfficer(Officer o) { officers.add(o); }
    public Officer findOfficer(String orgName, String username) {
        for (Officer o : officers)
            if (o.getOrganization().getName().equalsIgnoreCase(orgName) && o.getUsername().equals(username))
                return o;
        return null;
    }
    public List<Officer> getPendingOfficers() {
        List<Officer> list = new ArrayList<>();
        for (Officer o : officers) if (!o.isVerified()) list.add(o);
        return list;
    }

    // activities
    public void addActivity(Activity a) { activities.add(a); }
    public Activity findActivityById(int id) {
        for (Activity a : activities) if (a.getId() == id) return a;
        return null;
    }
    public List<Activity> findActivitiesByOfficer(String officerName) {
        List<Activity> list = new ArrayList<>();
        for (Activity a : activities) if (a.getOfficerName().equals(officerName)) list.add(a);
        return list;
    }

    // printing helpers
    public void printAllActivitiesWithIds() {
        System.out.println("\n--- All Activities ---");
        if (activities.isEmpty()) System.out.println("No activities submitted yet.");
        for (Activity a : activities) {
            System.out.printf("ID:%d - %s [%s] - Org: %s%n", a.getId(), a.getTitle(), a.getStatus(), a.getOrgName());
        }
    }

    public void printApprovedActivitiesForOrg(String orgName) {
        Organization org = findOrganization(orgName);
        if (org == null) { System.out.println("Organization not found."); return; }
        System.out.println("\n--- Approved Activities for " + orgName + " ---");
        boolean found = false;
        for (Activity a : activities) {
            if (a.getOrgName().equalsIgnoreCase(orgName) && a.getStatus().equalsIgnoreCase("Approved")) {
                a.showSummary(); found = true;
            }
        }
        if (!found) System.out.println("No approved activities for this organization.");
    }

    public void printTransparencyForOrg(String orgName) {
        Organization org = findOrganization(orgName);
        if (org == null) { System.out.println("Organization not found."); return; }
        System.out.println("\n=== Transparency Report for " + org.getName() + " ===");
        boolean found = false;
        for (Activity a : activities) {
            if (a.getOrgName().equalsIgnoreCase(orgName) && a.getStatus().equalsIgnoreCase("Approved")) {
                a.showDetails(); found = true;
            }
        }
        if (!found) System.out.println("No approved activities to show for this organization.");
        org.printFunds();
    }

    public void printOrganizationBalances() {
        if (organizations.isEmpty()) System.out.println("No organizations.");
        for (Organization o : organizations) o.printFunds();
    }

    // persistence: saves silently (no console output)
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(this);
        } catch (Exception e) {
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }

    public static DataStore load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof DataStore) return (DataStore) obj;
        } catch (FileNotFoundException e) {
            // first run: ignore
        } catch (Exception e) {
            System.out.println("Failed to load data: " + e.getMessage());
        }
        return new DataStore();
    }
}
