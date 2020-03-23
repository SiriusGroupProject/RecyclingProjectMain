package com.sirius.web.model;

import lombok.Data;

@Data
public class BaseConnection {

    private String connectedUserId;
    private boolean automatIsAcceptUser;
    private String scannedBarcode;
    private boolean automatIsAcceptBarcode;

    public BaseConnection(String connectedUserId, boolean automatIsAcceptUser, String scannedBarcode, boolean automatIsAcceptBarcode) {
        this.connectedUserId = connectedUserId;
        this.automatIsAcceptUser = automatIsAcceptUser;
        this.scannedBarcode = scannedBarcode;
        this.automatIsAcceptBarcode = automatIsAcceptBarcode;
    }

}
