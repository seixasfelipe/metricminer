package org.metricminer.infra.csv;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

public class SimpleCSVWriterTest {

    @Test
    public void shouldWriteCsv() {
        SimpleCSVWriter writer = new SimpleCSVWriter();
        
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("column1", "value11");
        row.put("column2", "value21");
        results.add(row);
        
        row = new HashMap<String, Object>();
        row.put("column1", "value21");
        row.put("column2", "value22");
        results.add(row);
    
        ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
        writer.write(csvOutputStream, results);
        
        String expected = "column1;column2;\n" +
                "value11;value21;\n" +
                "value21;value22;\n";
        String csv = new String(csvOutputStream.toByteArray());
        

        assertEquals(expected, csv);
    }

}
