package org.metricminer.scm.git;

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

}
