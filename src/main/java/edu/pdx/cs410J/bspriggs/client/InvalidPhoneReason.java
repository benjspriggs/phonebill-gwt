package edu.pdx.cs410J.bspriggs.client;

import static edu.pdx.cs410J.bspriggs.client.PhoneCall.phoneNumberPattern;

public enum InvalidPhoneReason {
    DATE("Start date must be before end date"),
    CALLER("Caller must be a valid phone number that matches " + phoneNumberPattern),
    CALLEE("Callee must be a valid phone number that matches" + phoneNumberPattern),
    NONE("No errors");

    private String justification;

    InvalidPhoneReason(String justification) {
        this.justification = justification;
    }

    @Override
    public String toString(){
        return justification;
    }
}
