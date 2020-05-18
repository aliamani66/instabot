package com.ali.insbot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class HumanSimulatorTime {

    private static Logger logger = Logger.getLogger(HumanSimulatorTime.class.getName());
    private static int startOfDayHour = 1;
    private static int endOfDayHour= 23;
    private static long minWaitTimeSecond=20;
    private static long maxWaitTimeSecond=30;

    public static void main(String[] args) {
        System.out.println(generateNextInterval());
    }
    public static boolean hourIsvalid(){
        try {

            Date nowDate = new Date();

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(nowDate);
            startCalendar.set(Calendar.HOUR_OF_DAY,startOfDayHour);
            logger.info("start of day = " + startCalendar.getTime().toString());

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(nowDate);
            endCalendar.set(Calendar.HOUR_OF_DAY, endOfDayHour);
            logger.info("end of day = " + endCalendar.getTime().toString());

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(nowDate);
            Date x = currentDate.getTime();
            logger.info("current = " + endCalendar.getTime());

            if (x.after(startCalendar.getTime()) && x.before(endCalendar.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long generateNextInterval(){
        if(hourIsvalid()){
            Random rand = new Random();
            return (minWaitTimeSecond* 1000 + rand.nextInt((int) (maxWaitTimeSecond* 1000 - minWaitTimeSecond* 1000))) ;
        }

        return 6*60*60*1000;
    }
}
