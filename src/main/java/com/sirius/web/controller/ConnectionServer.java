package com.sirius.web.controller;

import com.sirius.web.model.Automat;
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

    @Autowired
    public ConnectionServer(UserService userService, AutomatService automatService, BottleService bottleService) {
        this.userService = userService;
        this.automatService = automatService;
        this.bottleService = bottleService;
    }

    @PostMapping("connection/{userId}/{automatId}")
    public boolean requestToConnect(@PathVariable String userId,
                                    @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(userId);
        boolean existsDbAutomat = automatService.exists(automatId);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if (existsDbUser && existsDbAutomat && dbAutomat.getBaseConnection() == null && dbAutomat.isActive()) {
            BaseConnection baseConnection =
                    new BaseConnection(userId,false, "", false);
            dbAutomat.setBaseConnection(baseConnection);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {

        boolean existDbAutomat = automatService.exists(automatId);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if(existDbAutomat && dbAutomat.getBaseConnection() != null && dbAutomat.isActive()) {
            dbAutomat.getBaseConnection().setAutomatIsAcceptUser(true);
            automatService.updateAutomat(dbAutomat);
            return dbAutomat.getBaseConnection().getConnectedUserId();
        }
        return "";
    }

    @GetMapping("waitingForConnection/{userId}/{automatId}")
    public boolean waitingForConnection(@PathVariable String userId,
                                        @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(userId);
        boolean existsDbAutomat = automatService.exists(automatId);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if(existsDbUser && existsDbAutomat && dbAutomat.getBaseConnection() != null && dbAutomat.isActive()) {
            if(!dbAutomat.getBaseConnection().getConnectedUserId().equals(userId)) {
                return false;
            }
            return dbAutomat.getBaseConnection().isAutomatIsAcceptUser();
        }
        return false;
    }


    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("forwardScannedBarcode/{connectedUserId}/{automatId}/{barcode}")
    public boolean forwardScannedBarcode(@PathVariable("connectedUserId") String connectedUserId,
                                    @PathVariable("automatId") String automatId,
                                    @PathVariable("barcode") String barcode) {

        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = bottleService.exists(barcode);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if(existsDbUser && existsDbAutomat && existsDbBottle && dbAutomat.getBaseConnection() != null) {
            if(!dbAutomat.getBaseConnection().getConnectedUserId().equals(connectedUserId) || !dbAutomat.getBaseConnection().isAutomatIsAcceptUser()) {
                return false;
            }
            dbAutomat.getBaseConnection().setScannedBarcode(barcode);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getScannedBarcode/{connectedUserId}/{automatId}")
    public String getScannedBarcode(@PathVariable String connectedUserId,
                                    @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if(existsDbUser && existsDbAutomat && dbAutomat.getBaseConnection() != null && dbAutomat.isActive()) {
            if(!dbAutomat.getBaseConnection().getConnectedUserId().equals(connectedUserId) || !dbAutomat.getBaseConnection().isAutomatIsAcceptUser()) {
                return "";
            }
            dbAutomat.getBaseConnection().setAutomatIsAcceptBarcode(true);
            automatService.updateAutomat(dbAutomat);
            return dbAutomat.getBaseConnection().getScannedBarcode();
        }
        return "";
    }

    @GetMapping("waitingForScannedBarcode/{userId}/{automatId}")
    public boolean waitingForScannedBarcode(@PathVariable String userId,
                                            @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(userId);
        boolean existsDbAutomat = automatService.exists(automatId);

        Automat dbAutomat = automatService.findAutomatById(automatId);

        if(existsDbUser && existsDbAutomat && dbAutomat.getBaseConnection() != null && dbAutomat.isActive()) {
            if(!dbAutomat.getBaseConnection().getConnectedUserId().equals(userId) || !dbAutomat.getBaseConnection().isAutomatIsAcceptUser()) {
                return false;
            }
            return dbAutomat.getBaseConnection().isAutomatIsAcceptBarcode();
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("closeConnection/{automatId}")
    public boolean closeConnection(@PathVariable String automatId) {
        Automat dbAutomat = automatService.findAutomatById(automatId);
        if (dbAutomat != null && dbAutomat.getBaseConnection() != null) {
            dbAutomat.setBaseConnection(null);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;
    }

}
