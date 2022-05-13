package com.sparity.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	/**
	 * 
	 * It will take temperature as request body and stores in db via service class
	 * 
	 * @param temperature
	 * @return
	 */
	@PostMapping("/insert")
	public ResponseEntity<?> insertTemperature(@RequestBody Temperature temperature) {
		try {
			temperature = service.insertTempData(temperature);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Error while saving data");
		}
		return ResponseEntity.ok(temperature);
	}

	/**
	 * updates the temperature object
	 * @param temperature
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateTemperature(@RequestBody Temperature temperature) {
		try {
			temperature = service.updateTemperature(temperature);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Invalid input , Unable to update data");
		}
		return ResponseEntity.ok(temperature);
	}

	/**
	 * gathers all temps vailable in db
	 * @return
	 */
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
			return ResponseEntity.internalServerError().body("Error while fetching data");

		}
		return ResponseEntity.ok(allStoredTemps);
	}

	/**
	 * deletes one node based on id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTemperature(@PathVariable Long id) {
		try {
			service.deleteTempById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Invalid input , Unable to delete data");

		}
		return ResponseEntity.ok("Temperature details with id " + id + " deleted. ");
	}

	/**
	 * gets the temparature of the location
	 * @apiNote location should be geographical location
	 * @param location
	 * @return
	 */
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
			return ResponseEntity.internalServerError().body("Error while getting temperature");

		}

		return ResponseEntity.ok(location + " temperature is " + getTemperature);
	}

}
