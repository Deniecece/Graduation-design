package com.supply.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String formatDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 获取到当天结束还有多少毫秒
     *
     * @return
     */
    public static Long getEndTime() {
        Calendar curDate = Calendar.getInstance();
        Calendar nextDayDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
        return (nextDayDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
    }

    /**
     * 获取当我00:00
     *
     * @return
     */
    public static int getRemainingSeconds() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (24 * 60 * 60) - (hour * 60 * 60);
    }

    //time 格式为 00:08:01
    public static int HHmmsstoTime(String time) {
        String[] split = time.split(":");
        int hh = Integer.parseInt(split[0]);
        int mm = Integer.parseInt(split[1]);
        int ss = Integer.parseInt(split[2]);
        return hh * 3600 + mm * 60 + ss;
    }

    /**
     * 毫秒转换为时间
     *
     * @param time
     */
    public static String second2Time(Integer time) {
        if (time == null || time == 0) {
            return "0分钟";
        }
        if (time < 60) {
            return "1分钟";
        }
        int hour = time / 3600;
        int minute = time % 3600 / 60;
        if (hour == 0 && minute != 0) {
            return String.format("%s%s", minute, "分钟");
        } else if (hour != 0 && minute == 0) {
            return String.format("%s%s", hour, "小时");
        } else {
            return String.format("%s%s%s%s", hour, "小时", minute, "分钟");
        }
    }

    /**
     * 将秒数转换为日时分秒，
     * 7783 格式为 02:09:43
     *
     * @param second
     * @return
     */
    public static String secondToTime(Long second) {
        if (second == null) {
            return null;
        }
        long hours = second / 3600;             //转换小时
        second = second % 3600;                 //剩余秒数
        long minutes = second / 60;             //转换分钟
        second = second % 60;                   //剩余秒数
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", second);
    }

    //时间戳转日期
    public static String getTimestamp2DateStr(String seconds, String format) {
        if (seconds == null || seconds.isEmpty()) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds) * 1000));
    }

    //时间戳转日期
    public static String getTimestamp2DateStr(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date getformatDate(String date, String format) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getformatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    //时间戳转日期
    public static Date getTimestamp2Date(String seconds) {
        if (seconds == null || seconds.isEmpty()) {
            return null;
        }
        return new Date(Long.valueOf(seconds) * 1000);
    }

    /***
     *
     * @param date
     * @param format : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }


    /***
     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    public static String getCron(String seconds) {
        Date date = getTimestamp2Date(seconds);
        return getCron(date);
    }


    public static Long getUnixTime(String date) {
        return getformatDate(date, null).getTime() / 1000;
    }

    public static Long getUnixTime(Date date) {
        return (date.getTime() / 1000);
    }


    /**
     * 设置日期时间-精确到秒00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 设置日期时间-精确到秒23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 昨天
     *
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    public static Date getAmountBefore(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }

    public static Date getMinuteBefore(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, amount);
        return calendar.getTime();
    }

    /**
     * 秒转毫秒
     *
     * @param second
     * @return
     */
    public static Integer second2Millisecond(String second) {
        if (second == null || second.length() == 0) {
            return null;
        }
        Double ms = Double.valueOf(second) * 1000;
        return ms.intValue();
    }

    /**
     * 当前时间是 12:25:12， 先获取5分钟前的时间即分钟数-5为 12:20:12
     *
     * @param amount 分钟
     * @return
     */
    public static String getCurrentAmountTime(Integer amount, String format) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -(amount == null ? 5 : amount));
        Date beforeD = beforeTime.getTime();
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format).format(beforeD);
    }

    /**
     * 秒转时间
     * @param seconds
     * @return
     */
    public static String getTimestamp2DateStr(String seconds) {
        if (seconds == null || seconds.isEmpty()) {
            return null;
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds) * 1000));
    }
}
