package FarkleGame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//Author: 			Shandon Probst
//Description:		

@SuppressWarnings("serial")
public class GUI_Server extends JFrame {

	// Server object
	private FarkleServer server;

	// JLabels
	private JLabel status; // Initialized to "Not Connected"

	// JButtons
	private JButton listen;
	private JButton close;
	private JButton stop;
	private JButton quit;

	// JTextFields and corresponding labels
	private String[] labels = { "Port #", "Timeout" };
	private JTextField[] textFields = new JTextField[labels.length];

	// JTextArea
	private JTextArea serverlog;

	// Variables
	private Boolean started;

	// Constructor
	public GUI_Server() {

		// Sets the title of the frame
		this.setTitle("Farkle Game Server");
		started = false;

		// Makes GUI visible
		this.setSize(500, 600);
		;
		setVisible(true);

		// Set layout as borderlayout
		this.setLayout(new BorderLayout(0, 0));

		// Default close operation
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add text and set color to red to status JLabel
		// Solution pulled from StackOverflow @ https://stackoverflow.com/a/2966363
		status = new JLabel(("<html>Status: <font color='red'>Not Connected</font></html>"));

		// Creation of buttons
		listen = new JButton("Listen");
		close = new JButton("Close");
		stop = new JButton("Stop");
		quit = new JButton("Quit");

		// Store the "status" JLabel in a JPanel named "north"
		JPanel north = new JPanel(new FlowLayout());
		north.add(status);

		// Store the buttons in a JPanel named "south"
		JPanel south = new JPanel(new FlowLayout());
		south.add(listen);
		south.add(close);
		south.add(stop);
		south.add(quit);

		// Grid Layout for all text fields and text areas
		JPanel center = new JPanel(new GridLayout(0, 1, 10, 10));

		// FlowLayout for the other panels
		JPanel outer = new JPanel(new FlowLayout());
		JPanel textfieldspanel = new JPanel(new GridLayout(labels.length, 1, 5, 10));

		for (int i = 0; i < labels.length; i++) {

			JLabel jlabel = new JLabel(labels[i], SwingConstants.CENTER);
			textFields[i] = new JTextField(12);

			textfieldspanel.add(jlabel);
			textfieldspanel.add(textFields[i]);
		}

		// Sets the default port and timeout
		textFields[0].setText("8300"); // Port
		textFields[1].setText("500"); // Timeout

		outer.add(textfieldspanel);

		// Creates new panel and label for server log
		JPanel serverlogpanel = new JPanel(new BorderLayout(0, 0));
		JLabel serverloglabel = new JLabel("Server Log");
		serverloglabel.setHorizontalAlignment(JLabel.CENTER);

		// Creation of text areas and setting size
		serverlog = new JTextArea("", 12, 40);

		// Creating scroll pane around text area
		JScrollPane serverlogscroll = new JScrollPane(serverlog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel outer1 = new JPanel(new FlowLayout());

		// Adds the text areas to their outer panels
		outer1.add(serverlogscroll);

		// Sets up server log panel
		serverlogpanel.add(serverloglabel, BorderLayout.NORTH);
		serverlogpanel.add(outer1, BorderLayout.CENTER);

		center.add(outer, BorderLayout.NORTH);
		center.add(serverlogpanel, BorderLayout.SOUTH);

		// Adds to JFrame
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(south, BorderLayout.SOUTH);

		// Creation of FarkleServer instance
		server = new FarkleServer();
		server.setLog(this.serverlog);
		server.setStatus(this.status);

		// Listen actionlistener
		listen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If either the port or timeout is blank
				if (textFields[0].getText().isBlank() || textFields[1].getText().isBlank()) {
					serverlog.append("Port Number / Timeout not entered before pressing \"Listen\" button\n");
				}
				// Else set the server port and timeout
				else {
					// Set port and timeout
					server.setPort(Integer.parseInt(textFields[0].getText()));
					server.setTimeout(Integer.parseInt(textFields[1].getText()));

					// Try to start listening
					try {
						server.listen();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					started = true;
				}
			}
		});

		// Close actionlistener
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (started.equals(false)) {
					serverlog.append("Server not currently started\n");
				} else {
					// Try to close server
					try {
						server.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					started = false;
				}
			}
		});

		// Stop actionlistener
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (started.equals(false)) {
					serverlog.append("Server not currently started\n");
				} else {
					// Stop server listening
					server.stopListening();
					started = false;
				}
			}
		});

		// Quit actionlistener
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Set up the farkle server object
		server = new FarkleServer();
		server.setStatus(status);
		server.setLog(serverlog);

	}
}
