/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 5		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package threadPool_version;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Inserisci il nome del logfile: ");
			String filename = sc.nextLine();		
			sc.close();
				
			BufferedReader reader;
			ExecutorService service = Executors.newCachedThreadPool();
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			int n, i;
			
			Long datetime1 = System.currentTimeMillis();
			
			try {
				reader = new BufferedReader(new FileReader(filename));
				line = reader.readLine();
				while (line != null) {
					n = ThreadLocalRandom.current().nextInt(1, 10);
					for(i=0; i<n; i++) {
						lines.add(line);
						line = reader.readLine();
						if(line == null) break;
					}
					
					// Sottomettere come task
					service.execute(new Task(lines));
					lines.clear();	
				}
				reader.close();
			}
			catch (IOException e) {
				System.err.println("Errore file");
			}
			
			service.shutdown();
	        try {
	        	while(!service.awaitTermination(5000, TimeUnit.MILLISECONDS))
	        		;
	        				
			} catch (InterruptedException e) {
				;
			}
	        
	        Long datetime2 = System.currentTimeMillis();
	        Long datetime = datetime2 - datetime1;
	        //System.out.println("TIME: " + datetime); // ***TIME: 14,81 secondi
			
	}

}
