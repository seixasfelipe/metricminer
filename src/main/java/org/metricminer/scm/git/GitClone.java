package org.metricminer.scm.git;

import java.util.List;

import org.metricminer.changesets.ChangeSet;
import org.metricminer.executor.CommandExecutor;
import org.metricminer.scm.CommitData;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SCMException;



public class GitClone implements SCM{

	private final Git git;

	public GitClone(String gitRepository, String destiny, CommandExecutor exec, Git git) {
		this.git = git;
		
		try {
			exec.execute("git clone " + gitRepository + " " + destiny, destiny);
		} catch (Exception e) {
			throw new SCMException(e);
		}
	}

	public List<ChangeSet> getChangeSets() {
		return git.getChangeSets();
	}

	public String goTo(String id) {
		return git.goTo(id);
	}

	public CommitData detail(String id) {
		return git.detail(id);
	}

	public String sourceOf(String hash, String fileName) {
		return git.sourceOf(hash, fileName);
	}

	public String getSourceCodePath() {
		return git.getSourceCodePath();
	}

	public String blame(String commitId, String file, int line) {
		return git.blame(commitId, file, line);
	}
	

}
