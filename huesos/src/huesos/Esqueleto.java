package huesos;

public class Prueba {

	public Prueba() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Huesos!!");
		
		try {
			
			Hueso hueso = new Hueso("HuesoDePrueba.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
