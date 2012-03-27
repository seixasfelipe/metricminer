package br.com.caelum.revolution.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
public class PrefixedConfigTests {

	private Config cfg;

	@Before
	public void setUp() {
		cfg = mock(Config.class);
	}
	
	@Test
	public void shouldPutAPrefixInAnything() {
		when(cfg.asString("prefix.suffix")).thenReturn("found it");
		
		PrefixedConfig prefixedConfig = new PrefixedConfig(cfg, "prefix");
		
		assertEquals("found it", prefixedConfig.asString("suffix"));
	}
	
	@Test
	public void shouldPutAPrefixWhenLookingForAConfig() {
		when(cfg.contains("prefix.suffix")).thenReturn(true);
		
		PrefixedConfig prefixedConfig = new PrefixedConfig(cfg, "prefix");
		
		assertTrue(prefixedConfig.contains("suffix"));		
	}
}
