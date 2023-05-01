package FarkleGame;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

//Author: 			Shandon Probst
//Description:		

public class FarkleServer extends AbstractServer {

	// Final Variables
	final int WINNING_SCORE = 2500;		// The score required to win
	
	// Objects
	private Database database;

	// JTextAreas
	private JTextArea log;

	// JLabels
	private JLabel status;

	// Variables
	private ArrayList<Long> client_ids;
	private ArrayList<ConnectionToClient> client_connections;
	private ArrayList<String> client_users_connected;

	// Constructor
	public FarkleServer() {

		super(12345);
		this.setTimeout(500);

		this.status = new JLabel();
		this.log = new JTextArea();
		this.database = new Database();

		this.client_ids = new ArrayList<Long>();
		this.client_connections = new ArrayList<ConnectionToClient>();
		this.client_users_connected = new ArrayList<String>();
	}

	public void setLog(JTextArea log) {
		this.log = log;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public void handleMessageFromClient(Object arg0, ConnectionToClient arg1) {
		// Return message/object
		Object toClient = null;

		// Checks if the object sent is LoginData or CreateAccountData
		if (arg0 instanceof LoginData || arg0 instanceof CreateAccountData) {

			// Checks if object is LoginData. Verifies info if true
			if (arg0 instanceof LoginData) {
				LoginData toVerify = (LoginData) arg0;
				boolean logged_in = false;

				for (int i = 0; i < client_users_connected.size(); i++) {
					if (client_users_connected.get(i).endsWith(toVerify.getUsername())) {
						logged_in = true;
					}
				}

				// Checks if login data can be verified
				if (database.verifyAccount(toVerify.getUsername(), toVerify.getPassword()) && logged_in == false) {
					toClient = "LoginSuccessful_" + toVerify.getUsername();
					log.append("Client " + arg1.getId() + " successfully logged in as \"" + toVerify.getUsername()
							+ "\"\n");
					client_connections.add(arg1);
					client_users_connected.add(arg1.getId() + "_" + toVerify.getUsername());

					// Checks if only one player is connected
					if (client_connections.size() == 1) {
						log.append("Client " + arg1.getId() + " (\"" + toVerify.getUsername()
								+ "\") is waiting for more players to connect\n");
					} else {
						log.append("All players have connected\n");
						
						for (int i = 0; i < client_connections.size(); i++) {
							try {
								client_connections.get(i).sendToClient("StartGame_" + (i + 1));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

				else if (logged_in == true) {
					toClient = new Error("User is already logged in.", "Login");
					log.append("Client " + arg1.getId() + " failed to log in as \"" + toVerify.getUsername()
							+ "\" (user is already logged in)\n");
				}

				// If there is an error, make object an error and set the message and message
				// type
				else {
					toClient = new Error("The username and password are incorrect.", "Login");
					log.append("Client " + arg1.getId() + " failed to log in\n");
				}

				// Sends message to client
				try {
					arg1.sendToClient(toClient);
				}
				// Pointless exception
				catch (IOException e) {
					return;
				}
			}

			// Checks if object is CreateAccountData. Creates with info if true
			else if (arg0 instanceof CreateAccountData) {
				CreateAccountData toCreate = (CreateAccountData) arg0;

				// Checks if account can be created
				if (database.createNewAccount(toCreate.getUsername(), toCreate.getPassword())) {
					toClient = "CreateAccountSuccessful_" + toCreate.getUsername();
					log.append("Client " + arg1.getId() + " created a new account called \"" + toCreate.getUsername()
							+ "\"\n");
				}

				// If there is an error, make object an error and set the message and message
				// type
				else {
					toClient = new Error("Username has already been selected.", "CreateAccount");
					log.append("Client " + arg1.getId() + " failed to create a new account\n");
				}

				// Sends message to client
				try {
					arg1.sendToClient(toClient);
				}
				// Pointless exception
				catch (IOException e) {
					return;
				}
			}
		}

		// Checks if object sent is JButton
		else if (arg0 instanceof JButton) {
			JButton button = (JButton) arg0;

			// Checks to see if user selects to logout
			if (button.getText().equals("Logout")) {
				try {
					arg1.sendToClient("Disconnect");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// Checks if object sent is String
		else if (arg0 instanceof String) {
			String message = (String) arg0;
			
			// If message is bank score, then get the player who banked and set the current turn to the next player
			if (message.startsWith("BankScore_")) {
				// Gets score value
				int score = Integer.parseInt(message.substring(12));
				
				log.append("Player #" + message.substring(10, 11) + " now has " + score + " points\n");
				
				if (score >= WINNING_SCORE) {
					log.append("Player #" + message.substring(10, 11) + " won the game with " + score + " points\n");
					sendToAllClients("Winner_" + message.substring(10, 11));
				}
				else {
					for (int i = 0; i < client_users_connected.size(); i++) {

						if (!message.substring(10, 11).equals(Integer.toString(i + 1))) {
							try {
								client_connections.get(i).sendToClient("OppoScored_" + score);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendToAllClients("PlayerTurn_" + (i + 1));
							log.append("Telling clients it is now Client #" + arg1.getId() + "'s turn\n");
						}
					}
				}
			}
			
			// If the message is farkle, then it is now the next player's turn
			else if (message.startsWith("Farkled_")) {
				log.append("Telling clients that Client #" + arg1.getId() + " farkled\n");
				for (int i = 0; i < client_users_connected.size(); i++) {
					if (!message.substring(8).equals(Integer.toString(i + 1))) {
						sendToAllClients("PlayerTurn_" + (i + 1));
						log.append("Client #" + arg1.getId() + " farkled\n");
						log.append("Telling clients it is now Client #" + arg1.getId() + "'s turn\n");
					}
				}
			}
			
			// If the message is starting, then the player will be given their opponent's name
			else if (message.startsWith("Starting")) {
				for (int i = 0; i < client_users_connected.size(); i++) {
					if (i == 0) {
						try {
							client_connections.get(i).sendToClient("OppUsername_" + client_users_connected.get(1).substring(3));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (i == 1) {
						try {
							client_connections.get(i).sendToClient("OppUsername_" + client_users_connected.get(0).substring(3));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// Tells the game to start with player #1
				sendToAllClients("SetLabels");
				sendToAllClients("PlayerTurn_1");
			}
		}
	}

	public void listeningException(Throwable exception) {
		status.setText("<html>Status: <font color='red'>Exception Occurred When Listening</font></html>");
		log.append((exception.getMessage()));
		log.append("Press \"Listen\" to restart server");
	}

	public void serverStarted() {
		status.setText("<html>Status: <font color='green'>Listening</font></html>");
		log.append("Server started\n");
	}

	public void serverStopped() {
		status.setText("<html>Status: <font color='red'>Stopped</font></html>");
		log.append("Server stopped accepting new clients - press listen to start accepting new clients\n");
	}

	public void serverClosed() {
		status.setText("<html>Status: <font color='red'>Closed</font></html>");
		log.append("Server and all current clients are closed - press \"Listen\" to restart\n");
	}

	public void clientConnected(ConnectionToClient client) {
		// Displays when a client connects
		log.append("Client " + client.getId() + " connected" + "\n");
		client_ids.add((long) client.getId());
	}

	public void clientException(ConnectionToClient client, Throwable exception) {
		// Displays when a client unexpectedly disconnects
		log.append("Client " + client.getId() + " disconnected" + "\n");

		// Removes the client that was disconnected from the arrays
		client_connections.remove(client);
		client_ids.remove(client.getId());

		// Removes connected users
		for (int i = 0; i < client_users_connected.size(); i++) {
			if (client_users_connected.get(i).startsWith(String.valueOf(client.getId()))) {
				client_users_connected.remove(i);
			}
		}

		// Disconnects all clients if one disconnects
		if (client_users_connected.size() <= 1) {
			sendToAllClients("Disconnect");
		}
	}

	// Main driver
	// Creates farkle server GUI
	public static void main(String[] args) {
		new GUI_Server();
	}
}
