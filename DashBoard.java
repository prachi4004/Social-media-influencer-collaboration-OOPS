//LATEST DASHBOARD!!!!

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
//import social.*;  //error?

public class DashBoard {
	private String role;
    private String username;
    private String password;
    private String email;
    private String phone;

    static ArrayList<User> users = new ArrayList<>();

    private static final String ADMIN_SECRET_KEY = "admin123";
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

    
    
    loadUsersFromFile();   //read user.txt and make <User>users array list
     
    Scanner scanner = new Scanner(System.in); 
   	System.out.println("Enter Login OR Signup");
   	String Opt=scanner.nextLine();
   	 if(Opt.equalsIgnoreCase("Signup")) {
   		DashBoard signup = new DashBoard();
        signup.startSignUp();
   	 }
   	 
	 else if(Opt.equalsIgnoreCase("Login")) {
		 DashBoard login = new DashBoard();
         try{
         login.startLogin();
         }

         catch(InvalidCredentialException e)
         {
            System.out.println(e.getMessage());
         }
	 }
	 else {              //
		System.out.println("Invalid input.");
	 }
   	scanner.close(); 
    }


    public static void loadUsersFromFile() {
        try {
            FileReader f1 = new FileReader("admins.txt");
            FileReader f2 = new FileReader("influencers.txt");
            FileReader f3 = new FileReader("brands.txt");
            FileReader f4 = new FileReader("advertisers.txt");
    
            Scanner sc1 = new Scanner(f1);
            Scanner sc2 = new Scanner(f2);
            Scanner sc3 = new Scanner(f3);
            Scanner sc4 = new Scanner(f4);
    
            // Load Admins
            while (sc1.hasNextLine()) {
                String line = sc1.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                Admin admin = new Admin(parts[0], parts[1], parts[2], parts[3]);
                admin.role = "admin";
                users.add(admin);
    
            }
    
            // Load Influencers
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
    
                String username = parts[0];
                String password = parts[1];
                String email = parts[2];
                String phone = parts[3];
                String niche = parts[4];
                int minAge = Integer.parseInt(parts[5]);
                int maxAge = Integer.parseInt(parts[6]);
                String bio = parts[7];
    
                // Platform parsing
                String platformRaw = parts[8];
                String[] platformData = platformRaw.split("\\|"); // escape the | character
                Platform[] platforms = new Platform[platformData.length];
    
                for (int i = 0; i < platformData.length; i++) {
                    String[] pf = platformData[i].split(":");
                    platforms[i] = new Platform(pf[0], pf[1], Integer.parseInt(pf[2]), Double.parseDouble(pf[3]));
                }
    
                Influencer influencer = new Influencer(username, password, email, phone, niche, minAge, maxAge, bio, platforms);
                influencer.role="influencer";
                users.add(influencer);
            }
    
            // Load BrandManagers
            while (sc3.hasNextLine()) {
                String line = sc3.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
    
                String username = parts[0];
                String password = parts[1];
                String email = parts[2];
                String phone = parts[3];
                String niche = parts[4];
                int maxCapacity = Integer.parseInt(parts[5]);
    
                // No advertiser object yet, will assign when Advertiser is created
                BrandManager brandManager = new BrandManager(username, password, email, phone, niche, maxCapacity, null);
                brandManager.role = "brand";
                users.add(brandManager);
            }
    
            // Load Advertisers
            while (sc4.hasNextLine()) {
                String line = sc4.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
    
                String username = parts[0];
                String password = parts[1];
                String email = parts[2];
                String phone = parts[3];
                String brandUsername = parts[4];
    
                // Find BrandManager based on advertiser username
                BrandManager brand = null;
    
                for (User u : users) {
                    if (u instanceof BrandManager && u.getUsername().equals(brandUsername)) {
                        brand = (BrandManager) u;
                        break;
                    }
                }
    
                if (brand == null) {
                    System.out.println("BrandManager not found for advertiser: " + username);
                    continue;
                }
    
                // Get all Influencers
                ArrayList<Influencer> influencerList = new ArrayList<>();
                for (User u : users) {
                    if (u instanceof Influencer) {
                        influencerList.add((Influencer) u);
                    }
                }
                Influencer[] influencerArray = influencerList.toArray(new Influencer[0]);
    
                // Create Advertiser
                Advertiser advertiser = new Advertiser(username, password, email, phone, brand, influencerArray);
                advertiser.role = "advertiser";
                users.add(advertiser);
    
                // Now link brandManager to this advertiser
                //brand.setAdvertiser(advertiser);
            }
    
            sc1.close();
            sc2.close();
            sc3.close();
            sc4.close();
    
        } catch (IOException e) {
            System.out.println("All files are not created.");
        }
    }
    
    //done
    public void startLogin() throws InvalidCredentialException{
        boolean usernameValid = false;
        String loginUsername = "";
        
        while (!usernameValid) {
            System.out.print("Enter Username (or type 'exit' to exit): ");
            loginUsername = scanner.nextLine().trim();
    
            if (loginUsername.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program...");
                System.exit(0);  // Exit the program
            }
    
            try {
                // Check if the username exists in the users list
                boolean usernameFound = false;

                for (User user : users) {
                    //System.out.println(user.getUsername()+"USer check");
                    if (user.getUsername().equals(loginUsername)) {
                        usernameFound = true;
                        break;
                    }
                }
    
                if (!usernameFound) {
                    // Throw custom exception for invalid username
                    throw new InvalidCredentialException("Username not found! Please try again.");
                }
                
                usernameValid = true; // Exit loop if username is valid
            } catch (InvalidCredentialException e) {
                // Print the exception message and prompt the user to try again
                System.out.println(e.getMessage());
            }
        }

        //got username till this point
        if(checkIfBlocked(loginUsername))
        {
            System.out.println("User is blocked by administration.");
            System.exit(0);
        }

        
        System.out.print("Enter Password: ");
        String loginPassword = scanner.nextLine();

        System.out.print("Enter Role (admin/influencer/advertiser/brand): ");
        String loginRole = scanner.nextLine().toLowerCase();

        boolean loggedIn = false;
        //User currentUser=null;
        for (User user : users) {
            
            if (user.getUsername().equals(loginUsername) && user.getRole().equalsIgnoreCase(loginRole)) { //found user
                if(user.getPassword().equals(loginPassword))
                {
                	switch(loginRole) {
                	case "admin":
                		((Admin)user).login(loginUsername, loginPassword);
                        break;
                    case "influencer":
                    	((Influencer)user).login(loginUsername, loginPassword);
                   	 
                        break;
                    case "brand":
                    	((BrandManager)user).login(loginUsername, loginPassword);
                        break;
                    case "advertiser":
                    	((Advertiser)user).login(loginUsername, loginPassword);
                        break;
                    default:
                        System.out.println("Invalid role entered.");
                	}

                    loggedIn = true;
                    break;
                    
                } 

            }
                
            }
        
        
        if(!loggedIn) throw new InvalidCredentialException("Wrong credentials entered."); 
    }



    //done
    public void startSignUp() {
            username = "";

            boolean usernameValid = false;

            // Loop until a unique username is entered or user exits
            while (!usernameValid) {
                System.out.print("Enter Username (or type 'exit' to quit): ");
                username = scanner.nextLine().trim();
        
                if (username.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting signup...");
                    return;
                }
        
                try {
                    // Check if username already exists
                    boolean exists = false;
                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(username)) {
                            exists = true;
                            break;
                        }
                    }
        
                    if (exists) {
                        throw new UsernameAlreadyExistsException("Username already exists! Please try another one.");
                    }
        
                    usernameValid = true;  // Exit loop if username is unique
        
                } catch (UsernameAlreadyExistsException e) {
                    System.out.println(e.getMessage());
                }
            }

            //keep stuck in loop until they enter both passwords
            boolean passwordNotMatched = true;
            while(passwordNotMatched){
            System.out.print("Enter Password: ");
            password = scanner.nextLine();
             
            System.out.print("Confirm Password: ");
            String password2 = scanner.nextLine();

             // Check if passwords match
            if (!password.equals(password2)) {
                System.out.println("Passwords don't match! Try again!");
            }

            else passwordNotMatched = false;
        }

            System.out.print("Enter Role (admin, influencer, brand, advertiser): ");
            role = scanner.nextLine();
             
            System.out.print("Enter your Phone Number: ");
            phone = scanner.nextLine();
             
            System.out.print("Enter your Email: ");
            email = scanner.nextLine();
            
           

            //USERNAME, PASSWORD, ROLE, PHONE, EMAIL
             // Append the login data to file
            try (FileWriter fw = new FileWriter("users.txt", true)) {
                 fw.write(username + "," + password + "," + role+ "," + phone+ "," + email + "\n");
                 System.out.println("Login data recorded successfully.");
             } 
             
            catch (IOException e) {
                 System.out.println("An error occurred while writing to the file.");
                 e.printStackTrace();
             }
             
            switch (role.toLowerCase()) {
             case "admin":
                 handleAdminSignup();
                 break;
             case "influencer":
                 handleInfluencerSignup();
                 break;
             case "brand":
                 handleBrandSignup();
                 break;
             case "advertiser":
                 handleAdvertiserSignup();
                 break;
             default:
                 System.out.println("Invalid role entered.");
             }
    		  	
     }
    
    //done
    private void writeToFile(String filename, String data) {
         try (FileWriter fw = new FileWriter(filename, true)) {
             fw.write(data);
         } catch (IOException e) {
             System.out.println("Error writing to file: " + filename);
             e.printStackTrace();
         }
     }

    //done
    private void handleAdminSignup() {
         System.out.print("Enter secret key: ");
         String key = scanner.nextLine();
         if (ADMIN_SECRET_KEY.equals(key)) {
             String data = username + "," + password + ","+phone+"," + email+"\n";
             writeToFile("admins.txt", data);
             System.out.println("Admin signed up successfully!");
         } 
         
        else {
             System.out.println("Incorrect secret key. Signup failed.");
             System.exit(0);
         }
         
     }
    
    //done
    private void handleInfluencerSignup() {
         System.out.println("Enter the number of social media platforms you want to register:");
         int num = Integer.parseInt(scanner.nextLine());

        String[] platforms = new String[num];
        String[] handles = new String[num];
        int[] followers = new int[num];
        double[] engagementRates = new double[num];

         for (int i = 0; i < num; i++) {
             System.out.print("Enter platform name (Instagram/Facebook/Twitter/YouTube): ");
             platforms[i] = scanner.nextLine();

             System.out.print("Enter username on " + platforms[i] + ": ");
             handles[i] = scanner.nextLine();

             System.out.print("Enter followers on " + platforms[i] + ": ");
             followers[i] = Integer.parseInt(scanner.nextLine());

             System.out.print("Enter engagement rate on " + platforms[i] + ": ");
             engagementRates[i] = Double.parseDouble(scanner.nextLine());
         }

         System.out.print("Enter your niche: ");
         String niche = scanner.nextLine();

         System.out.print("Enter your age group (e.g. 18-25). ");
         System.out.print("Min age: ");
         int minAge = Integer.parseInt(scanner.nextLine());

         System.out.print("Max age:");
         int maxAge = Integer.parseInt(scanner.nextLine());

         System.out.print("Enter your bio: ");
         String bio = scanner.nextLine();

         // Build platform data string
         StringBuilder platformData = new StringBuilder();
         for (int i = 0; i < num; i++) {
             platformData.append(platforms[i])
                         .append(":").append(handles[i])
                         .append(":").append(followers[i])
                         .append(":").append(engagementRates[i]);
             if (i != num - 1) platformData.append("|");
         }
        

         //username,password,email,phone,niche,minAge,maxAge,bio,platformData
         String data = username + "," + password + "," +email+","+phone+","+ niche + "," + minAge +"," + maxAge + "," + bio + ","+ platformData + "\n";

        writeToFile("influencers.txt", data);
         //Create constructor
        System.out.println("Influencer signed up successfully!");
     }
    
    //dpme
    private void handleBrandSignup() {
         System.out.print("Enter brand niche: ");
         String niche = scanner.nextLine();

         System.out.print("Enter max campaign capacity: ");
         int capacity = Integer.parseInt(scanner.nextLine());

        String data = username + "," + password + "," + email + "," + phone + "," + niche + "," + capacity + "\n";
        writeToFile("brands.txt", data);
        System.out.println("Brand signed up successfully!");
     }
    
    //done
    private void handleAdvertiserSignup() {
     	System.out.print("Enter associated brand's username on this platform: ");
        String brandName = scanner.nextLine();

        String data =  username + "," + password + "," + email + "," + phone + "," + brandName + "\n";
        writeToFile("advertisers.txt", data);
        System.out.println("Advertiser signed up successfully!");
     }
     

    public static boolean checkIfBlocked(String username) {
        try (Scanner sc = new Scanner(new FileReader("users.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue; // Skip empty lines
    
                String[] parts = line.split(",");
                if (parts.length < 5) continue; // Defensive coding: skip invalid lines
    
                String fileUsername = parts[0];
                String password = parts[1];
    
                if (fileUsername.equals(username)) {
                    return password.equals("DELETED");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username not found OR error
    }
    
}