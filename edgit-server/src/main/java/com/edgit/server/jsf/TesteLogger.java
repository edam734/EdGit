package com.edgit.server.jsf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TesteLogger {

	static final Logger LOGGER = LoggerFactory.getLogger(TesteLogger.class);
	
	public static void main(String[] args) {
		LOGGER.info("Example log from {}", TesteLogger.class.getSimpleName());
		LOGGER.debug("Isto é uma mensagem de debugg!");
		LOGGER.error("Isto é uma mensagem de erro!");
	}
}
