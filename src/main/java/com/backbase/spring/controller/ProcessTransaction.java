package com.backbase.spring.controller;

import com.backbase.spring.model.BBBank;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;


public class ProcessTransaction {

    public BBBank Process(JSONObject jsonobj) throws ParseException {
        {

            System.out.println("Processing inside ProcessTransaction()  .... \n");
            BBBank backbaseBank = new BBBank ();
            
            // Processing each transaction json string

			System.out.println("\n id: " +jsonobj.get("id"));

                if (jsonobj.get("id") != null) {
                        try {
                            JSONObject thisAc = (JSONObject) jsonobj.get("this_account");
                            JSONObject otherAc = (JSONObject) jsonobj.get("other_account");
                            JSONObject details = (JSONObject) jsonobj.get("details");
                            JSONObject holder = (JSONObject) otherAc.get("holder");
                            JSONObject metadata = (JSONObject) holder.get("metadata");
                            JSONObject value = (JSONObject) details.get("value");
                            System.out.println("Setting bakcbase bank now");

                            System.out.println("details.value.currency : " + value.get("currency"));
                            System.out.println("details.value.amount : " + value.get("amount"));
                            System.out.println("details.value.currency : " + value.get("currency"));

                            backbaseBank.setId((String)jsonobj.get("id"));
                            backbaseBank.setAccountId((String)thisAc.get("id"));
                            backbaseBank.setCounterPartyAccount((String) otherAc.get("number"));
                            backbaseBank.setCounterPartyName((String) holder.get("name"));

                            backbaseBank.setCounterPartyLogoPath((String) metadata.get("image_URL"));
                            backbaseBank.setInstructedAmount(Double.parseDouble(value.get("amount").toString()));

                            backbaseBank.setInstructedCurrency((String) value.get("currency"));
                            backbaseBank.setTransactionAmount(Double.parseDouble(value.get("amount").toString()));
                            backbaseBank.setTransactionCurrency((String) value.get("currency"));

                        } catch (Exception e) {
                           // e.printStackTrace();
                        }
                    }
            return backbaseBank;
        }


    }
}

