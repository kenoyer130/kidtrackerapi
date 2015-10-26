package com.jkenoyer.kidtrackerapi;

import static org.junit.Assert.assertEquals;
import static us.monoid.web.Resty.content;

import org.junit.Before;
import org.junit.Test;

import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class AuthenticationHandlerInfraTest extends InfrastructureBase {

	private String url = "https://wy70q1kw5b.execute-api.us-west-2.amazonaws.com/prod/KidTrackerAuthenticationHandler";

	@Before
	public void Before() {
		baseBefore();
	}

	@Test
	public void AuthenticationHandler() throws Exception {

		// assemble
		Resty r = new Resty();
		
		String httpKey = System.getenv("HTTPKey");
		
		r.withHeader("x-api-key", httpKey);
		
		JSONObject json = new JSONObject();
		json.put("email", System.getenv("emailTest"));
		json.put("password", System.getenv("passwordTest"));
		
		// act
		JSONResource result = r.json(url, content(json));
		
		// assert
		assertEquals(System.getenv("emailTest"), result.get("email").toString());
	}
}
