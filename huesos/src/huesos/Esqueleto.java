package huesos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;

import org.apache.commons.cli.*;

public class Esqueleto {

	String sPlantilla = null;
	boolean bVerbose = false;
	boolean bPortapapeles = false;
	String sDelimitador;
	String sHuesoPersonalizado;

	String[] asDatos = null;
	
	/**
	 * Procesa los argumentos e instancia el hueso adecuado con la plantilla dada.
	 * Devuelve en el portapapeles la plantilla con los tokens reemplazados
	 * 
	 * @param args
	 * @param options
	 * @throws Exception
	 */
	public Esqueleto(String[] args, Options options) throws Exception {

		// Procesa los argumentos del programa
		procesaArgumentos(args, options);
		
	    // Hueso Principal
		Hueso hueso = null;
		
		// Si existe el argumento '-e', se asume que los datos de los tokens vienen en el portapapeles
		if (bPortapapeles) {

			// Carga los datos desde el portapapeles en el array asDatos
			String sPortapapeles = copiaDelPortapapeles();
			if (sPortapapeles.isEmpty()) throw (new ParseException("Portapapeles vacío"));
			int i = sPortapapeles.lastIndexOf("\n");
			if (i>0) sPortapapeles = (new StringBuilder(sPortapapeles).replace(i,i+1,"")).toString();
		    asDatos = sPortapapeles.split(sDelimitador);

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
			
		if (bVerbose) {

			System.out.println("Tokens: ");
	        Iterator<String> iTokens = hueso.getTokensPlantilla().iterator(); 
	        while(iTokens.hasNext()) {        
	           System.out.print(iTokens.next()+"\t");
	        }		

			System.out.println("\n\nTokens y Datos: ");
			int j = 0;
			iTokens = hueso.getTokensPlantilla().iterator(); 
	        while(iTokens.hasNext()) {        
	           String sToken = iTokens.next();
	           if (asDatos.length > j) {
	               System.out.println(j+" "+sToken+" "+asDatos[j++]);
	           }
	        }	

			System.out.println("\nPlantilla: ");
			System.out.println(hueso.getPlantilla());
		}



	}

	
	/**
	 * Procesa los argumentos recibidos como parámetro y actualiza las propiedades
	 * del esqueleto
	 * 
	 * @param args Argumentos de la línea de comandos
	 * @param options Opciones válidas
	 * @throws ParseException
	 */
	private void procesaArgumentos(String[] args, Options options) throws ParseException {
		
        CommandLineParser parser  = null;  
        CommandLine       cmdLine = null;   

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
		
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Options options = new Options(); 
	    options.addOption("p", true,  "Plantilla principal del esqueleto");
	    options.addOption("e", "excel", false, "Lee los tokens del portapapeles de una excel");
	    options.addOption("d", true,  "Delimitador del portapapeles (Por defecto el TAB \t");
	    options.addOption("v", "verbose", false, "Para ver que hace");
	    options.addOption("h", "hueso", true, "Hueso con tokens personalizados");
	    options.addOption("?", "help", false, "Ayuda ");	
		
		try {
			
			Esqueleto esqueleto = new Esqueleto(args, options);
			
        } catch (ParseException ex){  
            System.out.println(ex.getMessage());  
            new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );    // Error, imprimimos la ayuda  
            return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
				

        
	}
	
	private String copiaDelPortapapeles() {
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
