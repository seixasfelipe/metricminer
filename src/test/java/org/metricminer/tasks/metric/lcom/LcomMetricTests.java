package org.metricminer.tasks.metric.lcom;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.interfaceDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LcomMetricTests {

	private LComMetric metric;

	@Before
	public void setUp() {
		metric = new LComMetric();
	}
	@Test
	public void shouldCalculateLcomForASimpleClass() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { a = a + 1; }\r\n"
								)));
	
		assertEquals(0, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldCalculateLcomForAMethodThatConsumesOnlyOneAttribute() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"private int b;\r\n"+
								"public void x() { a = a + 1; }\r\n"
								)));
	
		assertEquals(0.5, metric.lcom(), 0.000001);
	}

	@Test
	public void shouldCalculateLcomForAClassThatDoesNotConsumeAttributes() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { }\r\n"
								)));
	
		assertEquals(1, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldCalculateLcomForAClassThatContainsMethodThatConsumesOnlyOneAttributeEach() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"private int b;\r\n"+
								"public void x() { a = a + 1; }\r\n"+
								"public void y() { b = b + 1; }\r\n"
								)));
	
		assertEquals(0.5, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldCalculateLcomForAClassInWhichTheAttributesAreSpread() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { a = a + 1; b.getBla(); }\r\n"+
								"private int b;\r\n"+
								"public void y() { System.out.println(c); }\r\n"+
								"private int c;\r\n"
								)));
	
		assertEquals(0.5, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldPutLessCohesiveValueForAClassWithoutAttributesOrMethods() {
		metric.calculate(toInputStream(
			"public class Total extends ElementWrapper {"+
			    "public Total(Element internal) {"+
			        "super(internal);"+
			    "}"+
			    "public Total(Factory factory) {"+
			        "super(factory, ThreadConstants.THRTOTAL);"+
			    "}"+
			    "public int getValue() {"+
			        "String val = getText();"+
			        "return (val != null) ? Integer.parseInt(val) : -1;"+
			    "}"+
			    "public void setValue(int value) {"+
			        "setText(String.valueOf(value));"+
			    "}"+
			"}"));
		
		assertEquals(1,metric.lcom(),0.00001);

	}

	@Test
	public void shouldIgnoreNonAttributes() {
		
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"private int c;\r\n"+
								"public void x() { Object b; a = a + 1; b.getBla(); b++; }\r\n"
								)));
	
		assertEquals(0.5, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldNotCountInnerClassesInNumberOfMethodsButShouldConsiderItsBody() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { " + 
								"new Thread(new Runnable() { public void run() { a=a+1; b++; } }).start();"+
								"}\r\n"
								)));
	
		assertEquals(0, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldCalculateBadCohesiveClassesWithInnerClasses() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { " + 
								"new Thread(new Runnable() { public void run() { b++; } }).start();"+
								"}\r\n"
								)));

		assertEquals(1, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldReturnMinusOneToInterfaces() {
		metric.calculate(
				toInputStream(
						interfaceDeclaration(
								"void x();" 
								)));

		assertEquals(-1, metric.lcom(), 0.000001);
	}
	
	@Test
	public void shouldGetNameOfTheClass() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { a = a + 1; }\r\n"
								)));
		
		assertEquals("Program", metric.getName());
	}

	@Test
	public void shouldNotBeFooledByInnerClassesAndSoOn() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { a = a + 1; }\r\n"
								)+"class CrazyInnerClass { private int x; }"));
		
		assertEquals("Program", metric.getName());		
	}
	
}
