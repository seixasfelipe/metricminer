package org.metricminer.infra.csv;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SimpleOneColumnCSVReaderTest {

	private SimpleOneColumnCSVReader reader;

	@Before
	public void setUp() {
		reader = new SimpleOneColumnCSVReader();
	}
	
	@Test
	public void shouldIdentifyTheHeader() {
		StringReader is = new StringReader("cc;");
		
		List<Double> numbers = reader.readNumbers(is);
		
		assertEquals(0, numbers.size());
	}

	@Test
	public void shouldParseANumber() {
		StringReader is = new StringReader("cc;\n1.2;");
		
		List<Double> numbers = reader.readNumbers(is);
		
		assertEquals(1, numbers.size());
		assertEquals(1.2, numbers.get(0), 0.00001);
	}

	@Test
	public void shouldParseUnlimitedNumbers() {
		StringReader is = new StringReader("cc;\n1.2;\n2.3;\n3.4;\n");
		
		List<Double> numbers = reader.readNumbers(is);
		
		assertEquals(3, numbers.size());
		assertEquals(1.2, numbers.get(0), 0.00001);
		assertEquals(2.3, numbers.get(1), 0.00001);
		assertEquals(3.4, numbers.get(2), 0.00001);
	}
}
