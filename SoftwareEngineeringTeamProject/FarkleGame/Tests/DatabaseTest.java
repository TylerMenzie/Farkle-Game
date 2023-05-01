package FarkleGame.Tests;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

import org.junit.*;

import FarkleGame.Database;


public class DatabaseTest {

	String[] users = {"jsmith@uca.edu","msmith@uca.edu","tjones@yahoo.com","jjones@yahoo.com"};
	String[] passwords = {"hello123","pass123","123456","hello1234"};

	private Database db; 
	
	private int rando;

	@Before //Makes Run before each test
	public void setUp() throws Exception 
	{
		db = new Database(); 
		db.setStream("./FarkleGame/users.txt");
		rando = ((int)Math.random()*users.length); 
	}

	@Test
	public void testSetStream() throws FileNotFoundException
	{
		//1. call setStream() with users.txt

		db.setStream("./FarkleGame/users.txt");

		//2. call getStream() and return a FileInputStream object (fis)

		FileInputStream fis = db.getStream();

		//3. make sure FileInputStream object returned by getStream is not null

		assertNotNull("Check setStream", fis); //Place object here 

		//fail("not yet implemented");
	}

	@Test(expected = FileNotFoundException.class)
	public void testStream() throws FileNotFoundException
	{

		//1. Set the stream with user.txt (wrong name) – should throw FileNotFoundException
		db.setStream("./FarkleGame/user.txt");
	}

	@Test
	public void testQuery() throws IOException 
	{
		int i = 0 ;
		for(i = 0; i <users.length;i++)
		{
			db = new Database(); 
			db.setStream("./FarkleGame/users.txt");
			
		//Use Random # to extract username/ and expected password	
		String username = users[i]; 
		String expected = passwords[i];

		//get actual result (invoke query with username
		ArrayList<String> actual = db.query(username);	


		//compare expected with actual using assertEquals
		assertEquals("testQuery: input " + username , expected, actual);
		}

	}

	@Test  //Test for bad user name
	public void testQuery2() throws IOException
	{

		//Set bad username to an invalid name

		ArrayList<String> actual = db.query("fake@gmail.com");

		//Extract actual name based on bad user name

		

		//compare actual with assertNull 

		assertNull("Check testquery2", actual); 
	}

}
