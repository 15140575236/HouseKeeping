package com.ibm.keeping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version 1.0
 */

public class SqlHelper {
	private static Log logger = LogFactory.getLog(SqlHelper.class);

    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     */
    public static int runUpdateSql(String sqlString, Connection conn) {
        logger.info("\t" + sqlString);
        int updateRows = -1;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            updateRows = stmt.executeUpdate(sqlString);
            return updateRows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
        	try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            ConnectionFactory.closeConnection(stmt, conn);
        }
    }
    
    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     */
    public static int runUpdateSql(String sqlString, Connection conn, HashMap<String,String> map) {
        logger.info("\t" + sqlString);
        int updateRows = -1;
        PreparedStatement pstmt = null;
        
        try {
        	pstmt = conn.prepareStatement(sqlString);
        	pstmt.setString(1, map.get("1"));
            pstmt.setString(2, map.get("2"));
            updateRows = pstmt.executeUpdate();
            return updateRows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        finally {
        	try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            ConnectionFactory.closeConnection(pstmt, conn);
        }
    }



    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     * @throws SQLException
     */
    public static int runUpdateSqlException(String sqlString, Connection conn) throws
        SQLException {
    	logger.info("\t" + sqlString);
        int updateRows = -1;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            updateRows = stmt.executeUpdate(sqlString);
            return updateRows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
        	try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            ConnectionFactory.closeConnection(stmt, conn);
        }
    }



    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     */
    public static ResultSet runQuerySql(String sqlString, Connection conn) {
        //logger.info("\t" + sqlString);
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlString);
            return rs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
          //  ConnectionFactory.closeConnection(rs, stmt, conn);
        
    }
    
    



    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     */
    public static boolean runProcedure(String sqlString, Connection conn) {
        logger.info("\t" + sqlString);
        boolean isOk = false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            isOk = stmt.execute(sqlString);
            return isOk;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
        	try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            ConnectionFactory.closeConnection(stmt, conn);
        }
    }


    public static boolean isExist(String sqlString, Connection conn) {
    	logger.info("\t" + sqlString);
    	ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlString);
            if (rs.next()) {
            	return true;
            }else{
            	return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionFactory.closeConnection(rs, stmt, conn);
        }        
        return true;
    	
    }
    
    public static String getDbDate(Connection conn) {
        String sqlString = "";
        sqlString = "select VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD') as yyyymmdd FROM SYSIBM.SYSDUMMY1";

        String dateString = "";
        try {
            dateString = (String) getSingleRow(sqlString,conn).get("yyyymmdd");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

 

   /**
    * 
    * @param sqlString
    * @param conn
    * @return
    */
    public static Hashtable<String, String> getSingleRow(String sqlString, Connection conn) {
        logger.info("\t" + sqlString);
        Hashtable<String, String> hTable = new Hashtable<String, String>();
        int t = 0;
        int numberOfColumns = 0;
        String columnName = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlString);
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                numberOfColumns = rsmd.getColumnCount();
                for (int k = 1; k <= numberOfColumns; k++) {
                    t = rsmd.getColumnType(k);
                    columnName = rsmd.getColumnName(k).toLowerCase();
                    //columnName = rsmd.getColumnName(k);
                    if(t==12||t==1||t==3)
                    {
                        hTable.put(columnName,(rs.getString(k)==null)?"":rs.getString(k));
                    }
                    else if(t==93)
                    {
                        hTable.put(columnName,(rs.getDate(k)==null)?"":rs.getDate(k).toString());
                    }
                    else if(t==2||t==4||t==-5)
                    {
                        hTable.put(columnName,Long.toString(rs.getLong(k)));
                    }
                    else
                    {
                          hTable.put(columnName,rs.getString(k) );
                    }

                }
            }
            return hTable;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionFactory.closeConnection(rs, stmt, conn);
        }
        return hTable;
    }
    
    /**
     * 
     * @param sqlString
     * @param conn
     * @return
     */
     @SuppressWarnings("rawtypes")
	public static Hashtable getSingleRow(String sqlString,boolean isUpper, Connection conn) {
         logger.info("\t" + sqlString);
         Hashtable<String, String> hTable = new Hashtable<String, String>();
         int t = 0;
         int numberOfColumns = 0;
         String columnName = null;
         ResultSet rs = null;
         Statement stmt = null;
         try {
             stmt = conn.createStatement();
             rs = stmt.executeQuery(sqlString);
             if (rs.next()) {
                 ResultSetMetaData rsmd = rs.getMetaData();
                 numberOfColumns = rsmd.getColumnCount();
                 for (int k = 1; k <= numberOfColumns; k++) {
                     t = rsmd.getColumnType(k);
                     if(isUpper){
                    	 columnName = rsmd.getColumnName(k).toUpperCase();
                     }else{
                    	 columnName = rsmd.getColumnName(k).toLowerCase();
                     }
                     
                     //columnName = rsmd.getColumnName(k);
                     if(t==12||t==1||t==3)
                     {
                         hTable.put(columnName,(rs.getString(k)==null)?"":rs.getString(k));
                     }
                     else if(t==93)
                     {
                         hTable.put(columnName,(rs.getDate(k)==null)?"":rs.getDate(k).toString());
                     }
                     else if(t==2||t==4||t==-5)
                     {
                         hTable.put(columnName,Long.toString(rs.getLong(k)));
                     }
                     else
                     {
                           hTable.put(columnName,rs.getString(k) );
                     }

                 }
             }
             return hTable;
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
         finally {
             ConnectionFactory.closeConnection(rs, stmt, conn);
         }
         return hTable;
     }

   /**
    * 
    * @param sqlStatement
    * @param conn
    * @return
    */
    public static ArrayList<Hashtable<String, String>> getMultiRowInfo(String sqlStatement,Connection conn)
    {
        logger.info("\t" + sqlStatement);
        ArrayList<Hashtable<String, String>> recordArray = new ArrayList<Hashtable<String, String>>();
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int numberOfColumns = 0;
        int t=0;  
        String columnName="";
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            rs=stmt.executeQuery(sqlStatement);
            rsmd = rs.getMetaData();
            numberOfColumns = rsmd.getColumnCount();
            while(rs.next())
            {
                Hashtable<String, String> hTable=new Hashtable<String, String>();
                
                for (int k=1;k<=numberOfColumns;k++)
                {
                    t = rsmd.getColumnType(k);
                    columnName = rsmd.getColumnName(k).toLowerCase();
                    if(t==12||t==1||t==3)
                    {
                        hTable.put(columnName,(rs.getString(k)==null)?"":rs.getString(k));
                    }
                    else if(t==93)
                    {
                        hTable.put(columnName,(rs.getDate(k)==null)?"":rs.getDate(k).toString());
                    }
                    else if(t==2||t==4||t==-5)
                    {
                        hTable.put(columnName,Long.toString(rs.getLong(k)));
                    }
                    else
                    {
                          hTable.put(columnName,rs.getString(k) );
                    }
                }
                recordArray.add(hTable);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ConnectionFactory.closeConnection(rs, stmt, conn);
        }
        return recordArray;
    }
    
    
    
 

     public static void main(String[] args){
         String jdbc_url = "jdbc:db2://rdtst1e4.sby.ibm.com:50000/pprdshad";
         String jdbc_user = "wangyul";
         String jdbc_password = "";
         String jdbc_driver = "com.ibm.db2.jcc.DB2Driver";
         Connection conn = null;
         try {
             Class.forName(jdbc_driver);
             conn = DriverManager.getConnection(jdbc_url, jdbc_user,jdbc_password);
             logger.info("connection success");
             conn.commit();
             conn.close();
         }
         catch (ClassNotFoundException cne) {
             cne.printStackTrace();
         }
         catch (SQLException se) {
             se.printStackTrace();
         }
     }
}