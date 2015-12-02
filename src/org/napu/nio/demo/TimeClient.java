package org.napu.nio.demo;

public class TimeClient {
	public static void main(String[] args) {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}

		new Thread(new TimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
	}
}
