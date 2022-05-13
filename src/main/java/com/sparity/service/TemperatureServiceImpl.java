package com.sparity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparity.entity.Temperature;
import com.sparity.repo.TemperatureRepo;

@Service
public class TemperatureServiceImpl implements ITemperatureService {

	@Autowired
	private TemperatureRepo repo;

	@Override
	public List<Temperature> getAllStoredTemps() {
		return repo.findAll();
	}

	@Override
	public Long deleteTempById(Long id) {
		repo.deleteById(id);
		return id;
	}

	@Override
	public void storedTemp(Temperature temperature) {
		repo.save(temperature);
	}

}
