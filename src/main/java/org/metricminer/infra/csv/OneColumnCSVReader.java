package org.metricminer.infra.csv;

import java.io.Reader;
import java.util.List;

public interface OneColumnCSVReader {
	List<Double> readNumbers(Reader is);
}
