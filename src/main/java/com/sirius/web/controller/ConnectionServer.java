package com.sirius.web.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("connection")
public class ConnectionServer {

    //When put mappings are implemented here won't be hardcoded anymore. There will be a data structure to hold them
    private String automatId = "automat1";
    private String connectedUser = "maymayan@gmail.com";
    private String scannedBarcode = "4444444";

    @GetMapping("getConnectedUser/{automatId}")
    public String getConnectedUser(@PathVariable String automatId) {
        if (automatId.equals(this.automatId))
            return connectedUser;
        else
            return "";
    }


    @GetMapping("getScannedBarcode/{connectedUser}/{automatId}")
    public String getScannedBarcode(@PathVariable("connectedUser") String connectedUser,
                                    @PathVariable("automatId") String automatId) {
        if (automatId.equals(this.automatId) && connectedUser.equals(this.connectedUser))
            return scannedBarcode;
        else
            return "";
    }

    @PostMapping("closeConnection/{userId}")
    public String closeConnection(@PathVariable String userId) {
        if (userId.equals(connectedUser)) {
            connectedUser = "";
            return "Closed";
        } else
            return "";

    }
}
