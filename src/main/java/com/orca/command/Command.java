package com.orca.command;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

public abstract class Command {

	@Parameter(names = { "-h", "--help" }, help = true, description = "command help")
	public boolean help;
	@Parameter(description = "other")
	private List<String> other = new ArrayList<>();

	public static CommandType getCommandTypeByName(String name) {
		for (CommandType type : CommandType.values()) {
			if (name.equals(type.getName()))
				return type;
		}
		return null;
	}

	public abstract CommandType getCommandType();

	public static enum CommandType {
		COMMAND_1("command1"), COMMAND_2("command2"), COMMAND_3("command3"), COMMAND_LIST("list");

		private String name;

		private CommandType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	@Parameters(commandDescription = "list info")
	public static class CommandList extends Command {
		@Override
		public CommandType getCommandType() {
			return CommandType.COMMAND_LIST;
		}

		@Override
		public String toString() {
			return "CommandList [help=" + help + "]";
		}
	}

	@Parameters(commandDescription = "command1 info")
	public static class Command1 extends Command {

		@Parameter(names = { "-p1", "--param1" }, required = true, description = "param1 info")
		String param1;
		@Parameter(names = { "-p2", "--param2" }, description = "param2 info")
		Integer param2;// o

		@Override
		public CommandType getCommandType() {
			return CommandType.COMMAND_1;
		}

		@Override
		public String toString() {
			return "Command1 [param1=" + param1 + ", param2=" + param2 + ", help=" + help + "]";
		}
	}

	@Parameters(commandDescription = "command2 info")
	public static class Command2 extends Command {

		@Parameter(names = { "-p1", "--param1" }, required = true, description = "param1 info")
		String param1;
		@Parameter(names = { "-p2", "--param2" }, description = "param2 info")
		Integer param2;// o

		@Override
		public CommandType getCommandType() {
			return CommandType.COMMAND_2;
		}

		@Override
		public String toString() {
			return "Command2 [param1=" + param1 + ", param2=" + param2 + ", help=" + help + "]";
		}
	}

	@Parameters(commandDescription = "command3 info")
	public static class Command3 extends Command {

		@Parameter(names = { "-p1", "--param1" }, required = true, description = "param1 info")
		String param1;
		@Parameter(names = { "-p2", "--param2" }, description = "param2 info")
		Integer param2;// o

		@Override
		public CommandType getCommandType() {
			return CommandType.COMMAND_3;
		}

		@Override
		public String toString() {
			return "Command3 [param1=" + param1 + ", param2=" + param2 + ", help=" + help + "]";
		}
	}
}
