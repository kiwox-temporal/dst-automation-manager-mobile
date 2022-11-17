package net.kiwox.manager.dst.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppiumTestErrorOutput implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppiumTestErrorOutput.class);
	
	private Process process;

	public AppiumTestErrorOutput(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		try (InputStream is = process.getErrorStream();
				Reader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(isr)) {
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			LOGGER.error("Error reading error stream", e);
		}
		if (sb.length() > 0) {
			String err = sb.toString().trim();
			LOGGER.error("Error in process: {}", err);
		}
	}

}
