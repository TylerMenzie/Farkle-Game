package FarkleGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

//Author: 			Tyler Menzie
//Description: 		Farkle Game GUI

@SuppressWarnings("serial")
public class GUI_Client extends JFrame {

	// Client object
	private FarkleClient client;

	// JFrames and JPanels
	private JFrame LoginFrame;
	private JFrame FarkleGameFrame;

	// JButtons
	private JButton BankScoreButton;
	private JButton SetAsideButton;
	private JButton RollDiceButton;
	
	// JLabels
	private JLabel player_label;
	private JLabel warning_label;
	private JLabel game_label;
	private JLabel scoreboard_label;
	
	// ArrayLists
	private ArrayList<JButton> dice_buttons; 				// Stores the JToggleButtons associated with the dice
	private ArrayList<Boolean> dice_set_aside; 				// Stores the JButtons associated with the dice that are to be
															// set aside
	private ArrayList<Boolean> dice_selected; 				// Stores the boolean values associated with the dice. True if selected
	public ArrayList<Integer> dice_values;
	private ArrayList<Integer> selected_dice;
	
	private ArrayList<JLabel> player_username_labels;
	private ArrayList<JLabel> player_score_labels;

	// Variables
	private int score;
	private int round_score;
	private int potential_bank;
	private int player_number;

	// Constructor
	public GUI_Client(FarkleClient client) {
		this.client = client;
		client.setGUI(this);
		
		connectToServer();
	}


	// Returns the login frame
	public JFrame getLoginFrame() {
		return this.LoginFrame;
	}

	// Returns the game frame
	public JFrame getFarkleGameFrame() {
		return this.FarkleGameFrame;
	}

	// Connect method
	public void connectToServer() {
	    JFrame connectFrame = new JFrame("Connect to Server");
	    connectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    connectFrame.setSize(300, 150);
	    connectFrame.setLocationRelativeTo(null);
	    
	    JLabel ipLabel = new JLabel("Enter server IP address:");
	    JTextField ipField = new JTextField(15);
	    
	    JButton connectButton = new JButton("Connect");
	    connectButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String ip = ipField.getText();
	            client.setHost(ip);
	            client.setPort(8300);
	            
	            try {
	                client.openConnection();
	            } catch (IOException e1) {
	                // TODO Auto-generated catch block
	                //e1.printStackTrace();
	            }
	            
	            if (client.isConnected()) {
	                connectFrame.dispose();
	            }
	        }
	    });
	    
	    JPanel connectPanel = new JPanel();
	    connectPanel.add(ipLabel);
	    connectPanel.add(ipField);
	    connectPanel.add(connectButton);
	    connectFrame.add(connectPanel);
	    
	    connectFrame.setVisible(true);
	}
	
	// Login method
	public void login(GUI_Client gui) {
		LoginFrame = new JFrame();

		// Set the title, default close operation, and resizable as false.
		LoginFrame.setTitle("Farkle Client Login");
		LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginFrame.setLocationRelativeTo(null);

		// Create the card layout container.
		CardLayout cardLayout = new CardLayout();
		JPanel container = new JPanel(cardLayout);

		// Create the Controllers next
		// Next, create the Controllers
		InitialControl ic = new InitialControl(container);
		LoginControl lc = new LoginControl(container, client, LoginFrame, gui);
		CreateAccountControl cac = new CreateAccountControl(container, client);

		// Set client info
		client.setLoginControl(lc);
		client.setCreateAccountControl(cac);

		// Create the four views. (need the controller to register with the Panels
		JPanel view1 = new InitialPanel(ic);
		JPanel view2 = new LoginPanel(lc);
		JPanel view3 = new CreateAccountPanel(cac);

		// Add the views to the card layout container.
		container.add(view1, "1");
		container.add(view2, "2");
		container.add(view3, "3");

		// Show the initial view in the card layout.
		cardLayout.show(container, "1");

		// Add the card layout container to the JFrame.
		LoginFrame.add(container, BorderLayout.CENTER);

		// Show the JFrame.
		LoginFrame.setSize(550, 350);
		LoginFrame.setVisible(true);
	}

	// Initialize the contents of the frame
	public void initialize(String username) {

		// Start of the Farkle Game Frame
		FarkleGameFrame = new JFrame();
		FarkleGameFrame.setTitle("Farkle Client - Playing as \"" + username + "\"");
		FarkleGameFrame.getContentPane().setLayout(null);
		FarkleGameFrame.setSize(1000, 500);
		FarkleGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FarkleGameFrame.setResizable(false);
		FarkleGameFrame.setLocationRelativeTo(null);

		ImageIcon game_icon = new ImageIcon("/GUI/game_icon.jpg");
		FarkleGameFrame.setIconImage(game_icon.getImage());

		// Initialize score
		score = 0;

		// Creation of buttons
		SetAsideButton = new JButton("Set Aside");
		RollDiceButton = new JButton("Roll Dice");
		BankScoreButton = new JButton("Bank Score");
		
		// Creation of JLabels
		player_label = new JLabel("Waiting for other player to join");
		warning_label = new JLabel();
		game_label = new JLabel();
		scoreboard_label = new JLabel("Scoreboard");
		
		// Username labels
		JLabel user1 = new JLabel();
		JLabel user2 = new JLabel();
		
		JLabel score1 = new JLabel("0");
		JLabel score2 = new JLabel("0");

		// Initialize ArrayLists
		dice_selected = new ArrayList<Boolean>();
		dice_values = new ArrayList<Integer>();
		dice_set_aside = new ArrayList<Boolean>();
		selected_dice = new ArrayList<Integer>();
		player_username_labels = new ArrayList<JLabel>();
		player_score_labels = new ArrayList<JLabel>();
		
		// Creation of dice buttons
		dice_buttons = new ArrayList<JButton>();
		
		// Creates all of the buttons and maps action listeners to them
		for (int i = 0; i < 6; i++) {
			// Creation of individual button
			JButton button = new JButton("");
			dice_buttons.add(button);
			
			// All dice are initialized as not selected
			dice_selected.add(false);
			dice_set_aside.add(false);

			// Add button to panel
			button.setBounds((90 + ((i + 1) * 65)), 315, 53, 49);
			FarkleGameFrame.getContentPane().add(button);

			// Disables button and hides them
			button.setEnabled(false);
			button.setVisible(false);

			// Adds action listeners to each button
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					// Checks if button is in dice_buttons. Moves over to set_aside_buttons if
					// selected
					
					for (int j = 0; j < dice_buttons.size(); j++) { 
						if (dice_selected.get(j).equals(false) && j == dice_buttons.indexOf(button)) {
							// Show button in new location
							button.setBounds((90 + ((dice_buttons.indexOf(button) + 1) * 65)), 255, 53, 49);
							
							// Set selected as true
							dice_selected.set(j, true);
						}
						
						// Checks if button is in set_aside_buttons. Moves over to dice_buttons if
						// selected
						else if (dice_selected.get(j).equals(true) && j == dice_buttons.indexOf(button)) {
						    // Sets bounds
						    button.setBounds((90 + ((dice_buttons.indexOf(button) + 1) * 65)), 315, 53, 49);

						    // Set selected as false
						    dice_selected.set(j, false);
						}
					}
				}
			});
		}

		// Disabling buttons
		SetAsideButton.setEnabled(false);
		RollDiceButton.setEnabled(false);
		BankScoreButton.setEnabled(false);

		// Setting JButton bounds
		SetAsideButton.setBounds(155, 385, 100, 40);
		RollDiceButton.setBounds(295, 385, 100, 40);
		BankScoreButton.setBounds(435, 385, 100, 40);
		
		// Setting JLabel bounds and alignment
		player_label.setBounds(245, 10, 200, 15);
		warning_label.setBounds(245, 35, 200, 15);
		game_label.setBounds(245, 55, 200, 15);
		scoreboard_label.setBounds(650, 10, 100, 15);
		
		player_label.setHorizontalAlignment(SwingConstants.CENTER);
		warning_label.setHorizontalAlignment(SwingConstants.CENTER);
		game_label.setHorizontalAlignment(SwingConstants.CENTER);
		scoreboard_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Adding buttons to frame
		FarkleGameFrame.getContentPane().add(SetAsideButton);
		FarkleGameFrame.getContentPane().add(RollDiceButton);
		FarkleGameFrame.getContentPane().add(BankScoreButton);
		
		// Adding JLabels to frame
		FarkleGameFrame.getContentPane().add(player_label);
		FarkleGameFrame.getContentPane().add(warning_label);
		FarkleGameFrame.getContentPane().add(game_label);
		FarkleGameFrame.getContentPane().add(scoreboard_label);

		FarkleGameFrame.getContentPane().add(user1);
		FarkleGameFrame.getContentPane().add(user2);
		FarkleGameFrame.getContentPane().add(score1);
		FarkleGameFrame.getContentPane().add(score2);
		
		// Adding JLabels to ArrayList
		player_username_labels.add(user1);
		player_username_labels.add(user2);
		player_score_labels.add(score1);
		player_score_labels.add(score2);

		// Button Listener to register when bank score button is submitted
		BankScoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				game_label.setText("You banked " + potential_bank + " points!!!");
				score += potential_bank;
				
				try {
					client.sendToServer("BankScore_" + player_number + "_" + score);
					client.setScore(score);
					updateMyScoreLabel();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		SetAsideButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        
		    	// Clears warning message
		    	warning_label.setText("");
		    	
		    	// Checks if any selection was made
		    	if (dice_selected.contains(true)) {
		    		
		    		// Get list of selected dice values
			        for (int i = 0; i < dice_buttons.size(); i++) {
			            if (dice_selected.get(i).equals(true)) {
			                selected_dice.add(dice_values.get(i));
			            }
			        }
			        
			        if (selected_dice.contains(4) || selected_dice.contains(3) || selected_dice.contains(2) || selected_dice.contains(6)) {
			        	warning_label.setText("Invalid selection!");
			        }
			        
			        else {
			        	// Verify and score the selected dice
				        round_score = getScore(selected_dice);
				        potential_bank += round_score;
				        
				        game_label.setText("Potential bank: " + potential_bank);
				        
				        int pos_helper = -15;
				        
				        // Loop through and disable dice buttons that were selected and move them
				        for (int i = 0; i < dice_buttons.size(); i++) {
				        	pos_helper += 55;
				            if (dice_selected.get(i).equals(true)) {
				                dice_selected.set(i, false);
				                dice_set_aside.set(i, true);
				                dice_buttons.get(i).setEnabled(false);
				                dice_buttons.get(i).setBounds(35 , pos_helper, 53, 49);
				            }
				        }
				        
				        // Enable the bank score and roll button
				        BankScoreButton.setEnabled(true);
				        RollDiceButton.setEnabled(true);
			        }
			        
			        // Clear the list of selected dice
			        selected_dice.clear();     
		    	}
		    	
		    	// If no selection was made, tell the user
		    	else {
		    		warning_label.setText("No dice selected!");
		    	}
		    } 
		});


		// Button Listener to register when roll dice button is submitted
		RollDiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < dice_buttons.size(); i++) {
					if (dice_set_aside.get(i) == false && dice_selected.get(i) == true) {
						dice_buttons.get(i).setBounds((90 + ((dice_buttons.indexOf(dice_buttons.get(i)) + 1) * 65)), 315, 53, 49);
						dice_selected.set(i, false);
					}
				}
				
				roll();
				RollDiceButton.setEnabled(false);
			}
		});
		
		// Sets the frame as visible
		FarkleGameFrame.setVisible(true);
	}
	
	// Sets the player labels once the game starts
	public void setMyLabel() {
		// Username Label
		player_username_labels.get(client.getNumber() - 1).setText("Player #" + client.getNumber() + " - " + client.getUsername() + ": ");
		player_username_labels.get(client.getNumber() - 1).setBounds(630, ((28 * client.getNumber())), 250, 20);
		player_username_labels.get(client.getNumber() - 1).setHorizontalAlignment(SwingConstants.LEFT);
		
		// Opponent's score label
		player_score_labels.get(client.getNumber() - 1).setBounds(770, ((28 * client.getNumber())), 250, 20);
		player_score_labels.get(client.getNumber() - 1).setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	// Sets the opponent's label once the game starts
	public void setOpponentLabel() {
		// Opponent's username label
		player_username_labels.get(client.getOppNumber() - 1).setText("Player #" + client.getOppNumber() + " - " + client.getOppUsername() + ": ");
		player_username_labels.get(client.getOppNumber() - 1).setBounds(630, ((28 * client.getOppNumber())), 250, 20);
		player_username_labels.get(client.getOppNumber() - 1).setHorizontalAlignment(SwingConstants.LEFT);
		
		// Opponent's score label
		player_score_labels.get(client.getOppNumber() - 1).setBounds(770, ((28 * client.getOppNumber())), 250, 20);
		player_score_labels.get(client.getOppNumber() - 1).setHorizontalAlignment(SwingConstants.LEFT);
		
	}
	
	public void updateMyScoreLabel() {
		player_score_labels.get(client.getNumber()-1).setText(Integer.toString(client.getScore()));
	}
	
	public void updateOppScoreLabel() {
		player_score_labels.get(client.getOppNumber()-1).setText(Integer.toString(client.getOppScore()));
	}

	// Lets the chosen player play and interact with the game
	public void play(int player_number) {
		resetBoard();
		potential_bank = 0;
		
		// Enables control for the user
		this.player_number = player_number;
		RollDiceButton.setEnabled(true);
		
		// Shows the player it is their turn
		player_label.setText("It is currently your turn");
	}

	// Lets the chosen player spectate and not interact with the game
	public void spectate(int opp_number) {
		
		// Shows the player it is their turn
		player_label.setText("It is currently " + client.getOppUsername() + "'s turn");
		
		// Disables control for the user
		disableControl();
	}

	// Sets most buttons as disabled and invisible
	public void disableControl() {
		// Disabling buttons
		SetAsideButton.setEnabled(false);
		RollDiceButton.setEnabled(false);
		BankScoreButton.setEnabled(false);

		for (int i = 0; i < dice_buttons.size(); i++) {
			dice_buttons.get(i).setEnabled(false);
		}
	}

	// Sets most buttons as enabled and visible
	public void enableControl() {
		// Enabling buttons
		SetAsideButton.setEnabled(true);

		for (int i = 0; i < dice_buttons.size(); i++) {
			dice_buttons.get(i).setEnabled(true);
			dice_buttons.get(i).setVisible(true);
		}
	}
	
	public void userDisconnectedError() {
		JOptionPane.showMessageDialog(FarkleGameFrame, "The other user disconnected. Closing game.", "Connection Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	public void userLost() {
		JOptionPane.showMessageDialog(FarkleGameFrame, "Better luck next time!.", "Game Results", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	
	public void userWon() {
		JOptionPane.showMessageDialog(FarkleGameFrame, "You won!!! Looks like luck was on your side!", "Game Results", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	// Resets components on the board
	private void resetBoard() {
		for (int i = 0; i < dice_buttons.size(); i++) {
			
			// Moves buttons back to where they belong :)
			dice_buttons.get(i).setBounds((90 + (i + 1) * 65), 315, 53, 49);
			
			dice_selected.set(i, false);
			dice_set_aside.set(i, false);
			
			// Sets the buttons as invisible
			dice_buttons.get(i).setVisible(false);
		}
		
		// Clears info labels
		player_label.setText("");
		warning_label.setText("");
		game_label.setText("");
		
		dice_values.clear();
	}

	// Randomizes the dice for the client
	public void roll () {
		
		// Hides info label
		warning_label.setText("");
		
	    // Random number generator
	    Random random = new Random();
	    
	    // Boolean values
	    boolean hasOne = false;
	    boolean hasFive = false;

	    // Creates and populates the empty dice_values array list with random numbers 1-6
	    if (dice_values.isEmpty()) {
	        for (int i = 0; i < 6; i++) {
	            // Sets the dice button's text to its assigned number
	            int random_num = 1 + random.nextInt(7 - 1);
	            dice_values.add(random_num);
	            dice_buttons.get(i).setIcon(new ImageIcon(GUI_Client.class.getResource("/GUI/" + (random_num) + "_dice.jpg")));
	            enableControl();

	            // Check if the roll contains a one or five
	            if (random_num == 1) {
	                hasOne = true;
	            }
	            if (random_num == 5) {
	                hasFive = true;
	            }
	        }
	        
	        RollDiceButton.setEnabled(false);
	    }
	    
	    // If the dice_values array is not empty and all dice have been selected, that means the player can reroll their
	    // all of their dice for a potential streak
	    else if (!dice_values.isEmpty() && !dice_set_aside.contains(false)) {
	    	warning_label.setText("Lucky Streak!!! Wow!");
	    	for (int i = 0; i < dice_values.size(); i++) {
	    		
	    		// Moves buttons back to where they belong :)
				dice_buttons.get(i).setBounds((90 + (i + 1) * 65), 315, 53, 49);
	    		
	    		dice_set_aside.set(i, false);
	    		
	    		int random_num = 1 + random.nextInt(7 - 1);
                dice_values.set(i, random_num);
                dice_buttons.get(i).setIcon(new ImageIcon(GUI_Client.class.getResource("/GUI/" + (random_num) + "_dice.jpg")));
                
                // Check if the roll contains a one or five
                if (random_num == 1) {
                    hasOne = true;
                }
                if (random_num == 5) {
                    hasFive = true;
                }
                
                dice_buttons.get(i).setEnabled(true);
	    	}
	    }
	    
	    // If the dice_values array is not empty, reroll only the dice that have not been set aside
	    else if (!dice_values.isEmpty()){
	        for (int i = 0; i < 6; i++) {
	            if (dice_set_aside.get(i) == false) {
	                int random_num = 1 + random.nextInt(7 - 1);
	                dice_values.set(i, random_num);
	                dice_buttons.get(i).setIcon(new ImageIcon(GUI_Client.class.getResource("/GUI/" + (random_num) + "_dice.jpg")));

	                // Check if the roll contains a one or five
	                if (random_num == 1) {
	                    hasOne = true;
	                }
	                if (random_num == 5) {
	                    hasFive = true;
	                }
	            }
	            else {
	                dice_buttons.get(i).setEnabled(false);
	            }
	        }            
	    }
	    
	    BankScoreButton.setEnabled(false);

	    // Check for farkle
	    if (!(hasOne || hasFive)) {
	    	game_label.setText("You farkled!!!");
	    	warning_label.setText("");
	        
	        // Sends to the server the player who farkled
	        try {
				client.sendToServer("Farkled_" + player_number);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        disableControl();
	    } 
	    
	}
	
	
	
	

	// Gets the score of the currently selected dice
	public static int getScore(ArrayList<Integer> dice) {
	    int score = 0;
	    HashMap<Integer, Integer> countMap = new HashMap<>();
	    for (int die : dice) {
	        countMap.put(die, countMap.getOrDefault(die, 0) + 1);
	    }
	    for (int die : countMap.keySet()) {
	        int count = countMap.get(die);

	        
	        if (die == 1) {
	            score += count * 100;
	        } else if (die == 5) {
	            score += count * 50;
	        }
	    }
	    return score;
	}
}
