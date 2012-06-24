package org.metricminer.scm.git;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.metricminer.changesets.ChangeSet;
import org.metricminer.infra.executor.CommandExecutor;
import org.metricminer.model.ArtifactKind;
import org.metricminer.scm.CommitData;
import org.metricminer.scm.DiffData;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SCMException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Git implements SCM {

	private final String repository;
	private final GitLogParser logParser;
	private final CommandExecutor exec;
	private final GitDiffParser diffParser;
	private final GitBlameParser blameParser;
	private String currentPosition;

	public Git(String repository, GitLogParser logParser,
			GitDiffParser diffParser, GitBlameParser blameParser,
			CommandExecutor exec) {
		this.repository = repository;
		this.logParser = logParser;
		this.diffParser = diffParser;
		this.blameParser = blameParser;
		this.exec = exec;
	}

	public String goTo(String id) {
		if (!id.equals(currentPosition)) {
			try {
				// exec.execute("git checkout master -f", getRepoPath());
				exec.execute("git branch --no-track -f metricminer " + id,
						getRepoPath());
				exec.execute("git checkout metricminer -f", getRepoPath());
			} catch (Exception e) {
				throw new SCMException(e);
			}

			currentPosition = id;
		}

		return repository;
	}

	private String getRepoPath() {
		return repository;
	}

	public List<ChangeSet> getChangeSets() {
		try {
			// exec.execute("git checkout master -f", getRepoPath());
			String output = exec.execute(
					"git log --format=medium --date=iso --reverse",
					getRepoPath());
			return logParser.parse(output);
		} catch (Exception e) {
			throw new SCMException(e);
		}
	}

	public String sourceOf(String id, String fileName) {
		return exec.execute("git show " + id + ":" + fileName, repository);
	}

	public CommitData detail(String id) {
		try {
			CommitData parsedCommit = parseCommit(id);
			String priorCommit = findPriorCommitOf(id);
			parsedCommit.setPriorCommit(priorCommit.trim());

			for (DiffData diffData : diffParser.parse(parsedCommit.getDiff())) {
				diffData.setFullSourceCode(sourceOf(id, diffData.getName()));
				//parseBlameInformation(id, diffData);
				parsedCommit.addDiff(diffData);
			}

			return parsedCommit;
		} catch (Exception e) {
			throw new SCMException(e);
		}
	}

	private void parseBlameInformation(String id, DiffData diffData) {
		if (diffData.getArtifactKind() != ArtifactKind.BINARY) {
			Map<Integer, String> blamedLines = blame(id, diffData.getName());
			for (Entry<Integer, String> blamedLineEntry : blamedLines
					.entrySet()) {
				diffData.blame(blamedLineEntry.getKey(),
						blamedLineEntry.getValue());
			}
		}
	}

	private String findPriorCommitOf(String id) {
		String priorCommit = exec.execute("git log " + id
				+ "^1 --pretty=format:%H -n 1", getRepoPath());
		return priorCommit;
	}

	private CommitData parseCommit(String id) {
		String response = exec
				.execute(
						"git show "
								+ id
								+ " --pretty=format:<Commit><commitId>%H</commitId><author><![CDATA[%an]]></author><email><![CDATA[%ae]]></email><date>%ai</date><message><![CDATA[%s]]></message></Commit>",
						getRepoPath());
		response = cleanCDATA(response);
		XStream xs = new XStream(new DomDriver());
		xs.alias("Commit", CommitData.class);
		CommitData parsedCommit = (CommitData) xs.fromXML(response.substring(0,
				response.indexOf("</Commit>") + 9));
		parsedCommit
				.setDiff(response.substring(response.indexOf("</Commit>") + 9));
		return parsedCommit;
	}

	private int linesIn(String modifiedSource) {
		int lines = 0;
		for (int i = 0; i < modifiedSource.length() - 1; i++) {
			if (modifiedSource.charAt(i) == '\n')
				lines++;
		}
		int lastCharIndex = modifiedSource.length() - 1;
		if (lastCharIndex > 0) {
			lines += modifiedSource.charAt(lastCharIndex) == '\n' ? 0 : 1;
		}
		return lines;
	}

	private String cleanCDATA(String response) {
		String message = response.substring(response.indexOf("<message>") + 9,
				response.indexOf("</message>"));
		message = message.replaceAll("\\]\\]>", "");
		message = message + "]]>";
		response = response.substring(0, response.indexOf("<message>") + 9)
				+ message
				+ response.substring(response.indexOf("</message>"),
						response.length());
		return response;
	}

	public String getSourceCodePath() {
		return repository;
	}

	public String clone(String scmUrl, String destinationPath) {
		String command = "git clone " + scmUrl + " " + destinationPath;
		exec.execute("mkdir -p " + destinationPath, "/");
		return exec.execute(command, destinationPath);
	}

	public String blame(String commitId, String file, int line) {
		goTo(commitId);
		String response = exec.execute("git blame " + file + " -L " + line
				+ "," + line + " -p -l", getRepoPath());

		return blameParser.getAuthor(response);
	}

	@Override
	public Map<Integer, String> blame(String commitId, String filePath) {
		String response = exec.execute("git blame " + commitId + " " + filePath
				+ " -p ", getRepoPath());
		
		return blameParser.getAuthors(response);
	}
}
