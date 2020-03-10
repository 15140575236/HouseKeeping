/* Copyright IBM Corp. 2016 */
package com.ibm.keeping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * the class is a utility class to format date
 * 
 * @author will
 * 
 */
public class DateUtility
{
    private static DateFormat _format;
    private static DateFormat _format_simple;
    private static DateFormat _format_detail;
    private static DateFormat _format_time;

    static
    {
        _format = new SimpleDateFormat("yyyy-MM-dd");
        _format_simple = new SimpleDateFormat("yyyyMMdd");
        _format_time = new SimpleDateFormat("HH:mm:ss");
        _format_detail = new SimpleDateFormat("yyyyMMdd_HHmmss");
    }

    public static Date getTodayDate()
    {
        Calendar cal = getToday();
        return cal.getTime();
    }

    public static Calendar getToday()
    {
        return Calendar.getInstance();
    }
    
    public static boolean isBeforeToday(Date date)
    {
        return date.before(getTodayDate());
    }
    
    public static boolean isAfterToday(Date date)
    {
        return date.after(getTodayDate());
    }

    /**
     * get the date string of today based on SAP format
     * 
     * @return
     */
    public static String getTodayStringWithSapFormat()
    {
        return getDateStringWithSapFormat(getTodayDate());
    }

    public static String getDateStringWithSapFormat(Date date)
    {
        return getDateStringUsing(date, _format);
    }
    
    public static String getDateStringWithSimpleFormat(Date date)
    {
        return getDateStringUsing(date, _format_simple);
    }
    
    public static String getDateStringWithDetailFormat(Date date)
    {
        return getDateStringUsing(date, _format_detail);
    }
    
    public static String getDateStringWithTimeFormat(Date date)
    {
        return getDateStringUsing(date, _format_time);
    }
    
    public static String getTodayStringWithSimpleFormat()
    {
        return getDateStringWithSimpleFormat(getTodayDate());
    }

    private static String getDateStringUsing(Date date, DateFormat dateFormat)
    {
        if(date == null)
        {
            return null;
        }
        return dateFormat.format(date);
    }
}
