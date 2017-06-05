package com.orca.telnet;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.beust.jcommander.JCommander;
import com.orca.command.Command;
import com.orca.command.Command.CommandType;
import com.orca.command.CommandFactory;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.Statusbar;
import net.wimpi.telnetd.io.toolkit.Titlebar;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

public class CommandShell implements Shell {

	private Connection connection;
	private BasicTerminalIO termIO;
	private final String CRLF = BasicTerminalIO.CRLF;

	public static Shell createShell() {
		return new CommandShell();
	}

	@Override
	public void run(Connection con) {
		try {
			connection = con;
			termIO = connection.getTerminalIO();

			this.connection.addConnectionListener(this);

			while (true) {

				termIO.eraseScreen();

				Titlebar title = new Titlebar(termIO, "");
				title.setTitleText("orca server");
				title.setForegroundColor(ColorHelper.YELLOW);
				title.setBackgroundColor(ColorHelper.BLUE);
				title.setAlignment(Titlebar.ALIGN_CENTER);
				title.draw();

				Statusbar status = new Statusbar(termIO, "");
				status.setStatusText("version: " + 1.0);
				status.setForegroundColor(ColorHelper.YELLOW);
				status.setBackgroundColor(ColorHelper.BLUE);
				status.setAlignment(Statusbar.ALIGN_RIGHT);
				status.draw();

				termIO.homeCursor();
				termIO.moveCursor(BasicTerminalIO.DOWN, 3);
				termIO.write("enter the command and press [Enter] or type 'list' for command list" + CRLF + "# ");
				termIO.flush();

				final int MAX_EDITFIELD_CHARS = 100;

				Editfield editfield = new Editfield(termIO, "", MAX_EDITFIELD_CHARS);
				editfield.setJustBackspace(true);
				editfield.run();

				////

				HashMap<String, Command> map = new LinkedHashMap<>();
				for (CommandType type : CommandType.values()) {
					map.put(type.getName(), CommandFactory.getInstance(type));
				}

				JCommander jc = new JCommander();

				for (CommandType type : CommandType.values()) {
					jc.addCommand(type.getName(), map.get(type.getName()));
				}

				try {
					jc.parse(editfield.getValue().split(" "));

					Command parsed = map.get(jc.getParsedCommand());
					if (parsed.help == true) {
						StringBuilder help = new StringBuilder();
						jc.usage(jc.getParsedCommand(), help);
						termIO.write(CRLF + help.toString() + CRLF);
					} else {
						if (parsed.getCommandType() == CommandType.COMMAND_LIST) {
							for (Entry<String, Command> entry : map.entrySet()) {
								termIO.write(CRLF + entry.getKey().toString());
							}
						} else {
							termIO.write(CRLF + parsed.toString() + CRLF);
						}
					}
				} catch (Exception e) {
					termIO.write(CRLF + e.getMessage() + CRLF);
				} finally {
					termIO.flush();
				}

				////

				termIO.write(CRLF + "press any key");
				termIO.flush();
				termIO.read();
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
			termIO.write("*** connection break ***" + CRLF);
			termIO.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
