 import java.util.Scanner;
public class Influencer extends User implements LoginLogout{

    private String niche; //influencer's niche
    private BrandManager[] brands; //all the brands the influencer is collaborating with
    private Platform[] platforms; //influencer's social media accounts
    private double earnings; //total earnings
    private String bio; //short intro of influencer
    private double avgEngagementRate; //average engagement rate across all platforms
    private int followers;
    private int minAge;
    private int maxAge;
    private Campaign.Contract[] contracts;
    private int contractCount = 0;
    Scanner scanner=new Scanner(System.in);
    
    public Influencer(String username, String password,String email,String phone, String niche,int minAge,int maxAge, String bio, Platform[] platforms)
    {
        super(username, password, "Influencer",phone,email); // Calls User constructor
        //this.name = name;
        this.niche = niche;
        this.bio = bio;
        this.earnings = 0; 
        this.avgEngagementRate = 0;
        this.brands = new BrandManager[10]; //max 10 deals possible
        this.platforms= platforms;
        this.contracts = new Campaign.Contract[10]; //max 10 deals possible
    }

    //method to accept, reject offer???
    public void viewMyContracts() {
        if(contractCount==0) 
        System.out.println("No contracts yet.");

        for (int i = 0; i < contractCount; i++) {
            Campaign.Contract c = contracts[i];
            System.out.println("Brand: " + c.getBrand().getName() + 
                               ", Amount: ₹" + c.getPaymentAmount() +
                               ", Paid: " + c.isPaymentDone());
        }
    }
    
    public void viewPaymentStatus() {
        if (contractCount == 0) {
            System.out.println("No payments found.");
            return;
        }
        System.out.println("Payment Status for Contracts:");
        for (int i = 0; i < contractCount; i++) {
            Campaign.Contract c = contracts[i];
            String status = c.isPaymentDone() ? "Paid" : "Pending";
            System.out.println("[" + (i+1) + "] " +
                "Brand: " + c.getBrand().getName() +
                ", Amount: ₹" + c.getPaymentAmount() +
                ", Status: " + status);
        }
    }

    public void addBrand(BrandManager newBrand) {
        for (int i = 0; i < brands.length; i++) {
            if (brands[i] == null) {
                brands[i] = newBrand;
                return;
            }
        }
        System.out.println("Max brand deals reached.");
    }
    
    public void addContract(Campaign.Contract contract) {
        if (contractCount < contracts.length) {
            contracts[contractCount++] = contract;
        }
    }
    
    public void addPlatform(Platform newPlatform) {
        for (int i = 0; i < platforms.length; i++) {
            if (platforms[i] == null) {
                platforms[i] = newPlatform;
                return;
            }
        }
        System.out.println("All 4 platform slots are filled.");
    }

    public void calculateEarnings()
    {
        for(Platform myPlatform : platforms)
        {
            this.earnings = this.earnings + myPlatform.getEarning(); //each platform should have an earning field
        }

    }
    
    public void receivePayment(int amount) {
        this.earnings += amount;
    }

    public int getFollowers() {
    	return this.followers;
    }
    
    public double getEarnings()
    { 
        return earnings;
    }

    public double getEarnings(String platformName) {
        //platform specific earning
        for (Platform platform : platforms) {
            if (platform != null && platform.getName().equalsIgnoreCase(platformName)) {
                return platform.getEarning(); // Calls getEarnings() from Platform class
            }
        }
        return -1.0; //if error
    }
    
    public void calculateAvgEngagementRate() {
    	double totalEngagement=0;
        for (Platform platform : platforms) {
            totalEngagement = platform.getEngagementRate(); // Directly sum engagement rates
        }
    
        this.avgEngagementRate = totalEngagement / 4; // Always divide by 4 since all platforms exist
    }

    public double getAvgEngagementRate(String platformName) {
        for (Platform platform : platforms) {
            if (platform.getName().equalsIgnoreCase(platformName)) {
                return platform.getEngagementRate();
            }
        }
        return -1.0; // error
    }
    
    public void changeBio(String newBio) {
        this.bio = newBio;
        System.out.println("Bio updated successfully!");
    }
    

    //GETTER FUNCTIONS FROM HERE

    public String getNiche() {
        return niche;
    }
    
    public BrandManager[] getBrands() {
        return brands;
    }
    
    public Platform[] getPlatforms() {
        return platforms;
    }
    
    public String getBio() {
        return bio;
    }
    
    public int getMinAge() {
        return minAge;
    }
    public int getMaxAge() {
        return maxAge;
    }
    
    public double getAvgEngagementRate() {
        return avgEngagementRate;
    }
    

    @Override
    public void showDashboard() {
    	while (true){
            System.out.println("Welcome "+getUsername());

        	System.out.println("Influencer Dashboard: \n a-view collaborated brands \n b-view earnings \n c-view payment status for brands \n e-logout");
			String option=scanner.next();
			if (option.equals("a")){
				viewMyContracts();
			}
			else if (option.equals("b")){
				System.out.println("Total earnings:"+earnings);
			}
           else if (option.equals("c")){
				viewPaymentStatus();
			}
			else if (option.equals("e")){
				break;
			}
			else {
				System.out.println("Invalid option");
			}
		}

		this.logout();
    }

    @Override
    public void login(String username, String password) {
        loggedIn = true;
        showDashboard();
    }

    @Override
    public void logout() {
        loggedIn = false;
        System.out.println("Influencer logged out.");
        System.exit(0);
    }
}
