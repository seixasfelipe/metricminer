package br.com.caelum.revolution.scm.git;

public class GitBlameParser {

	public String getHash(String line) {
		return line.substring(0, line.indexOf(" "));
	}

}
