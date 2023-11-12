package com.adosar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("database")
public class DbHealthIndicator implements HealthIndicator, HealthContributor {

	@Autowired
	private DataSource dataSource;

	@Override
	public Health health() {
		try (Connection connection = dataSource.getConnection()) {
			connection.createStatement().execute("select 1 from map");
		} catch (SQLException exception) {
			return Health.outOfService().withException(exception).build();
		}
		return Health.up().build();
	}
}
