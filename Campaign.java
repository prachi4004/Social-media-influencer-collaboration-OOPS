//FINAL UPDATED CLASS
//package social;
public class Campaign {

    private int minAge;
    private int maxAge; 
    private String niche;
    private static BrandManager brandManager;
    private double minEngagement;
    private int minFollowers;

    public Campaign(int minAge, int maxAge, String niche, BrandManager brandManager,double minEngagement, int minFollowers) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.niche = niche;
        Campaign.brandManager = brandManager;
        this.minEngagement=minEngagement;
        this.minFollowers=minFollowers;
    }

    // Nested Contract class
    public static final class Contract {
        private Influencer influencer;
        private int duration; // in weeks/months etc.
        private double engagementRate;
        private boolean paymentStatus;
        private int paymentAmount;

        // Constructor with parameters
        public Contract(Influencer influencer) {
            this.influencer = influencer;
            this.paymentStatus = false;
            this.duration = 6; // default duration=6 months
            this.engagementRate = influencer.getAvgEngagementRate();
            calculatePay();
        }

        // Constructor with default payment, duration, etc.
        public Contract(Influencer influencer,int duration) {
            this.influencer = influencer;
            this.paymentStatus = false;
            this.duration = duration; // default duration
            this.engagementRate = influencer.getAvgEngagementRate();
            calculatePay();
        }

        // Calculate payment based on engagement rate
        public void calculatePay() {
        	engagementRate+=influencer.getFollowers()*duration;
            this.paymentAmount = (int) (100 * engagementRate);
        }
        public void markPaymentStatus() {
        	paymentStatus=true;
        }
        // Getters (optional)
        public int getPaymentAmount() {
            return paymentAmount;
        }

        public boolean isPaymentDone() {
            return paymentStatus;
        }

        public Influencer getInfluencer() {
            return influencer;
        }

        public int getDuration() {
            return duration;
        }
        
        public double getEngagementRate() {
            return engagementRate;
        }
        public BrandManager getBrand() {
        	return brandManager;
        }

    }

// Getters for minAge, maxAge, niche
public int getMinAge() {
    return minAge;
}

public int getMaxAge() {
    return maxAge;
}

public String getNiche() {
    return niche;
}

// Getters for minEngagement, minFollowers
public double getMinEngagement() {
    return minEngagement;
}

public int getMinFollowers() {
    return minFollowers;
}

    
}
