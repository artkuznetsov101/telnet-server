package com.orca.telnet;

import java.io.IOException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.Label;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class LoginShell implements Shell {

	private Connection connection;
	private BasicTerminalIO termIO;
	private final String CRLF = BasicTerminalIO.CRLF;

	public static Shell createShell() {
		return new LoginShell();
	}

	@Override
	public void run(Connection con) {
		try {
			connection = con;
			termIO = connection.getTerminalIO();

			this.connection.addConnectionListener(this);

			termIO.eraseScreen();
			termIO.homeCursor();
			termIO.write("welcome to orca server (user=any password=12345)");

			final Label userNameLabel = new Label(termIO, "userNameLabel", "User Name: ");
			final Editfield userNameField = new Editfield(termIO, "userNameField", 50);
			userNameLabel.setLocation(0, 3);
			userNameField.setLocation(userNameLabel.getDimension().getWidth(), 3);
			userNameLabel.draw();
			userNameField.run();

			String userName = userNameField.getValue();

			int attemptsRemaining = 5;
			final Label passwordLabel = new Label(termIO, "passwordLabel", "Password: ");
			final Editfield passwordField = new Editfield(termIO, "passwordField", 50);
			passwordLabel.setLocation(0, 5);
			passwordField.setLocation(passwordLabel.getDimension().getWidth(), 5);
			passwordField.setPasswordField(true);
			boolean correct = false;
			while (!correct && attemptsRemaining > 0) {
				passwordField.clear();
				passwordLabel.draw();
				passwordField.run();
				attemptsRemaining--;
				correct = passwordField.getValue().equalsIgnoreCase("12345");
			}

			if (!correct) {
				// LOGGER.info("user logged in");
				return;
			}

			// LOGGER.info("user rejected");
			termIO.eraseScreen();
			termIO.homeCursor();

			if (connection.setNextShell("command")) {
				connection.removeConnectionListener(this);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionTimedOut(ConnectionEvent ce) {

		try {
			termIO.write("*** connection timedout ***" + CRLF);
			termIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.close();

	}

	@Override
	public void connectionIdle(ConnectionEvent ce) {

		try {
			termIO.write(CRLF + "*** connection idle ***" + CRLF);
			termIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLogoutRequest(ConnectionEvent ce) {
		try {
			termIO.write(CRLF + "*** connection logout request ***" + CRLF);
			termIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionSentBreak(ConnectionEvent ce) {
		try {
			termIO.write("*** connection break" + CRLF);
			termIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
