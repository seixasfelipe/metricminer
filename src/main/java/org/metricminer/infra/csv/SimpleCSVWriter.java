package org.metricminer.infra.csv;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SimpleCSVWriter implements CSVWriter {

    public void emptyResult(OutputStream csvOutputStream) {
    	PrintStream csvPrint = new PrintStream(csvOutputStream);
    	csvPrint.println("Your query returned 0 rows.");
	}

	public void write(OutputStream csvOutputStream, List<Map<String,Object>> results) {
        PrintStream csvPrint = new PrintStream(csvOutputStream);
        Map<String, Object> first = results.get(0);
        printHeader(csvPrint, first);
        for (Map<String,Object> row : results) {
            for (Entry<String, Object> entry : row.entrySet()) {
                csvPrint.print(entry.getValue() + ";");
            }
            csvPrint.print("\n");
        }
    }

    private void printHeader(PrintStream csvPrint, Map<String, Object> first) {
        for (Entry<String, Object> entry : first.entrySet()) {
            csvPrint.print(entry.getKey() + ";");
        }
        csvPrint.print("\n");
    }
}
