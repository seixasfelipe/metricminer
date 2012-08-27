package org.metricminer.tasks.projectmetric.truckfactor;

public class ArtifactAndAuthor {

    private Long artifactId;
    private Long authorId;

    public ArtifactAndAuthor(Integer artifactId, Integer authorId) {
        this.artifactId = Long.valueOf(artifactId);
        this.authorId = Long.valueOf(authorId);
    }

    public Long getArtifactId() {
        return artifactId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result
                + ((authorId == null) ? 0 : authorId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArtifactAndAuthor other = (ArtifactAndAuthor) obj;
        if (artifactId == null) {
            if (other.artifactId != null)
                return false;
        } else if (!artifactId.equals(other.artifactId))
            return false;
        if (authorId == null) {
            if (other.authorId != null)
                return false;
        } else if (!authorId.equals(other.authorId))
            return false;
        return true;
    }

    public boolean sameArtifact(ArtifactAndAuthor nextArtifact) {
        return this.artifactId == nextArtifact.getArtifactId();
    }

    @Override
    public String toString() {
        return "ArtifactAndAuthor [artifactId=" + artifactId + ", authorId="
                + authorId + "]";
    }

    
}
