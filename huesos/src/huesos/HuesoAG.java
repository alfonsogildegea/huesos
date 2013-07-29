package huesos;

public class HuesoAG extends Hueso {

	public HuesoAG(String fichero, String[] asDatos, boolean bPortapapeles) throws Exception {
		super(fichero, asDatos, bPortapapeles);
	}
	
	protected String getToken(String sToken, String sDato) throws Exception {

		if (sToken.equals("<TAREA>")) {
			return "Esto es la tarea personalizada: "+sDato;
		}

		return super.getToken(sToken, sDato);
	}

}
