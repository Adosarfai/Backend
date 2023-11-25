package com.adosar.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.javapoet.ClassName;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootTest
class BackendApplicationTests {

	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	
	@Test
	void contextLoads() {
		LOGGER.log(Level.ALL, "Context Loaded");
	}

}
