//final admin!!!

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User implements LoginLogout  {

    private Scanner scanner = new Scanner(System.in);

    public Admin(String username, String password, String phone, String email) {
        super(username, password, email, phone, "Admin");
    }

    @Override
    public void showDashboard() {
       
        System.out.println("Welcome "+getUsername());
        System.out.println("Admin Dashboard: Manage users and platform data.");

        while(true){

        System.out.println("Choose an option:");
        System.out.println("a: Block user");
        System.out.println("b: View platform data");
        System.out.println("c: Search user");
        System.out.println("d: Unblock user");
        System.out.println("e: View all users");

        System.out.println("logout: Logout");
        String choice = scanner.nextLine().toLowerCase();

        switch(choice){
            case("a"):
            System.out.println("Enter username to block:");
            String username = scanner.nextLine();
            deleteUsername(username);
            break;
            
            case("b"):
            System.out.println("Total admins: "+countProfile("admin"));
            System.out.println("Total influencers: "+countProfile("influencer"));
            System.out.println("Total brands: "+countProfile("brand"));
            System.out.println("Total advertisers: "+countProfile("advertiser"));
            //System.out.println("TOTAL PROFILES CREATED: "+ countProfile("admin") + countProfile("influencer")+countProfile("brand")+countProfile("advertiser"));
            break;

            case("c"):
            System.out.println("Enter username to search:");
            String searchUsername = scanner.nextLine();
            searchUser(searchUsername);
            break;

            case("d"):
            System.out.println("Enter username to unblock:");
            String unblockUsername = scanner.nextLine();
            unblockUser(unblockUsername);
            break;

            case("e"):
            System.out.println("Here is a list of all users on the platform-");
            viewAllUsers();
            break;

            case("logout"):
            System.out.println("Logging out.");
            System.exit(0);
            break;

            default:
            System.out.println("Invalid input.");
        }


        }
    }

    
    public int countProfile(String role) {
        File file = new File("users.txt");
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[2].trim().toLowerCase().equals(role.toLowerCase())) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.txt: " + e.getMessage());
        }

        return count;
    }
    
    // Method to search a user by username
    public void searchUser(String username) {
    File file = new File("users.txt");

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        boolean userFound = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 5 && parts[0].equalsIgnoreCase(username)) {
                // If user is found, print details
                System.out.println("User found:");
                System.out.println("Username: " + parts[0]);
                System.out.println("Password: " + parts[1]); 
                System.out.println("Role: " + parts[2]);
                System.out.println("Phone: " + parts[3]);
                System.out.println("Email: " + parts[4]);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User not found: " + username);
        }

    } catch (IOException e) {
        System.out.println("Error reading users.txt: " + e.getMessage());
    }
}


    public void deleteUsername(String usernameToDelete) {
    File file = new File("users.txt");
    List<String> updatedLines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        boolean userFound = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 5 && parts[0].equals(usernameToDelete)) {
                parts[1] = "DELETED";  // set password to "DELETED"
                userFound = true;
                System.out.println("User '" + usernameToDelete + "' has been marked as deleted.");
            }
            updatedLines.add(String.join(",", parts));
        }

        if (!userFound) {
            System.out.println("User not found: " + usernameToDelete);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        }

    } catch (IOException e) {
        System.out.println("Error updating users.txt: " + e.getMessage());
    }
}

    public void unblockUser(String usernameToUnblock) {
    File file = new File("users.txt");
    List<String> updatedLines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        boolean userFound = false;

        // Read through the file and process each line
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            
            // Check if the username matches
            if (parts.length >= 5 && parts[0].equalsIgnoreCase(usernameToUnblock)) {
                
                // If the user is already unblocked, inform the admin
                if (!parts[1].equals("DELETED")) {
                    System.out.println("User '" + usernameToUnblock + "' is already unblocked.");
                    return;  // Exit the method if the user is not in "DELETED" status
                }

                // If the user is marked as "DELETED", unblock them and reset the password
                parts[1] = "password";  // Restore password to "password"
                userFound = true;
                System.out.println("User '" + usernameToUnblock + "' has been unblocked.");
            }
            
            updatedLines.add(String.join(",", parts));  // Add the updated line to the list
        }

        // If the user was not found, notify the admin
        if (!userFound) {
            System.out.println("User not found or not marked as DELETED: " + usernameToUnblock);
            return;
        }

        // Write the updated lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();  // Add a new line after each updated line
            }
        }

    } catch (IOException e) {
        System.out.println("Error updating users.txt: " + e.getMessage());
    }
}

public void viewAllUsers() {
    File file = new File("users.txt");

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        boolean userFound = false;

        System.out.println("List of all users:");

        // Read through the file and print each user's details
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            
            // Check if the line has enough data (username, password, role, etc.)
            if (parts.length >= 5) {
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                String phone = parts[3];
                String email = parts[4];

                // Print user details
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                System.out.println("Role: " + role);
                System.out.println("Phone: " + phone);
                System.out.println("Email: " + email);
                System.out.println("----------------------------");
                userFound = true;
            }
        }

        if (!userFound) {
            System.out.println("No users found.");
        }

    } catch (IOException e) {
        System.out.println("Error reading users.txt: " + e.getMessage());
    }
}

    

    @Override
    public void login(String username, String password) {
        loggedIn = true;
        showDashboard();
    }
    @Override
    public void logout() {
        loggedIn = false;
        System.out.println("Admin logged out.");
    }
}
