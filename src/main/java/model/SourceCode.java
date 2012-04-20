package model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import br.com.caelum.revolution.domain.Artifact;
import br.com.caelum.revolution.domain.Commit;

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

    public SourceCode() {
    }

    public SourceCode(Artifact artifact, Commit commit, String source) {
        this.artifact = artifact;
        this.commit = commit;
        this.source = source;
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
}
