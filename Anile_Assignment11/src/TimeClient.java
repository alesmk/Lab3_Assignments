
/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 11		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TimeClient {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: TimeClient dategroupAddress");
			System.exit(1);
		}

		String address = args[0];
		final int PORT = 1234;

		// Unirsi al gruppo multicast
		MulticastSocket client = null;
		InetAddress ia = null;
		try {
			client = new MulticastSocket(PORT);
			ia = InetAddress.getByName(address);
			client.setSoTimeout(5000);
			client.joinGroup(ia);
		} catch (IOException e) {
			System.out.println("[CLIENT] Errore durante l'unione al gruppo." + e.getMessage());
			client.close();
			System.exit(1);
		}
		System.out.println("[CLIENT]");

		// Ricezione
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		String receivedMsg = "";
		int i = 0;

		while (i < 10) {
			try {
				client.receive(dp);
			} catch (IOException e) {
				System.out.printf("[CLIENT] (%d) Timeout scaduto.\n", ++i);
				continue;
			}

			try {
				receivedMsg = new String(dp.getData(), 0, dp.getLength(), "US-ASCII");
			} catch (IOException e) {
				System.out.println("[CLIENT] " + e.getMessage());

			}
			System.out.printf("%d: %s\n", ++i, receivedMsg);
		}

		try {
			client.leaveGroup(ia);
		} catch (IOException e) {
			System.out.println("[CLIENT] Errore abbandono gruppo.");
		}

		client.close();
		System.out.println("[CLIENT] *** Fine ***");

	}

}
