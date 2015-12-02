package org.napu.bio.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String[] args) throws IOException {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {

			}
		}
		ServerSocket server = null;
		// try {
		// server = new ServerSocket(port);
		// System.out.println("The time server is start in port:" + port);
		// Socket socket = null;
		// while (true) {
		// socket = server.accept();
		// new Thread(new TimeServerHandler(socket)).start();
		// }
		// } finally {
		// if (server != null) {
		// System.out.println("The time server close");
		// server.close();
		// server = null;
		// }
		// }

		try {
			server = new ServerSocket(port);
			System.out.println("===================");
			Socket socket = null;
			TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
			while (true) {
				socket = server.accept();
				singleExecutor.execute(new TimeServerHandler(socket));
			}
		} finally {
			if (server != null) {
				server.close();
				server = null;
			}
		}
	}
}
