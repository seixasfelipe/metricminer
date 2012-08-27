package org.metricminer.tasks.projectmetric.truckfactor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.metricminer.model.Project;
import org.metricminer.tasks.metric.common.MetricResult;

public class TruckFactorTest {
    
    private TruckFactorDao dao;
    private Project project;
    private TruckFactor truckFactor;

    @Before
    public void setUp() {
        dao = mock(TruckFactorDao.class);
        project = mock(Project.class);
        truckFactor = new TruckFactor(dao, project);
    }
    
    @Test
    public void shouldFindTwoTruckFactors() {
        // two truck factors
        ArrayList<ArtifactAndAuthor> artifacts = xArtifactsOfSomeAuthor(11, 10, 1);
        artifacts.addAll(xArtifactsOfSomeAuthor(11, 20, 2));
        artifacts.addAll(xArtifactsOfSomeAuthor(10, 20, 3));
        
        // not truck factor
        artifacts.addAll(xArtifactsOfSomeAuthor(9, 30, 3));
        
        // not truck factor
        artifacts.addAll(xArtifactsOfSomeAuthor(4, 40, 4));
        artifacts.addAll(xArtifactsOfSomeAuthor(4, 40, 5));
        artifacts.addAll(xArtifactsOfSomeAuthor(4, 40, 6));
        
        when(project.getId()).thenReturn(1l);
        when(dao.listAllArtifactsByAuthorAndCommitForProject(project)).thenReturn(artifacts);
        
        List<MetricResult> results = truckFactor.calculate();
        assertEquals(2, results.size());
        
        TruckFactorResult result = (TruckFactorResult) results.get(1);
        assertEquals(10l, (long)result.getArtifactId());
        assertEquals(1l, (long)result.getAuthorId());
        
        result = (TruckFactorResult) results.get(0);
        assertEquals(20l, (long)result.getArtifactId());
        assertEquals(2l, (long)result.getAuthorId());
    }

    @Test
    public void shouldFindTruckFactor() throws Exception {
        ArrayList<ArtifactAndAuthor> artifacts = xArtifactsOfSomeAuthor(11, 1, 1);
        artifacts.addAll(xArtifactsOfSomeAuthor(10, 1, 2));
        TruckFactorResult result = truckFactor.isTruckFactor(artifacts);
        
        assertTrue(result.isTruckFactor());
        assertEquals(Long.valueOf(1l), result.getAuthorId());
        assertEquals(Long.valueOf(1l), result.getArtifactId());
    }

    private ArrayList<ArtifactAndAuthor> xArtifactsOfSomeAuthor(int count, Integer artifact, Integer authorId) {
        ArrayList<ArtifactAndAuthor> artifacts = new ArrayList<ArtifactAndAuthor>();
        for (int i = 0; i < count; i++) {
            artifacts.add(new ArtifactAndAuthor(artifact, authorId));
        }
        return artifacts;
    }

}
