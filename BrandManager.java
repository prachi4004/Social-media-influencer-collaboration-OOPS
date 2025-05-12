//FINAL UPDATED CLASS
//package social;
import java.util.InputMismatchException;
import java.util.Scanner;
public class BrandManager extends User implements LoginLogout {
	Scanner scanner = new Scanner(System.in);
	private Influencer[] influencers;   //List of all influencers the Brand has a contract with
    private Advertiser advertiser;  //Assuming there is only 1 advertiser
	private String name;            //Name of the brand
	private int countInfluencers;   //Count of Contracts
	private String niche;   //3 Niches must be selected 
	private Influencer[] recommendedInfluencers;
	private int maxCapacity;
	private Campaign.Contract[] contracts;
	private int contractCount = 0;

    public BrandManager(String username, String password, String email, String phone, String niche,int maxCapacity,Advertiser advertiser) {
        super(username, password, email, phone, "Brand Manager");
        this.niche=niche;
        this.maxCapacity=maxCapacity;
        this.countInfluencers=0;
        this.recommendedInfluencers=new Influencer[maxCapacity];
        this.advertiser=advertiser;
        this.contracts = new Campaign.Contract[maxCapacity];
    }
   
	public boolean addInfluencer(Influencer influencer) {
			influencers[countInfluencers]=influencer;
			countInfluencers++;
			return true;
    }
	//create campaign
    
    public boolean makePayment(Influencer influencer) {
		boolean found = false;

		for(Influencer myInfluencer : influencers)
		{
			if(influencer==myInfluencer)
			{
				found = true;
				break;
			}
		}

		if(!found)
		{
			System.out.println("Influncer not found.");
		}

    	    for (int i = 0; i < contractCount; i++) 
			{
    	        Campaign.Contract contract = contracts[i];
    	        if (contract.getInfluencer().equals(influencer)) {
    	            if (!contract.isPaymentDone()) {
    	                int amount = contract.getPaymentAmount();
    	                
    	                // Mark as paid
    	                contract.markPaymentStatus();

    	                // Update influencer's earnings
    	                influencer.receivePayment(amount);

    	                System.out.println("Paid ₹" + amount + " to influencer: " + influencer.getUsername());
    	                return true;
    	            } else {
    	                System.out.println("Payment already done for this influencer.");
    	                return false;
    	            }
    	        }
    	    }
    	    System.out.println("No contract found with this influencer.");
    	    return false;
    }

    
    public String getName() {
    	return this.name;
    }
    
	public void listInfluencers() {

        System.out.println("Available Influencers:");
        for (Influencer i : influencers) {
            if(i!=null) {
            	System.out.println(i.getUsername()+"/n");
            }
        }
    }

    @Override
    public void showDashboard() {
    	//Scanner scanner = new Scanner(System.in);
        System.out.println("Brand Manager Dashboard: Create and manage brand campaigns.");
		while (true){
			System.out.println("Options: \n a-Create campaign \n b-Display Influencers who have a contract with your Brand \n c-Make Payment to contracted Influencers \n d-Show Payment History \n e-want to logout");

			String option=scanner.next();
			if (option.equals("a")){
				System.out.println("Enter campaign name:");
				String name=scanner.nextLine();
				this.createCampaign(name);
			}

			else if (option.equals("b")){
				try{
					this.listInfluencers();
				}

				catch(NullPointerException e)
				{
					System.out.println("No influencers yet.");
				}
			}

			else if (option.equals("c")){
				boolean status=false;
				for(Influencer i:influencers) {
					status=this.makePayment(i);
					if(status==false) {
						System.out.println("Failure to make Payment!");
					}
					else {
						System.out.println("Payment successfull!!");
					}
				}
				
			}

			else if (option.equals("d")){
				viewPayments();
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
    
	public void viewPayments() {
		if(contractCount==0)
		{
			System.out.println("No payments yet.");
		}

        System.out.println("Payment History:");
        for (int i = 0; i < contractCount; i++) {
            Campaign.Contract c = contracts[i];
            System.out.println("Influencer: " + c.getInfluencer().getUsername() +
                               ", Paid: " + c.isPaymentDone() +
                               ", Amount: ₹" + c.getPaymentAmount());
        }
    }

    public void createCampaign(String campaignName) {


		int minAge=0;
		int maxAge=0;
		int minEngagement=0;
		int minFollowers=0;
		
		boolean validInput = false;

		while(!validInput)
		{
			try{
				if(countInfluencers<maxCapacity) {
				System.out.println("Enter the minimum age of audience you are targetting for:\n");
					minAge=scanner.nextInt();
				System.out.println("Enter the maximum age of audience you are targetting for:\n");
					maxAge=scanner.nextInt();
				System.out.println("Enter the minimum Engagement rate of influencer you are searching for:\n");
					minEngagement=scanner.nextInt();
				System.out.println("Enter the minimum followers of influencer you are searching for:\n");
					minFollowers=scanner.nextInt();
				}

				validInput = true;
			}

			catch(InputMismatchException e)
			{
				System.out.println("Invalid input.");
			}
		}
    	
    	//Campaign campaign = new Campaign(minAge,maxAge,niche,this,minEngagement,minFollowers); //

    	System.out.println("New Campaign Created!\n");
    	System.out.println("Send Campaign to Advertiser? Yes/No\n");

    	String choice=scanner.nextLine().toLowerCase();
    	if(choice.equalsIgnoreCase("no")) {
    		return;
    		}

    	if(choice.equalsIgnoreCase("yes")) {
    		recommendedInfluencers=advertiser.suggestInfluencers(this.niche,minEngagement,minFollowers,maxCapacity,minAge,maxAge);
            System.out.println("Do you want to set the duration in the contract? Y or N");
            String c=scanner.nextLine();
            //Constructor Overloading
            if(c.equalsIgnoreCase("y")) {
            	System.out.println("Enter the duration in months:");
            	int duration=scanner.nextInt(); 
            	for (int i = 0; i < recommendedInfluencers.length; i++) {
        		    Influencer inf = recommendedInfluencers[i];
        		    influencers[countInfluencers] = inf;
        		    Campaign.Contract contract = new Campaign.Contract(inf,duration);
        		    contracts[contractCount++] = contract;
        		    inf.addContract(contract);
        		    countInfluencers++;
        		}
            	
            }
            else {
            	for (int i = 0; i < recommendedInfluencers.length; i++) {
        		    Influencer inf = recommendedInfluencers[i];
        		    influencers[countInfluencers] = inf;
        		    recommendedInfluencers[i].addBrand(this);
        		    Campaign.Contract contract = new Campaign.Contract(inf);
        		    contracts[contractCount++] = contract;
        		    inf.addContract(contract);
        		    countInfluencers++;
        		}
            	
            }
            
            
    		for (int i = 0; i < recommendedInfluencers.length; i++) {
    		    Influencer inf = recommendedInfluencers[i];
    		    influencers[countInfluencers] = inf;
    		    Campaign.Contract contract = new Campaign.Contract(inf);
    		    contracts[contractCount++] = contract;
    		    countInfluencers++;
    		}

    		System.out.println("New Contracts with influencers formed!");
    		
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
        System.out.println("Brand Manager logged out.");
    }
}
