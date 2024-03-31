
/**
 * 
 * 		Alessia Anile
 * 		Matricola 619554
 * 		Assignment 8		
 * 		Reti e Laboratorio III - A.A. 2022/23
 * 
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class Utility {
	
	public static File createFile(String filename) {
		File file = null;
		try {
	         file = new File(filename);
	         if(!file.exists()) {
	        	 file.createNewFile();
	         }
	         else {
	        	 
		         file.delete();
		         if(file.exists()) {
		        	 System.err.println("Eliminazione non effettuata...");
		         }
		         file.createNewFile();
		         
	         }
	      } catch(Exception e) {
	         System.err.println("Eccezione: creazione output file.");
	      }
		return file;
	}
	
	public static FileChannel initSourceNIO(File input) {
		FileChannel src = null;

		try {
			src = FileChannel.open(input.toPath(), StandardOpenOption.READ);
		} catch (IOException e) {
			System.err.println("Eccezione: apertura FileChannel src. " + e);
		}

		return src;
	}

	public static FileChannel initDestNIO(File output) {
		FileChannel dest = null;

		try {
			dest = FileChannel.open(output.toPath(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.err.println("Eccezione: apertura FileChannel dest. " + e);

		}
		return dest;
	}

	public static void closeAllNIO(FileChannel src, FileChannel dest) {
		try {
			src.close();
		} catch (IOException e) {
			System.err.println("Eccezione: chiusura FileChannel src. " + e);
		}
		try {
			dest.close();
		} catch (IOException e) {
			System.err.println("Eccezione: chiusura FileChannel dest. " + e);
		}
	}

	public static void closeAllIO(InputStream src, OutputStream dest) {
		try {
			src.close();
		} catch (IOException e) {
			System.err.println("Eccezione: chiusura InputStream. " + e);
		}
		try {
			dest.close();
		} catch (IOException e) {
			System.err.println("Eccezione: chiusura OutputStream. " + e);
		}
	}
}
