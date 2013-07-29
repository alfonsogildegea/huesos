package huesos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hueso
 * 
 * Un hueso es una parte de un esqueleto, de manera que un esqueleto es un conjunto
 * de 1 o más huesos. 
 * Quizás el término más apropiado es snippet. Yo prefiero llamarlo hueso.
 */

/**
 * @author Alfonso De Gea
 *
 */
public class Hueso {

	// Constantes
	private String NL = System.getProperty("line.separator");

	// Propiedades
	private String plantilla;
	private Hashtable<String,String> tokens;
	private ArrayList<String> tokensPlantilla;
	
	// Getters-Setters
	public String getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	public ArrayList<String> getTokensPlantilla() {
		return tokensPlantilla;
	}
	public void setTokensPlantilla(ArrayList<String> tokensPlantilla) {
		this.tokensPlantilla = tokensPlantilla;
	}
	
	// Constructores
	/**
	 * Pide los tokens en la entrada estándar y devuelve la plantilla actualizada
	 * en el portapapeles
	 * 
	 * @param fichero Plantilla
	 * @throws Exception
	 */
	public Hueso(String fichero) throws Exception {
		super();
		setPlantilla(leeFichero(fichero));
		this.tokens = new Hashtable<String,String>();
		
	    // Añade los tokens de la plantilla a una lista
		addTokensPlantilla();
        
        // Recorre los tokens del esqueleto
        Scanner scanner = new Scanner(System.in);
        Iterator<String> it = getTokensPlantilla().iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           System.out.println(sToken);
           setPlantilla(getPlantilla().replaceAll(sToken, scanner.nextLine()));
        }
        scanner.close();
        
        //Copia al portapapeles
        copiaAlPortapapeles();
        
	}
	

	/**
	 * Los tokens vienen en un array bidimensional con el token y el dato. El constructor
	 * actualiza la plantilla y la devuelve en el portapapeles según el parámetro bPortapapeles
	 * 
	 * @param fichero
	 * @param aTokens
	 * @param bPortapapeles
	 * @throws Exception
	 */
	public Hueso(String fichero, Hashtable<String,String> aTokens, boolean bPortapapeles) throws Exception {
		super();
		setPlantilla(leeFichero(fichero));
		if (aTokens == null) {
			tokens = new Hashtable<String,String>();
		} else {
			tokens = aTokens;
		}
		
	    // Añade los tokens de la plantilla a una lista
		addTokensPlantilla();
		
        // Reemplaza los tokens del esqueleto
        Iterator<String> it = getTokensPlantilla().iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           if (tokens.containsKey(sToken)) {
        	   setPlantilla(getPlantilla().replaceAll(sToken, getToken(sToken, tokens.get(sToken))));
           }
        }
        
        //Copia al portapapeles
        if (bPortapapeles) copiaAlPortapapeles();
        
	}

	
	/**
	 * El texto de los tokens está en un array de String, de manera que el 
	 * dato asDatos[i] será el valor del i-ésimo token distinto de la plantilla.
	 * El constructor devuelve la plantilla actualizada en el portapapeles según
	 * el parámetro bPortapapeles.
	 * 
	 * @param fichero
	 * @param asDatos
	 * @param bPortapapeles
	 * @throws Exception
	 */
	public Hueso(String fichero, String[] asDatos, boolean bPortapapeles) throws Exception {
		super();
		setPlantilla(leeFichero(fichero));
		tokens = new Hashtable<String,String>();
		
	    // Añade los tokens de la plantilla a una lista
		addTokensPlantilla();

        // Reemplaza los tokens del esqueleto
		int i = 0;
        Iterator<String> it = getTokensPlantilla().iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           if (asDatos.length > i) {
        	   setPlantilla(getPlantilla().replaceAll(sToken, getToken(sToken,asDatos[i++])));
           }
        }
        
        //Copia al portapapeles
        if (bPortapapeles) copiaAlPortapapeles();
        
	}

	/**
	 * Devuelve el texto por el que se reemplaza el token. Si el token lleva una plantilla 
	 * se crea un hueso estándar con ésta y se devuelve su resultado.
	 * 
	 *  Ejemplos: <NOMBRE> <PLANTILLA ficheroPlantilla.txt> <PLANTILLA2 fic.txt \\|>
	 * 
	 * @param sToken
	 * @param sDato
	 * @return
	 * @throws Exception
	 */
	protected String getToken(String sToken, String sDato) throws Exception {
		
		String[] asToken = sToken.split(" ");
		
		if ((asToken.length == 1) || sDato.isEmpty()) {
			return sDato;	
		} else {
			
			String sPlantilla = asToken[1].replace(">", "");
			String sDelimitador = "\\|";

			if (sDato.charAt(0)=='"') sDato = sDato.substring(1,sDato.lastIndexOf('"')).trim();

			String sResultado = "";
			String[] asDatos = sDato.split("\n");
			for (String sLinea: asDatos){
				String[] aLineas = sLinea.split(sDelimitador);
				Hueso hueso = new Hueso(sPlantilla,aLineas,false);
				sResultado += hueso.getPlantilla();
			}
			
			return sResultado;
		}
		
	}

	private void addTokensPlantilla() {
	    tokensPlantilla = new ArrayList<String>();
	    Pattern p = Pattern.compile("\\<[A-Z]+[a-zA-Z_0-9. ]*\\>");
        Matcher matcher = p.matcher(getPlantilla());
        while (matcher.find()) {
        	if (!tokensPlantilla.contains(matcher.group())) tokensPlantilla.add(matcher.group());
        }
	}

	private void copiaAlPortapapeles() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(plantilla);
		clipboard.setContents(strSel, null);
	}


	private String leeFichero(String sFichero) throws Exception {
		StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(new FileInputStream(sFichero));
		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}
		return text.toString();
	}	

}
