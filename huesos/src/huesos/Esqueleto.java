package huesos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.apache.commons.cli.*;

public class Esqueleto {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		String sPlantilla = null;
		boolean bVerbose = false;
		boolean bPortapapeles = false;
		String sDelimitador;
		
		
		// --> http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=commonsCLI

        CommandLineParser parser  = null;  
        CommandLine       cmdLine = null;   

		Options options = new Options();  
        options.addOption("h", true,  "Hueso principal del esqueleto");
        options.addOption("p", "portapapeles", false, "Lee los tokens del portapapeles ");
        options.addOption("d", true,  "Delimitador del portapapeles (Por defecto el TAB \t");
        options.addOption("v", "verbose", false, "Para ver que hace");
        options.addOption("?", "help", false, "Ayuda ");  
        
        try {
        	
			parser  = new BasicParser();  
			cmdLine = parser.parse(options, args);  
			
			// Help
			if (cmdLine.hasOption("?")) {      
			    new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );  
			    return;  
			}
			
            // Hueso
			sPlantilla = cmdLine.getOptionValue("h");
		    if (sPlantilla == null) {  
		    	throw new org.apache.commons.cli.ParseException("Falta el hueso principal del esqueleto");  
	        }
		    
		    // Portapapeles
			if (cmdLine.hasOption("p")) {
				bPortapapeles = true;
			} else {
				bPortapapeles = false;
			}

		    // Delimitador Portapapeles          
            if (cmdLine.hasOption("d")){  
                sDelimitador = cmdLine.getOptionValue("d");    
            } else {  
            	sDelimitador = "\t";  
            } 

            // Verbose
			bVerbose = cmdLine.hasOption("verbose");
		    		    
        } catch (org.apache.commons.cli.ParseException ex){  
            System.out.println(ex.getMessage());  
            new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );    // Error, imprimimos la ayuda  
            return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

   
	    // Principal
		try {
			
			Hueso hueso = null;
			if (bPortapapeles) {

				// Carga los datos desde el portapapeles en un array
				String sPortapapeles = copiaDelPortapapeles();
				int i = sPortapapeles.lastIndexOf("\n");
				sPortapapeles = (new StringBuilder(sPortapapeles).replace(i,i+1,"\n")).toString();
				
			    String[] asDatos = sPortapapeles.split(sDelimitador);
				if (bVerbose) System.out.println("Portapapeles: "+sPortapapeles);

			    hueso = new Hueso(sPlantilla, asDatos, bPortapapeles);
				
			} else {
				hueso = new Hueso(args[1]);
			}
			
			if (bVerbose) System.out.println(hueso.getPlantilla());


		} catch (Exception e) {
			e.printStackTrace();
		}
        
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
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return sResult;
	}

}
