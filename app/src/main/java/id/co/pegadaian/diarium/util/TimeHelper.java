package id.co.pegadaian.diarium.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aang on 28/05/2017.
 */

public class TimeHelper {

    public String getTimeFormat(String date){

        SimpleDateFormat spf=new SimpleDateFormat("yyyy-mm-dd");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd MMM yyyy");
        return spf.format(newDate);
    }

    public String getElapsedTime(String time){

        Date statusDate = null;
        try{
            statusDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(time);
            System.out.println(time);
            //date2 = new SimpleDateFormat("MMM d, yyyy hh:mm:ss").parse(DateFormat.getDateTimeInstance().format(new Date()));

            System.out.println(statusDate.toString());

            long different = System.currentTimeMillis() - statusDate.getTime();
            System.out.println(different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long monthsInMilli = daysInMilli * 30;

            long elapsedMonths = different / monthsInMilli;
            different = different % monthsInMilli;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;


            if(elapsedMonths!=0) {
                if (elapsedMonths == 1) {
                    return elapsedMonths + " month ago";
                } else {
                    return elapsedMonths + " months ago";
                }
            }else if(elapsedDays!=0){
                if(elapsedDays==1){
                    return elapsedDays+" day ago";
                }else{
                    return elapsedDays+" days ago";
                }
            }else if(elapsedHours!=0){
                if(elapsedHours==1){
                    return elapsedHours+" hour ago";
                }else{
                    return elapsedHours+" hours ago";
                }
            }else if(elapsedMinutes!=0){
                if(elapsedMinutes==1){
                    return elapsedMinutes+" minute ago";
                }else{
                    return elapsedMinutes+" minutes ago";
                }
            }else{
                if(elapsedSeconds==1){
                    return "just now";
                }else{
                    return "just now";
                }
            }
        }catch (Exception e){
            System.out.println(e);
            return "";
        }

    }

}
