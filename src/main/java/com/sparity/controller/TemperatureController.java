package com.sparity.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sparity.entity.Temperature;
import com.sparity.service.ITemperatureService;

@RestController
public class TemperatureController {

	// Static data
	static String apiEndPoint = "https://api.openweathermap.org/data/2.5/weather?q=";
	static String apiKey = "a96a81c6bf448b560a9c48a56170ae28";
	static String units = "metric";

	@Autowired
	private RestTemplate rt;

	@Autowired
	private ITemperatureService service;

	@GetMapping("/all")
	public ResponseEntity<?> getData() {
		List<Temperature> allStoredTemps = null;
		try {
			allStoredTemps = service.getAllStoredTemps();
			if (allStoredTemps.isEmpty()) {
				return ResponseEntity.ok("No temperature details available");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(allStoredTemps);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTemperature(@PathVariable Long id) {
		try {
			service.deleteTempById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("Temperature details with id " + id + " deleted. ");
	}

	@GetMapping("/temperature/{location}")
	public ResponseEntity<String> temperature(@PathVariable String location) {

		Float getTemperature = null;
		try {
			// preparing the url
			StringBuilder requestBuilder = new StringBuilder(apiEndPoint);
			requestBuilder.append(location);
			requestBuilder.append("&appid=").append(apiKey);
			requestBuilder.append("&units=").append(units);

			// get the response from weather api
			ResponseEntity<Map> e = rt.getForEntity(requestBuilder.toString(), Map.class);

			// parse the response and get temperature of city
			Gson gson = new Gson();
			String responseBody = gson.toJson(e.getBody());
			JsonObject convertedToJson = gson.fromJson(responseBody, JsonObject.class);
			JsonObject getMainData = convertedToJson.get("main").getAsJsonObject();
			getTemperature = getMainData.get("temp").getAsFloat();
			
			// create temperature obj
			Temperature temperature = new Temperature();
			temperature.setCityName(location);
			temperature.setTemperature(getTemperature);

			// store the searched temperature details
			service.storedTemp(temperature);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(location + " temperature is " + getTemperature);
	}

}
