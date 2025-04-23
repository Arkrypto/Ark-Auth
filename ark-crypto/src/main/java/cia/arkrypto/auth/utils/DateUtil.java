package cia.arkrypto.auth.utils;

import java.util.Date;

public class DateUtil {

    public static int getDayGap(Date start){
        System.out.println("start: " + start.getTime());
        System.out.println("end: " + new Date().getTime());
        long gap = new Date().getTime()-start.getTime();
        return (int)gap/24/60/60/1000;
    }
}
