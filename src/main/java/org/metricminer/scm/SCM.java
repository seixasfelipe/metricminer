package org.metricminer.scm;

import java.util.List;
import java.util.Map;

import org.metricminer.changesets.ChangeSet;



public interface SCM {
	List<ChangeSet> getChangeSets();
	String goTo(String id);
	CommitData detail(String id);
	String sourceOf(String hash, String fileName);
	
	String getSourceCodePath();
	String blame(String commitId, String file, int line);
	String clone(String scmUrl, String destinationPath);
	Map<Integer, String> blame(String commmitId, String string);
}
