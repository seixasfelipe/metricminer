package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Modification {

	@Id @GeneratedValue
	private int id;
	@Type(type="text")
	private String diff;
	@ManyToOne
	private Commit commit;
	@ManyToOne
	private Artifact artifact;
	@Enumerated(EnumType.STRING)
	private ModificationKind kind;
	
	public Modification() {
	}
	
	public Modification(String diff, Commit commit, Artifact artifact,
			ModificationKind kind) {
		this.diff = diff;
		this.commit = commit;
		this.artifact = artifact;
		this.kind = kind;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public Commit getCommit() {
		return commit;
	}
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	public Artifact getArtifact() {
		return artifact;
	}
	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}
	public ModificationKind getKind() {
		return kind;
	}
	public void setKind(ModificationKind kind) {
		this.kind = kind;
	}
	
	
}
