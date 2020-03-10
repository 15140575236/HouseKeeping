package com.ibm.keeping;


/**
 * SAPR3.ZDM_LOGDTL
 * SAPR3.ZDM_LOGHDR
 * SAPR3.ZDM_PARKTABLE
 * 
 * SAPR3.ZDM_LOGDTL_FULL
 * SAPR3.ZDM_LOGHDR_FULL
 * SAPR3.ZDM_PARKTABLE_FULL
 * 
 *  "SAPR3"."KEYWORD_T"
 * quey SAPR3.KEYWORD_T
 * select categrory, kwvalue,name from SAPR3.KEYWORD_T
 * ParkingKeeping   360  		keepingdays 
 * 1. first time full backup
 *   when there is no keepingUpdate value in the SAPR3.KEYWORD_T talbe
 * 		 
 * 
 * 1.1 copy from park table(3) to park full table
 * 		when finished then 
 * 		insert the data into "SAPR3"."KEYWORD_T"
 * 		ParkingKeeping   2019-03-20 	keepingUpdate
 * 
 * 1.2 get the keepingdays value from the SAPR3.KEYWORD_T table
 *    Delete the ZDM_PARKTABLE record 
 *    where the now-ZDM_CREATE_DATE or now-ZDM_UPDATE_DATE>keepingdays value
 *    
 *    Delete the ZDM_LOGDTL ZDM_LOGHDR record 
 *    where the now-ZDM_CREATE_DATE or now-ZDM_UPDATE_DATE>keepingdays value
 * 
 * 
 * 2. daily backup data
 * 2.1 get the keepingUpdate from the SAPR3.KEYWORD_T table
 * 	  where the ZDM_CREATE_DATE or ZDM_UPDATE_DATE>=keepingUpdate value
 *    merge ZDM_PARKTABLE table to ZDM_PARKTABLE_full table
 *    
 *    where CREATE_TIME >=keepingUpdate value
 *    merge ZDM_LOGDTL ZDM_LOGHDR table to ZDM_LOGDTL_full ZDM_LOGHDR_full table
 *    
 *    when finished then update the keepingUpdate
 * 2.2 get the keepingdays value from the SAPR3.KEYWORD_T table
 *    
 *    where the now-ZDM_CREATE_DATE or now-ZDM_UPDATE_DATE>keepingdays value
 *    merge ZDM_PARKTABLE table to ZDM_PARKTABLE_full table
 *    
 *    when success then Delete the ZDM_PARKTABLE record 
 *    where the now-ZDM_CREATE_DATE or now-ZDM_UPDATE_DATE>keepingdays value
 *    
 *    where now- CREATE_TIME >keepingdays value			 *    
 *    merge ZDM_LOGDTL ZDM_LOGHDR table to  ZDM_LOGDTL_full ZDM_LOGHDR_full table
 *    
 *    when success then Delete the ZDM_LOGDTL ZDM_LOGHDR record 
 *    where the now-ZDM_CREATE_DATE or now-ZDM_UPDATE_DATE>keepingdays value
 *  			 * 
 * 
 * 
 */

public class ParkKeepingMain extends KeepingCommon{
	public static void main(String[] args) {
		ParkKeepingMain keep = new ParkKeepingMain();
		try {
			
			FirstStep firstStep = new FirstStep();
			firstStep.KeepingDataFullUpdate();
			
			SecondStep secondStep = new SecondStep();
			secondStep.dailyUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally{
			keep.closePrintWriters(infoMessagewriter);
			keep.closePrintWriters(errorMessagewriter);		
		}
	}
}
