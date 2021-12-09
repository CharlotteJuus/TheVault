// Created by carme
// Creation date 30/11/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bank extends Gebruiker{

    private final String BANKNAAM = "The Vault";
    public final static String BANKCODE = "TVLT";

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Bank.class);

    public Bank() {
        super();
        logger.info("New Bank");
    }

    public String getBANKCODE() {
        return BANKCODE;
    }
}
