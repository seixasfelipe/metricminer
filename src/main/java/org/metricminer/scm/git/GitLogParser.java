package org.metricminer.scm.git;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.metricminer.changesets.ChangeSet;



public class GitLogParser {

	private final int SHA1_SIZE = 40;
	
	public List<ChangeSet> parse(String log) throws Exception {
		String[] lines = log.replace("\r\n", "\n").split("\n");
		List<ChangeSet> shas = new ArrayList<ChangeSet>();
		
		String sha = "";
		for(String line : lines) {
			if(line.startsWith("commit ")) {
				String[] commitLine = line.split(" ");
				if(commitLine[1].length() == SHA1_SIZE) {
					sha = commitLine[1];
				}
			}
			if(line.startsWith("Date: ")) {
				String date = line.substring(6).trim();
				Calendar cal = parseDate(date);
				shas.add(new ChangeSet(sha, cal));
				sha = "";
			}
		}
		
		return shas;
	}

	private Calendar parseDate(String date) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sf.parse(date));
		return cal;		
	}

}
