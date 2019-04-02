/**
 * This programme, as part of the assignment 3 of the COMP2396 course, is responsible for providing a secure login for users and stores their details. 
 */
import java.io.BufferedWriter;//Bufferedwriter import
import java.io.File;//File class import
import java.io.FileReader;//Filereader class import
import java.io.FileWriter;//Filewriter class import
import java.io.IOException;//IOException class import
import java.security.MessageDigest;//Imports the MessageDigest class of the security library
import java.security.NoSuchAlgorithmException;//If there exists no such exception
import java.text.SimpleDateFormat; //Imports for date writing
import org.json.simple.*; //Imports JSON for JSON writing and reading using the given JSON Jar file
import org.json.simple.parser.*;
import java.util.Scanner;//Import Scanner class of Utility package

import javax.xml.bind.DatatypeConverter;

import java.util.ArrayList;//Import ArrayList class of Utility package
import java.util.Date; //Using for date
/**
 * @author ritvikverma
 * Main class for driving the programme. Includes many methods tht perform various functions
 */

public class Main implements Hash{
	private Scanner in = new Scanner(System.in);//Scanner object
	private ArrayList<User> users = new ArrayList<User>();//Creates new Users
	private User admin = new User();
	private boolean hasAdmin = false; //Sees if we have an administrator already


	/**
	 * Constructor of this class, initialises variables to default
	 */
	public Main()
	{
		hasAdmin = false;
	}
	/**
	 * @param args empty string array
	 * @throws IOException 
	 * This is the main method of the class that drives the programme through inputs and outputs
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		//Drive the programme through inputs and outputs
		Main m = new Main();//Creates new object of main class for using across functions
		//Prints the menu prompt
		int input = 30; //Random input to enter the loop once
		m.printMenu();
		m.read(); 
		do{
			input = m.in.nextInt(); //Takes the user input once entered the loop
			if(input == 1) //For login
				m.logIn();
			else if(input == 2)//For creating new user
				m.userInput();
			else if(input == 3) //For updating the record of the new user
			{
				User u = new User();
				u = m.logIn(); //Sees of the user exists
				if(u != null) //If returns an object after successful login then we can update the user record
					m.updateRecord(u);
			}
			else if(input == 4) //We'll see if an admin exists, if not then we create a new admin
				if(m.admin() == true)
					m.resetPassword();			
			m.update();//Updates the JSON
			if(input!=0)
				m.printMiniMenu();
		}while(input!=0);


	}



	/**
	 * Prints the small menu of the programme
	 */
	private void printMiniMenu() {
		System.out.print("Please enter your command (1-4, or 0 to terminate the system): ");		
	}
	/**
	 * @throws IOException
	 * Reads the file input for further processing; puts the users in arrayList
	 */
	private void read() throws IOException
	{
		File file = new File("User.txt");
		if (!file.exists()) {//If not exists then create
			file.createNewFile();
			return;
		}
		JSONArray array = new JSONArray();
		try{
			JSONParser parser=new JSONParser();
			Object obj = parser.parse(new FileReader("User.txt"));
			array.add(obj);
			array= (JSONArray) ((JSONObject)array.get(0)).get("user_array");//Gets the first argument of the JSON as a JSONArray
			for(int length=0;length<array.size();length++){
				JSONObject jObj =(JSONObject) array.get(length);//Explicit type conversion
				users.add(new User(jObj.get("username").toString(),jObj.get("hash_password").toString(),jObj.get("Full Name").toString(), jObj.get("Email").toString(), Integer.valueOf(jObj.get("Phone number").toString()), Integer.valueOf(jObj.get("Fail count").toString()), jObj.get("Last Login Date").toString(), Boolean.getBoolean(jObj.get("Account locked").toString())));
				if(jObj.get("username").toString().equals("administrator")) {
					hasAdmin = true;//If we have an administrator
					admin = users.get(users.size()-1);//The admin will be stored in a User object
				}
			}

		} catch (Exception e) {}
	}
	/**
	 * @throws IOException
	 * this function is for updating the users once we have the changes made by the user input
	 */
	private void update() throws IOException {
		File file = new File("User.txt");
		JSONArray array = new JSONArray();
		for(User u : users)//For each user, add the user's JSON to the array
		{
			array.add(CreateJSON(u));
		}
		JSONObject superjson = new JSONObject();//The big JSON containing key as "user_array" and value as array of users
		superjson.put("user_array", array);
		BufferedWriter fw = new BufferedWriter(new FileWriter(file, false));
		fw.write((superjson.toString()));//Writes to file after text conversion
		fw.flush();
		fw.close();
	}




	/**
	 * @return boolean to see if we were able to authenticate the administrator or not
	 * This function is used to see if we were able to authenticate the admin, if doesn't exist then we create the admin
	 */
	private boolean admin() {
		if(!hasAdmin)
			for(User u : users)
				if(u.getUsername().equals("administrator"))
					hasAdmin = true;
		
		if(hasAdmin) {
			System.out.print("Please enter the password of administrator: ");
			try {
				if(admin.getHashedPassword().equals(hashPassword(in.next())))
					return true;
				else
				{
					System.out.print("Password not match! ");
					return false;
				}
			} catch (NoSuchAlgorithmException e) {
				//see if there is an exception
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Administrator account not exist, please create the administrator account by setting up a password for it.");
			createAdminPassword();
			hasAdmin = true;
			return false;
		}
		return false;

	}

	/**
	 * This is used to reset the password of the user by the administrator after he inputs his own password
	 * @throws NoSuchAlgorithmException 
	 */
	private void resetPassword() throws NoSuchAlgorithmException {
		System.out.print("Please enter the user account need to reset: "); 
		String username = in.next();
		boolean exists = false;
		for(User u : users)
		{
			if (u.getUsername().equals(username))
			{
				exists = true;
				changePassword(u);
				u.setAccountLocked(false);
				u.setFailedLoginCount(0);
				break;
				}
				
			}
		

		if(!exists)
			System.out.println("User does not exist!");//If passwords don't match
	}


	/**
	 * Prints the menu of the programme to get user input
	 */
	private void printMenu() {
		System.out.println("Welcome to the COMP2396 Authentication system!");
		System.out.println("1. Authenticate user");
		System.out.println("2. Add user record");
		System.out.println("3. Edit user record");
		System.out.println("4. Reset user password");
		System.out.println("What would you like to perform?");
		System.out.print("Please enter your command (1-4, or 0 to terminate the system): ");
	}

	/**
	 * Takes the input of the user
	 */
	private void userInput()
	{
		User u = new User();
		System.out.print("Please enter your username: ");
		String username = in.next();
		boolean userExists = false;
		for(User user : users)//We will see if the user already exists to avoid conflict
		{
			if(user.getUsername().equals(username))
				userExists = true;
		}
		if(userExists)
			System.out.println("User already exists!");
		else {
			u.setUsername(username);		
			addPassword(u);//Adds password
			getFullName(u);//Gets the full name of the user
			getEmail(u);//Gets the email ID of the user
			getPhone(u);//Gets the phone number of the user
			System.out.println("Record added successfully! "); //Adding success and we move on to the next task, adding
			users.add(u);
		}
	}



	/**
	 * @param u for the user that we input whose record has to get updated
	 * This method gets the phone number of the user
	 */
	private void getPhone(User u) {
		// gets the phone number of the user
		System.out.print("Please enter your Phone number: ");
		long name = in.nextLong();
		u.setPhoneNumber(name);
	}

	/**
	 * @param u for the user that we need to update
	 * Gets the email of the user through inputting
	 */
	private void getEmail(User u) {
		// gets the email of the user
		System.out.print("Please enter your email address: ");
		String name = in.next();
		u.setEmail(name);
	}

	/**
	 * @param u for the full name of the user that we need to update
	 * Gets the full name of the user 
	 */
	private void getFullName(User u) {
		// gets the full name of the user
		System.out.print("Please enter your full name: ");
		String name = in.next() + " ";//First name
		name+=in.next(); //Surname
		u.setFullName(name);
	}

	/**
	 * @param checkPass the password that needs to be checked for validation
	 * @return if the password enters matches the stated requirements
	 * Checks if the password matches the requirements stated
	 */
	private boolean isLegit(String checkPass) {
		// check if the password is valid
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasDigit = false;
		if(checkPass.length()>=6)
		{
			for(int i= 0; i<checkPass.length(); i++)
			{
				if(Character.isUpperCase(checkPass.charAt(i)))
					hasUpper = true;
				else if(Character.isLowerCase(checkPass.charAt(i)))
					hasLower = true;
				else if(Character.isDigit(checkPass.charAt(i)))
					hasDigit = true;
			}

			if (hasUpper && hasLower && hasDigit) //Sees if it has everything we need 
				return true;
		}
		return false;
	}

	/**
	 * @param a is for the User that we need to convert into the JSON object
	 * @return the converted JSON object
	 * This function converts a User object into a JSON object
	 */
	private JSONObject CreateJSON(User a) { 
		JSONObject obj = new JSONObject();
		obj.put("username", a.getUsername());
		obj.put("hash_password", a.getHashedPassword());
		obj.put("Full Name", a.getFullName());
		obj.put("Email", a.getEmail());
		obj.put("Phone number", a.getPhoneNumber());
		obj.put("Fail count", a.getFailedLoginCount());
		obj.put("Last Login Date", a.getLastLoginDate());
		obj.put("Account locked", a.isAccountLocked());
		return obj;
	}

	/**
	 * @return the User that has just logged into the system
	 * This function logs the user in and updates the date of the user if user exists
	 * @throws NoSuchAlgorithmException 
	 */
	private User logIn() throws NoSuchAlgorithmException{
		System.out.print("Please enter your username: ");
		String username = in.next();
		for(User u : users)
		{
			if (u.getUsername().equals(username))
			{
				if(u.isAccountLocked() == true) //If exists and is locked then we print account is locked
				{
					System.out.println("Your account has been locked!");
					return null;
				}
				
				else{				
					System.out.print("Please enter your password: ");//If account not locked then we proceed further
					String password = in.next();
					if (authenticate(u, hashPassword(password)) == true)
					{
						System.out.println("Login success! Hello " +u.getFullName().split(" ")[0]+"!");
						u.setFailedLoginCount(0);//Refresh failed log
						String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//Write date
						u.setLastLoginDate(date);//Set the date
						return u;//Return the logged in user
					}
					else{
						u.setFailedLoginCount(u.getFailedLoginCount()+1);//We set failed count if we see that failed to login
						System.out.print(u.getFailedLoginCount()<3?"Login failed!\n":"");//Login failed
						if(u.getFailedLoginCount()>=3) {
							u.setAccountLocked(true);
							System.out.println("Login failed! Your account has been locked!");
							return null;
						}
						return this.logIn();
					}
				}
			}
		}
		System.out.println("User does not exist!");
		return null;
	}


	/**
	 * @param u to update the user record
	 * Updates the user record with new details
	 */
	private void updateRecord(User u) {
		// update user record after logging in 
		String name = "";
		if(u.isAccountLocked() == false) {
			changePassword(u);
			System.out.print("Please enter your new full name: ");
			name = in.next();//First name
			name+=in.next();//Surname
			u.setFullName(name);
			System.out.print("Please enter your new email address: ");
			u.setEmail(in.next());
			System.out.println("Record update successfully!");//We were able to update the record successfully
		}		
	}

	/**
	 * This function creates the administrator password using regular printing and password checking
	 */
	private void createAdminPassword() {
		// creates admin password
		System.out.print("Please enter the password: ");
		String passCheck = in.next();
		System.out.print("Please re-enter the password: ");
		if(in.next().equals(passCheck)) //Sees if the password is the same
			try {
				admin.setHashedPassword(hashPassword(passCheck));
				admin.setFullName("Administrator");
				admin.setUsername("administrator");
				users.add(admin);
				System.out.println("Administrator account created successfully!");
			} catch (NoSuchAlgorithmException e) {
				//to print if exception
				System.out.println(e);
			}
		else {
			System.out.println("Passwords don't match!");//Password does not match
			createAdminPassword();
		}
	}

	/**
	 * @param u to change the password of the user
	 * This function changes the password of the user through prompting
	 */
	private void changePassword(User u) {
		//resets the user's password
		System.out.print("Please enter the new password: ");
		String passCheck = in.next();
		System.out.print("Please re-enter the new password: ");
		if(in.next().equals(passCheck))
			try {
				u.setHashedPassword(hashPassword(passCheck));
				System.out.println("Password update successfully!");
			} catch (NoSuchAlgorithmException e) {
				// to print if exception
				System.out.println(e);
			}
		else {
			System.out.println("Passwords don't match!");
			if(u.isAccountLocked() == false)
				changePassword(u); //If we couldn't change then we restart this function
		}

	}

	/**
	 * @param u the user to add the password to 
	 * This function adds the password to the user
	 */
	private void addPassword(User u)
	{
		System.out.print("Please enter your password: ");
		String checkPass = in.next();
		String confirmedPass ="";
		while(!isLegit(checkPass))
		{
			System.out.println("Your password has to fulfil: at least 1 small letter, 1 capital letter, 1 digit!");
			System.out.print("Please enter your password: ");
			checkPass = in.next();
		}

		if(isLegit(checkPass))
		{
			System.out.print("Please re-enter your password: "); 
			confirmedPass = in.next();
		}

		if(checkPass.equals(confirmedPass))
		{
			try {
				u.setHashedPassword(hashPassword(confirmedPass));
			} catch (NoSuchAlgorithmException e) {
				//sees if there is an exception here 
				e.printStackTrace();
			}
		}

		else {
			System.out.print("Password not match! "); 
			addPassword(u);//Recurses to the function if not matched
		}

	}

	/**
	 * @param u the user object
	 * @param password the password that has been inputted
	 * @return if the authentication was successful
	 */
	private boolean authenticate(User u, String password) {
		//authenticate user transaction
		if(password.equals(u.getHashedPassword()))
			return true;
		else
			return false;

	}

	/* 
	 * @return string for hashed password
	 * @param the password for hashing
	 * This is the implementation of the interface that we have created in Hash.java
	 * It hashes the passed password
	 */
	public String hashPassword(String password) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("MD5");//We will be using the MD5 hashing algorithm
		md.update(password.getBytes());
		return DatatypeConverter.printHexBinary(md.digest()).toString().toLowerCase();//This will return the String
	}
}