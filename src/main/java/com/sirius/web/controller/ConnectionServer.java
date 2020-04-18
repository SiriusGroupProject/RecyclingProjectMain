package com.sirius.web.controller;

import com.sirius.web.model.Automat;
import com.sirius.web.model.BaseConnection;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.BottleService;
import com.sirius.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sirius.web.utils.Utility;

import java.util.List;

@RestController
@RequestMapping("connections")
public class ConnectionServer {

    private static Logger logger = LoggerFactory.getLogger(ConnectionServer.class);
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

        boolean isDB = isExistsDb(userId, automatId);
        if (!isDB) {
            logger.error("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if (baseConn == null && dbAutomat.isActive()) {
            baseConn = new BaseConnection(userId,false, "", 2, 2, false);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            logger.info("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Connection request successful");
            return true;
        }
        logger.error("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Connection request failed");
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {

        boolean isDB = isExistsDb(automatId);
        if (!isDB) {
            logger.error("$" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return "";
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive()) {
            baseConn.setAutomatIsAcceptUser(true);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            logger.info("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***User was accepted by automat");
            return baseConn.getConnectedUserId();
        }
        logger.error("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***User was not accepted by automat");
        return "";
    }

    @GetMapping("waitingForConnection/{userId}/{automatId}")
    public boolean waitingForConnection(@PathVariable String userId, @PathVariable String automatId) {

        boolean isDB = isExistsDb(userId, automatId);
        if (!isDB) {
            logger.error("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(userId) && baseConn.isAutomatIsAcceptUser()) {
            logger.info("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has confirmed that the connection is successful");
            return true;
        }
        logger.error("#" + userId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has not confirmed that the connection was successful");
        return false;
    }

    @PostMapping("forwardScannedBarcode/{connectedUserId}/{automatId}/{barcode}")
    public boolean forwardScannedBarcode(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode) {

        boolean isDB = isExistsDb(connectedUserId, automatId, barcode);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser()) {
            baseConn.setScannedBarcode(barcode);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android forwarded the barcode");
            return true;
        }
        logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android could not forward the barcode");
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("getScannedBarcode/{connectedUserId}/{automatId}")
    public String getScannedBarcode(@PathVariable String connectedUserId, @PathVariable String automatId) {

        boolean isDB = isExistsDb(connectedUserId, automatId);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return "";
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("")) {
            logger.info("#" + connectedUserId + " $" + automatId + " %" + baseConn.getScannedBarcode() + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat received the barcode.");
            return baseConn.getScannedBarcode();
        }
        logger.error("#" + connectedUserId + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat has not yet received the barcode");
        return "";
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("bottleVerification/{connectedUserId}/{automatId}/{barcode}/{verified}")
    public boolean bottleVerification(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode, @PathVariable int verified) {
        boolean isDB = isExistsDb(connectedUserId, automatId, barcode);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("")) {
            baseConn.setVerified(verified);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            if(verified == 0) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The bottle has not been verified");
            }else if(verified == 1){
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The bottle has been verified");
            }
            return true;
        }
        logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The bottle verification result was not confirmed");
        return false;
    }

    @GetMapping("getBottleVerification/{connectedUserId}/{automatId}/{barcode}")
    public int getBottleVerification(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode) {
        boolean isDB = isExistsDb(connectedUserId, automatId, barcode);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return 2;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() != 2) {
            if(baseConn.getVerified() == 0) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android received the bottle verification result as \"failed\"");
            }else if(baseConn.getVerified() == 1) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android received the bottle verification result as \"successful\"");
            }
            return baseConn.getVerified();
        }
        logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has not yet received the verification result of the bottle");
        return 2;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("closeOrNewTransaction/{connectedUserId}/{automatId}/{barcode}/{verified}/{result}")
    public boolean closeOrNewTransaction(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode,@PathVariable("verified") int verified, @PathVariable int result) {
        boolean isDB = isExistsDb(connectedUserId, automatId, barcode);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() == verified && baseConn.getVerified() != 2) {
            baseConn.setResult(result);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            if(result == 0) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The next step : \"close connection\"");
            }else if(result == 1) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The next step : \"new transaction\"");
            }else if(result == 3) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The next step : \"continue processing with the same bottle\"");
            }
            return true;
        }
        logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The next step was not confirmed");
        return false;

    }

    @GetMapping("getResult/{connectedUserId}/{automatId}/{barcode}/{verified}")
    public int getResult(@PathVariable("connectedUserId") String connectedUserId, @PathVariable("automatId") String automatId, @PathVariable("barcode") String barcode, @PathVariable("verified") int verified) {
        boolean isDB = isExistsDb(connectedUserId, automatId, barcode);
        if (!isDB) {
            logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return 2;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.getConnectedUserId().equals(connectedUserId) && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() == verified && baseConn.getVerified() != 2 && baseConn.getResult() != 2) {
            baseConn.setAndroidIsAcceptResult(true);
            dbAutomat.setBaseConnection(baseConn);
            automatService.updateAutomat(dbAutomat);
            int result = baseConn.getResult();
            if(result == 0) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has accepted the result : \"close connection\"");
            }else if(result == 1) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has accepted the result : \"new transaction\"");
            }else if(result == 3) {
                logger.info("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has accepted the result : \"continue processing with the same bottle\"");
            }
            return result;
        }
        logger.error("#" + connectedUserId + " $" + automatId + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Android has not yet accepted the result");
        return 2;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @GetMapping("waitingForResult/{automatId}")
    public boolean waitingForResult(@PathVariable String automatId) {
        boolean isDB = isExistsDb(automatId);
        if (!isDB) {
            logger.error("$" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }

        Automat dbAutomat = automatService.findAutomatById(automatId);
        BaseConnection baseConn = dbAutomat.getBaseConnection();

        if(baseConn != null && dbAutomat.isActive() && baseConn.isAutomatIsAcceptUser() && !baseConn.getScannedBarcode().equals("") && baseConn.getVerified() != 2 && baseConn.getResult() != 2 && baseConn.isAndroidIsAcceptResult()) {
            BaseConnection newBS;

            switch (baseConn.getResult()) {
                case 0 :
                    logger.info("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Connection closed");
                    newBS = null;
                    break;

                case 1 :
                    logger.info("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***New transaction started");
                    newBS = new BaseConnection(baseConn.getConnectedUserId(), baseConn.isAutomatIsAcceptUser(),
                            "", 2, 2, false);
                    break;

                case 3 :
                    logger.info("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Processing with the same bottle continued");
                    newBS = new BaseConnection(baseConn.getConnectedUserId(), baseConn.isAutomatIsAcceptUser(),
                            baseConn.getScannedBarcode(), 2, 2, false);
                    break;

                default :
                    return false;
            }
            dbAutomat.setBaseConnection(newBS);
            automatService.updateAutomat(dbAutomat);
            return true;
        }
        logger.error("#" + baseConn.getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The acceptance message for \"result\" has not yet arrived on Automat");
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("directlyCloseConnection/{automatId}")
    public boolean directlyCloseConnection(@PathVariable String automatId) {
        Automat dbAutomat = automatService.findAutomatById(automatId);
        if (dbAutomat != null && dbAutomat.getBaseConnection() != null) {
            dbAutomat.setBaseConnection(null);
            automatService.updateAutomat(dbAutomat);
            logger.info("#" + dbAutomat.getBaseConnection().getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***An error occurred.Connection was closed directly");
            return true;
        }
        logger.error("#" + dbAutomat.getBaseConnection().getConnectedUserId() + " $" + automatId + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The connection could not be closed");
        return false;
    }

    @CrossOrigin(origins = "http://localhost:5000")
    @PostMapping("closeAllConnection")
    public boolean closeAllConnection() {
        List<Automat> automats = automatService.getAllAutomats();
        for (Automat automat : automats) {
            automat.setBaseConnection(null);
            automatService.updateAutomat(automat);
        }
        logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***All connections were closed directly.");
        return true;
    }

    private boolean isExistsDb(String... args) {
        switch (args.length) {
            case 1 :
                return automatService.exists(args[0]);

            case 2 :
                return userService.exists(args[0]) && automatService.exists(args[1]);

            case 3 :
                return userService.exists(args[0]) && automatService.exists(args[1]) && bottleService.exists(args[2]);

            default :
                return false;
        }
    }
}

