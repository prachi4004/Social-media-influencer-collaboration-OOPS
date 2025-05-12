public abstract class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phone;
    protected String role; // NEW: Role (e.g., Admin, Influencer, Advertiser, etc.)
    protected boolean loggedIn = false;
    protected boolean blocked=false;  //The admin might block the user
    
    public User(String username, String password, String role, String phone, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;  
    }
    public boolean isLoggedIn() {
    	return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public boolean getBlockedStatus() {
    	return this.blocked;
    }

    public abstract void showDashboard(); // Role-specific actions
}
