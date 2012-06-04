package org.metricminer.tasks.metric.lcom;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.source;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MethodsPerAttributeVisitorTests {

	private MethodsPerAttributeVisitor visitor;
	private List<String> attributes;

	@Before
	public void setUp() {
		attributes = Arrays.asList("a", "b", "c");
		
		visitor = new MethodsPerAttributeVisitor(attributes);
	}
	
	@Test
	public void shouldCountNumberOfAttributesThatAMethodDealsWith() {
		
		visitor.visit(
				source(
						classDeclaration(
								"private int a;\r\n"+
								"public void x() { a = a + 1; }\r\n"
								)), null);
	
		assertEquals(1, visitor.getMethodsPerAttribute().get("a").size());
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("x"));
	}

	@Test
	public void shouldCountNumberOfAttributesThatManyMethodsDealWith() {
		visitor.visit(
				source(
						classDeclaration(
								"private int a;\r\n"+
								"private int b;\r\n"+
								"public void x() { a = a + 1; }\r\n"+
								"public void y() { a = a + 1; b = b + 1; }\r\n"
				)), null);
	
		assertEquals(2, visitor.getMethodsPerAttribute().get("a").size());
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("x"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("y"));

		assertEquals(1, visitor.getMethodsPerAttribute().get("b").size());
		assertTrue(visitor.getMethodsPerAttribute().get("b").contains("y"));
	}
	
	@Test
	public void shouldCountNumberOfAttributesThatAMethodDealsWithInVeryStrangeWays() {
		
		visitor.visit(
				source(
						classDeclaration(
								"private int a;\r\n"+
								"public void a1() { a = a + 1; }\r\n"+
								"public void a2() { System.out.println(a); }\r\n"+
								"public void a3() { someMethod(a); }\r\n"+
								"public void a5() { a++; }\r\n"+
								"public void a4() { a = new Object(); }\r\n"+
								"public void a6() { a.getBla(); }\r\n"+
								"public void a7() { return a; }\r\n"
								)), null);
	
		assertEquals(7, visitor.getMethodsPerAttribute().get("a").size());
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a1"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a2"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a3"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a4"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a5"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a6"));
		assertTrue(visitor.getMethodsPerAttribute().get("a").contains("a7"));
	}
	
}
