/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 10		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class PingServer {

	public static void main(String[] args) {

		int port = -1;
		long seed = new Random().nextLong(); // Inizializzo il seed nel caso in cui non sia stato passato come param.
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("ERR -arg 1");
				System.exit(1);
			}
		} else if (args.length == 2) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("ERR -arg 1");
				System.exit(1);
			}
			try {
				seed = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("ERR -arg 2");
				System.exit(1);
			}
		} else {
			System.err.println("[SERVER] Usage: java PingServer port [seed]");
			System.exit(1);
		}

		DatagramSocket server = null;
		InetAddress clientAddress = null;
		byte[] responseData = null, buffer = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
		DatagramPacket responsePacket = null;
		int probPerdita = 25, ritardo, clientPort;
		boolean perdita;
		String receivedMsgString;
		Random r = new Random(seed);

		try {
			server = new DatagramSocket(port); 
			Runtime.getRuntime().addShutdownHook(new TerminationHandler(server));
		} catch (SocketException e) {
			System.err.println("[SERVER] " + e.getMessage());
			System.exit(1);
		}

		while (!server.isClosed()) {
			
			// Ricevo il messaggio
			try {
				server.receive(receivedPacket);
				if (receivedPacket == null)	continue;
				
				clientAddress = receivedPacket.getAddress();
				clientPort = receivedPacket.getPort();

				receivedMsgString = new String(receivedPacket.getData(), 0, receivedPacket.getLength(), "US-ASCII");

			} catch (IOException e) {
				System.err.println("[SERVER] Receive - " + e.getMessage());
				continue;
			}

			int n = 1 + r.nextInt(100 + 1);
			perdita = (n <= probPerdita);  

			// Echo
			if (!perdita) {

				ritardo = r.nextInt(2500); 
				
				System.out.println(clientAddress + ":" + clientPort + "> " + receivedMsgString + " ACTION: delayed "
						+ ritardo + " ms");

				try {
					Thread.sleep(ritardo);
				} catch (InterruptedException e1) {
					System.err.println("Interruzione su sleep");
					break;
				}

				try {
					responseData = receivedMsgString.getBytes("US-ASCII");
					responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);

					server.send(responsePacket);
				} catch (IOException | RuntimeException e) {
					System.err.println("[SERVER] Response - " + e.getMessage());
				} 
			} else {
				System.out.println(clientAddress + ":" + clientPort + "> " + receivedMsgString + " ACTION: not sent");
			}

		}

		server.close();
	}

}
