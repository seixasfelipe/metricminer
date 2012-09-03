package org.metricminer.tasks.projectmetric.truckfactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.metricminer.model.Project;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.tasks.metric.common.MetricResult;
import org.metricminer.tasks.projectmetric.CalculateProjectMetricTaskFactory;
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
        int total = 0;
        Long artifactId = artifactsAndAuthors.get(0).getArtifactId();
        HashMap<Long, Integer> countByAuthorId = new HashMap<Long, Integer>();
        for (ArtifactAndAuthor artifactAndAuthor : artifactsAndAuthors) {
            Integer count = countByAuthorId
                    .get(artifactAndAuthor.getAuthorId());
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
        double percentage = (double) ((double)max/ (double) total);
        percentage *= 100;
        return new TruckFactorResult(percentage, artifactId, authorId, total);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        
        SessionFactory sf = new Configuration().configure()
                .buildSessionFactory();
        Session session = sf.openSession();
        Project p = (Project) session.load(Project.class, 1l);
        Task task = new TaskBuilder()
                .forProject(p)
                .withPosition(p.taskCount() + 1)
                .withRunnableTaskFactory(
                        new CalculateProjectMetricTaskFactory()).build();
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.PROJECT_METRIC_FACTORY_CLASS, TruckFactorFactory.class.getCanonicalName());
        p.addTask(task);
        session.getTransaction().begin();
        session.save(p);
        session.getTransaction().commit();
    }
}
