package com.orca.telnet;

import java.io.FileInputStream;
import java.util.Properties;

import net.wimpi.telnetd.TelnetD;

public class main {

	public static void main(String[] args) throws Exception {

		System.out.println("orca started");

		Properties settings = new Properties();
		settings.load(new FileInputStream("telnetd.properties"));

		TelnetD daemon = TelnetD.createTelnetD(settings);
		daemon.start();
	}
}