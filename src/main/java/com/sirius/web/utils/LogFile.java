package com.sirius.web.utils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.internal.ListWithAutoConstructFlag;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.auth.AWSCredentials;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sirius.web.model.Automat;
import com.sirius.web.model.Location;

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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String todate = dateFormat.format(date1);

        ArrayList<String> logs = new ArrayList<>();

        for (int i = 0; i < 7 ; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            Date todate1 = cal.getTime();
            String fromdate = dateFormat.format(todate1);
            String filename = fromdate + ".log";
            System.out.println(filename);
            try {
                s3object = s3Client.getObject(new GetObjectRequest("sirius-bucket", filename));
                InputStream input = s3object.getObjectContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String strLine = "";
                while ((strLine = br.readLine()) != null) {
                    logs.add(strLine);
                }
            } catch (AmazonServiceException ase) {
                System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonServiceException from GET requests, rejected reasons: " + ase);
            } catch (AmazonClientException ace) {
                System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonClientException: Error Message: " + ace.getMessage());
            }
        }
        ArrayList<ArrayList<String>> list = new ArrayList<>(4);
        int counter = 0;
        for (int i = 0; i < logs.size(); i++){
            try {
                String strLine = logs.get(i);
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

    public static ArrayList<ArrayList<String>> listAutomats() throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials("AKIA4OUYV5H35A73I6ML", "XIbFPynJ0N/NghcbqtDv6nE/rxvqhXDaeeWTxi/l");
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        S3Object s3object = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String todate = dateFormat.format(date1);
        String logs = "";
        for (int i = 0; i < 7 ; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            Date todate1 = cal.getTime();
            String fromdate = dateFormat.format(todate1);
            String filename = fromdate + ".log";
            System.out.println(filename);
            try {
                s3object = s3Client.getObject(new GetObjectRequest("sirius-bucket", filename));
                InputStream input = s3object.getObjectContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String strLine = "";
                int counter = 0;
                while ((strLine = br.readLine()) != null) {
                    if (strLine.toLowerCase().contains("the bottle has been verified")) {
                        strLine = strLine.substring(strLine.indexOf("]: ") + 3);
                        logs += strLine;
                    }
                }
            } catch (AmazonServiceException ase) {
                System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonServiceException from GET requests, rejected reasons: " + ase);
            } catch (AmazonClientException ace) {
                System.out.println("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Caught an AmazonClientException: Error Message: " + ace.getMessage());
            }
        }
        ArrayList<ArrayList<String>> list = new ArrayList<>(3);
        ArrayList<ArrayList<String>> sortedList = new ArrayList<>(3);
        try {
            List<Automat> automats = AutomatClient.listAutomats();
            for (int i=0; i< automats.size(); i++){
                Automat automat = automats.get(i);
                Location location= automat.getLocation();
                String loc = location.getNeighborhood() + ", " + location.getDistrict() + ", " + location.getProvince();
                String id = automat.getId();
                String num = ""+ (int)((logs.length() - logs.replace(id,"").length()) / id.length());
                ArrayList<String> line = new ArrayList<>();
                line.add(id);
                line.add(loc);
                line.add(num);
                list.add(line);
            }
            int maxIndex = 0;
            int max = 0;
            for (int i = 0; i< list.size(); i++){
                if (Integer.parseInt(list.get(i).get(2)) > max){
                    max = Integer.parseInt(list.get(i).get(2));
                    maxIndex = i;
                }
                if (i == list.size()-1){
                    sortedList.add(list.get(maxIndex));
                    list.remove(maxIndex);
                    max = 0;
                    maxIndex = 0;
                    i = -1;
                }
            }
        }
        catch (Exception e){
        }
        return sortedList;
    }

}
