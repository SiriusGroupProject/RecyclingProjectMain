package com.sirius.web.controller;

import com.sirius.web.model.BaseConnection;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.BottleService;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("connections")
public class ConnectionServer {

    private final UserService userService;
    private final AutomatService automatService;
    private final BottleService bottleService;
    private HashMap<String, BaseConnection> connections;

    @Autowired
    public ConnectionServer(UserService userService, AutomatService automatService, BottleService bottleService) {
        this.userService = userService;
        this.automatService = automatService;
        this.bottleService = bottleService;
        connections = new HashMap<>();
    }

    @PostMapping("connection/{userId}/{automatId}")
    public boolean requestToConnect(@PathVariable String userId,
                                    @PathVariable String automatId) {

        boolean dbUser = userService.exists(userId);
        boolean dbAutomat = automatService.exists(automatId);

        if (dbUser && dbAutomat && !connections.containsKey(automatId) &&
                automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection =
                    new BaseConnection(userId, false, "", false);
            connections.put(automatId, baseConnection);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {

        boolean dbAutomat = automatService.exists(automatId);

        if(dbAutomat && connections.containsKey(automatId) &&
                automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = connections.get(automatId);
            baseConnection.setAutomatIsAcceptUser(true);
            return baseConnection.getConnectedUserId();
        }
        return "";
    }

    @GetMapping("waitingForConnection/{userId}/{automatId}")
    public boolean waitingForConnection(@PathVariable String userId,
                                        @PathVariable String automatId) {

        boolean dbUser = userService.exists(userId);
        boolean dbAutomat = automatService.exists(automatId);

        if(dbUser && dbAutomat && connections.containsKey(automatId) &&
                automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = connections.get(automatId);
            if(!baseConnection.getConnectedUserId().equals(userId)) {
                return false;
            }
            return baseConnection.isAutomatIsAcceptUser();
        }
        return false;
    }


    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("forwardScannedBarcode/{connectedUserId}/{automatId}/{barcode}")
    public boolean forwardScannedBarcode(@PathVariable("connectedUserId") String connectedUserId,
                                    @PathVariable("automatId") String automatId,
                                    @PathVariable("barcode") String barcode) {

        boolean dbUser = userService.exists(connectedUserId);
        boolean dbAutomat = automatService.exists(automatId);
        boolean dbBottle = bottleService.exists(barcode);

        if(dbUser && dbAutomat && dbBottle && connections.containsKey(automatId)) {
            BaseConnection baseConnection = connections.get(automatId);
            if(!baseConnection.getConnectedUserId().equals(connectedUserId) || !baseConnection.isAutomatIsAcceptUser()) {
                return false;
            }
            baseConnection.setScannedBarcode(barcode);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getScannedBarcode/{connectedUserId}/{automatId}")
    public String getScannedBarcode(@PathVariable String connectedUserId,
                             @PathVariable String automatId) {

        boolean dbUser = userService.exists(connectedUserId);
        boolean dbAutomat = automatService.exists(automatId);

        if(dbUser && dbAutomat && connections.containsKey(automatId) &&
                automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = connections.get(automatId);
            if(!baseConnection.getConnectedUserId().equals(connectedUserId) || !baseConnection.isAutomatIsAcceptUser()) {
                return "";
            }
            baseConnection.setAutomatIsAcceptBarcode(true);
            return baseConnection.getScannedBarcode();
        }
        return "";
    }

    @GetMapping("waitingForScannedBarcode/{userId}/{automatId}")
    public boolean waitingForScannedBarcode(@PathVariable String userId,
                                            @PathVariable String automatId) {

        boolean dbUser = userService.exists(userId);
        boolean dbAutomat = automatService.exists(automatId);

        if(dbUser && dbAutomat && connections.containsKey(automatId) &&
                automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = connections.get(automatId);
            if(!baseConnection.getConnectedUserId().equals(userId) || !baseConnection.isAutomatIsAcceptUser()) {
                return false;
            }
            return baseConnection.isAutomatIsAcceptBarcode();
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("closeConnection/{automatId}")
    public boolean closeConnection(@PathVariable String automatId) {
        if (connections.containsKey(automatId)) {
            connections.remove(automatId);
            return true;
        }
        return false;
    }
    
}
