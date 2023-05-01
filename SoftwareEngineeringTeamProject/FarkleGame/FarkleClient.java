package FarkleGame;

import java.io.IOException;
import ocsf.client.AbstractClient;

//Author: 			Shandon Probst
//Description:		

public class FarkleClient extends AbstractClient {

	// Objects
	private GUI_Client client_gui;

	// Controllers
	private LoginControl loginControl;
	private CreateAccountControl createAccountControl;
	
	// User vars
	private String username;
	private int player_number;
	private int score;
	
	// Opponent vars
	private String opp_username;
	private int opp_number;
	private int opp_score;

	// Constructor
	public FarkleClient() {
		super("localhost", 8300);
	}

	// Sets loginControl
	public void setLoginControl(LoginControl loginControl) {
		this.loginControl = loginControl;
	}

	// Sets createAccountControl
	public void setCreateAccountControl(CreateAccountControl createAccountControl) {
		this.createAccountControl = createAccountControl;
	}

	// Sets the GUI for... reasons
	public void setGUI(GUI_Client client_gui) {
		this.client_gui = client_gui;
	}

	// Prints to console when connection is established
	public void connectionEstablished() {
		client_gui.login(client_gui);
	}

	// Prints to console when connection is closed
	public void connectionClosed() {
		System.out.println("Connection closed");
	}

	// Sets the current player's username
	public void setUsername(String username) {
		this.username = username;
	}

	// Gets the current player's username
	public String getUsername() {
		return this.username;
	}
	
	// Sets the user's player number
	public void setNumber(int player_number) {
		this.player_number = player_number;
	}
	
	// Gets the user's player number
	public int getNumber() {
		return this.player_number;
	}
	
	// Sets the current opponenet's username
	public void setOppUsername(String opp_username) {
		this.opp_username = opp_username;
	}
	
	// Gets the current opponent's username
	public String getOppUsername() {
		return this.opp_username;
	}
	
	// Sets the opponent's player number
	public void setOppNumber(int opp_number) {
		this.opp_number = opp_number;
	}
	
	// Gets the opponent's player number
	public int getOppNumber() {
		return this.opp_number;
	}

	// Sets the current player's score
	public void setScore(int score) {
		this.score = score;
	}

	// Gets the current player's score
	public int getScore() {
		return this.score;
	}
	
	// Sets the opponent's score
	public void setOppScore(int opp_score) {
		this.opp_score = opp_score;
	}
	
	// Gets the opponent's score
	public int getOppScore() {
		return this.opp_score;
	}

	@Override
	protected void handleMessageFromServer(Object arg0) {

		// Checks if argument being sent is a string
		if (arg0 instanceof String) {
			String message = (String) arg0;

			// Tells controller login was successful
			if (message.startsWith("LoginSuccessful_")) {
				this.setUsername(message.substring(16));
				loginControl.loginSuccess();

			}
			// Tells controller account creation was successful
			else if (message.startsWith("CreateAccountSuccessful_")) {
				createAccountControl.createAccountSuccess();
			}

			// Tells the client to close the connection with the server
			else if (message.startsWith("Disconnect")) {
					client_gui.userDisconnectedError();
			}

			// Tells the client the game is starting
			else if (message.startsWith("StartGame_")) {
				this.player_number = Integer.parseInt(message.substring(10));
				setNumber(player_number);
				
				if (player_number == 1) {
					setOppNumber(2);
					
				}
				else if (player_number == 2) {
					setOppNumber(1);
				}
				
				try {
					this.sendToServer("Starting_" + player_number);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if (message.startsWith("Winner_")) {
				int winner_number = Integer.parseInt(message.substring(7));
				if (player_number == winner_number) {
					client_gui.userWon();
				}
				else {
					client_gui.userLost();
				}
			}

			else if (message.startsWith("EndGame_")) {
				// int winner_number = Integer.parseInt(message.substring(8));
				// System.out.println("Player #" + winner_number + " won the game.");

				client_gui.disableControl();
			}

			// Tells the client whose turn it is
			else if (message.startsWith("PlayerTurn_")) {
				int player_turn = Integer.parseInt(message.substring(11));

				// Enables control for the current player
				if (player_turn == player_number) {
					client_gui.play(player_number);
				}

				// Disables control the other player
				else {
					client_gui.spectate(opp_number);
				}
			}
			
			else if (message.startsWith("OppoScored_")) {
				setOppScore(Integer.parseInt(message.substring(11)));
				client_gui.updateOppScoreLabel();
			}
			
			// Sets the clien't opponent's username
			else if (message.startsWith("OppUsername_")) {
				this.setOppUsername(message.substring(12));
				client_gui.setMyLabel();
				client_gui.setOpponentLabel();
			}
			
			// Sets the labels of the client and their opponent
			else if (message.startsWith("SetLabels")) {
				client_gui.setMyLabel();
				client_gui.setOpponentLabel();
			}
		}

		else if (arg0 instanceof Error) {
			// Typecast argo0 as Error
			Error error = (Error) arg0;

			// Show login error using login controller
			if (error.getType().equals("Login"))
				loginControl.displayError(error.getMessage());

			// Show createAccount error using createAccount controller
			else if (error.getType().equals("CreateAccount"))
				createAccountControl.displayError(error.getMessage());
		}
	}

	// Main driver
	public static void main(String[] args) {

		FarkleClient client = new FarkleClient();
		new GUI_Client(client);
	}
}
