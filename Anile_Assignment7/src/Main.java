/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 7		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

//Uno di essi legge dal file gli oggetti "conto corrente" e li passa, uno per volta, ai thread presenti in un thread pool.
public class Main {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		Map<String, Integer> finalCounter = new ConcurrentHashMap<>();
		finalCounter.put("BONIFICO", 0);
		finalCounter.put("ACCREDITO", 0);
		finalCounter.put("BOLLETTINO", 0);
		finalCounter.put("F24", 0);
		finalCounter.put("PAGOBANCOMAT", 0);

		// Decomprimere il file
		String filename = "accounts.json.gz"; 
		if(filename.endsWith(".gz")) {
			String oldFilename = filename;
			filename = filename.substring(0,filename.lastIndexOf("."));;
			System.out.println("Decomprimo il file...");
			decompress(oldFilename, filename);
		}
		
		File file = new File(filename);
		if (!file.exists() || file.isDirectory()) {
			System.err.printf("Errore: %s non è un file valido!\n", filename);
			System.exit(1);
		}
		
		// Leggo il file e sottometto task
		System.out.printf("Avvio il conteggio...\n\n");
		try { 
			JsonReader reader = null;
			try{
				reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
			}catch(IOException e) {
				System.err.println("Errore reader");
			}
			
			Gson gson = new GsonBuilder().create();
			
			reader.beginArray();
			while (reader.hasNext()) {
				Type ccType =new TypeToken<ContoCorrente>() {}.getType();
				ContoCorrente cc = gson.fromJson(reader, ccType);
				service.submit(new ReasonCounterTask(cc, finalCounter));
			}
			reader.close();
		}catch(IOException ex){
			System.err.println("IOException");
		}
	
			
		// Terminazione thread
		service.shutdown();
		try	{
			while (!service.awaitTermination(5000, TimeUnit.MILLISECONDS))
				System.out.println("... attendo la terminazione dei thread.");
	
		}catch(InterruptedException e){
			System.err.println("Interruzione su awaitTermination");
		}
		
	
		// Stampo i risultati
		for (Map.Entry<String, Integer> entry : finalCounter.entrySet())
			System.out.printf("%s: %d\n", entry.getKey(), entry.getValue());
		
		
	}
	
	private static void decompress(String input, String output) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(input));
            FileOutputStream fos = new FileOutputStream(output);
            byte[] buffer = new byte[1024];
            int len;
            while((len = gis.read(buffer)) != -1)
                fos.write(buffer, 0, len);
            
            fos.close(); gis.close();
        } catch (IOException e) {
        	System.err.println("Eccezione durante la decompressione");
        }
        System.out.println("Decompressione andata a buon fine");
     }
	

}
