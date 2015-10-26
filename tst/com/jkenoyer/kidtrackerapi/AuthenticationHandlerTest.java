package com.jkenoyer.kidtrackerapi;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class AuthenticationHandlerTest {

    private Context createContext() {
        TestContext ctx = new TestContext();
        return ctx;
    }

    @Test
    public void testAuthenticationHandler() {
    	
    	// assemble
    	Object input;
    	String testEmail = System.getenv("testEmail");
    	String testPassword = System.getenv("testPassword");
    	
    	JSONObject json = new JSONObject();
    	json.put("email", testEmail);
    	json.put("password", testPassword);
    	
        input = (Object) json;
    	
        AuthenticationHandler handler = new AuthenticationHandler();
        Context ctx = createContext();

        // act
        Object output = handler.handleRequest(input, ctx);
        
        // assert
        JSONObject result = new JSONObject(output.toString());
        
        assertEquals(testEmail, result.getString("email"));
        assertEquals("parent", result.getString("type"));
    }
}
