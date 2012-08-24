package org.metricminer.infra.csv;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface CSVWriter {

	void emptyResult(OutputStream csvOutputStream);
	void write(OutputStream csvOutputStream, List<Map<String, Object>> results);

}
