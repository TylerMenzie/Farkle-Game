package FarkleGame;

import java.io.Serializable;

//Author: 			Shandon Probst
//Description:		

@SuppressWarnings("serial")
public class CreateAccountData implements Serializable {
	
	private String username;
	private String password;
  
	// Constructor
	public CreateAccountData(String username, String password){
		setUsername(username);
		setPassword(password);
	}
  
	// Gets new account username
	public String getUsername() {
		return username;
	}
  
	// Gets new account password
	public String getPassword() {
		return password;
	}
  
	// Sets new account username
	public void setUsername(String username) {
		this.username = username;
	}
  
	// Sets new account password
	public void setPassword(String password) {
		this.password = password;
	}
}