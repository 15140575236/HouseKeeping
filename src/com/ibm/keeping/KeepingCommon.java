package com.ibm.keeping;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class KeepingCommon {
	
	static PrintWriter errorMessagewriter = null;
	static PrintWriter infoMessagewriter = null;
	{
		if(infoMessagewriter==null)
			infoMessagewriter = setupPrintWriters(infoMessagewriter,"info_" ,"_" +DateUtility.getTodayStringWithSimpleFormat());
		if(errorMessagewriter==null) 
			errorMessagewriter = setupPrintWriters(errorMessagewriter,"error_" ,"_" +DateUtility.getTodayStringWithSimpleFormat());
		
		
	}
	
	public void closePrintWriters(PrintWriter pwriter) {
        if (pwriter != null){
        	pwriter.flush();
        	pwriter.close();
        	pwriter = null;
        }
    }
	
	public PrintWriter setupPrintWriters(PrintWriter pwriter,String name,String sDate){
		String path = ConfigUtils.getProperty("rdh.service.logs.path");
		System.out.println("path=" + path);
        String dbgfn = path+ "/df_"+ name + sDate + ".log";
        try {
        	pwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dbgfn, false), "UTF-8"));
        } catch (Exception x) {
            System.out.println("error in the printWriters");
            x.getMessage();
        }
        return pwriter;
    }
	
	protected void addStringDebug(PrintWriter pwriter,String msg) {
    	
	    if (pwriter != null){
	    	pwriter.println(msg);
	    	pwriter.flush();
	    }
	 }
	
}
