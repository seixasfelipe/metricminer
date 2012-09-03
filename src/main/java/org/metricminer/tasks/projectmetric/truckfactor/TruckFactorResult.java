package org.metricminer.tasks.projectmetric.truckfactor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class TruckFactorResult implements MetricResult {

    @Id @GeneratedValue
    private Long id;
    
    private final Long authorId;
    private final Long artifactId;
    private double percentage;

    @Transient
    private final int totalCommits;

    public TruckFactorResult(double percentage, Long artifactId, Long authorId, int totalCommits) {
        this.percentage = percentage;
        this.artifactId = artifactId;
        this.authorId = authorId;
        this.totalCommits = totalCommits;
    }

    public double getPercentage() {
        return percentage;
    }
    
    public Long getAuthorId() {
        return authorId;
    }

    public Long getArtifactId() {
        return artifactId;
    }
    
    public boolean isTruckFactor() {
        return totalCommits > 10 && percentage > 50.0;  
    }

    @Override
    public String toString() {
        return "TruckFactorResult [authorId=" + authorId + ", artifactId="
                + artifactId + "]";
    }
    
    
}
