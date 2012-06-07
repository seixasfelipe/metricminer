package org.metricminer.tasks.metric.lines;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LinesOfCodeMetricTest {

	private LinesOfCodeMetric metric;
	@Before
	public void setUp() {
		this.metric = new LinesOfCodeMetric();
	}
	
	@Test
	public void shouldCountNumberOfLinesInAMethod() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void x() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"
								)));
		
		assertEquals(new Integer(4), metric.getMethodLines().get("x/0"));
	}
	
	@Test
	public void shouldCountNumberOfLinesInOverridedMethod() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void x() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"+
								"public void x(int a) {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"
								)));
		
		assertEquals(new Integer(4), metric.getMethodLines().get("x/0"));
		assertEquals(new Integer(4), metric.getMethodLines().get("x/1[int]"));
	}
	
	@Test
	public void shouldCountNumberOfLinesInAMethodRegardlessOfModifier() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void x1() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"+
								"private void x2() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"+
								"protected void x3() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"+
								"void x4() {\r\n"+
								"line1();\r\n"+
								"line2();\r\n"+
								"line3();\r\n"+
								"}"
								)));
		
		assertEquals(new Integer(4), metric.getMethodLines().get("x1/0"));
		assertEquals(new Integer(4), metric.getMethodLines().get("x2/0"));
		assertEquals(new Integer(4), metric.getMethodLines().get("x3/0"));
		assertEquals(new Integer(4), metric.getMethodLines().get("x4/0"));
	}
}
