package org.napu.nio.demo;

public class TimeServer {
	public static void main(String[] args) {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {

			}
		}
		MulitplexerTimeServer timeServer = new MulitplexerTimeServer(port);
		new Thread(timeServer, "NIO-MulitplexerTimeServer-001").start();
	}
}
