package org.metricminer.tasks.metric.methods;

import static br.com.aniche.msr.tests.ParserTestUtils.classDeclaration;
import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MethodsCountMetricTest {

	private MethodsCountMetric metric;
	
	@Before
	public void setUp() {
		this.metric = new MethodsCountMetric();
	}
	
	@Test
	public void shouldCountPrivateMethods() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private void a1() {}"+
								"private void a2() {}"+
								"private void a3() {}"+
								"private void a4() {}"
								)));
		
		assertEquals(4, metric.getPrivateMethods().size());
	}
	
	@Test
	public void shouldCountPublicMethods() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void a1() {}"+
								"public void a2() {}"+
								"public void a3() {}"+
								"public void a4() {}"
								)));
		
		assertEquals(4, metric.getPublicMethods().size());
	}
	
	@Test
	public void shouldCountProtectedMethods() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"protected void a1() {}"+
								"protected void a2() {}"+
								"protected void a3() {}"+
								"protected void a4() {}"
								)));
		
		assertEquals(4, metric.getProtectedMethods().size());
	}
	
	@Test
	public void shouldCountDefaultMethods() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"void a1() {}"+
								"void a2() {}"+
								"void a3() {}"+
								"void a4() {}"
								)));
		
		assertEquals(4, metric.getDefaultMethods().size());
	}
	
	@Test
	public void shouldCountConstructors() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public Program() {}"+
								"protected Program() {}"+
								"private Program() {}"+
								"Program() {}"
								)));
		
		assertEquals(4, metric.getConstructorMethods().size());
	}
	
	@Test
	public void shouldCountPrivateAttributes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"private int a1;"+
								"private int a2;"+
								"private int a3;"+
								"private int a4;"
								)));
		
		assertEquals(4, metric.getPrivateAttributes().size());
	}
	
	@Test
	public void shouldCountPublicAttributes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public int a1;"+
								"public int a2;"+
								"public int a3;"+
								"public int a4;"
								)));
		
		assertEquals(4, metric.getPublicAttributes().size());
	}
	
	@Test
	public void shouldCountProtectedAttributes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"protected int a1;"+
								"protected int a2;"+
								"protected int a3;"+
								"protected int a4;"
								)));
		
		assertEquals(4, metric.getProtectedAttributes().size());
	}
	
	@Test
	public void shouldCountDefaultAttributes() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"int a1;"+
								"int a2;"+
								"int a3;"+
								"int a4;"
								)));
		
		assertEquals(4, metric.getDefaultAttributes().size());
	}
	
	@Test
	public void shouldDifferentMethodsWithSameName() {
		metric.calculate(
				toInputStream(
						classDeclaration(
								"public void a1() {}"+
								"public void a1(int x) {}"+
								"public void a1(int x, int y) {}"
								)));
		
		assertEquals(3, metric.getPublicMethods().size());
		
	}
}
