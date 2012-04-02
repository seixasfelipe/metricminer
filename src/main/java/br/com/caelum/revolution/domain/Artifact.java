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
import model.SourceCode;

import org.hibernate.annotations.Index;

@Entity
public class Artifact {

	@Id
	@GeneratedValue
	private int id;
	@Index(name = "artifact_name")
	private String name;
	@Enumerated(EnumType.STRING)
	private ArtifactKind kind;
	@ManyToMany(mappedBy = "artifacts")
	private List<Commit> commits;
	@OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
	private List<Modification> modifications;
	@ManyToOne
	@Index(name = "artifact_project")
	private Project project;
	@OneToMany(mappedBy = "artifact")
	private List<SourceCode> sources;

	public Artifact() {
		modifications = new ArrayList<Modification>();
		sources = new ArrayList<SourceCode>();
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

	public void addSource(SourceCode source) {
		sources.add(source);
	}

}
