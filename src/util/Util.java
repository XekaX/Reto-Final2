package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Util {
	public static String introduceCadena(String mensaje) {
		String cadena = null;
		Scanner sc = new Scanner(System.in);

		System.out.println(mensaje);
		try {
			cadena = sc.next();
		} catch (NoSuchElementException e) {
			System.out.println("Error al introducir la cadena. ");
		}
		return cadena;
	}

	public static int introduceNumero(String mensaje) {
		int numero = 0;
		String cadena = null;
		boolean correcto = true;
		do {
			cadena = introduceCadena(mensaje);
			try {
				numero = Integer.parseInt(cadena);
			} catch (NumberFormatException e) {
				System.out.println("Esto no es un numero entero. ");
				correcto = false;
			}
		} while (!correcto);
		return numero;
	}

	public static LocalDate introduceFecha(String mensaje) {
		DateTimeFormatter formate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fecha = null;
		String cadena = null;
		boolean correcto = true;

		do {
			correcto=true;
			cadena = introduceCadena(mensaje);
			try {
				fecha = LocalDate.parse(cadena, formate);
			} catch (DateTimeParseException e) {
				System.out.println("Fecha incorrecta. ");
				correcto = false;
			}
		} while (!correcto);

		return fecha;
	}

	public static float introducirNumeroConDecimales(String mensaje) {
		float numero = 0;
		String cadena = null;
		boolean correcto = true;

		do {
			cadena = introduceCadena(mensaje);
			try {
				numero = Float.parseFloat(cadena);
			} catch (NumberFormatException e) {
				System.out.println("Numero incorrecta. ");
				correcto = false;
			}
		} while (!correcto);
		
		return 0;
	}
	
	public static char introducirCharacter(String mensaje) {
		char letra = 0;
		String cadena=null;
		boolean correcto=true;
		do {
			cadena = introduceCadena(mensaje);
			try {
				letra = cadena.toUpperCase().charAt(0);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Letra incorrecta. ");
				correcto = false;
			}
		} while (!correcto);
		
		return letra;
	}
	
	public static boolean introducirBoolean(String mensaje) {
		String respu;
		do {
			respu = introduceCadena(mensaje).toLowerCase();
		} while (!respu.equals("0") && !respu.equals("1") && !respu.equals("si") && !respu.equals("no")
				&& !respu.equals("s") && !respu.equals("n") && !respu.equals("true") && !respu.equals("false"));
		if (respu.equals("1") || respu.equals("si") || respu.equals("s") || respu.equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static float leerFloat(String mensaje) {
		float fNumero = 0;
		boolean ok;
		do {
			try {
				ok = true;
				fNumero = Float.parseFloat(introduceCadena(mensaje));
			} catch (NumberFormatException e) {
				System.out.println("Error al introducir el n�mero");
				ok = false;
			}
		} while (!ok);
		return fNumero;
	}
	
	public static int leerInt(){
		int num=0;
		boolean error;
		do{
			error=false;
			try{
				num=Integer.parseInt(introducirCadena());
			}
			catch(NumberFormatException e){
				System.out.println("Error al introducir el número. Introduce un número sin decimales");
				error=true;
			}
		}while(error);
		return num;
	}
	
	public static String introducirCadena(){
		 String cadena="";
		 InputStreamReader entrada =new InputStreamReader(System.in);
		 BufferedReader teclado= new BufferedReader(entrada);
		try {
			cadena=teclado.readLine();
		} catch (IOException e) {
			System.out.println("Error en la entrada de datos");
		}
		 return cadena;
	}

}	