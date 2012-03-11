package br.com.caelum.revolution.scm;

import br.com.caelum.revolution.domain.ArtifactKind;
import br.com.caelum.revolution.domain.ModificationKind;

public class DiffData {

	private String name;
	private String diff;
	private ModificationKind modificationKind;
	private ArtifactKind artifactKind;
	
	public DiffData(String name, String diff,
			ModificationKind modificationKind, ArtifactKind artifactKind) {
		this.name = name;
		this.diff = diff;
		this.modificationKind = modificationKind;
		this.artifactKind = artifactKind;
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
	
	
}
