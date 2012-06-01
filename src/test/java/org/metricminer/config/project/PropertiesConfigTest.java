package org.metricminer.config.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.metricminer.config.project.ConfigNotFoundException;
import org.metricminer.config.project.Configs;
import org.metricminer.config.project.PropertiesConfig;


public class PropertiesConfigTest {

	@Test
	public void shouldFindConfig() throws IOException {
		PropertiesConfig config = new PropertiesConfig(basedOnConfig());
		
		assertEquals("git", config.asString(Configs.SCM));
	}
	
	@Test(expected=ConfigNotFoundException.class)
	public void shouldWarnWhenConfigWasNotFound() throws IOException {
		PropertiesConfig config = new PropertiesConfig(basedOnConfig());
		
		config.asString("inexistent-config");
	}
	
	@Test
	public void shouldTellWhenConfigExists() throws IOException {
		PropertiesConfig config = new PropertiesConfig(basedOnConfig());
		
		assertTrue(config.contains("scm"));
		assertFalse(config.contains("invalid-config"));
	}

	private InputStream basedOnConfig() {
		String config = "scm=git\n";
		
		return new ByteArrayInputStream(config.getBytes());
	}
}
