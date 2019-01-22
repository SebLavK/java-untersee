package main;

/**
*@author Sebas Lavigne
*/

public class Main {
	
	public static void main(String[] args) {
		int heading = 345;
		int myHeading = 70;

		if ((myHeading - heading + 360) % 360 > 180) {
			System.out.println("izquierda");
		} else {
			System.out.println("derecha");
		}
		System.out.println(myHeading - heading);
		System.out.println((360 + myHeading - heading) % 360);
	}
}
