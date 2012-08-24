package org.metricminer.stats.r;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.csv.OneColumnCSVReader;
import org.metricminer.infra.executor.CommandExecutor;
import org.metricminer.model.QueryResult;
import org.metricminer.model.StatisticalTest;
import org.metricminer.model.StatisticalTestResult;

public class RScriptExecutor {

	private final CommandExecutor cmd;
	private final MetricMinerConfigs config;
	private final OneColumnCSVReader reader;

	public RScriptExecutor(CommandExecutor cmd, MetricMinerConfigs config, OneColumnCSVReader reader) {
		this.cmd = cmd;
		this.config = config;
		this.reader = reader;
	}
	
	public StatisticalTestResult execute(StatisticalTest test, QueryResult q1, QueryResult q2) throws Exception {
		FileReader f1 = null;
		FileReader f2 = null;

		try {
			f1 = new FileReader(q1.getCsvFilename());
			f2 = new FileReader(q2.getCsvFilename());
			
			List<Double> set1 = reader.readNumbers(f1);
			List<Double> set2 = reader.readNumbers(f2);
			
			String algorithm = test.algorithmFor(set1, set2);
			String rFile = saveFile(algorithm);
			
			String output = cmd.execute("Rscript " + rFile, ".");
			
			return new StatisticalTestResult(q1, q2, test, output);
		}
		finally {
			if(f1!=null) f1.close();
			if(f2!=null) f2.close();
		}
	}

	private String saveFile(String algorithm) throws Exception {
		String file = config.getStatsResultDir() + "/stat-" + Calendar.getInstance().getTimeInMillis() + ".r";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		bw.append(algorithm);
		bw.flush();
		bw.close();
		
		return file;
	}
	
	
}
