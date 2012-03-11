package br.com.caelum.revolution.scm;

import java.util.List;

import br.com.caelum.revolution.changesets.ChangeSet;


public interface SCM {
	List<ChangeSet> getChangeSets();
	String goTo(String id);
	CommitData detail(String id);
	String sourceOf(String hash, String fileName);
	
	String getSourceCodePath();
	String blame(String commitId, String file, int line);
}
