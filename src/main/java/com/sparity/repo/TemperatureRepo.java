package com.sparity.repo;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.sparity.entity.Temperature;

public interface TemperatureRepo extends Neo4jRepository<Temperature, Long> {

	Temperature findByCityName(String cityName);
}
