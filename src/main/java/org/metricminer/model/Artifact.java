package org.metricminer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Artifact {

    @Id
    @GeneratedValue
    private int id;
    @Index(name = "name_index")
    private String name;
    @Enumerated(EnumType.STRING)
    private ArtifactKind kind;
    @ManyToMany(mappedBy = "artifacts", fetch = FetchType.LAZY)
    private List<Commit> commits;
    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
    private List<Modification> modifications;
    @ManyToOne
    @Index(name = "project_index")
    private Project project;
    @OneToMany(mappedBy = "artifact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public List<SourceCode> getSources() {
        return sources;
    }
    public Project getProject() {
		return project;
	}
    
    public boolean isSourceCode() {
    	return getKind() != ArtifactKind.BINARY;
    }
    
    public int getCommitCount() {
        return commits.size();
    }
}
