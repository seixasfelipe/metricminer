package org.metricminer.tasks.metric.testedmethods;

import static br.com.aniche.msr.tests.ParserTestUtils.toInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestedMethodFinderMetricTest {
	private TestedMethodFinderMetric metric;

	@Before
	public void setUp() {
		this.metric = new TestedMethodFinderMetric();
	}
	
	@Test
	public void shouldFindTheLastProductionClassMethodInvocationInATest() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Bla x = new Bla();" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"x.method1();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method1"));
		
	}
	
	@Test
	public void shouldFindAllProductionClassMethodInvocationsInATest() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Bla x = new Bla();" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"x.method1();" +
						"x.method2();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method1"));
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method2"));
		
	}
	
	@Test
	public void shouldFindAllProductionClassMethodInvocationsInDifferentVariablesInATest() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Bla x = new Bla();" +
						"Bla x1 = new Bla();" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"x.method1();" +
						"x1.method2();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method1"));
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method2"));
		
	}
	
	@Test
	public void shouldFindTheProductionClassMethodInvocationWhenAnAttributeIsInvoked() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"private Bla x = new Bla();" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"x.method1();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method1"));
	}
	
	@Test
	public void shouldReturnNothingIfNoInvokationWasFound() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"private Bla x = new Bla();" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"assertEquals(y.isAbc());" +
						"}" +
						"}"
								));
		
		assertFalse(metric.getMethods().containsKey("aSimpleTest"));
	}
	
	@Test
	public void shouldFindTheLastProductionClassMethodInvocationInManyMethodsATest() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"private Bla k = new Bla();" +
						"@Test " +
						"public void aSimpleTest() {" +
						"Bla x = new Bla();" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"x.method1();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"@Test " +
						"public void aSimpleTest2() {" +
						"Ble y = new Ble();" +
						"y.nothing();" +
						"k.method2();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aSimpleTest").contains("method1"));
		assertTrue(metric.getMethods().get("aSimpleTest2").contains("method2"));
		
	}
	
	@Test
	public void shouldIgnoreInvocationsThatYouDontKnow() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"@Test " +
						"public void aSimpleTest() {" +
						"y.nothing();" +
						"x.method1();" +
						"assertEquals(x.isAbc());" +
						"}" +
						"}"
								));
		
		assertEquals(0, metric.getMethods().size());
		
	}
	
	@Test
	public void shouldIgnoreConstructors() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"public BlaTest() {" +
						"Bla a;" +
						"a.method();" +
						"}" +
						"}"
								));
		
		assertEquals(0, metric.getMethods().size());
	}
	
	@Test
	public void shouldUnderstandSetupInitializations() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"private Bla x;" +
						"@Before public void setUp() {" +
						"x = new Bla();" +
						"}" +
						"@Test public void aTest() {" +
						"x.method();" +
						"}" +
						"}"
								));
		
		assertTrue(metric.getMethods().get("aTest").contains("method"));
	}
	
	
	@Test
	public void shouldUnderstandInlineVariableDeclarations() {
		metric.calculate(
				toInputStream(
						"class BlaTest {" +
						"@Test public void aTest() {" +
						"for(int i = 0; i < 10; i++) {" +
						"assertTrue(i > -1);" +
						"}" +
						"}" +
						"}"
								));
		
		assertEquals(0, metric.getMethods().size());
	}
	
	@Test
	public void shouldUnderstandInvocationTogetherWithVariableDeclaration() {
		metric.calculate(toInputStream(
			"class IntervaloTest {" +
			"@Test " +
			"public void getMesesFuncionaMesmoQuandoIntervaloComecaNoUltimoMillisegundoDoMes() throws Exception {" +
			"	Calendar inicio = new GregorianCalendar(2008, Calendar.DECEMBER, 31, 23, 59, 59);" +
			"	inicio.set(Calendar.MILLISECOND, 999);" +
			"	Calendar fim = new GregorianCalendar(2009, Calendar.JANUARY, 1);" +
				
			"	Intervalo intervalo = new Intervalo(inicio, fim);" +
			"	List<MesAno> meses = intervalo.getMeses();" +
			"	assertThat(meses, contains(new MesAno(Mes.DEZEMBRO, 2008), new MesAno(Mes.JANEIRO, 2009)));" +
			"}" +
			"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("getMesesFuncionaMesmoQuandoIntervaloComecaNoUltimoMillisegundoDoMes").contains("getMeses"));
	}
	
	@Test
	public void shouldNotUnderstandOnlyConstructor() {
		metric.calculate(toInputStream(
			"class CaelumHeaderTest { " +
			"@Test "+
			"public void enderecoDeSaoPauloEstaCorreto() throws Exception {"+
			"	Unidade unidade = Unidades.SAO_PAULO.model();"+
			"	PDFDocument pdf = new PDFDocument(os);"+
			
			"	CaelumHeader header = new CaelumHeader(unidade);"+
			"	pdf.use(header);"+
			
			"	pdf.open();"+
			"	pdf.add(new PDFHelper(pdf).chunk(\" XPTO SP\"));"+
			"	pdf.close();"+
			
			
			"	assertContains(\"Rua Vergueiro\");"+
			"}" +
			"}"
		));
		
		assertEquals(0, metric.getMethods().size());
	}
	
	@Test
	public void shouldUnderstandConstructorExpressionsAndInvocationWithVariableDeclaration() {
		metric.calculate(toInputStream(
				"class ParcelasTest { " +
				"@Test "+
				"public void pendenciasQuandoAsSituacoesSaoAVistaGeramAvisoCorreto() throws Exception {"+
				"	Parcela primeiraEmDinheiro = mother.umaParcela(SituacaoDeParcela.AGUARDANDO_DINHEIRO, new BigDecimal(\"100\"));"+
				"	Parcela primeiraDeCheque = mother.umaParcela(SituacaoDeParcela.A_RECEBER_CHEQUE, new BigDecimal(\"100\"));"+
				"	Parcela primeiraDeCartao = mother.umaParcela(SituacaoDeParcela.AGUARDANDO_PASSAR_CARTAO, new BigDecimal(\"100\"));"+
				"	primeiraDeCartao.setNumeroDeParcelasDoCartao(1);"+
				
				"	Parcelas parcelas = new Parcelas(Arrays.asList(primeiraEmDinheiro, primeiraDeCheque, primeiraDeCartao));"+
				
				"	List<String> pendencias = parcelas.getPendencias();"+
				
				"	Assert.assertEquals(\"Valor a receber em dinheiro R$ 100,00. \" , pendencias.get(0));"+
				"	Assert.assertEquals(\"Receber cheque de R$ 100,00 &agrave; vista. \" , pendencias.get(1));"+
				"	Assert.assertEquals(\"Passar cart&atilde;o R$ 100,00 &agrave; vista. \" , pendencias.get(2));"+
				"}" +
				"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("pendenciasQuandoAsSituacoesSaoAVistaGeramAvisoCorreto").contains("getPendencias"));
		
	}
	
	@Test
	public void shouldUnderstandStaticInvocations() {
		metric.calculate(toInputStream(
			"public class FormatadorParaLoteDeNotasTest {" +
			"@Test "+
			"public void formatadorDeValorMonetarioDevolveOValorEmCentavos() throws Exception {"+
				"assertEquals(\"256\", FormatadorParaLoteDeNotas.valorMonetarioFormatado(BigDecimal.valueOf(2.56), 3));"+
			"}" +
			"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("formatadorDeValorMonetarioDevolveOValorEmCentavos").contains("valorMonetarioFormatado"));

	}
	
	@Test
	public void shouldUnderstandDeclarationAndInvokationInSequence() {
		metric.calculate(toInputStream(
			"class ParcelaTest {" +
			"@Test "+
			"public void parcelaEhInicializadaComNumeroDeParcelamentoIgualAUm() throws Exception {"+
			"	assertEquals(new Integer(1), new Parcela().getNumeroDeParcelasDoCartao());"+
			"}" +
			"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("parcelaEhInicializadaComNumeroDeParcelamentoIgualAUm").contains("getNumeroDeParcelasDoCartao"));

	}
	
	@Test
	public void shouldNotBeFooledByAConstructorThatReceivesAnotherNewDeclaration() {
		metric.calculate(toInputStream(
				"class TurmasPorMesTest {" +
				"	@Test "+
				"	public void retornaMediaDePagosZeradosSeNecessario() {"+
				"		TurmasPorMes porMes = new TurmasPorMes(new ArrayList<Turma>(), false);"+
				"		assertEquals(0.0, porMes.getMediaDePagos(), 0.0);"+
				"	}" +
				"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("retornaMediaDePagosZeradosSeNecessario").contains("getMediaDePagos"));
	}
	
	@Test
	public void shouldUnderstandListOfTypeThatIsBeingTested() {
		metric.calculate(toInputStream("class BlaTest {" +
				"@Test " +
				"public void test1() {" +
				"List<Bla> list = new ArrayList<Bla>();" +
				"assertEquals(1, list.get(0).doX());" +
				"}" +
				"}"));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("test1").contains("doX"));

	}
	
	@Test
	public void shouldLookToConcreteTypes() {
		metric.calculate(toInputStream(
		"public class BowlingGameTest extends TestCase {"+
		"	  private Bowling g;"+

		"	  protected void setUp() throws Exception {" +
		"		doSomething();"+
		"	    g = new BowlingGame();"+
		"	  }" +
		"	  @Test public void test1() {" +
		"		y();" +
		"		g.doX();" +
		"	  }"+
		"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("test1").contains("doX"));

	}
	
	@Test
	public void shouldLookToConcreteTypesEvenIfTheyMatchReferenceType() {
		metric.calculate(toInputStream(
		"public class BowlingGameTest extends TestCase {"+
		"	  private BowlingGame g;"+

		"	  protected void setUp() throws Exception {" +
		"		doSomething();"+
		"	    g = new BowlingGame();"+
		"	  }" +
		"	  @Test public void test1() {" +
		"		y();" +
		"		g.doX();" +
		"	  }"+
		"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("test1").contains("doX"));

	}
	
	@Test
	public void shouldNotBeFooledByCrazyConcreteTypes() {
		metric.calculate(toInputStream(
		"public class BowlingGameTest extends TestCase {"+
		"	  private Bowling g;"+

		"	  protected void setUp() throws Exception {" +
		"		doSomething();"+
		"	    g = new BowlingGame2();"+
		"	  }" +
		"	  @Test public void test1() {" +
		"		y();" +
		"		g.doX();" +
		"	  }"+
		"}"
		));
		
		assertEquals(0, metric.getMethods().size());

	}
	
	@Test
	public void shouldLookToConcreteTypesInsideTestMethod() {
		metric.calculate(toInputStream(
		"public class BowlingGameTest extends TestCase {"+
		"	  private Bowling g;"+
		"	  @Test public void test1() {" +
		"	    g = new BowlingGame();"+
		"		y();" +
		"		g.doX();" +
		"	  }"+
		"}"
		));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("test1").contains("doX"));

	}
	
	@Test
	public void shouldNotBeFooledByManyInstantiationsInProductionClass() {
		String code = 
			"public class AttributeWikiPageFinderTest implements SearchObserver {"+
			"	  private WikiPage root;"+
			"	  private PageCrawler crawler;"+
			"	  private WikiPage page;"+
			"	  private AttributeWikiPageFinder searcher;"+
			
			"	  private List<WikiPage> hits = new ArrayList<WikiPage>();"+
			
			  "@Before"+
			  "public void setUp() throws Exception {"+
			  "  root = InMemoryPage.makeRoot(\"RooT\");"+
			  "  crawler = root.getPageCrawler();"+
			  "  searcher = new AttributeWikiPageFinder(this, Arrays.asList(TEST),"+
			  "      new HashMap<String, Boolean>(), new ArrayList<String>());"+
			  "  page = crawler.addPage(root, PathParser.parse(\"TestPage\"));"+
			  "  hits.clear();"+
			  "}"+
			
			  "@Test"+
			  "public void testPageMatchesQueryWithIncludedSetUps() throws Exception {"+
			  "  Map<String, Boolean> attributes = new HashMap<String, Boolean>();"+
			  "  attributes.put(\"SetUp\", true);"+
			
			  "  List<PageType> pageTypes = Arrays.asList(TEST, STATIC, SUITE);"+
			  "  searcher = generateSearcherByPageTypesAndSearchAttributes(pageTypes, attributes);"+
			  "  setPageProperty(page, TEST.toString(), \"true\");"+
			  "  assertFalse(searcher.pageMatches(page));"+
			
			  "  page = crawler.addPage(root, PathParser.parse(\"SetUp\"));"+
			  "  assertTrue(searcher.pageMatches(page));"+
			
			  "  page = crawler.addPage(root, PathParser.parse(\"TearDown\"));"+
			  "  assertFalse(searcher.pageMatches(page));"+
			
			  "  page = crawler.addPage(root, PathParser.parse(\"SuiteSetUp\"));"+
			  "  assertTrue(searcher.pageMatches(page));"+
			
			  "  page = crawler.addPage(root, PathParser.parse(\"SuiteTearDown\"));"+
			  "  assertFalse(searcher.pageMatches(page));"+
			  "}" +
			  "}";
		
		metric.calculate(toInputStream(code));
		
		assertEquals(1, metric.getMethods().size());
		assertTrue(metric.getMethods().get("testPageMatchesQueryWithIncludedSetUps").contains("pageMatches"));
	}

}
