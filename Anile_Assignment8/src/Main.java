
/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 8		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */


import java.io.File;
import java.util.Scanner;

/*
 * Da confrontare:
 * 1. FileChannel con buffer indiretti
 * 2. FileChannel con buffer diretti
 * 3. FileChannel utilizzando l'operazione transferTo()
 * 4. Buffered Stream di I/O
 * 5. Stream letto in un byte-array gestito dal programmatore
 * 
 * 		file_1k
 * 1. 10 millisecondi
 * 2. 1 millisecondi
 * 3. 1 millisecondi
 * 4. 1 millisecondi
 * 5. 0 millisecondi
 * 
 * 		file_100k
 * 1. 10 millisecondi
 * 2. 1 millisecondi
 * 3. 2 millisecondi
 * 4. 8 millisecondi
 * 5. 1 millisecondi
 * 
 * 		file_20M
 * 1. 30 millisecondi
 * 2. 15 millisecondi
 * 3. 16 millisecondi
 * 4. 82 millisecondi
 * 5. 80 millisecondi 
 * 
 * 		file_80M
 * 1. 85 millisecondi
 * 2. 63 millisecondi
 * 3. 48 millisecondi
 * 4. 307 millisecondi
 * 5. 331 millisecondi
 *
 */

public class Main {

	public static void main(String[] args) {

		// scanner per nome input file
		System.out.print("Nome del file > ");
		Scanner sc = new Scanner(System.in);
		String _input = sc.next();
		sc.close();

		// controlli sul file
		File input = new File(_input);
		if (!input.exists() || input.isDirectory()) {
			System.err.printf("Errore: %s non è un file valido!\n", _input);
			System.exit(1);
		}
		
		_input = input.getAbsolutePath();
		String _output = _input + "__copy";
				

		File output = Utility.createFile(_output+"1");		
		@SuppressWarnings("unused")
		Long datetime = System.currentTimeMillis();
		Copy.copy_FCIndirectBuffer(input, output);
		@SuppressWarnings("unused")
		Long now = System.currentTimeMillis();
		
		output = Utility.createFile(_output+"2");
		datetime = System.currentTimeMillis();
		Copy.copy_FCDirectBuffer(input, output);
		now = System.currentTimeMillis();
		
		output = Utility.createFile(_output+"3");
		datetime = System.currentTimeMillis();
		Copy.copy_transferTo(input, output);
		now = System.currentTimeMillis();
		
		output = Utility.createFile(_output+"4");
		datetime = System.currentTimeMillis();
		Copy.copy_bufferedStream(input, output);
		now = System.currentTimeMillis();
		
		output = Utility.createFile(_output+"5");
		datetime = System.currentTimeMillis();
		Copy.copy_byteArrayStream(input, output);
		now = System.currentTimeMillis();
		

		System.out.println("**FINE**");

	}

}
