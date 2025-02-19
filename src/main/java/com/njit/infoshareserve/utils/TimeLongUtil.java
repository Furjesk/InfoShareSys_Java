package com.njit.infoshareserve.utils;

public class TimeLongUtil {

    public static String millisecondToTimeString(Long millisecond) {
        long ls = new Long(0);
        String length = "";

        ls = millisecond / 1000;
        Integer hour = (int) (ls / 3600);
        Integer minute = (int) (ls % 3600) / 60;
        Integer second = (int) (ls - hour * 3600 - minute * 60);
        String hr = hour.toString();
        String mi = minute.toString();
        String se = second.toString();
        if (hr.length() < 2) {
            hr = "0" + hr;
        }
        if (mi.length() < 2) {
            mi = "0" + mi;
        }
        if (se.length() < 2) {
            se = "0" + se;
        }
        length = hr + ":" + mi + ":" + se;
        return length;
    }

    public static String secondToTimeString(Long seconds) {
        String length = "";

        Integer hour = (int) (seconds / 3600);
        Integer minute = (int) (seconds % 3600) / 60;
        Integer second = (int) (seconds - hour * 3600 - minute * 60);
        String hr = hour.toString();
        String mi = minute.toString();
        String se = second.toString();
        if (hr.length() < 2) {
            hr = "0" + hr;
        }
        if (mi.length() < 2) {
            mi = "0" + mi;
        }
        if (se.length() < 2) {
            se = "0" + se;
        }
        length = hr + ":" + mi + ":" + se;
        return length;
    }
}
