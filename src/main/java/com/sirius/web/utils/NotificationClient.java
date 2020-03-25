package com.sirius.web.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class NotificationClient {
    public static void notificate(String subAdminArea,String activateOrAdd) throws UnirestException {
        HttpResponse httpResponse = Unirest.get("http://192.168.2.242:8080/send/{subAdminArea}/{activateOrAdd}").asString();
    }
}
