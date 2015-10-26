package com.jkenoyer.kidtrackerapi;

import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class AuthenticationHandler implements RequestHandler<Object, Object> {

	private  LambdaLogger logger;
	
	@Override
	public Object handleRequest(Object input, Context context) {
		
		logger = context.getLogger();
		
		try {

			JSONObject json;
			
			try {
				json = new JSONObject(input.toString());
			} catch(JSONException e) {
				return getErrorResult("Invalid data!").toString();
			}

			String email = json.getString("email");
			String password = json.getString("password");
			
			logger.log("handling authentication request for " + email);
			
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(Config.Get("AWSAccessKeyId"), Config.Get("AWSSecretKey"));
	
			AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);
			client.setRegion(Region.getRegion(Regions.US_WEST_2));

			//todo - replace with query
			ScanRequest scanRequest = new ScanRequest().withTableName("kidtracker_account");

			ScanResult result = client.scan(scanRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				String itemEmail = item.get("email").getS();
				String itemPassword = item.get("password").getS();
				
				if(itemEmail.equals(email) && itemPassword.equals(password)) {
					JSONObject returnJson = new JSONObject();
					returnJson.put("email", email);
					returnJson.put("type", item.get("type").getS());
					logger.log(email + " authenticated!");
					return returnJson.toString();
				}
			}

		} catch (Exception error) {
			throw new RuntimeException(error.getMessage());
		}

		try {
			return getErrorResult("User Not Found or Password Incorrect!").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private JSONObject getErrorResult(String error) throws JSONException {
		logger.log(error);
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("error", error);
		return returnJson;
	}
}
