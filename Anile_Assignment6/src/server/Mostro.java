/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 6		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

package server;
import java.util.concurrent.ThreadLocalRandom;

public class Mostro {

	private int Z;
	private int salute;
	
	public Mostro() {
		this.Z = ThreadLocalRandom.current().nextInt(1, 101);
		this.salute = this.Z;
	}
	
	public int getZ() {
		return Z;
	}

	public int getSalute() {
		return salute;
	}

	public void setSalute(int salute) {
		this.salute = salute;
	}
	
}
