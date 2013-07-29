package huesos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hueso
 */

/**
 * @author Alfonso De Gea
 *
 */
public class Hueso {

	private String NL = System.getProperty("line.separator");

	private String plantilla;
	
	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	private Hashtable<String,String> tokens;
	private ArrayList<String> tokensPlantilla;
	
	
	public Hueso(String fichero) throws Exception {
		super();
		setPlantilla(leeFichero(fichero));
		this.tokens = new Hashtable<String,String>();
		
	    // Añade los tokens de la plantilla a una lista
		addTokensPlantilla();
        
        // Recorre los tokens del esqueleto
        Scanner scanner = new Scanner(System.in);
        Iterator<String> it = tokensPlantilla.iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           System.out.println(sToken);
           setPlantilla(getPlantilla().replaceAll(sToken, scanner.nextLine()));
        }
        scanner.close();
        
        //Copia al portapapeles
        copiaAlPortapapeles();
        
	}
	
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
        Iterator<String> it = tokensPlantilla.iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           if (tokens.containsKey(sToken)) {
        	   setPlantilla(getPlantilla().replaceAll(sToken, getToken(sToken, tokens.get(sToken))));
           }
        }
        
        //Copia al portapapeles
        if (bPortapapeles) copiaAlPortapapeles();
        
	}

	public Hueso(String fichero, String[] asDatos, boolean bPortapapeles) throws Exception {
		super();
		setPlantilla(leeFichero(fichero));
		tokens = new Hashtable<String,String>();
		
	    // Añade los tokens de la plantilla a una lista
		addTokensPlantilla();
		
        // Reemplaza los tokens del esqueleto
		int j = 0;
        Iterator<String> ite = tokensPlantilla.iterator(); 
        while(ite.hasNext()) {        
           String sToken = ite.next();
           if (asDatos.length > j) {
               System.out.println(j+" "+sToken+" "+asDatos[j]);
        	   setPlantilla(getPlantilla().replaceAll(sToken, getToken(sToken,asDatos[j++])));
           }
        }		
		
        // Reemplaza los tokens del esqueleto
		int i = 0;
        Iterator<String> it = tokensPlantilla.iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           if (asDatos.length > i) {
        	   setPlantilla(getPlantilla().replaceAll(sToken, getToken(sToken,asDatos[i++])));
           }
        }
        
        //Copia al portapapeles
        if (bPortapapeles) copiaAlPortapapeles();
        
	}

	private String getToken(String sToken, String sDato) throws Exception {
		
		String[] asToken = sToken.split(" ");
		
		if (asToken.length == 1) {
			return sDato;	
		} else {
			
			String sPlantilla = asToken[1].replace(">", "");
			String sDelimitador = "\\|";
			if (asToken.length == 3) sDelimitador = asToken[2];

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

	public void copiaAlPortapapeles() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(plantilla);
		clipboard.setContents(strSel, null);
	}


	public static String copiaDelPortapapeles() {
		String sResult = "";
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) 
				       && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				sResult = (String)contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex){
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		
		return sResult;
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
