package com.sirius.web.utils;

import com.sirius.web.model.Automat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class AutomatClient {

    public static List<Automat> listAutomats() throws UnirestException, ParseException {
        HttpResponse httpResponse = Unirest.get("http://localhost:8080/rest/automats/listAutomats").header("Content-Type", "application/json").asJson();
        String jsonStr = httpResponse.getBody().toString();
        JSONArray jsonarray = new JSONArray(jsonStr);
        List<Automat> automats = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            Automat automat = new Automat();
            automat.setId(jsonobject.getString("id"));
            automat.setCapacity((float)jsonobject.getDouble("capacity"));
            automat.setActive(jsonobject.getBoolean("active"));
            automat.setNumberOfBottles(jsonobject.getInt("numberOfBottles"));
            //location will be inserted
            automats.add(automat);
        }
        return automats;
    }
}
