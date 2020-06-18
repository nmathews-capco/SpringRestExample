package com.backbase.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.backbase.spring.model.BBBank;
import com.backbase.spring.model.Transaction;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.web.client.RestTemplate;

/**
 * Handles requests for Transaction services.
 */
@Controller
public class TransactionController {


	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	public static final String BANK_SERVER_URL = "https://apisandbox.openbankproject.com/obp/v1.2.1";
	//Map to store transactions, ideally we should use database
	Map<String, Transaction> trnData = new HashMap<String, Transaction>();

	// Landing URL
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String Test() {
		logger.info("Start First Page");
		String s = "Welcome";
		return s;
	}

	// Endpoint to get all banks
	@RequestMapping(value="/banks", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getAllBanks() {
		logger.info("Start AllBanks");
		RestTemplate restTemplate = new RestTemplate();
		String str1 = restTemplate.getForObject(BANK_SERVER_URL, String.class);
		System.out.println(str1);
		return str1;

	}

	// Endpoint to get all trnsactions
	@RequestMapping(value="/banks/{bankId}/accounts/{accountId}/{viewId}/transactions", method=RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Transaction>  getallTransaction(@PathVariable("bankId") String bankId,
															  @PathVariable("accountId") String accountId,
															  @PathVariable("viewId") String viewId )throws ParseException  {
		logger.info("Start getallTransaction");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;

		String endStr = BANK_SERVER_URL+"/banks/"+bankId+"/accounts/"+accountId + "/"+ viewId + "/transactions";

		/* -- Get all Transactions */
		response = restTemplate.exchange(endStr, HttpMethod.GET, null, String.class);
		String returnJson = response.getBody();

		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		JSONObject jobj = (JSONObject)parser.parse(returnJson);
		JSONArray jsonarr = (JSONArray) jobj.get("transactions");

		/* Loop through all transactions  and process it */
		List<Transaction> trnList= new ArrayList<Transaction>();
		System.out.println("Jason Array List size: " + jsonarr.size() );
		for(int i=0;i<jsonarr.size();i++)
		{
			JSONObject jsonobj = (JSONObject)jsonarr.get(i);
			if(jsonobj.get("id") != null) {
				Transaction tran = new Transaction();
				ProcessTransaction processTran = new ProcessTransaction();
				BBBank bbfields = processTran.Process(jsonobj);
				tran.setId((String) jsonobj.get("id"));
				trnList.add(tran);
			}
		}
		return trnList;
	}

	//@RequestMapping(value = BANK_SERVER_URI, method = RequestMethod.GET)
	@RequestMapping(value="/banks/{bankId}/accounts/{accountId}/{viewId}/transactions/{tranId}/transaction", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BBBank getAllTransactionsByType(@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
														 @PathVariable("viewId") String viewId,
														 @PathVariable("tranId") String tranId) throws ParseException {

		logger.info("Start getAllTransactionsByType");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		BBBank backbaseBank = new BBBank();
		String endStr = BANK_SERVER_URL+"/banks/"+bankId+"/accounts/"+accountId + "/"+ viewId + "/transactions/" +tranId + "/transaction";

		response = restTemplate.exchange(endStr, HttpMethod.GET, null, String.class);
		String returnJson = response.getBody();

		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		JSONObject jsonobj = (JSONObject)parser.parse(returnJson);
		Transaction tran = new Transaction();
		tran.setId((String) jsonobj.get("id"));
		ProcessTransaction processTran = new ProcessTransaction();
		backbaseBank = processTran.Process(jsonobj);
		return backbaseBank;
	}

	@RequestMapping(value="/banks/{bankId}/accounts/{accountId}/{viewId}/transactions/{tranId}/account", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody double getAmountByTransactionType(@PathVariable("bankId") String bankId,
															@PathVariable("accountId") String accountId,
															@PathVariable("viewId") String viewId,
															@PathVariable("tranId") String tranId)
															throws ParseException  {
		logger.info("Start getAmountByTransactionType");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		BBBank backbaseBank = new BBBank();
		String endStr = BANK_SERVER_URL+"/banks/"+bankId+"/accounts/"+accountId + "/"+ viewId + "/transactions/" +tranId + "/account";

		System.out.println ("getAmountByTransactionType :"+ endStr);

		response = restTemplate.exchange(endStr, HttpMethod.GET, null, String.class);
		String returnJson = response.getBody();

		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		JSONObject jsonobj = (JSONObject)parser.parse(returnJson);
		Transaction tran = new Transaction();
		tran.setId((String) jsonobj.get("id"));
		ProcessTransaction processTran = new ProcessTransaction();
		backbaseBank = processTran.Process(jsonobj);
		Double totalAmt = backbaseBank.getInstructedAmount() + backbaseBank.getTransactionAmount();
		return totalAmt;
	}
	
}
