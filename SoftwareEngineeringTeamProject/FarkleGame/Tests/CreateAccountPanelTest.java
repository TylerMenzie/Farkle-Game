package FarkleGame.Tests;

import org.junit.Before;
import org.junit.Test;

import FarkleGame.CreateAccountControl;
import FarkleGame.CreateAccountPanel;

import javax.swing.*;

import static org.junit.Assert.*;

public class CreateAccountPanelTest {

    private CreateAccountPanel createAccountPanel;

    @Before
    public void setUp() {
        // Create an instance of CreateAccountPanel
        CreateAccountControl cac = new CreateAccountControl(createAccountPanel, null);
        createAccountPanel = new CreateAccountPanel(cac);
    }

    @Test
    public void testGetUsername() {
        // Set username field text
        createAccountPanel.usernameField.setText("testUsername");
        // Check if getUsername() returns the correct username
        assertEquals("testUsername", createAccountPanel.getUsername());
    }

    @Test
    public void testGetPassword() {
        // Set password field text
        createAccountPanel.passwordField.setText("testPassword");
        // Check if getPassword() returns the correct password
        assertEquals("testPassword", createAccountPanel.getPassword());
    }

    @Test
    public void testGetPasswordVerify() {
        // Set verify password field text
        createAccountPanel.passwordVerifyField.setText("testPasswordVerify");
        // Check if getPasswordVerify() returns the correct verify password
        assertEquals("testPasswordVerify", createAccountPanel.getPasswordVerify());
    }

    @Test
    public void testSetError() {
        // Set error label text
        createAccountPanel.setError("testError");
        // Check if setError() sets the error label text correctly
        assertEquals("testError", createAccountPanel.errorLabel.getText());
    }
}
