public interface RecommendationEngine {
	    public Influencer[] suggestInfluencers(String brandNiche, double minEngagement, int minFollowers,int count,int minAge,int maxAge);
}
