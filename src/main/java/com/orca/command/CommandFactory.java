package com.orca.command;

import com.orca.command.Command.Command1;
import com.orca.command.Command.Command2;
import com.orca.command.Command.Command3;
import com.orca.command.Command.CommandList;
import com.orca.command.Command.CommandType;

public class CommandFactory {

	public static Command getInstance(CommandType type) {
		switch (type) {
		case COMMAND_1:
			return new Command1();
		case COMMAND_2:
			return new Command2();
		case COMMAND_3:
			return new Command3();
		case COMMAND_LIST:
			return new CommandList();
		}
		return null;
	}
}
