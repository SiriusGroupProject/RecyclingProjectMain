package com.sirius.web.controller;

import com.sirius.web.model.Automat;
import com.sirius.web.model.BaseConnection;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.BottleService;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public boolean requestToConnect(@PathVariable String userId, @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(userId);
        boolean existsDbAutomat = automatService.exists(automatId);
        if(!existsDbUser || !existsDbAutomat) {
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if (baseConn == null && dbAutomat.isActive()) {
            baseConn = new BaseConnection(userId,false, "", 2, 2);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {

        boolean existsDbAutomat = automatService.exists(automatId);
        if(!existsDbAutomat) {
            return "";
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive()) {
            baseConn.setAutomatIsAcceptUser(true);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            return baseConn.getConnectedUserId();
        }
        return "";
    }

    @GetMapping("waitingForConnection/{userId}/{automatId}")
    public boolean waitingForConnection(@PathVariable String userId, @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(userId);
        boolean existsDbAutomat = automatService.exists(automatId);
        if(!existsDbUser || !existsDbAutomat) {
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(userId)) {
            return baseConn.isAutomatIsAcceptUser();
        }
        return false;
    }

    @PostMapping("forwardScannedBarcode/{connectedUserId}/{automatId}/{barcode}")
    public boolean forwardScannedBarcode(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode) {

        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = bottleService.exists(barcode);
        if(!existsDbUser || !existsDbAutomat || !existsDbBottle) {
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser()) {
            baseConn.setScannedBarcode(barcode);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getScannedBarcode/{connectedUserId}/{automatId}")
    public String getScannedBarcode(@PathVariable String connectedUserId, @PathVariable String automatId) {

        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        if(!existsDbUser && !existsDbAutomat) {
            return "";
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser()) {
            return baseConn.getScannedBarcode();
        }
        return "";
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("bottleVerification/{connectedUserId}/{automatId}/{barcode}/{verified}")
    public boolean bottleVerification(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode, @PathVariable int verified) {
        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = bottleService.exists(barcode);
        if(!existsDbUser || !existsDbAutomat || !existsDbBottle) {
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("")) {
            baseConn.setVerified(verified);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        return false;

    }

    @GetMapping("getBottleVerification/{connectedUserId}/{automatId}/{barcode}")
    public int getBottleVerification(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode) {
        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = automatService.exists(barcode);
        if(!existsDbUser && !existsDbAutomat && !existsDbBottle) {
            return 2;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("")) {
            return baseConn.getVerified();
        }
        return 2;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("closeOrNewTransaction/{connectedUserId}/{automatId}/{barcode}/{verified}/{result}")
    public int closeOrNewTransaction(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode,@PathVariable("verified") String verified, @PathVariable int result) {
        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = bottleService.exists(barcode);
        if(!existsDbUser || !existsDbAutomat || !existsDbBottle) {
            return 2;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() != 2) {
            baseConn.setResult(result);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            return baseConn.getResult();
        }
        return 2;

    }

    @GetMapping("getResult/{connectedUserId}/{automatId}/{barcode}")
    public int getResult(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode) {
        boolean existsDbUser = userService.exists(connectedUserId);
        boolean existsDbAutomat = automatService.exists(automatId);
        boolean existsDbBottle = automatService.exists(barcode);
        if(!existsDbUser && !existsDbAutomat && !existsDbBottle) {
            return 2;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() != 2) {
            int tempResult = baseConn.getResult();
            BaseConnection newBS;

            switch (tempResult) {
                case 0 :
                    dbAutomat.setBaseConnection(null);
                    automatService.updateAutomat(dbAutomat);
                    break;

                case 1 :
                    newBS = new BaseConnection(baseConn.getConnectedUserId(), baseConn.isAutomatIsAcceptUser(),
                            "", 2, 2);
                    dbAutomat.setBaseConnection(newBS);
                    automatService.updateAutomat(dbAutomat);
                    break;

                case 3 :
                    newBS = new BaseConnection(baseConn.getConnectedUserId(), baseConn.isAutomatIsAcceptUser(),
                            baseConn.getScannedBarcode(), 2, 2);
                    dbAutomat.setBaseConnection(newBS);
                    automatService.updateAutomat(dbAutomat);
                    break;

                default :
                    break;
            }
            return tempResult;
        }
        return 2;
    }
}

