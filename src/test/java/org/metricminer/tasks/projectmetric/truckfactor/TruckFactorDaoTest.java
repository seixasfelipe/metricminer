package org.metricminer.tasks.projectmetric.truckfactor;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.metricminer.infra.dao.DaoTest;
import org.metricminer.model.Artifact;
import org.metricminer.model.ArtifactKind;
import org.metricminer.model.Author;
import org.metricminer.model.Commit;
import org.metricminer.model.CommitMessage;
import org.metricminer.model.Diff;
import org.metricminer.model.Project;

public class TruckFactorDaoTest extends DaoTest {

    @Test
    public void shouldListAllArtifactsByCommitAndAuthor() throws Exception {
        session.getTransaction().begin();
        
        TruckFactorDao truckFactorDao = new TruckFactorDao(session);
        Project project = aProjectWithTwoArtifactsAndOneCommitFromOneAuthor();
        aProjectWithTwoArtifactsAndOneCommitFromOneAuthor();
        
        session.flush();

        List<ArtifactAndAuthor> artifactsAndAuthors = truckFactorDao.listAllArtifactsByAuthorAndCommitForProject(project);
        assertEquals(2, artifactsAndAuthors.size());
        
        session.getTransaction().rollback();
    }

    private Project aProjectWithTwoArtifactsAndOneCommitFromOneAuthor() {
        Project project = new Project();
        session.save(project);
        Artifact artifact1 = new Artifact("ClassA.java", ArtifactKind.CODE,
                project);
        session.save(artifact1);
        Artifact artifact2 = new Artifact("ClassB.java", ArtifactKind.CODE,
                project);
        session.save(artifact2);
        Author author1 = new Author();
        session.save(author1);
        Commit commit1 = new Commit("id", author1, Calendar.getInstance(),
                new CommitMessage("message"), new Diff("diff"), "prior",
                project);
        commit1.addArtifact(artifact1);
        commit1.addArtifact(artifact2);
        session.save(commit1);
        return project;
    }
}
