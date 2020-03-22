package com.sirius.web.controller;

import com.sirius.web.model.BaseConnection;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("connections")
public class ConnectionServer {

    private final UserService userService;
    private final AutomatService automatService;
    private HashMap<String, BaseConnection> connections;

    @Autowired
    public ConnectionServer(UserService userService, AutomatService automatService) {
        this.userService = userService;
        this.automatService = automatService;
        connections = new HashMap<>();
    }

    @PostMapping("connection/{userId}/{automatId}")
    public boolean requestToConnectFromUser(@PathVariable String userId, @PathVariable String automatId) {

        boolean dbUser = userService.exists(userId);
        boolean dbAutomat = automatService.exists(automatId);

        if (dbUser && dbAutomat && !connections.containsKey(automatId) && automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = new BaseConnection(userId, false, "", false);
            connections.put(automatId, baseConnection);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {

        boolean dbAutomat = automatService.exists(automatId);

        if(dbAutomat && connections.containsKey(automatId) && automatService.findAutomatById(automatId).isActive()) {
            BaseConnection baseConnection = connections.get(automatId);
            baseConnection.setAutomatIsAcceptUser(true);
            return baseConnection.getConnectedUserId();
        }
        return "Connection failed";
    }

//    @CrossOrigin(origins = "http://localhost:5000")
//    @GetMapping("getScannedBarcode/{connectedUser}/{automatId}")
//    public String getScannedBarcode(@PathVariable("connectedUser") String connectedUser,
//                                    @PathVariable("automatId") String automatId) {
//        if (automatId.equals(this.automatId) && connectedUser.equals(this.connectedUser))
//            return scannedBarcode;
//        else
//            return "";
//    }
//
//    @CrossOrigin(origins = "http://localhost:5000")
//    @PostMapping("closeConnection/{userId}")
//    public String closeConnection(@PathVariable String userId) {
//        if (userId.equals(connectedUser)) {
//            connectedUser = "";
//            return "Closed";
//        } else
//            return "";
//
//    }
}
