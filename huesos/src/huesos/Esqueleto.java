package huesos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Constructor;

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
		String sHuesoPersonalizado;
		
		
		// --> http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=commonsCLI

        CommandLineParser parser  = null;  
        CommandLine       cmdLine = null;   

		Options options = new Options();  
        options.addOption("p", true,  "Plantilla principal del esqueleto");
        options.addOption("e", "excel", false, "Lee los tokens del portapapeles de una excel");
        options.addOption("d", true,  "Delimitador del portapapeles (Por defecto el TAB \t");
        options.addOption("v", "verbose", false, "Para ver que hace");
        options.addOption("h", "hueso", true, "Hueso con tokens personalizados");
        options.addOption("?", "help", false, "Ayuda ");
        
        try {
        	
			parser  = new BasicParser();  
			cmdLine = parser.parse(options, args);  
			
			// Help
			if (cmdLine.hasOption("?")) {      
			    new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );  
			    return;  
			}
			
            // Plantilla
			sPlantilla = cmdLine.getOptionValue("p");
		    if (sPlantilla == null) {  
		    	throw new org.apache.commons.cli.ParseException("Falta la plantilla principal del esqueleto");  
	        }
		    
		    // Portapapeles
			if (cmdLine.hasOption("e")) {
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

            // Hueso Personalizado
			sHuesoPersonalizado = cmdLine.getOptionValue("hueso");

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

				// Carga los datos desde el portapapeles en el array asDatos
				String sPortapapeles = copiaDelPortapapeles();
				int i = sPortapapeles.lastIndexOf("\n");
				sPortapapeles = (new StringBuilder(sPortapapeles).replace(i,i+1,"\n")).toString();
			    String[] asDatos = sPortapapeles.split(sDelimitador);
				if (bVerbose) System.out.println("Portapapeles: "+sPortapapeles);

				// Lanza el hueso principal o el personalizado
			    if (sHuesoPersonalizado == null) {
			    	hueso = new Hueso(sPlantilla, asDatos, bPortapapeles);	
			    } else {
			        Constructor<?> c = Class.forName(sHuesoPersonalizado).getConstructor(String.class, String[].class, Boolean.TYPE);
			        hueso = (Hueso) c.newInstance(sPlantilla, asDatos, bPortapapeles);
			    }
			
			} else {
				
				// Lanza el hueso principal o el personalizado
			    if (sHuesoPersonalizado == null) {
			    	hueso = new Hueso(args[1]);
			    } else {
			        Constructor<?> c = Class.forName(sHuesoPersonalizado).getConstructor(String.class);
			        hueso = (Hueso) c.newInstance(args[1]);
			    }

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
