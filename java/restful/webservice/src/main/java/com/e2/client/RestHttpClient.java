package com.e2.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.e2.restful.util.JsonUtil;
import com.e2.restful.webservice.Car;

public class RestHttpClient {

	public static void main(String[] args) {
		Car car = new Car();
		car.setColor("red");

		String json = JsonUtil.toJson(car);
		
		post("http://localhost:8080/webservice/rest/car/addcar", json);
	}

	public static void post(String url, String json) {
		try {
			HttpClient client = new DefaultHttpClient();
			
			HttpPost post = new HttpPost(url);
			
			post.addHeader("Content-Type", "application/json");
			
			if(json != null) {
				StringEntity input = new StringEntity(json);
				post.setEntity(input);
			}
		
			HttpResponse response = client.execute(post);
		  
			int status = response.getStatusLine().getStatusCode();
			
			HttpEntity entity = response.getEntity();
			
			if(entity != null) {			
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = null;
				while ((line = rd.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
