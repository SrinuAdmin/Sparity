package com.sparity.service;

import java.util.List;

import com.sparity.entity.Temperature;

public interface ITemperatureService {

	public List<Temperature> getAllStoredTemps();

	public Long deleteTempById(Long id);

	public Temperature insertTempData(Temperature temperature);

	public void storedTemp(Temperature temperature);
	
	public Temperature updateTemperature(Temperature temperature);
	
	

}
