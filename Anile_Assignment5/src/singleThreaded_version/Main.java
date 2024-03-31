/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 5		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package singleThreaded_version;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main
{	
	public static void main(String[] args) {
	     
		Scanner sc = new Scanner(System.in);
		System.out.print("Inserisci il nome del logfile: ");
		String filename = sc.nextLine();		
		sc.close();
		
		BufferedReader reader;
		String[] splittedLine;
		String _ip;
		InetAddress addr = null;
		
		Long datetime1 = System.currentTimeMillis();
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				
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
				
				// Leggo la prossima riga
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			System.err.println("Errore file");
		}
		Long datetime2 = System.currentTimeMillis();

        Long datetime = datetime2 - datetime1;
        //System.out.println("TIME: " + datetime); // ***TIME: 181394 = 3 minuti
		 		
	}
}