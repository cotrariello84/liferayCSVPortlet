package com.sorpresario.scheduler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;


public class DateUtil {
	
	
	static int getDiffDays(String dateStart, String dateStop){
		
		int diff = 0;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			Date d1 = null;
			Date d2 = null;
			
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			DateTime dt1 = new DateTime(d1);
			DateTime dt2 = new DateTime(d2);
			
			diff= Days.daysBetween(dt1, dt2).getDays();
		

	 } catch (Exception e) {
			e.printStackTrace();
	 }
		return diff;
	
	}
	
	
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); 
        return cal.getTime();
    }

    public static Date subDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    
    public static String convertDatetoString(Date indate)
    {
       String dateString = null;
       SimpleDateFormat sdfr = new SimpleDateFormat("YYYY-MM-dd");
           try{
    	dateString = sdfr.format( indate );
       }catch (Exception ex ){
    	System.out.println(ex);
       }
       return dateString;
    }



    
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//    String dateInString = "20150101"; 
//     
//    try{
//     
//    Date date = formatter.parse(dateInString);
//    System.out.println(date);

}
	

