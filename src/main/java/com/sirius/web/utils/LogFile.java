package com.sirius.web.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogFile {
    private static String date ="";
    private static String time ="";
    private static String info ="";
    private static String body ="";
    private static String temp ="";
    private static String todayDate = "";

    public static ArrayList<ArrayList<String>> list() throws IOException {

        Date today = Calendar.getInstance().getTime();
        String dateFormat = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(dateFormat);
        todayDate = df.format(today);
        ArrayList<ArrayList<String>> list = new ArrayList<>(4);
        FileInputStream fstream = new FileInputStream("D:\\logs\\sirius_"+todayDate+".log"); // change here
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        int counter = 0;
        while ((strLine = br.readLine()) != null)   {
            try {
                date = strLine.substring(0, strLine.indexOf(' '));
                temp = strLine.substring(strLine.indexOf(' ') + 1);
                time = temp.substring(0, temp.indexOf(' '));
                temp = temp.substring(temp.indexOf(' ') + 1).trim();
                info = temp.substring(0, temp.indexOf(' '));
                temp = temp.substring(temp.indexOf(' ') + 1);
                temp = temp.substring(temp.indexOf(' ') + 1);
                body = temp.substring(temp.indexOf(' ') + 1);
                DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss.SSS");
                Date saat = dateFormat1.parse(time);
                list.add(new ArrayList());
                list.get(counter).add(date);
                list.get(counter).add(time);
                list.get(counter).add(info);
                list.get(counter).add(body);
                counter++;
            }
            catch(Exception e){

            }
        }
        fstream.close();

        return list;
    }

}
