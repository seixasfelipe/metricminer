package br.com.caelum.revolution.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import model.Project;

@Entity
public class Artifact {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	@Enumerated(EnumType.STRING)
	private ArtifactKind kind;
	@ManyToMany(mappedBy = "artifacts")
	private List<Commit> commits;
	@OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
	private List<Modification> modifications;
	@ManyToOne
	private Project project;

	public Artifact() {
		modifications = new ArrayList<Modification>();
	}

	public Artifact(String name, ArtifactKind kind) {
		this();
		this.name = name;
		this.kind = kind;
	}

	public Artifact(String name, ArtifactKind kind, Project project) {
		this();
		this.name = name;
		this.kind = kind;
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArtifactKind getKind() {
		return kind;
	}

	public void setKind(ArtifactKind kind) {
		this.kind = kind;
	}

	public void addModification(Modification modification) {
		modification.setArtifact(this);
		modifications.add(modification);
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public List<Modification> getModifications() {
		return modifications;
	}

}
