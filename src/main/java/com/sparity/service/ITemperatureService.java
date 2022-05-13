package com.sparity.service;

import java.util.List;

import com.sparity.entity.Temperature;

public interface ITemperatureService {

	public List<Temperature> getAllStoredTemps();

	public Long deleteTempById(Long id);

	public void storedTemp(Temperature temperature);

}
