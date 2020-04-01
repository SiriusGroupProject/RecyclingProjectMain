package com.sirius.web.model;

import lombok.Data;

@Data
public class BaseConnection {

    private String connectedUserId;
    private boolean automatIsAcceptUser;
    private String scannedBarcode;
    private int verified;
    private int result;
    private boolean androidIsAcceptResult;

    public BaseConnection(String connectedUserId, boolean automatIsAcceptUser, String scannedBarcode,
                          int verified, int result, boolean androidIsAcceptResult) {
        this.connectedUserId = connectedUserId;
        this.automatIsAcceptUser = automatIsAcceptUser;
        this.scannedBarcode = scannedBarcode;
        this.verified = verified; //<0,1,2> !!! 0:not verified 1:verified 2:not yet answered
        this.result = result; //<0,1,2,3> !!! 0:close connection 1:new transaction 2:no choice yet 3:continue processing with the same bottle
        this.androidIsAcceptResult = androidIsAcceptResult;
    }

}
