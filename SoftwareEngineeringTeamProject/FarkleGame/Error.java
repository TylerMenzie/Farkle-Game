package FarkleGame;

import java.io.Serializable;

//Author: 			Shandon Probst
//Description:		

@SuppressWarnings("serial")
public class Error implements Serializable {
  
	private String message;
	private String type;
  
	// Error object constructor
	public Error(String message, String type) {
		setMessage(message);
		setType(type);
	}
  
	// Get error message
	public String getMessage() {
		return message;
	}
  
	// Get error message type
	public String getType() {
		return type;
	}
  
	// Set error message
	public void setMessage(String message) {
		this.message = message;
	}
  
	//Set error message type
	public void setType(String type) {
		this.type = type;
	}
}
