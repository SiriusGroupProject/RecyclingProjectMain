package com.sirius.web.utils;

import com.sirius.web.model.Automat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sirius.web.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class AutomatClient {

    public static List<Automat> listAutomats() throws UnirestException, ParseException {
        HttpResponse httpResponse = Unirest.get("https://siriusrecyclingproject.herokuapp.com/rest/automats/listAutomats").header("Content-Type", "application/json").asJson();
        String jsonStr = httpResponse.getBody().toString();
        JSONArray jsonarray = new JSONArray(jsonStr);
        List<Automat> automats = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            Automat automat = new Automat();
            automat.setId(jsonobject.getString("id"));
            automat.setCapacity((float) jsonobject.getDouble("capacity"));
            automat.setActive(jsonobject.getBoolean("active"));
            automat.setOverallVolume((float) jsonobject.getDouble("overallVolume"));
            automat.setNumberOfBottles(jsonobject.getInt("numberOfBottles"));
            try {
                JSONObject locOj = jsonobject.getJSONObject("location");
                Location location = new Location();
                location.setProvince(locOj.getString("province"));
                location.setDistrict(locOj.getString("district"));
                location.setNeighborhood(locOj.getString("neighborhood"));
                location.setLatitude((float)locOj.getDouble("latitude"));
                location.setLongitude((float)locOj.getDouble("longitude"));
                automat.setLocation(location);
            }
            catch (Exception e) {
                System.out.println(e);
            }
            automats.add(automat);
        }
        return automats;
    }
}
