/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 3		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package assignment3;
import java.util.Comparator;

public class Comparatore implements Comparator<Utente> {

	@Override
	public int compare(Utente u1, Utente u2) {
		
		if((u1 instanceof Professore && u2 instanceof Professore) || 
				(u1 instanceof Tesista && u2 instanceof Tesista) ||
				(u1 instanceof Studente && u2 instanceof Studente)) {
			return u1.getUltimoUtilizzo().compareTo(u2.getUltimoUtilizzo());
		}
		else if(u1 instanceof Professore) {
			return -1;
		}
		else if(u2 instanceof Professore) {
			return 1;
		}
		else if(u1 instanceof Tesista) {
			return -1;
		}
		else if(u2 instanceof Tesista) {
			return 1;
		}
		
		return 1;
	}

}

