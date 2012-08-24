package org.metricminer.stats.r;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class RNumberListGeneratorTest {

	@Test
	public void shouldGenerateForOnlyOneNumber() {
		RNumberListGenerator gen = new RNumberListGenerator();
		String c = gen.generate(Arrays.asList(3.14));
		
		assertEquals("c(3.14)", c);
	}
	
	@Test
	public void shouldGenerateForManyNumbers() {
		RNumberListGenerator gen = new RNumberListGenerator();
		String c = gen.generate(Arrays.asList(1.1, 2.2, 3.3, 4.4));
		
		assertEquals("c(1.1,2.2,3.3,4.4)", c);
	}
}
