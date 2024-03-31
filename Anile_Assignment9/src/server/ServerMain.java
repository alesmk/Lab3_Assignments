/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 9		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerMain {

	public static void main(String[] args) {

		ServerSocketChannel serverSocket = null;
		ArrayList<SocketChannel> connectedClients = new ArrayList<SocketChannel>();
		Selector selector = null;
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		Set<SelectionKey> selectedKeys;
		Iterator<SelectionKey> keyIterator;
		SelectionKey key;

		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(false);
			Runtime.getRuntime().addShutdownHook(new TerminationHandler(serverSocket, connectedClients));
			serverSocket.bind(new InetSocketAddress("localhost", 1313));
		} catch (IOException e) {
			System.err.println("[SERVER] Connessione non riuscita!");
			return;
		}

		System.out.println("[SERVER] Server attivo...");

		try {
			selector = Selector.open();

			serverSocket.register(selector, SelectionKey.OP_ACCEPT, null);
		} catch (IOException e) {
			System.err.println("[SERVER] Eccezione Selector - " + e.getMessage());
			return;
		}


		while (true) {
			int readyChannels = 0;
			try {
				readyChannels = selector.selectNow(); // Non bloccante
			} catch (IOException e) {
				System.err.println("[SERVER] - " + e.getMessage());
			}

			if (readyChannels == 0)
				continue;

			selectedKeys = selector.selectedKeys();
			keyIterator = selectedKeys.iterator();

			while (keyIterator.hasNext()) {
				key = keyIterator.next();

				// ServerSocketChannel accetta una connessione
				if (key.isAcceptable())
					acceptable(serverSocket, selector, connectedClients);

				// Un channel è pronto per la lettura
				else if (key.isReadable()) {
					int rr = readable(key, selector, connectedClients, keyIterator, map);
					if (rr != 0)
						break;
				}

				// Un channel è pronto per la scrittura
				else if (key.isWritable())
					writable(key, selector, connectedClients, keyIterator, map);

				keyIterator.remove();

			}
		}

	}

	private static void acceptable(ServerSocketChannel serverSocket, Selector sel,
			ArrayList<SocketChannel> connectedClients) {

		SocketChannel client = null;
		try {
			client = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("[SERVER] - " + e.getMessage());
		}

		if (client == null) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.err.println("[SERVER] Interruzione sleep - " + e.getMessage());
			}
			return;
		}

		connectedClients.add(client);

		try {
			client.configureBlocking(false);
			client.register(sel, SelectionKey.OP_READ);
		} catch (IOException e) {
			System.err.println("[SERVER] -- " + e.getMessage());
		}

		System.out.println("[SERVER] Nuova connessione accettata.");
	}

	private static int readable(SelectionKey key, Selector sel, ArrayList<SocketChannel> connectedClients,
			Iterator<SelectionKey> keyIterator, HashMap<String, ArrayList<String>> map) {

		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		String message = "", data, clientAddress;
		ArrayList<String> list;

		try {
			client.read(buffer);
		} catch (IOException e) {
			System.err.println("[SERVER] Eccezione durante lettura client - " + e.getMessage());
			keyIterator.remove();
			try {
				client.close();
			} catch (IOException e1) {
				System.err.println("[SERVER] - " + e.getMessage());
			}
			return -1;

		}

		data = new String(buffer.array(), StandardCharsets.UTF_8).trim();
		message = data + " [echoed by server]\n";

		if (data.equalsIgnoreCase("END")) {
			try {
				client.close();
				System.out.println("[SERVER] Connessione terminata");
				keyIterator.remove();
				connectedClients.remove(client);
				return 1;
			} catch (IOException e) {
				System.err.println("[SERVER] " + e.getMessage());
			}

		}

		clientAddress = client.socket().getInetAddress().toString() + ":" + client.socket().getPort();
		list = map.get(clientAddress);

		if (list == null) {
			list = new ArrayList<String>();
			map.put(clientAddress, list);
		}

		list.add(message);

		try {
			client.register(sel, SelectionKey.OP_WRITE);
		} catch (ClosedChannelException e) {
			System.err.println("[SERVER] " + e.getMessage());
		}

		return 0;
	}

	private static void writable(SelectionKey key, Selector sel, ArrayList<SocketChannel> connectedClients,
			Iterator<SelectionKey> keyIterator, HashMap<String, ArrayList<String>> map) {

		SocketChannel client = (SocketChannel) key.channel();
		String clientAddress = client.socket().getInetAddress().toString() + ":" + client.socket().getPort();
		ArrayList<String> list = map.get(clientAddress);
		ByteBuffer resBuffer;
		String msg;
		byte[] response;

		if (list != null) {

			while (!list.isEmpty()) {
				msg = list.get(0);
				response = msg.getBytes(StandardCharsets.UTF_8);
				resBuffer = ByteBuffer.wrap(response);

				try {
					client.write(resBuffer);
				} catch (IOException e) {
					System.err.println("[SERVER] Eccezione durante scrittura - " + e.getMessage());
					keyIterator.remove();
					try {
						client.close();
					} catch (IOException e1) {
						System.err.println("[SERVER] " + e.getMessage());
					}
					continue;
				}
				list.remove(msg); // Rimuovo dalla lista l'elemento appena scritto
			}
		}

		try {
			client.register(sel, SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			System.err.println("[SERVER] " + e.getMessage());

		}
	}

}
