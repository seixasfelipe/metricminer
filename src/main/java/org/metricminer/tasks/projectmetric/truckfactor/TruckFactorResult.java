package org.metricminer.tasks.projectmetric.truckfactor;

import org.metricminer.tasks.metric.common.MetricResult;

public class TruckFactorResult implements MetricResult {

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
}
