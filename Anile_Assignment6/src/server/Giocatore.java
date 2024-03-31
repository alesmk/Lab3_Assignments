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

public class Giocatore {
	private int X;	//livello salute iniziale
	private int Y;	//livello pozione iniziale
	private int salute;
	private int pozione;
	private Mostro mostro;
	
	public Giocatore() {
		this.X = ThreadLocalRandom.current().nextInt(1, 101);
		this.Y = ThreadLocalRandom.current().nextInt(1, 101);
		this.mostro = new Mostro();
		this.salute = this.X;
		this.pozione = this.Y;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public Mostro getMostro() {
		return mostro;
	}

	public int getSalute() {
		return salute;
	}

	public void setSalute(int salute) {
		this.salute = salute;
	}

	public int getPozione() {
		return pozione;
	}

	public void setPozione(int pozione) {
		this.pozione = pozione;
	}

	
}
