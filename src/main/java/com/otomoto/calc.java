package com.otomoto;

import java.util.Scanner;

public class calc {

	public static void main(String[] args) {

		int a, b, wynik, reszta, dzialanie;

		Scanner dupa = new Scanner(System.in);

		do {
			System.out.println("Liczba a");
			a = dupa.nextInt();
			System.out.println("Liczba b");
			b = dupa.nextInt();

			System.out.println("Wybierz działanie");
			System.out.println("1.Dodawanie - wybierz 1");
			System.out.println("2.Odejmowanie - wybierz 2");
			System.out.println("3.Mnożenie - wybierz 3");
			System.out.println("4.Dzielenie - wybierz 4");
			System.out.println("0.Wyjście - wybierz 0");
			dzialanie = dupa.nextInt();

			switch (dzialanie) {
			case 1: {
				wynik = a + b;
				System.out.println("Wynmik = " + wynik);
				break;
			}
			case 2: {
				wynik = a - b;
				System.out.println("Wynmik = " + wynik);
				break;
			}
			case 3: {
				wynik = a * b;
				
				System.out.println("Wynik = " + wynik);
				break;
			}
			case 4: {
				if (b != 0) {
					wynik = a / b;
					reszta = a % b;
					System.out.println("Wynmik = " + wynik + " reszty = " + reszta);
					break;
				} else {
					System.out.println("Pamiętaj cholero nie dziel przez 0!;)");
				}
			}
			case 0: {
				System.exit(0);
			}
			default:
				System.out.println("Wartość nieprawidłowa! Podaj cyfre z zakresu od 0 do 4");				
			}
		} while (dzialanie != 0);
	}
}
