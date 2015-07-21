package com.sanshisoft.wenjiang.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenleicpp on 2015/7/21.
 */
public class CommenUtils {
    public static String parseDate(String date){
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = df2.parse(df1.parse(date))
                    ;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
