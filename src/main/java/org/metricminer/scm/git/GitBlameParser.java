package org.metricminer.scm.git;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitBlameParser {

    public String getHash(String line) {
        return line.substring(0, line.indexOf(" "));
    }

    public String getAuthor(String line) {
        Pattern pattern = Pattern.compile(".*\\S+\\s+\\(([\\w\\s]+)\\d{4}-\\d{2}-\\d{2}.*\\).*");
        Matcher matcher = pattern.matcher(line);
        return matcher.group(1);
    }

}
