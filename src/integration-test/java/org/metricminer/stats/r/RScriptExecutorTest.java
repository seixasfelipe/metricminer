package org.metricminer.stats.r;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.csv.SimpleOneColumnCSVReader;
import org.metricminer.infra.executor.SimpleCommandExecutor;
import org.metricminer.model.QueryResult;
import org.metricminer.model.StatisticalTest;
import org.metricminer.model.StatisticalTestResult;
import org.metricminer.model.User;

public class RScriptExecutorTest {

	private RScriptExecutor r;
	private String path;
	private MetricMinerConfigs config;

	@Before
	public void setUp() throws IOException {
		config = mock(MetricMinerConfigs.class);
		path = new File( "." ).getCanonicalPath() + "/src/test/resources/stats-dir";
		when(config.getStatsResultDir()).thenReturn(path);

		r = new RScriptExecutor(new SimpleCommandExecutor(), config, new SimpleOneColumnCSVReader());
	}
	
	@Test
	public void shouldInvokeR() throws Exception {
		StatisticalTest test = new StatisticalTest("wilcoxon", wilcoxon(), new User());
		QueryResult q1 = new QueryResult(path + "/q1.csv");
		QueryResult q2 = new QueryResult(path + "/q2.csv");
		
		StatisticalTestResult result = r.execute(test, q1, q2);
		
		assertTrue(result.getOutput().contains("Wilcoxon signed rank test"));
		assertTrue(result.getOutput().contains("p-value = 0.25"));
	}

	private String wilcoxon() {
		return 
				"set1 <- #set1#\n"+
				"set2 <- #set2#\n"+
				"t1 = wilcox.test(set1, set2, paired = TRUE)\n"+
				"t1";
	}
}
