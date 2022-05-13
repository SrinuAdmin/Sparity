package com.sparity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparity.entity.Temperature;
import com.sparity.repo.TemperatureRepo;

@Service
public class TemperatureServiceImpl implements ITemperatureService {

	@Autowired
	private TemperatureRepo repo;

	@Override
	public Temperature insertTempData(Temperature temperature) {
		temperature = repo.save(temperature);
		return temperature;
	}

	@Override
	public Temperature updateTemperature(Temperature temperature) {

		try {
			repo.findById(temperature.getId())
					.orElseThrow(() -> new RuntimeException("Incorrect temperature id sent as input input"));

		} catch (Exception e) {
			throw e;
		}

		return repo.save(temperature);

	}

	@Override
	public List<Temperature> getAllStoredTemps() {
		return repo.findAll();
	}

	@Override
	public Long deleteTempById(Long id) {
		try {

			repo.findById(id).orElseThrow(() -> new RuntimeException("Incorrect temperature id sent as input input"));
		} catch (Exception e) {
			throw e;
		}

		repo.deleteById(id);
		return id;
	}

	@Override
	public void storedTemp(Temperature temperature) {
		Temperature existingTemperature = repo.findByCityName(temperature.getCityName());
		if (existingTemperature != null) {
			existingTemperature.setTemperature(temperature.getTemperature());
			repo.save(existingTemperature);
		} else {
			repo.save(temperature);
		}

	}

}
