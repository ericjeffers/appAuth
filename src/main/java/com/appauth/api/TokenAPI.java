package com.appauth.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appauth.util.JWTHelper;
import com.appauth.domain.Customer;
import com.appauth.domain.CustomerFactory;
import com.appauth.domain.Token;

@RestController
@RequestMapping("/token")
public class TokenAPI {

	public static Token userToken;
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> getToken(@RequestBody Customer customer) {
		
		String username = customer.getName();
		String password = customer.getPassword();
		
		if (username != null && username.length() > 0 
				&& password != null && password.length() > 0 
				&& checkPassword(username, password)) {
			
			Token token = createToken(username);
			ResponseEntity<?> response = ResponseEntity.ok(token);
			return response;			
		}
		
		// bad request
		return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();		
	}
	
	private boolean checkPassword(String username, String password) {
		
		// make call to customer service 
		Customer cust = getCustomerByNameFromCustomerAPI(username);
		
		// compare name and password
		if(cust != null && cust.getName().equals(username) && cust.getPassword().equals(password)) {
			return true;				
		}
		
		return false;
		
	}
	
	private Customer getCustomerByNameFromCustomerAPI(String username) {
		try {			
			URL url = new URL("http://localhost:8080/api/customers/byname/" + username);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			Token token = getAppUserToken();
			conn.setRequestProperty("authorization", "Bearer " + token.getToken());
			
			if (conn.getResponseCode() != 200) {
				return null;
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = "";
				String out = "";
				while ((out = br.readLine()) != null) {
					output += out;
				}
				conn.disconnect();
				return CustomerFactory.getCustomer(output);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;

		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		}

	} 
	
	public static Token getAppUserToken() {
		if(userToken == null || userToken.getToken() == null || userToken.getToken().length() == 0) {
			userToken = createToken("ApiClientApp");
		}
		return userToken;
	}
	
	private static Token createToken(String username) {
    	String scopes = "com.webage.data.apis";
    	// special case for application user
    	if( username.equalsIgnoreCase("ApiClientApp")) {
    		scopes = "com.webage.auth.apis";
    	}
    	String tokenString = JWTHelper.createToken(scopes);

    	return new Token(tokenString);
    }
	
}
