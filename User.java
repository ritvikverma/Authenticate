import java.text.SimpleDateFormat;//Imports for date input
import java.util.Date;//Same as above

/**
 * @author ritvikverma
 * This is the User class the defines the blueprint of the User and initialises all variables when a new user is made
 */
public class User {
	private String username; //Stores the username of the user
	private String hashedPassword;//Stores the hashedpassword of the user using the given protocol
	private String fullName;//Stores the full name of the user 
	private String email;//Stores the email of the user
	private long phoneNumber;//Stores the phone number of the user
	private int failedLoginCount;//Stores the failed login count of the user
	private String lastLoginDate; //Stores the last logged in date of the user
	private boolean accountLocked;//Stores whether the account is locked or not
	/**
	 *  Initialise all values to default or default null initially
	 */
	public User() {
		username = "";
		hashedPassword = "";
		fullName = "";
		email = "";
		phoneNumber = 0;
		failedLoginCount = 0;
		lastLoginDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		accountLocked = false;
	}
	
	/**
	 * @param username the username to be registered
	 * @param hashedPassword the hashed password to be registered
	 * @param fullName the full name to be registered
	 * @param email the email to be registered
	 * @param phoneNumber the phone number to be registered
	 * @param failedLoginCount the failed login count to be registered
	 * @param lastLoginDate the last login date to be registered
	 * @param accountLocked locked? to be registered
	 */
	public User(String username, String hashedPassword, String fullName, String email, long phoneNumber, int failedLoginCount, String lastLoginDate, boolean accountLocked)
	{
		this.username=username;
		this.hashedPassword=hashedPassword;
		this.fullName=fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.failedLoginCount = failedLoginCount;
		this.lastLoginDate = lastLoginDate;
		this.accountLocked = accountLocked;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}
	/**
	 * @param hashedPassword the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the phoneNumber
	 */
	public long getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the failedLoginCount
	 */
	public int getFailedLoginCount() {
		return failedLoginCount;
	}
	/**
	 * @param failedLoginCount the failedLoginCount to set
	 */
	public void setFailedLoginCount(int failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}
	/**
	 * @return the lastLoginDate
	 */
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	/**
	 * @return the accountLocked
	 */
	public boolean isAccountLocked() {
		return accountLocked;
	}
	/**
	 * @param accountLocked the accountLocked to set
	 */
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

}
