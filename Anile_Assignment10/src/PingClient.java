
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
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PingClient {

	public static void main(String[] args) {

		String hostname = null;
		int port = 0;

		if (args.length == 1) {
			try {
				hostname = args[0];
			} catch (Exception e) {
				System.err.println("ERR -arg 1");
				System.exit(1);
			}
		} else if (args.length == 2) {
			try {
				hostname = args[0];
			} catch (Exception e) {
				System.err.println("ERR -arg 1");
				System.exit(1);
			}
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("ERR -arg 2");
				System.exit(1);
			}
		} else {
			System.err.println("[SERVER] Usage: java PingClient hostname port");
			System.exit(1);
		}

		DatagramSocket client = null;
		InetAddress hostAddress = null;
		DatagramPacket request = null, response;

		byte[] buffer = new byte[1024], toSendBytes = null;
		String toSend, pinged = "";

		long timestamp, responseTimestamp = 0;
		int transmitted = 0, seqno = 0, packetsReceived = 0, rtt, sumRTT = 0, minRTT = 2001, maxRTT = -1, totMsg = 10;
		float avgRTT = 0;
		 ArrayList<String> lost = new ArrayList<String>();

		try {
			client = new DatagramSocket();
			client.setSoTimeout(2000);
		} catch (SocketException e) {
			System.err.println("[CLIENT] " + e.getMessage());
			client.close();
			System.exit(1);
		}

		try {
			hostAddress = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			System.err.println("[CLIENT] UnknownHostException: " + e.getMessage());
		}

		for (; seqno < totMsg; seqno++) {
			toSend = "PING " + seqno + " " + (timestamp = System.currentTimeMillis());

			try {
				toSendBytes = toSend.getBytes("US-ASCII");
				request = new DatagramPacket(toSendBytes, toSendBytes.length, hostAddress, port);
				client.send(request);
				transmitted++;
			} catch (IOException e) {
				System.err.println("[CLIENT] Eccezione send - " + e.getMessage());
			}
			response = new DatagramPacket(buffer, buffer.length);

			try {
				do {
					client.receive(response);
					responseTimestamp = System.currentTimeMillis();
					pinged = new String(response.getData(), 0, response.getLength(), "US-ASCII");
				}while(lost.contains(pinged));
				
			} catch (IOException e) {
				lost.add(toSend);
				System.out.println(toSend + " RTT: *");
				continue;
			}
			
			rtt = (int) (responseTimestamp - timestamp);
			sumRTT += rtt;

			if (rtt > maxRTT)
				maxRTT = rtt;
			if (rtt < minRTT)
				minRTT = rtt;

			packetsReceived++;

			System.out.println(pinged + " RTT: " + rtt + " ms");
		}

		client.close();

		// Statistiche
		System.out.println("---- PING Statistics ----");

		int packetLoss = 100 - transmitted * packetsReceived;
		if (sumRTT != 0) {
			avgRTT = ((float) sumRTT) / packetsReceived;
		} else {
			minRTT = 0;
			maxRTT = 0;
		}

		System.out.printf("%d packets transmitted, ", transmitted);
		System.out.printf(" %d packets received, ", packetsReceived);
		System.out.printf(" %d%% packet loss, ", packetLoss);
		System.out.printf(" round-trip (ms) min/avg/max = %d/%.2f/%d\n", minRTT, avgRTT, maxRTT);

	}

}
