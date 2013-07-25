package huesos;

import java.io.OutputStream;

import org.apache.commons.cli.*;

public class Esqueleto {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		String sPlantilla = null;
		boolean bVerbose = false;
		
		// --> http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=commonsCLI

        OutputStream      output  = null;  
        CommandLineParser parser  = null;  
        CommandLine       cmdLine = null;   

		Options options = new Options();  
        options.addOption("h", true,  "Hueso principal del esqueleto");
        options.addOption("?", "help", false, "Ayuda ");  
        options.addOption("v", "verbose", false, "Para ver que hace");
        
        try {
        	
			parser  = new BasicParser();  
			cmdLine = parser.parse(options, args);  
			
			if (cmdLine.hasOption("?")){    // No hace falta preguntar por el parámetro "help". Ambos son sinónimos  
			    new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );  
			    return;  
			}
			
            // Hueso
			sPlantilla = cmdLine.getOptionValue("h");
		    if (sPlantilla == null) {  
		    	throw new org.apache.commons.cli.ParseException("Falta el hueso principal del esqueleto");  
	        } 

            // Verbose
			bVerbose = cmdLine.hasOption("verbose");
		    		    
		     
	        Hueso hueso = new Hueso(args[1]);
	        
	        if (bVerbose) {
	        	System.out.println(hueso.getPlantilla());
	        }
	        
		    
        } catch (org.apache.commons.cli.ParseException ex){  
            System.out.println(ex.getMessage());  
            new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );    // Error, imprimimos la ayuda  
        } catch (java.lang.NumberFormatException ex){  
            new HelpFormatter().printHelp(Esqueleto.class.getCanonicalName(), options );    // Error, imprimimos la ayuda  
		} catch (Exception e) {
			e.printStackTrace();
		}

   
		
	}

}
