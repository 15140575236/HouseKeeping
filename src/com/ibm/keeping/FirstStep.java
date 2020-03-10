package com.ibm.keeping;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;

public class FirstStep extends KeepingCommon {
	/*
	 * full backup and copy from park table(3) to park full table 
	 */
	
	public void KeepingDataFullUpdate() throws Exception {
		
		//add the log 
		addStringDebug(infoMessagewriter,"start the update");		
		Connection connection = ConnectionFactory.getRDHConnection();
		String DTL = "MANDT,ZSESSION,CREATE_TIME,TEXT,TEXT_TYPE";
		
		String PARK = "MANDT,ZDMCLASS,ZDMLOGSYS,ZDMMSGTYP,ZDMOBJTYP\n"
					+ ",ZDMOBJKEY,ZDM_SESSION,ZDMRELNUM,ZDM_CHANGE_NUM,TABNAME\n"
					+ ",ZDM_STATUS,ZDM_DEPSEQ,ZDM_TEXT,ZDM_CHANGE_TYPE,ZDM_CDCHGNO\n"
					+ ",ZDM_REQ_PRIORITY,ZDM_BROADCAST,ZDM_SOURCE,ZDM_CREATE_DATE,ZDM_CREATE_TIME\n"
					+ ",ZDM_CREATE_USER,ZDM_UPDATE_DATE,ZDM_UPDATE_TIME,ZDM_UPDATE_USER,ZDM_IDOC_SESSION\n"
					+ ",ZDM_JOBNUM,ZDM_JOBNAME,ZDM_VARIANT,ZDM_SUPD_JOBNUM,ZDM_SOURCE_IDOC\n"
					+ ",ZDM_TARGET_IDOC,ZDM_SIDOC_STATUS,ZDM_TIDOC_STATUS\n";		
		
		String keepingdays = "select kwvalue from SAPR3.KEYWORD_T where category = 'ParkingKeeping' and name = 'keepingdays' with ur";
		Hashtable<String, String> tbkeepingdays = SqlHelper.getSingleRow(keepingdays, connection);
		System.out.println(tbkeepingdays);
		if(tbkeepingdays==null || tbkeepingdays.size()==0){			
			String insertkeepingdays  = "insert into SAPR3.KEYWORD_T (category,kwvalue,name,description,sequence,isobsolete) \n"
					                  + "values('ParkingKeeping','500','keepingdays','House keeping days for the parking data','0','F')";
			int insertkeeping = SqlHelper.runUpdateSql(insertkeepingdays, connection);
			if(insertkeeping>0){
				System.out.println("success insert into keepingdays");
				addStringDebug(infoMessagewriter,"success insert into keepingdays");
			}
		}
		try{
			insertFullTable(connection, DTL, PARK);			
		}catch (Exception e) {
			e.printStackTrace();
			addStringDebug(errorMessagewriter,e.getMessage());
			System.exit(-1);
		}
	}


	private void insertFullTable(Connection connection, String DTL, String PARK) {
		int orderInsertPARK = 0;
		int orderInsertDTL = 0;
		String now = SqlHelper.getDbDate(connection);		
		addStringDebug(infoMessagewriter,"current_date is " + now);
		
		String queryLog = "select kwvalue from SAPR3.KEYWORD_T where CATEGORY='ParkingKeeping' and name = 'keepingUpdate' with ur";
		boolean exist = SqlHelper.isExist(queryLog, connection);
		addStringDebug(infoMessagewriter,"keepingUpdate exist in the database is " + exist);
		if(exist == false){
			String DTL_FULL_Null = "select count(*) as count from SAPR3.zdm_logdtl_full with ur";
			String getNumDTL = SqlHelper.getSingleRow(DTL_FULL_Null, connection).get("count");
			if(!getNumDTL.equals("0")){
				System.out.println("getNum:" + getNumDTL);
			}else{
				long startTime = System.currentTimeMillis();				    
			    
				String insertDTL = "insert into sapr3.zdm_logdtl_full select "+DTL+" from \n"
		                         + "(select "+DTL+",rownumber() over(order by MANDT,ZSESSION,TEXT desc) AS rn from sapr3.zdm_logdtl) \n"
		                         + "as dtl WHERE dtl.rn> ? AND dtl.rn<= ? with ur";				
				String countSql = " SELECT count(*) as count FROM sapr3.zdm_logdtl as dtl";
			    orderInsertDTL = runInsertAll(connection, insertDTL, countSql);
				
				long endTime = System.currentTimeMillis();
				long time = endTime-startTime;
				System.out.println(time);
				addStringDebug(infoMessagewriter,"Insert zdm_logdtl_full table spend time is " + time);

			}
			if(orderInsertDTL>0){
			
				String PARK_FULL_Null = "select count(*) as count from SAPR3.zdm_parktable_full with ur";
				Hashtable<String, String> PARK_Count = SqlHelper.getSingleRow(PARK_FULL_Null, connection);
				String getNumPARK = PARK_Count.get("count");

				if(!getNumPARK.equals("0")){
					System.out.println("getNum:" + getNumPARK);
				}else{
					long startTime = System.currentTimeMillis();				    
				    
					String insertPARK = "insert into sapr3.zdm_parktable_full select "+PARK+" from \n"
		                             + "(select "+PARK+",rownumber() over() AS rn from sapr3.zdm_parktable) \n"
		                             + "as park WHERE park.rn> ? AND park.rn<= ? with ur \n";
					
					String countSql = " SELECT count(*) as count FROM sapr3.zdm_parktable as park";
					orderInsertPARK = runInsertAll(connection, insertPARK, countSql);
					
					
					long endTime = System.currentTimeMillis();
					long time = endTime-startTime;
					addStringDebug(infoMessagewriter,"Insert zdm_parktable_full table spend time is " + time);

				}
			}
			if(orderInsertPARK>0 || orderInsertDTL>0){
				
				//add the DESCRIPTION/SEQUENCE/SOBSOLETE   value 
				//		house keeping data last update time/0/F
				String insertdata = "insert into SAPR3.KEYWORD_T (category,kwvalue,name,description,sequence,isobsolete) \n"
						          + " values('ParkingKeeping','"+now+"','keepingUpdate','house keeping data last update time','0','F') with ur";
				int data = SqlHelper.runUpdateSql(insertdata, connection);
				if(data>0){
					System.out.println("success to insert into:" +now);
					addStringDebug(infoMessagewriter,"success to insert into keepingUpdate " +now +" to KEYWORD_T table" );
				}
			}					

		}
	}
	
	public int runInsertAll(Connection rdhConnection, String sql, String countSql) {
		
		String scount = SqlHelper.getSingleRow(countSql, rdhConnection).get("count");

		int icount = Integer.parseInt(scount);
		int pagesize = 100000;
		int total = icount%pagesize==0? icount/pagesize: icount/pagesize+1;
		int sqldataList = 0 ;
		HashMap<String,String> map = new HashMap<String,String>();
		for(int i=0;i<total;i++){
			map.put("1", String.valueOf(i*pagesize));
			map.put("2", String.valueOf((i+1)*pagesize));			
			sqldataList = SqlHelper.runUpdateSql(sql,rdhConnection,map);
			if(sqldataList == -1){
				addStringDebug(errorMessagewriter, sql);
				break;
			}
		}
		return sqldataList;
	}


}
