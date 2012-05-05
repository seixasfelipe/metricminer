package org.metricminer.scm;

import java.util.HashMap;
import java.util.Map;

import org.metricminer.model.ArtifactKind;
import org.metricminer.model.ModificationKind;



public class DiffData {

	private String name;
	private String diff;
	private ModificationKind modificationKind;
	private ArtifactKind artifactKind;
	private String fullSourceCode;
	private Map<Integer, String> blameLines;

	public DiffData(String name, String diff,
			ModificationKind modificationKind, ArtifactKind artifactKind) {
		this.name = name;
		this.diff = diff;
		this.modificationKind = modificationKind;
		this.artifactKind = artifactKind;
		this.blameLines = new HashMap<Integer, String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public ModificationKind getModificationKind() {
		return modificationKind;
	}

	public void setModificationKind(ModificationKind modificationKind) {
		this.modificationKind = modificationKind;
	}

	public ArtifactKind getArtifactKind() {
		return artifactKind;
	}

	public void setArtifactKind(ArtifactKind artifactKind) {
		this.artifactKind = artifactKind;
	}

	public void setFullSourceCode(String sourceCode) {
		this.fullSourceCode = sourceCode;
	}

	public String getFullSourceCode() {
		return fullSourceCode;
	}

	public void blame(int line, String author) {
		blameLines.put(line, author);
	}
	
	public Map<Integer, String> getBlameLines() {
		return blameLines;
	}

}
