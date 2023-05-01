package FarkleGame.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;




import org.junit.Before;
import org.junit.Test;

import FarkleGame.FarkleClient;
import FarkleGame.GUI_Client;

public class GUI_ClientTest {
	
	private GUI_Client gui;
	private FarkleClient farkleClient;
	private ArrayList<Integer> dice_values;
	
	@Before
	public void setUp() {
		farkleClient = new FarkleClient();
		gui = new GUI_Client(farkleClient);
	}
	
	@Test
	public void testGetScore() {
		ArrayList<Integer> dice = new ArrayList<>();
		dice.add(1);
		dice.add(1);
		dice.add(1);
		dice.add(5);
		dice.add(3);
		dice.add(4);
		
		int score = GUI_Client.getScore(dice);
		
		// Check if the score is calculated correctly
		assertEquals(350, score);
	}
	
	
}