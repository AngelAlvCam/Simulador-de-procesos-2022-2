package practica;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ControlExcepciones {
	public static int IntInput() {
		try {
			Scanner sc = new Scanner(System.in);
			int input = sc.nextInt();
			return input;
		} catch (InputMismatchException e) {
			System.out.println("E: Seleccione con valores enteros");
			return IntInput();
		}
	}
	
	public static String nameInput() {
		boolean invalid = true;
		Scanner input = new Scanner(System.in);
		String nombre = null;
		while(invalid == true) {
			nombre = input.nextLine().replaceAll(" ", "");
			if (nombre.length() > 0) {
				// System.out.println(nombre.length());
				invalid = false;
			} else {
				System.out.println("E: Nombre invalido");
			}
		}
		return nombre;
	}
}