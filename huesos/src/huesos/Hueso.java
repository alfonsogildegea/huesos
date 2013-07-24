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
	private Hashtable<String,String> tokens;
	private ArrayList<String> tokensPlantilla;
	private String texto;
	
	
	public Hueso(String fichero) throws Exception {
		super();
		this.plantilla = leeFichero(fichero);
		this.tokens = new Hashtable<String,String>();
		
	    // Añade los tokens de la plantilla a una lista
	    tokensPlantilla = new ArrayList<String>();
	    Pattern p = Pattern.compile("\\<[A-Z]+[A-Z_0-9]*\\>");
        Matcher matcher = p.matcher(plantilla);
        while (matcher.find()) {
        	if (!tokensPlantilla.contains(matcher.group())) tokensPlantilla.add(matcher.group());
        }
        
        // Recorre los tokens del esqueleto
        Scanner scanner = new Scanner(System.in);
        Iterator<String> it = tokensPlantilla.iterator(); 
        while(it.hasNext()) {        
           String sToken = it.next();
           System.out.println(sToken);
           plantilla = plantilla.replaceAll(sToken, scanner.nextLine());
        }
        scanner.close();
        
        //Copia al portapapeles
        copiaAlPortapapeles();
        
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
