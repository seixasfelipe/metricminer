package org.metricminer.tasks.projectmetric.truckfactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.metricminer.model.Project;
import org.metricminer.tasks.metric.common.MetricResult;
import org.metricminer.tasks.projectmetric.common.ProjectMetric;

public class TruckFactor implements ProjectMetric {

    private final Project project;
    private final TruckFactorDao dao;

    public TruckFactor(TruckFactorDao dao, Project project) {
        this.dao = dao;
        this.project = project;
    }

    @Override
    public List<MetricResult> calculate() {
        ArrayList<ArtifactAndAuthor> artifacts = dao
                .listAllArtifactsByAuthorAndCommitForProject(project);

        HashMap<Long, List<ArtifactAndAuthor>> artifactsAndAndAuthorByArtifactId = artifactsAndAuthorsByArtifactId(artifacts);

        List<MetricResult> results = findTruckFactors(artifactsAndAndAuthorByArtifactId);

        return results;
    }

    private List<MetricResult> findTruckFactors(
            HashMap<Long, List<ArtifactAndAuthor>> artifactsAndAndAuthorByArtifactId) {
        List<MetricResult> results = new ArrayList<MetricResult>();
        Set<Long> artifactsIds = artifactsAndAndAuthorByArtifactId.keySet();
        for (Long id : artifactsIds) {
            List<ArtifactAndAuthor> artifactsAndAuthors = artifactsAndAndAuthorByArtifactId
                    .get(id);
            TruckFactorResult result = isTruckFactor(artifactsAndAuthors);
            if (result.isTruckFactor()) {
                results.add(result);
            }
        }
        return results;
    }

    private HashMap<Long, List<ArtifactAndAuthor>> artifactsAndAuthorsByArtifactId(
            ArrayList<ArtifactAndAuthor> artifacts) {
        HashMap<Long, List<ArtifactAndAuthor>> artifactsAndAndAuthorByArtifactId = new HashMap<Long, List<ArtifactAndAuthor>>();
        for (ArtifactAndAuthor artifactAndAuthor : artifacts) {
            List<ArtifactAndAuthor> l = artifactsAndAndAuthorByArtifactId
                    .get(artifactAndAuthor.getArtifactId());
            if (l == null) {
                l = new ArrayList<ArtifactAndAuthor>();
            }
            l.add(artifactAndAuthor);
            artifactsAndAndAuthorByArtifactId.put(
                    artifactAndAuthor.getArtifactId(), l);
        }
        return artifactsAndAndAuthorByArtifactId;
    }

    public TruckFactorResult isTruckFactor(
            List<ArtifactAndAuthor> artifactsAndAuthors) {
        int total= 0;
        Long artifactId = artifactsAndAuthors.get(0).getArtifactId();
        HashMap<Long, Integer> countByAuthorId = new HashMap<Long, Integer>();
        for (ArtifactAndAuthor artifactAndAuthor : artifactsAndAuthors) {
            Integer count = countByAuthorId.get(artifactAndAuthor.getAuthorId());
            if (count == null) {
                count = 0;
            }
            count++;
            countByAuthorId.put(artifactAndAuthor.getAuthorId(), count);
            total++;
        }
        Set<Long> ids = countByAuthorId.keySet();
        Integer max = 0;
        Long authorId = -1l;
        for (Long id : ids) {
            Integer count = countByAuthorId.get(id);
            if (count > max) {
                authorId = id;
                max = count; 
            }
        }
        boolean isTruckFactor = false;
        if (max * 2 > total && total > 10) {
            isTruckFactor = true;
        }
        return new TruckFactorResult(isTruckFactor, artifactId, authorId);
    }

}
