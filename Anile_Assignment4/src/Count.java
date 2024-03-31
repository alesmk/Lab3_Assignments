/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 4		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Count {
	private ExecutorService service;
	private ConcurrentHashMap<Character, Integer> finalMap; 
	
	public Count() {
		this.service = Executors.newCachedThreadPool();
		this.finalMap = new ConcurrentHashMap<Character, Integer>();
	}
	
	public void startCount(ArrayList<String> args) {
		System.out.println("Inizio il conteggio...");
		this.count(args);
		
		
		this.service.shutdown();
        try {
        	while(!this.service.awaitTermination(5000, TimeUnit.MILLISECONDS))
        		System.out.println("... attendo la terminazione dei thread.");
        				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        this.print();
	}
	
	private void count(ArrayList<String> args) {
		List<Future<ConcurrentHashMap<Character, Integer>>> list = new ArrayList<Future<ConcurrentHashMap<Character, Integer>>>();
		
		for(String pathname : args) {
			
			// Controllo se il file esiste. Se sì, sottometto un task
			try{
				File file = new File(pathname);
				if(file.isFile())
					list.add(this.service.submit(new FileTask(file)));
				
				else
					System.err.printf("%s non è un file...\n", pathname);

			}catch(NullPointerException | SecurityException e) {
				System.err.printf("Il file %s non esiste...\n", pathname);
			}
		}
		
		for(Future<ConcurrentHashMap<Character, Integer>> f : list) {
			try {
				
				ConcurrentHashMap<Character, Integer> tmpMap = f.get();
				for (Map.Entry<Character, Integer> entry : tmpMap.entrySet()) {
					
					char key = entry.getKey();
					if (!this.finalMap.containsKey(key))
						this.finalMap.put(key, 0);
					
					this.finalMap.put(key, this.finalMap.get(key) + entry.getValue());
				}
			
			} catch(Exception e) {
				;
			}
		}		
	}
	
	private void print() {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.err.println("Errore apertura scrittore.");
		}

		for (Map.Entry<Character, Integer> entry : this.finalMap.entrySet())
			writer.printf("<%c>,<%d>\n", entry.getKey(), entry.getValue());
				
		writer.flush();
		System.out.println("Scrittura su file terminata.");
		writer.close();
	}
	
}
