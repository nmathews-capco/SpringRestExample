package com.backbase.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    public String id;

    public String thisAccount;

    public String otherAccount;

    public String details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThisAccount() {
        return thisAccount;
    }

    public void setThisAccount(String thisAccount) {
        this.thisAccount = thisAccount;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
