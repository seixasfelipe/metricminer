package org.metricminer.scm.git;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metricminer.model.ArtifactKind;
import org.metricminer.model.ModificationKind;
import org.metricminer.scm.DiffData;




public class GitDiffParser {

	private static Map<ModificationKind, String> map;
	
	static {
		map = new HashMap<ModificationKind, String>();
		map.put(ModificationKind.NEW, "new file mode");
		map.put(ModificationKind.DELETED, "deleted file mode");
		map.put(ModificationKind.DEFAULT, "nothing");
	}
	
	public List<DiffData> parse(String diff) {
		List<DiffData> allDiffs = new ArrayList<DiffData>();

		List<String> diffs = Arrays.asList(diff.split("diff --git "));
		diffs = diffs.subList(1, diffs.size());
		for (String unformattedDiff : diffs) {
			List<String> lines = Arrays.asList(unformattedDiff.replace("\r", "").split("\n"));
			
			String content = findContent(lines);
			String name = extractFileNameIn(lines.get(0));
			ModificationKind status = findStatusIn(lines);
			
			allDiffs.add(new DiffData(name, content, status, content.isEmpty() ?  ArtifactKind.BINARY : ArtifactKind.CODE));
		}

		return allDiffs;
	}

	private int findTheLineWhereDiffStarts(List<String> lines) {
		int start = 0;
		for(String line : lines) {
			start++;
			if(line.startsWith("index ")) break;
		}
		return start;
	}
	
	private String findContent(List<String> lines) {
		int lineDiffsStarts = findTheLineWhereDiffStarts(lines);
		if(lineDiffsStarts == lines.size()) return "";
		
		if(!lines.get(lineDiffsStarts).startsWith("Binary files")) {
			return transformInStringTheList(lines.subList(lineDiffsStarts+2, lines.size()));
		}
		return "";
	}

	private ModificationKind findStatusIn(List<String> lines) {
		
		int diffStarts = findTheLineWhereDiffStarts(lines);
		int modeLine = diffStarts == lines.size() ? 1 : diffStarts - 2;
		
		for(ModificationKind st : EnumSet.allOf(ModificationKind.class)) {
			if(lines.get(modeLine).startsWith(map.get(st))) return st;
		}
		
		return ModificationKind.DEFAULT;
	}

	private String extractFileNameIn(String line) {
		return line.substring(2, line.indexOf(" "));
	}

	private String transformInStringTheList(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (String line : list) {
			builder.append(line + "\r\n");
		}

		return builder.toString();
	}

}
