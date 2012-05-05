package org.metricminer.scm.git;

import java.util.List;

import org.metricminer.changesets.ChangeSet;
import org.metricminer.executor.CommandExecutor;
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
				exec.execute("git checkout master -f", getRepoPath());
				exec.execute("git branch --no-track -f revolution " + id,
						getRepoPath());
				exec.execute("git checkout revolution -f", getRepoPath());
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
			exec.execute("git checkout master -f", getRepoPath());
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
			String response = exec
					.execute(
							"git show "
									+ id
									+ " --pretty=format:<Commit><commitId>%H</commitId><author><![CDATA[%an]]></author><email><![CDATA[%ae]]></email><date>%ai</date><message><![CDATA[%s]]></message></Commit>",
							getRepoPath());
			response = cleanCDATA(response);
			XStream xs = new XStream(new DomDriver());
			xs.alias("Commit", CommitData.class);
			CommitData parsedCommit = (CommitData) xs.fromXML(response
					.substring(0, response.indexOf("</Commit>") + 9));
			parsedCommit.setDiff(response.substring(response
					.indexOf("</Commit>") + 9));

			String priorCommit = exec.execute("git log " + id
					+ "^1 --pretty=format:%H -n 1", getRepoPath());
			parsedCommit.setPriorCommit(priorCommit.trim());

			for (DiffData diffData : diffParser.parse(parsedCommit.getDiff())) {
				diffData.setFullSourceCode(sourceOf(id, diffData.getName()));
				
				//for(int line = 1; line <= linesIn(diffData.getFullSourceCode()); line++) {
					//diffData.blame(line, blame(id, diffData.getName(), line));
				//}
				
				parsedCommit.addDiff(diffData);
			}

			return parsedCommit;
		} catch (Exception e) {
			throw new SCMException(e);
		}
	}

	private int linesIn(String modifiedSource) {
		char lastChar = modifiedSource.charAt(modifiedSource.length() - 1);
		return lastChar == '\n' ? modifiedSource.split("\n").length : modifiedSource.split("\n").length+1; 
	}

	private String cleanCDATA(String response) {
        String message = response.substring(response.indexOf("<message>") + 9,
                response.indexOf("</message>"));
        message = message.replaceAll("\\]\\]>", "");
        message = message + "]]>";
        response = response.substring(0, response.indexOf("<message>") + 9) + message
                + response.substring(response.indexOf("</message>"), response.length());
        return response;
    }

	//TODO: arrumar isso aqui, vai precisar de uma regex
    public String blame(String commitId, String file, int line) {
		goTo(commitId);
		String response = exec.execute("git blame " + file + " -L " + line
				+ "," + line + " -l", getRepoPath());
		
		//return blameParser.getAuthor(response);
		return blameParser.getHash(response);
	}

	public String getSourceCodePath() {
		return repository;
	}

	public String clone(String scmUrl, String localPath) {
		String command = "git clone " + scmUrl;
		exec.execute("mkdir -p " + localPath, "/");
		return exec.execute(command, localPath);

	}

}
