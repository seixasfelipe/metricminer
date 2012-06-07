package org.metricminer.tasks.metric.common;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.source;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class MethodsAndAttributesVisitorTests {

	@Test
	public void shouldSeparateMethodsByVisibility() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public void a() { }\r\n"+
								"protected void b() { }\r\n"+
								"private void c() { }\r\n"+
								"void d() { }\r\n"
								)), null);
		
		assertEquals(4, visitor.getMethods().size());

		assertEquals(1, visitor.getPublicMethods().size());
		assertTrue(visitor.getPublicMethods().get(0).getName().equals("a/0"));
		
		assertEquals(1, visitor.getProtectedMethods().size());
		assertTrue(visitor.getProtectedMethods().get(0).getName().equals("b/0"));

		assertEquals(1, visitor.getPrivateMethods().size());
		assertTrue(visitor.getPrivateMethods().get(0).getName().equals("c/0"));

		assertEquals(1, visitor.getDefaultMethods().size());
		assertTrue(visitor.getDefaultMethods().get(0).getName().equals("d/0"));
	}
	
	@Test
	public void shouldSeparateStaticMethodsByVisibility() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public static void a() { }\r\n"+
								"protected static void b() { }\r\n"+
								"private static void c() { }\r\n"+
								"static void d() { }\r\n"
								)), null);
		
		assertEquals(4, visitor.getMethods().size());

		assertEquals(1, visitor.getPublicMethods().size());
		assertTrue(visitor.getPublicMethods().get(0).getName().equals("a/0"));
		
		assertEquals(1, visitor.getProtectedMethods().size());
		assertTrue(visitor.getProtectedMethods().get(0).getName().equals("b/0"));

		assertEquals(1, visitor.getPrivateMethods().size());
		assertTrue(visitor.getPrivateMethods().get(0).getName().equals("c/0"));

		assertEquals(1, visitor.getDefaultMethods().size());
		assertTrue(visitor.getDefaultMethods().get(0).getName().equals("d/0"));
	}
	
	@Test
	public void shouldCountMethodLines() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public void a() { \r\n" +
								"line1();\r\n" +
								"line2();\r\n" +
								"line3();\r\n" +
								"}\r\n"+
								"public b() { \r\n" +
								"line1();\r\n" +
								"line2();\r\n" +
								"line3();\r\n" +
								"line4();\r\n" +
								"}\r\n"
								)), null);
		
		assertEquals(4, visitor.getPublicMethods().get(0).getLines());
		assertEquals(5, visitor.getConstructorMethods().get(0).getLines());
	}
	
	@Test
	public void shouldCountAllMethodsInAClass() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public void a() { }\r\n"+
								"public void b() { }\r\n"+
								"public void c() { }\r\n"
								)), null);
		
		assertEquals(3, visitor.getMethods().size());
	}
	
	@Test
	public void shouldCountAllMethodsInAClassWithDifferentModifiers() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public void a() { }\r\n"+
								"protected void b() { }\r\n"+
								"private void c() { }\r\n" +
								"void d() { }\r\n"
								)), null);
		
		assertEquals(4, visitor.getMethods().size());
	}
	
	@Test
	public void shouldCountConstructors() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"public void a() { }\r\n"+
								"public Program() { }\r\n" +
								"public Program(int param) { }\r\n"
								)), null);
	
		assertEquals(3, visitor.getMethods().size());
	}
	
	@Test
	public void shouldCountAttributes() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"private int a;\r\n"+
								"private double b;\r\n"+
								"private String c;\r\n"+
								"public void x() { }\r\n" +
								"public void y() { }\r\n" +
								"public Program(int param) { }\r\n"
								)), null);
	
		assertEquals(3, visitor.getAttributes().size());
	}
	
	@Test
	public void shouldCountAttributesWithDifferentModifiers() {
		MethodsAndAttributesVisitor visitor = new MethodsAndAttributesVisitor();
		
		visitor.visit(
				source(
						classDeclaration(
								"private int a;\r\n"+
								"public double b;\r\n"+
								"protected String c;\r\n"+
								"String d;\r\n"+
								"public void x() { }\r\n" +
								"public void y() { int d;}\r\n" +
								"public Program(int param) { }\r\n"
								)), null);
	
		assertEquals(4, visitor.getAttributes().size());
		
		assertEquals(1, visitor.getPrivateAttributes().size());
		assertTrue(visitor.getPrivateAttributes().contains("a"));
		
		assertEquals(1, visitor.getPublicAttributes().size());
		assertTrue(visitor.getPublicAttributes().contains("b"));

		assertEquals(1, visitor.getProtectedAttributes().size());
		assertTrue(visitor.getProtectedAttributes().contains("c"));

		assertEquals(1, visitor.getDefaultAttributes().size());
		assertTrue(visitor.getDefaultAttributes().contains("d"));
	}
	
}
