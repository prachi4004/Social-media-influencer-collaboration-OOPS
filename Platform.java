public class Platform {
    private int noOfFollowers;
    private String username;
    private String name;
    private double engagementRate;
    private int earnings;

    // Parameterized constructor
    public Platform(String name, String username, int noOfFollowers, double engagementRate)
    {
        this.name = name;
        this.username = username;
        this.noOfFollowers = noOfFollowers;
        this.engagementRate = engagementRate;
    }


    public void updateEngagementRate(int engagementRate) {
        this.engagementRate = engagementRate;
    }

    // Optional: Getters
    public int getNoOfFollowers() {
        return noOfFollowers;
    }

    public String getName() {
        return name;
    }
    public double getEngagementRate() {
        return engagementRate;
    }
    public int getEarning() {
        return earnings;
    }
    
    public String getUsername()
    {
        return username;
    }

}
