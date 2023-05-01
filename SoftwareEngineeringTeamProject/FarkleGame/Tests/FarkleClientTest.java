package FarkleGame.Tests;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import FarkleGame.CreateAccountControl;
import FarkleGame.FarkleClient;
import FarkleGame.GUI_Client;
import FarkleGame.LoginControl;

public class FarkleClientTest {

    private FarkleClient farkleClient;
    private GUI_Client guiClient;
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;

    @Before
    public void setUp() {
        farkleClient = new FarkleClient();
        farkleClient.setGUI(guiClient);
        farkleClient.setLoginControl(loginControl);
        farkleClient.setCreateAccountControl(createAccountControl);
    }


	@Test
    public void testConnectionClosed() {
        farkleClient.connectionClosed();
  
    }

    @Test
    public void testSetAndGetUsername() {
        farkleClient.setUsername("username");
        assertEquals("username", farkleClient.getUsername());
    }

    @Test
    public void testSetAndGetNumber() {
        farkleClient.setNumber(1);
        assertEquals(1, farkleClient.getNumber());
    }

    @Test
    public void testSetAndGetOppUsername() {
        farkleClient.setOppUsername("opp_username");
        assertEquals("opp_username", farkleClient.getOppUsername());
    }

    @Test
    public void testSetAndGetOppNumber() {
        farkleClient.setOppNumber(2);
        assertEquals(2, farkleClient.getOppNumber());
    }

    @Test
    public void testSetAndGetScore() {
        farkleClient.setScore(100);
        assertEquals(100, farkleClient.getScore());
    }

    @Test
    public void testSetAndGetOppScore() {
        farkleClient.setOppScore(50);
        assertEquals(50, farkleClient.getOppScore());
    }
}
