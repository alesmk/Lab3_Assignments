/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 5		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package threadPool_version;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Task implements Runnable {
	private ArrayList<String> lines;
	 
	
	public Task(ArrayList<String> _lines) { // Insieme di righe del file
		this.lines = new ArrayList<String>();
		for(String l : _lines)
			this.lines.add(l);		
	}

	public void run() {
		
		String[] splittedLine;
		String _ip;
		InetAddress addr = null;
		
		for(String line : this.lines) {
			splittedLine = line.split(" ");
			_ip = splittedLine[0];
							
			try{
			    addr = InetAddress.getByName(_ip);
			}
			catch(UnknownHostException e){
			    System.err.println("UnknownHostException");
			}
			
			_ip = addr.getCanonicalHostName();
	
			splittedLine[0] = _ip;
			line = String.join(" ", splittedLine);
			System.out.println(line);
		
		}

	}

}
