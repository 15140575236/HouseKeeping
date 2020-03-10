package com.ibm.keeping;

import java.sql.Connection;
import java.util.Hashtable;


public class SecondStep extends KeepingCommon {
	
	public void dailyUpdate() throws Exception{
		
		addStringDebug(infoMessagewriter,"start the dailyUpdate");
		
		Connection conn = ConnectionFactory.getRDHConnection();
		
		String FPARK = "F.MANDT,F.ZDMCLASS,F.ZDMLOGSYS,F.ZDMMSGTYP,F.ZDMOBJTYP,\n"
					 + "F.ZDMOBJKEY,F.ZDM_SESSION,F.ZDMRELNUM,F.ZDM_CHANGE_NUM,\n"
					 + "F.TABNAME,F.ZDM_STATUS,F.ZDM_DEPSEQ,F.ZDM_TEXT,F.ZDM_CHANGE_TYPE,\n"
					 + "F.ZDM_CDCHGNO,F.ZDM_REQ_PRIORITY,\n"
					 + "F.ZDM_BROADCAST,F.ZDM_SOURCE,F.ZDM_CREATE_DATE,F.ZDM_CREATE_TIME,\n"
					 + "F.ZDM_CREATE_USER,F.ZDM_UPDATE_DATE,\n"
					 + "F.ZDM_UPDATE_TIME,F.ZDM_UPDATE_USER,F.ZDM_IDOC_SESSION,F.ZDM_JOBNUM,\n"
					 + "F.ZDM_JOBNAME,F.ZDM_VARIANT,\n"
					 + "F.ZDM_SUPD_JOBNUM,F.ZDM_SOURCE_IDOC,F.ZDM_TARGET_IDOC,\n"
					 + "F.ZDM_SIDOC_STATUS,F.ZDM_TIDOC_STATUS\n";
		
		String SPARK = "S.MANDT,S.ZDMCLASS,S.ZDMLOGSYS,\n"
					+ "S.ZDMMSGTYP,S.ZDMOBJTYP,S.ZDMOBJKEY,\n"
					+ "S.ZDM_SESSION,S.ZDMRELNUM,S.ZDM_CHANGE_NUM,\n"
					+ "S.TABNAME,S.ZDM_STATUS,S.ZDM_DEPSEQ,\n"
					+ "S.ZDM_TEXT,S.ZDM_CHANGE_TYPE,S.ZDM_CDCHGNO,\n"
					+ "S.ZDM_REQ_PRIORITY,S.ZDM_BROADCAST,S.ZDM_SOURCE,\n"
					+ "S.ZDM_CREATE_DATE,S.ZDM_CREATE_TIME,\n"
					+ "S.ZDM_CREATE_USER,S.ZDM_UPDATE_DATE,\n"
					+ "S.ZDM_UPDATE_TIME,S.ZDM_UPDATE_USER,S.ZDM_IDOC_SESSION,\n"
					+ "S.ZDM_JOBNUM,S.ZDM_JOBNAME,S.ZDM_VARIANT,\n"
					+ "S.ZDM_SUPD_JOBNUM,S.ZDM_SOURCE_IDOC,S.ZDM_TARGET_IDOC,\n"
					+ "S.ZDM_SIDOC_STATUS,S.ZDM_TIDOC_STATUS\n";
		
		//TODO 
		//for the ZDM_PARKTABLE , need change the keepingUpdate value to yyyyMMdd 
		//to get the data with ZDM_CREATE_DATE and ZDM_UPDATE_DATE column
		
		try{
			
			mergeAndUpdate(conn, FPARK, SPARK);			
			mergeAndDeleteOldData(conn, FPARK, SPARK);			
		}catch(Exception e){			
			addStringDebug(errorMessagewriter,e.getMessage());
			System.exit(-1);
			
		}
	}

	public void mergeAndDeleteOldData(Connection conn, String FPARK,
			String SPARK) {
		String sqlkeepingdays = "select kwvalue from SAPR3.KEYWORD_T where name = 'keepingdays' and category ='ParkingKeeping' with ur";
		Hashtable<String, String> tbkeepingdays = SqlHelper.getSingleRow(sqlkeepingdays, conn);
		String s_keepingdays = tbkeepingdays.get("kwvalue");
		int keepingdays = Integer.parseInt(s_keepingdays);
		
		String values = "values current date-"+keepingdays+" days with ur";
		System.out.println(values);
		addStringDebug(infoMessagewriter,values);
		Hashtable<String, String> tbValues = SqlHelper.getSingleRow(values, conn);
		String temTime = tbValues.get("1");	
		
		//1. merge from sapr3.ZDM_LOGDTL to sapr3.ZDM_LOGDTL_FULL and delete the old data
		String sqlmergeDTLTwo = "MERGE INTO sapr3.ZDM_LOGDTL_FULL AS F USING(select * from sapr3.ZDM_LOGDTL where CREATE_TIME<'"+temTime+"' ) AS S \n"
							 + "ON S.MANDT = F.MANDT and S.ZSESSION = F.ZSESSION and S.TEXT = F.TEXT \n"
				   	         + "WHEN MATCHED THEN \n"
				             + "UPDATE SET \n"
				             + "(F.MANDT,F.ZSESSION,F.CREATE_TIME,F.TEXT,F.TEXT_TYPE) = (S.MANDT,S.ZSESSION,S.CREATE_TIME,S.TEXT,S.TEXT_TYPE) \n"
				             + "WHEN NOT MATCHED THEN \n"
				             + "INSERT \n"
				             + "(F.MANDT,F.ZSESSION,F.CREATE_TIME,F.TEXT,F.TEXT_TYPE) VALUES \n"
				             + "(S.MANDT,S.ZSESSION,S.CREATE_TIME,S.TEXT,S.TEXT_TYPE) \n";
		int MergeDTLTwo = SqlHelper.runUpdateSql(sqlmergeDTLTwo, conn);
		addStringDebug(infoMessagewriter,sqlmergeDTLTwo);
		System.out.println("Merge the old data for the ZDM_LOGDTL_FULL table, result=" + MergeDTLTwo);
		if(MergeDTLTwo>=0){
			System.out.println("success merge DTL");
			System.out.println("start to delete the old date for the ZDM_LOGDTL table");
			addStringDebug(infoMessagewriter,"start to delete the ZDM_LOGDTL");				
			String HDR_delete= "delete from sapr3.zdm_logdtl where CREATE_TIME<'"+temTime+"'";
			addStringDebug(infoMessagewriter,HDR_delete);
		    int deleteRecord = SqlHelper.runUpdateSql(HDR_delete, conn);
			if(deleteRecord>=0){
				if(deleteRecord==MergeDTLTwo){
					System.out.println("Delete successfully for the ZDM_LOGDTL table, " + deleteRecord + " records are deleted");
					addStringDebug(infoMessagewriter,"Delete successfully for the ZDM_LOGDTL table " + deleteRecord + " records are deleted");
				}else{
					System.out.println( "Delete is not correct , " + deleteRecord + " records are deleted \n" 
	                          + "but the Merged record is " + MergeDTLTwo);
					addStringDebug(infoMessagewriter, "Delete is not correct , " + deleteRecord + " records are deleted \n" 
	                          + "but the Merged record is " + MergeDTLTwo);
				}
			}
		}
		//2 merge from sapr3.ZDM_PARKTABLE to sapr3.ZDM_PARKTABLE_FULL and delete the old data	
		String format_temTime = temTime.replace("-","");
		System.out.println(format_temTime);
		String sqlmergePARKTwo = "MERGE INTO sapr3.ZDM_PARKTABLE_FULL AS F USING(select * from sapr3.ZDM_PARKTABLE where ZDM_CREATE_DATE<'"+format_temTime+"' and ZDM_UPDATE_DATE<'"+format_temTime+"' ) AS S \n"
								 + "ON S.MANDT = F.MANDT and S.ZDMCLASS = F.ZDMCLASS \n"
								 + "and S.ZDMLOGSYS = F.ZDMLOGSYS and S.ZDMMSGTYP = F.ZDMMSGTYP \n"
								 + "and S.ZDMOBJTYP = F.ZDMOBJTYP and S.ZDMOBJKEY = F.ZDMOBJKEY \n"
								 + "and S.ZDM_SESSION = F.ZDM_SESSION \n"
					   	         + "WHEN MATCHED THEN \n"
					             + "UPDATE SET \n"
					             + "("+FPARK+") = ("+SPARK+") \n"
					             + "WHEN NOT MATCHED THEN \n"
					             + "INSERT \n"
					             + "("+FPARK+") VALUES \n"
					             + "("+SPARK+")";
		int MergePARKTwo = SqlHelper.runUpdateSql(sqlmergePARKTwo, conn);
		addStringDebug(infoMessagewriter,sqlmergePARKTwo);
		System.out.println("Merge the old data for the ZDM_PARKTABLE_FULL table, result=" + MergePARKTwo);
		if(MergePARKTwo>=0){
			System.out.println("success merge PARK");
			System.out.println("start to delete the old date for the ZDM_PARTTABLE table");
			addStringDebug(infoMessagewriter,"start to delete the old date for the ZDM_PARTTABLE table");				
			String PARK_delete= "delete from sapr3.zdm_parktable \n"
			+ "where ZDM_CREATE_DATE<'"+format_temTime+"' and ZDM_UPDATE_DATE<'"+format_temTime+"'";//execute delete
			addStringDebug(infoMessagewriter,PARK_delete);
		    int deleteRecord = SqlHelper.runUpdateSql(PARK_delete, conn);
			if(deleteRecord>=0){
				if(deleteRecord==MergePARKTwo){
					System.out.println("Delete successfully for the ZDM_PARTTABLE table, " + deleteRecord + " records are deleted");
					addStringDebug(infoMessagewriter,"Delete successfully for the ZDM_PARTTABLE table, " + deleteRecord + " records are deleted");
				}else{
					System.out.println( "Delete is not correct , " + deleteRecord + " records are deleted \n" 
	                          + "but the PARK_full merged is " + MergePARKTwo);
					addStringDebug(infoMessagewriter,"Delete is not correct , " + deleteRecord + " records are deleted \n" 
	                          + "but the PARK_full merged is " + MergePARKTwo);
				}
			}
		}
		
		
		System.out.println("Run the Parkkeeping Application successfully!");
		addStringDebug(infoMessagewriter,"Run the Parkkeeping Application successfully!");
	}

	public void mergeAndUpdate(Connection conn, String FPARK, String SPARK) {
		
		String getKeepingUpdate = "select kwvalue,VARCHAR_FORMAT(kwvalue,'yyyymmdd') yyyymmdd from SAPR3.KEYWORD_T where name = 'keepingUpdate' and category ='ParkingKeeping' with ur";
		addStringDebug(infoMessagewriter,getKeepingUpdate);
		Hashtable<String, String> KeepingUpdate = SqlHelper.getSingleRow(getKeepingUpdate, conn);
		
		String getKeepingDays = KeepingUpdate.get("kwvalue");
		String format_datePark = KeepingUpdate.get("yyyymmdd");
		addStringDebug(infoMessagewriter,"getKeepingDays is " + getKeepingDays);
		addStringDebug(infoMessagewriter,"getKeepingDays format is " + format_datePark);
			
		//1 MERGE from sapr3.ZDM_LOGDTL to sapr3.ZDM_LOGDTL_FULL
		String sqlmergeDTL = "MERGE INTO sapr3.ZDM_LOGDTL_FULL AS F USING(select * from sapr3.ZDM_LOGDTL where CREATE_TIME >='"+getKeepingDays+"' ) AS S \n"
						   	+ "ON S.MANDT = F.MANDT and S.ZSESSION = F.ZSESSION and S.TEXT = F.TEXT \n"
			                + "WHEN MATCHED THEN \n"
						    + "UPDATE SET \n"
						    + "(F.MANDT,F.ZSESSION,F.CREATE_TIME,F.TEXT,F.TEXT_TYPE) = (S.MANDT,S.ZSESSION,S.CREATE_TIME,S.TEXT,S.TEXT_TYPE) \n"
						    + "WHEN NOT MATCHED THEN \n"
						    + "INSERT"
						    + "(F.MANDT,F.ZSESSION,F.CREATE_TIME,F.TEXT,F.TEXT_TYPE) VALUES \n"
						    + "(S.MANDT,S.ZSESSION,S.CREATE_TIME,S.TEXT,S.TEXT_TYPE) \n";
		addStringDebug(infoMessagewriter,sqlmergeDTL);
		int MergeDTL = SqlHelper.runUpdateSql(sqlmergeDTL, conn);
		
		int countDTL = Integer.parseInt(
				SqlHelper.getSingleRow("select count(*) as count from sapr3.ZDM_LOGDTL_FULL where CREATE_TIME >='"+getKeepingDays+"'", conn)
				.get("count"));

		if(MergeDTL==countDTL){	
			addStringDebug(infoMessagewriter,"update " + MergeDTL + " records successfully for the ZDM_LOGDTL_FULL table");
		}else{
			addStringDebug(infoMessagewriter,"update records is " + MergeDTL + " , but summary is " + countDTL);
		}
		
		//2 MERGE from sapr3.ZDM_PARKTABLE to sapr3.ZDM_PARKTABLE_FULL
		String sqlmergePARK  = "MERGE INTO sapr3.ZDM_PARKTABLE_FULL AS F USING(select * from sapr3.ZDM_PARKTABLE \n"
				             + "where ZDM_CREATE_DATE >='"+format_datePark+"' or ZDM_UPDATE_DATE >='"+format_datePark+"' ) AS S \n"
						   	 + "ON S.MANDT = F.MANDT and S.ZDMCLASS = F.ZDMCLASS \n"
						   	 + "and S.ZDMLOGSYS = F.ZDMLOGSYS and S.ZDMMSGTYP = F.ZDMMSGTYP \n"
						   	 + "and S.ZDMOBJTYP = F.ZDMOBJTYP and S.ZDMOBJKEY = F.ZDMOBJKEY \n"
						   	 + "and S.ZDM_SESSION = F.ZDM_SESSION \n"
						   	 + "WHEN MATCHED THEN \n"
						     + "UPDATE SET \n"
						     + "("+FPARK+") \n"
						     + "= ("+SPARK+") \n"
						     + "WHEN NOT MATCHED THEN \n"
						     + "INSERT \n"
						     + "("+FPARK+") VALUES \n"
						     + "("+SPARK+") \n";
		addStringDebug(infoMessagewriter,sqlmergePARK);
		int MergePARK = SqlHelper.runUpdateSql(sqlmergePARK, conn);
		int countPARK = Integer.parseInt(
				SqlHelper.getSingleRow("select count(*) as count from sapr3.ZDM_PARKTABLE_FULL "
									 + "where ZDM_CREATE_DATE >='"+format_datePark+"' or ZDM_UPDATE_DATE >='"+format_datePark+"'", conn)
				.get("count"));

		if(MergePARK==countPARK){	
			addStringDebug(infoMessagewriter,"update " + MergePARK + " records successfully for the ZDM_PARKTABLE_FULL table");
		}else{
			addStringDebug(infoMessagewriter,"update records is " + MergePARK + " , but summary is " + countPARK);
		}
		
			
		if(MergeDTL>=0 && MergePARK>=0){
			String now = SqlHelper.getDbDate(conn);
			System.out.println(now);
			String updateTime = "update sapr3.KEYWORD_T SET kwvalue ='" + now + "' WHERE name = 'keepingUpdate' and category ='ParkingKeeping' ";
			int sqlUpdateTime = SqlHelper.runUpdateSql(updateTime, conn);
			addStringDebug(infoMessagewriter,updateTime);
			if(sqlUpdateTime>0){
				System.out.println("success to update the updatedays");				
			}			
		}
	}

}
