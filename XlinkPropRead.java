package org.saki.XlinkPropRead;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
public class ReadXlinkXonfig {
public static void main(String[] args) throws IOException {



	BufferedReader br1 = new BufferedReader(new FileReader("C://temp//xlinklist.txt"));
	    
	    String line1;
		while (( line1 = br1.readLine()) != null) {
			
			System.out.println("Checking server: " + line1.trim());

			String url = "smb://" + line1.trim() + "//f$//sap//xlink//XL0//Configuration/";
			
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("DOMAIN", "user", "password");
			
			SmbFile dir = new SmbFile(url, auth);
			
			try{
			for (SmbFile f : dir.listFiles())
			{
			    if ( f.isFile() && ( f.getName().contains("SEND") || f.getName().contains("SND") ) && 
			    ( !f.getName().contains("TEMPLATE") ) && (!f.getName().contains("HOUSEKEEPING")  )		
			    ){ 
				
			    	
			    InputStream is = f.getInputStream();

			    BufferedReader reader = null;
			    reader = new BufferedReader(new InputStreamReader(is));
			    String line = reader.readLine();
		        while(line != null){
		        	

				    	System.out.println(f.getName().replace(".properties", ""));	    
				    	System.out.println(line);

		            
		        	try{
		        	line = reader.readLine();
		        	} catch (Exception e){}
		        }           
			    }

			    
			}			//endfor
			} catch (Exception e){
			System.out.println("Error connecting server: " + line1.trim());
			}
			
			
	    }
	}	
	

}
