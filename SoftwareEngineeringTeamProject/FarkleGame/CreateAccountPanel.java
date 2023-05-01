package FarkleGame;

import java.awt.*;
import javax.swing.*;

//Author: 			Shandon Probst
//Description:		

@SuppressWarnings("serial")
public class CreateAccountPanel extends JPanel {
	
	public JTextField usernameField;
	public JPasswordField passwordField;
	public JPasswordField passwordVerifyField;
	public JLabel errorLabel;
  
	// CreateAccountPanel constructor
	public CreateAccountPanel(CreateAccountControl cac) {
		
		// Creation of label panel
		JPanel labelPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		
		// Creation of error label
		errorLabel = new JLabel("", JLabel.CENTER);
		errorLabel.setForeground(Color.RED);
		
		// Creation of instruction label
		JLabel instructionLabel = new JLabel("Enter a username and password to create an account.", JLabel.CENTER);
		JLabel instructionLabel2 = new JLabel("Your password must be at least 6 characters.", JLabel.CENTER); 
		JLabel instructionLabel3 = new JLabel("You will be redirected to the home screen upon successful account creation.", JLabel.CENTER);
		
		// Adds labels to label panel
		labelPanel.add(errorLabel);
		labelPanel.add(instructionLabel);
		labelPanel.add(instructionLabel2);
		labelPanel.add(instructionLabel3);

		// Creation of account info panel
		JPanel accountPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		
		// Creation of username, password, and verify password labels and fields
		JLabel usernameLabel = new JLabel("Username:", JLabel.RIGHT);
		JLabel passwordLabel = new JLabel("Password:", JLabel.RIGHT);
		JLabel passwordVerifyLabel = new JLabel("Verify Password:", JLabel.RIGHT);
		
		// Corresponding fields
		usernameField = new JTextField(10);
		passwordField = new JPasswordField(10);
		passwordVerifyField = new JPasswordField(10);
		
		// Adds labels and fields to account info panel
		accountPanel.add(usernameLabel);
		accountPanel.add(usernameField);
		accountPanel.add(passwordLabel);
		accountPanel.add(passwordField);
		accountPanel.add(passwordVerifyLabel);
		accountPanel.add(passwordVerifyField);
    
		// Creation of button panel
		JPanel buttonPanel = new JPanel();
		
		// Creation of submit and cancel buttons
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");
		
		// Adds action listeners to buttons
		submitButton.addActionListener(cac);
		cancelButton.addActionListener(cac);   
		
		// Adds buttons to button panel
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		// Arranges the panels in a grid layout
		JPanel grid = new JPanel(new GridLayout(3, 1, 0, 10));
		grid.add(labelPanel);
		grid.add(accountPanel);
		grid.add(buttonPanel);
		this.add(grid);
	}
	
	// Gets the username field text
	public String getUsername() {
		return usernameField.getText();
	}
  
	// Gets the password field text
	public String getPassword() {
		return new String(passwordField.getPassword());
	}
  
	// Gets the verify password field text
	public String getPasswordVerify() {
		return new String(passwordVerifyField.getPassword());
	}
  
	// Sets the error label text
	public void setError(String error) {
		errorLabel.setText(error);
	}
}
