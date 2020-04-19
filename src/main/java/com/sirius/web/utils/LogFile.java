package com.sirius.web.utils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.auth.AWSCredentials;

public class LogFile {
    private static String date ="";
    private static String time ="";
    private static String info ="";
    private static String body ="";
    private static String temp ="";
    private static String todayDate = "";
    private static S3Object file;

    public static ArrayList<ArrayList<String>> list() throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials("AKIA4OUYV5H35A73I6ML", "XIbFPynJ0N/NghcbqtDv6nE/rxvqhXDaeeWTxi/l");
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        S3Object s3object = null;
        try {
            s3object = s3Client.getObject(new GetObjectRequest("sirius-bucket", "2020-04-18.log"));
        } catch (AmazonServiceException ase) {
            System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonServiceException from GET requests, rejected reasons: " + ase);
        } catch (AmazonClientException ace) {
            System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonClientException: Error Message: " + ace.getMessage());
        }
        InputStream input = s3object.getObjectContent();
        //JSONArray jsonarray = new JSONArray(jsonStr);
        Date today = Calendar.getInstance().getTime();
        String dateFormat = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(dateFormat);
        todayDate = df.format(today);
        ArrayList<ArrayList<String>> list = new ArrayList<>(4);
        //FileInputStream fstream = new FileInputStream("D:\\logs\\sirius_"+todayDate+".log");
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String strLine = "";
        int counter = 0;
        while ((strLine = br.readLine()) != null)   {
            try {
                System.out.println(strLine);
                strLine = strLine.substring(strLine.indexOf("]: ")+3);
                System.out.println(strLine);
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
        return list;
    }

}
