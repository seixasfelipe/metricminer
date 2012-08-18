package org.metricminer.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

@Entity
public class Commit {
	@Id
	@GeneratedValue
	private int id;

	private String commitId;
	@ManyToOne
	private Author author;
	private Calendar date;
	@Type(type = "text")
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private CommitMessage message;
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Diff diff;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Artifact> artifacts;
	@OneToMany(mappedBy = "commit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Modification> modifications;
	private String priorCommitId;
	@OneToMany(mappedBy = "commit", fetch = FetchType.LAZY)
	private List<SourceCode> sources;
	@ManyToOne
	private Project project;

	public Commit(String commitId, Author author, Calendar date,
			CommitMessage message, Diff diff, String priorCommitId, Project project) {
		this();
		this.commitId = commitId;
		this.author = author;
		this.date = date;
		this.message = message;
		this.diff = diff;
		this.priorCommitId = priorCommitId;
        this.project = project;
	}

	public Commit() {
		this.artifacts = new ArrayList<Artifact>();
		this.modifications = new ArrayList<Modification>();
		this.sources = new ArrayList<SourceCode>();
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public CommitMessage getMessage() {
		return message;
	}

	public void setMessage(CommitMessage message) {
		this.message = message;
	}

	public Diff getDiff() {
		return diff;
	}

	public void setDiff(Diff diff) {
		this.diff = diff;
	}

	public int getId() {
		return id;
	}

	public void addArtifact(Artifact artifact) {
		if (artifacts == null) {
			artifacts = new ArrayList<Artifact>();
		}
		artifacts.add(artifact);
	}

	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public List<Modification> getModifications() {
		return modifications;
	}

	public void addModification(Modification modification) {
		modification.setCommit(this);
		modifications.add(modification);
	}

	public String getPriorCommitId() {
		return priorCommitId;
	}

	public void setPriorCommit(String priorCommitId) {
		this.priorCommitId = priorCommitId;
	}

	public void addSource(SourceCode source) {
		sources.add(source);
	}

	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return "Commit [id=" + id + "]";
	}
	
}
