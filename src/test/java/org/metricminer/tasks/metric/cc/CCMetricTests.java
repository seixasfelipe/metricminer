package org.metricminer.tasks.metric.cc;

import org.junit.Before;
import org.junit.Test;
import static br.com.aniche.msr.tests.ParserTestUtils.*;
import static org.junit.Assert.assertEquals;

public class CCMetricTests {

	private CCMetric metric;

	@Before
	public void setUp() {
		metric = new CCMetric();
	}
	
	@Test
	public void shouldCalculateCCForAPlainClass() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"a=a+1;" +
						"b=b+1;" +
						"return a+b; }"))
				);
		
		assertEquals(1, metric.cc());
	}

	@Test
	public void shouldCalculateCCForOverridedMethods() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"a=a+1;" +
						"b=b+1;" +
						"return a+b; }"+
						"public int method(int x) {" +
						"a=a+1;" +
						"b=b+1;" +
						"return a+b; }"+
						"public int method(double x) {" +
						"a=a+1;" +
						"b=b+1;" +
						"return a+b; }"+
						"public int method(int x, double y) {" +
						"a=a+1;" +
						"b=b+1;" +
						"return a+b; }"
						))
				);
		
		// sdsaddsa
		assertEquals(1, metric.cc("method/0"));
		assertEquals(1, metric.cc("method/1[int]"));
		assertEquals(1, metric.cc("method/1[double]"));
		assertEquals(1, metric.cc("method/2[int,double]"));
		assertEquals(4, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachIf() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y) a=a+1;" +
						"if(x) b=b+1;" +
						"return a+b; }"))
				);
		
		assertEquals(3, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachIfAndIgnoreElse() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y) a=a+1; else a=a+2;" +
						"if(x) b=b+1; else b=b+2;" +
						"return a+b; }"))
				);
		
		assertEquals(3, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachIfAndElseIfAndIgnoreElse() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y) a=a+1; else if(y2) a=a+2; else a=a+3;" +
						"if(x) b=b+1; else if(x2) b=b+2; else b=b+3;" +
						"return a+b; }"))
				);
		
		assertEquals(5, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachAndInAnIf() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y && y1) a=a+1;" +
						"if(x && x2) b=b+1;" +
						"return a+b; }"))
				);
		
		assertEquals(5, metric.cc());
	}

	@Test
	public void shouldAddOneForEachOrInAnIf() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y || y1) a=a+1;" +
						"if(x || x2) b=b+1;" +
						"return a+b; }"))
				);
		
		assertEquals(5, metric.cc());
	}

	@Test
	public void shouldAddOneForEachAndOrOrInAnIf() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(y && y1 || y2) a=a+1;" +
						"if(x && x2 || x3) b=b+1;" +
						"return a+b; }"))
				);
		
		assertEquals(7, metric.cc());
	}

	
	@Test
	public void shouldAddOneForEachFor() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"for(int i = 0; i < 10; i++) {"+
						"a=a+1;" +
						"b=b+1; }" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachForEach() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
								"for(Item a : itens) {"+
								"a=a+1;" +
								"b=b+1; }" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
		
	}
	
	@Test
	public void shouldAddOneForEachWhile() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"while(true) {"+
						"a=a+1;" +
						"b=b+1; }" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachDoWhile() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"do {"+
						"a=a+1;" +
						"b=b+1; } while(true);" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachCase() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"switch(a) {"+
						"case 1: return bla;" +
						"case 2: return bla;" +
						"case 3: return bla;" +
						"}" +
						"return a+b; }"))
				);
		
		assertEquals(4, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachCaseButIgnoreDefault() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"switch(a) {"+
						"case 1: return bla;" +
						"case 2: return bla;" +
						"case 3: return bla;" +
						"default: return bla;" +
						"}" +
						"return a+b; }"))
				);
		
		assertEquals(4, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachCatch() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"try { bla(); }" +
						"catch(Exception e1) { ble(); }" +
						"catch(Exception e2) { bli(); }" +
						"return a+b; }"))
				);
		
		assertEquals(3, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachCatchButIgnoreFinally() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"try { bla(); }" +
						"catch(Exception e1) { ble(); }" +
						"catch(Exception e2) { bli(); }" +
						"finally { bli(); }" +
						"return a+b; }"))
				);
		
		assertEquals(3, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachTernaryIf() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"a=a==10? 1 : 2;" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
	}
	
	@Test
	public void shouldAddOneForEachTernaryIfInMiddleOfExpression() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"x.invoke((a=a==10? 1 : 2));" +
						"return a+b; }"))
				);
		
		assertEquals(2, metric.cc());
	}

	@Test
	public void shouldCalculateTheAverageCC() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method1() {" +
						"if(x) {}" +
						"if(x) {}" +
						"return a+b; }"+
						"public int method2() {" +
						"return a+b; }"
						))
				);
		
		assertEquals(2, metric.avgCc(), 0.00001);
	}
	
	@Test
	public void shouldCalculateForMoreThanOneMethod() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method1() {" +
						"if(x) {}" +
						"return a+b; }"+
						"public int method2() {" +
						"a=a==10? 1 : 2;" +
						"return a+b; }"+
						"public int method3() {" +
						"for(;;) {}" +
						"return a+b; }"
						
						))
				);
		
		assertEquals(6, metric.cc());
		assertEquals(2, metric.cc("method1/0"));
		assertEquals(2, metric.cc("method2/0"));
		assertEquals(2, metric.cc("method3/0"));
	}
	
	@Test
	public void shouldCalculateForInnerMethodsSeparately() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method1() {" +
						"if(x) {}" +
						"new Thread(new Runnable() { public void run() { nothing(); }}).start();" +
						"return a+b; }"
						))
				);
		
		assertEquals(2, metric.cc("method1/0"));
		assertEquals(1, metric.cc("run/0"));
		
	}

	@Test
	public void shouldCalculateCCForAllOperatorsTogethers() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public int method() {" +
						"if(x) bla();" +
						"for(;;) bla();" +
						"while(x) bla();" +
						"do { bla(); } while(x);" +
						"switch(x) { case 1: bla(); } " +
						"try{}catch(Exception e){}" +
						"if(x && y) bla();" +
						"if(x || y) bla();" +
						"int a = (x ? 1 : 0);" +
						"return a+b; }"))
				);
		
		assertEquals(12, metric.cc());
	}

	@Test
	public void shouldCalculateCCInConstructors() {
		metric.calculate(
				toInputStream(classDeclaration(
						"public Program() {" +
						"if(x) bla(); }")));

		assertEquals(2, metric.cc("Program/0"));
	}
	
	@Test
	public void shouldCalculateCCForStaticBlock() {
		metric.calculate(
				toInputStream(classDeclaration(
						"static {"+
						 "if(a) x();"+
						"}"))
				);
		
		assertEquals(2, metric.cc());
	}
	
	@Test
	public void shouldCalculateCCInAttributes() {
		metric.calculate(
				toInputStream(classDeclaration(
						"private static int a = x ? 1 : 2;"+
						"private static int b = x ? 1 : 2;"))
				);		
		
		assertEquals(2, metric.cc());
	}
}
