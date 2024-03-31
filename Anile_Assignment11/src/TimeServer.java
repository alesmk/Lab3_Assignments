
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
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServer {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: TimeServer dategroupAddress");
			System.exit(1);
		}

		String address = args[0];
		byte[] buf;

		DatagramSocket server = null;
		DatagramPacket toSend;
		final int PORT = 1234;
		InetAddress ia = null;
		int i = 0;

		try {
			server = new DatagramSocket();
			ia = InetAddress.getByName(address);
		} catch (IOException e) {
			System.out.println("[SERVER] " + e.getMessage());
			server.close();
			System.exit(1);
		}

		System.out.println("[SERVER] Avviato...");

		LocalDateTime now;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDateTime;

		while (true) {
			now = LocalDateTime.now();
			formatDateTime = now.format(formatter);
			buf = formatDateTime.getBytes();

			toSend = new DatagramPacket(buf, buf.length, ia, PORT);
			System.out.println("Invio messaggio n°" + (i++));
			try {
				server.send(toSend);
				Thread.sleep(2000);
			} catch (IOException e) {
				System.out.println("[SERVER] Invio - " + e.getMessage());
				break;
			} catch (InterruptedException e) {
				System.out.println("[SERVER] Interruzione sleep");
				break;
			}

		}

		server.close();
		System.out.println("[SERVER] Fine.");


	}

}
