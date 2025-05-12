//package social;
import java.util.Scanner;
public class Advertiser extends User implements LoginLogout,RecommendationEngine {  //multiple inheritance
    private Influencer[] influencers;
	private int count;
	private BrandManager brand;
	private int minEngagement;
	private int minFollowers;
	private Influencer[] finallist;
	
    public Advertiser(String username, String password, String email, String phone,BrandManager brand,Influencer[] influencers) {
        super(username, password, email, phone, "Advertiser");
        this.influencers=influencers;
        this.brand=brand;
    }
   
 
    @Override
    public Influencer[] suggestInfluencers(String brandNiche, double minEngagement, int minFollowers,int count,int minAge,int maxAge)
    {
    	this.count=count;
        Influencer[] suggestions = new Influencer[count];
        int suggestionCount = 0;

        for (int i = 0; i < count; i++) {
            Influencer influencer = influencers[i];
            if (influencer.getNiche().equalsIgnoreCase(brandNiche) &&
                influencer.getAvgEngagementRate() >= minEngagement &&
                influencer.getFollowers() >= minFollowers && influencer.getMinAge()<=minAge && influencer.getMaxAge()>=maxAge) {
                suggestions[suggestionCount++] = influencer;
            }
        }

        // Trim the array to fit the actual number of suggestions
        Influencer[] finalSuggestions = new Influencer[suggestionCount];
        System.arraycopy(suggestions, 0, finalSuggestions, 0, suggestionCount);
     // Trim the array to fit the actual number of suggestions
        this.finallist=finalSuggestions;
        
        
        return finalSuggestions;
    }

    @Override
    public void login(String username, String password) {
       loggedIn = true;
       this.showDashboard();
    }

    @Override
    public void logout() {
        loggedIn = false;
        System.out.println("Advertiser logged out.");
    }

    @Override
    public void showDashboard() {
    	Scanner scanner=new Scanner(System.in);
        
		while (true){
			System.out.println("Options:\n a-Display suggested influencers\n b-Display client brand\n e-want to logout");
			String option=scanner.next();
			if (option.equals("a")){
				for(Influencer i: finallist)
				{
					System.out.println(i.username);
				}
				
			}
			else if (option.equals("b")){
				
				System.out.println(brand.getName());
				
			}
			else if (option.equals("e")){
				break;
			}
			else {
				System.out.println("Invalid option");
			}
		}

        scanner.close();
		this.logout();
    }

    public void proposeCollaboration(Influencer influencer, String campaignDetails) {
        System.out.println("Proposed collaboration to " + influencer.getUsername() + " with campaign: " + campaignDetails);
    }

    // Getter for count
    public int getCount() {
        return count;
    }

    // Getter for minEngagement
    public int getMinEngagement() {
        return minEngagement;
    }

    // Getter for minFollowers
    public int getMinFollowers() {
        return minFollowers;
}

}
