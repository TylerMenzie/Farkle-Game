package FarkleGame;

import java.util.*;
import java.sql.*;
import java.io.*;

//Author: 			Shandon Probst
//Description:		

public class Database {

	// Private connection object
	private Connection connection;
	 private FileInputStream fis;
	public Database() {
		// Creation of properties and file input objects
		Properties file_properties = new Properties();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream("FarkleGame/farkle_database.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			file_properties.load(fis);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String url = file_properties.getProperty("url");
		String user = file_properties.getProperty("user");
		String pass = file_properties.getProperty("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> query(String query) {
		ArrayList<String> to_return = new ArrayList<String>();

		try {
			// Statement and query objects
			Statement statement = connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);

			// Checks if there is data found
			if (!result_set.next()) {
				return null;
			} else {

				// Result meta data object and column count
				ResultSetMetaData result_meta_data = result_set.getMetaData();
				int column_count = result_meta_data.getColumnCount();

				// Adds each record to to_return
				do {
					String record = "";
					for (int i = 1; i <= column_count; i++) {
						record += result_set.getString(i);

						if (i < column_count)
							record += ",";
					}
					to_return.add(record);
				} while (result_set.next());
			}
		} catch (SQLException e) {
			return null;
		}

		return to_return;
	}

	public void executeDML(String dml) throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute(dml);
	}

	// Returns true if the account is verified and returns false if otherwise.
	public boolean verifyAccount(String username, String password) {
		try {
			String query = "select aes_decrypt(encrypted_password, 'key') from users where username = '" + username
					+ "'";
			Statement statement = connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);

			// Checks if there is a password associated with the username and returns true
			// if found
			if (result_set.next()) {
				String decrypted_password = result_set.getString("aes_decrypt(encrypted_password, 'key')");

				// Check if the decrypted password is equal to entered password
				if (decrypted_password.equals(password)) {
					return true;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Returns false automatically if no results are returned
		return false;
	}

	// Returns true if the score is added to the user's profile.
	public boolean addHighScore(String username, int score) {
		return false;
	}
	
	 public void setStream(String fn) throws FileNotFoundException
	  {
	    fis = new FileInputStream(fn);
	    
	  }
	 
	 public FileInputStream getStream()
	  {
	    return fis;
	  }

	// Returns true if the account is created and returns false if otherwise.
	public boolean createNewAccount(String username, String password) {
		try {
			String query = "select username from users where username ='" + username + "'";
			Statement statement = connection.createStatement();
			ResultSet result_set = statement.executeQuery(query);

			// If the username can't be found, then create a new account in the users table
			if (!result_set.next()) {

				// Adds user account and password
				String update = "insert into users (username, encrypted_password) " + "values(\"" + username
						+ "\",aes_encrypt(\"" + password + "\",'key'))";
				statement.executeUpdate(update);

				// Returns true because account is created
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
