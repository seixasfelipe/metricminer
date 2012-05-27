package org.metricminer.scm.git;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class GitBlameParser {

	public String getHash(String line) {
		return line.substring(0, line.indexOf(" "));
	}

	public String getAuthor(String line) {
		line = line.substring(line.indexOf("\n") + 1, line.length());
		String authorLine = line.substring(line.indexOf("author"),
				line.indexOf("\n"));
		String author = authorLine.substring(6, authorLine.length()).trim();
		return author;
	}

	public Map<Integer, String> getAuthors(String response) {
		String currentAuthor = null;
		int currentLine = 1;
		Map<Integer, String> blamedLines = new TreeMap<Integer, String>();
		Scanner scanner = new Scanner(response);
		System.out.println(response);
		scanner.useDelimiter("\n");
		while (scanner.hasNext()) {
			scanner.next();
			if (!scanner.hasNext())
				return blamedLines;
			String codeLine = scanner.next();
			if (codeLine.startsWith("author")) {
				currentAuthor = codeLine.substring(7, codeLine.length());
				currentAuthor = currentAuthor.trim();
				for (int i = 0; i < 11; i++) {
					if (!scanner.hasNext())
						return blamedLines;
					scanner.next();
				}
			}
			blamedLines.put(currentLine, currentAuthor);
			currentLine++;
		}
		return blamedLines;
	}

}
