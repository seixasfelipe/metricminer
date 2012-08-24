package org.metricminer.infra.csv;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SimpleOneColumnCSVReader implements OneColumnCSVReader {

	@Override
	public List<Double> readNumbers(Reader is) {
		Scanner sc = new Scanner(is);
		
		List<Double> numbers = new ArrayList<Double>();
		
		while(sc.hasNextLine()) {
			try {
				String token = sc.nextLine().replace(";", "");
				numbers.add(Double.parseDouble(token));
			} catch (NumberFormatException e) {
				// oops!?
			}
		}
		
		return numbers;
	}

}
