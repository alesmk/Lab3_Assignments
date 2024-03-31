/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 4		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class FileTask implements Callable<ConcurrentHashMap<Character, Integer>> {
	private File file;
	private ConcurrentHashMap<Character, Integer> map; 
	
	public FileTask(File file) {
		this.file = file;
		this.map = new ConcurrentHashMap<Character, Integer>();
	}
	
	public ConcurrentHashMap<Character, Integer> getMap() {
		return this.map;
	}

	// Leggere il file e contare le occorrenze
	public ConcurrentHashMap<Character, Integer> call() {

		FileReader reader = null;
		int read;
	    char c;
	    
		try { 
			reader = new FileReader(this.file); 
		    
			read = reader.read();
			
			while(read != -1) {
				
				c = (char)read;
				
				if(Character.isLetter(c)) {
					
					// Rimuovo i segni diacritici
					c = (Normalizer.normalize(Character.toString(c), Normalizer.Form.NFKD).replaceAll("\\p{M}", "")).charAt(0);
					
					// Maiuscole
					if(Character.isUpperCase(c)) 
						c = Character.toLowerCase(c);		
					
					if(!this.map.containsKey(c))
						this.map.put(c, 0);
				
					this.map.put(c, this.map.get(c)+1);
				}
							
			    read = reader.read();	// Legge un carattere
			}
			
		}catch (IOException e) { //The exception FileNotFoundException is already caught by the alternative IOException
			System.err.println("Errore durante la lettura del file.");
		} 
		finally { 
			if (reader != null) { 
				try { 
					reader.close(); 
				} catch (IOException e) { 
					;
				}
			
			} 
		}
		return this.map;
	}
		
	

}
