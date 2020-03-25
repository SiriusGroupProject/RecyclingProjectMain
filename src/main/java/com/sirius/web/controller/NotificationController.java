package com.sirius.web.controller;

import com.sirius.web.service.AndroidPushNotificationsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class NotificationController {


    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @RequestMapping(value = "/send/{subAdminArea}/{activateOrAdd}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send(@PathVariable String subAdminArea,@PathVariable String activateOrAdd) throws JSONException {

        JSONObject notification = new JSONObject();
        if(activateOrAdd.equals("activate")){
            notification.put("title", "Yeni Aktif Otomat");
            notification.put("body", "Lokasyonunuza yakin bir otomat yeniden aktif hale geldi.");
        }
        else{
            notification.put("title", "Yeni Otomat Eklendi");
            notification.put("body", "Lokasyonunuza yakin yeni bir otomat eklendi.");
        }
        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + subAdminArea);
        body.put("priority", "high");

        JSONObject data = new JSONObject();
        data.put("Key-1", "JSA Data 1");
        data.put("Key-2", "JSA Data 2");

        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
