package org.metricminer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;


@Entity
public class SourceCode {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Artifact artifact;
    @ManyToOne(fetch = FetchType.LAZY)
    private Commit commit;
    @Type(type = "text")
    private String source;
    @Index(name = "sourceSize_index")
    private Long sourceSize;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="sourceCode", cascade = CascadeType.ALL)
    private List<BlamedLine> blamedLines = new ArrayList<BlamedLine>();

    public SourceCode() {
    }

    public SourceCode(Artifact artifact, Commit commit, String source) {
        this.artifact = artifact;
        this.commit = commit;
        this.source = source;
        sourceSize = (long) source.length();
    }

    public String getSource() {
        return source;
    }

    public byte[] getSourceBytesArray() {
        return source.getBytes();
    }

    public String getName() {
        return artifact.getName();
    }

    public Commit getCommit() {
        return commit;
    }

	public List<BlamedLine> getBlamedLines() {
		return Collections.unmodifiableList(blamedLines);
	}
	
	public BlamedLine blame(int line, Author author) {
		BlamedLine blamedLine = new BlamedLine(author, line, this);
		blamedLines.add(blamedLine);
		return blamedLine;
	}

	public Long getId() {
		return id;
	}
	
	public Long getSourceSize() {
		return sourceSize;
	}
}
