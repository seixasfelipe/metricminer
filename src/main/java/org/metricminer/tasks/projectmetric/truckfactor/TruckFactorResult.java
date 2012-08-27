package org.metricminer.tasks.projectmetric.truckfactor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class TruckFactorResult implements MetricResult {

    @Id @GeneratedValue
    private Long id;
    
    private final boolean truckFactor;
    private final Long authorId;
    private final Long artifactId;

    public TruckFactorResult(boolean truckFactor, Long artifactId, Long authorId) {
        this.truckFactor = truckFactor;
        this.artifactId = artifactId;
        this.authorId = authorId;
    }
    
    public boolean isTruckFactor() {
        return truckFactor;
    }
    
    public Long getAuthorId() {
        return authorId;
    }

    public Long getArtifactId() {
        return artifactId;
    }

    @Override
    public String toString() {
        return "TruckFactorResult [authorId=" + authorId + ", artifactId="
                + artifactId + "]";
    }
    
    
}
