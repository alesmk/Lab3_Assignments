/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 7		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.util.HashMap;
import java.util.Map;

//“reason” può assumere 5 valori distinti, ovvero: “BONIFICO”, “ACCREDITO”,“BOLLETTINO”, “F24” e “PAGOBANCOMAT
public class ReasonCounterTask implements Runnable {
	private ContoCorrente cc;
	private Map<String, Integer> localCounter;
	private Map<String, Integer> finalCounter; // Riferimento alla hashMap globale per memorizzare i risultati 
	
	public ReasonCounterTask(ContoCorrente contoCorrente, Map<String, Integer> counter) {
		this.cc = contoCorrente;
		this.localCounter = new HashMap<String, Integer>();
		this.finalCounter = counter;
	}
	
	public void run() {
		String key;
		
		// Scorro i record
		for(Record r : cc.getRecords()) {
			key = r.getReason();
			Integer value = this.localCounter.get(key);
			this.localCounter.put(key, (value == null ? 0 : value) + 1);
		}
		
		// Scrivo i risultati nella hash map globale
		for (Map.Entry<String, Integer> entry : localCounter.entrySet()) {
			Integer count = entry.getValue();
			this.finalCounter.compute(entry.getKey(), (k, v) -> ((v == null) ? count : v + count));
		}
	}

}
