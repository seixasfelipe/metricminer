package org.metricminer.tasks.projectmetric.truckfactor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.metricminer.model.Project;

public class TruckFactorDao {

    private final Session session;

    public TruckFactorDao(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ArtifactAndAuthor> listAllArtifactsByAuthorAndCommitForProject(Project p) {
        Query query = session
                .createSQLQuery("select a.id as artifact, au.id as author from Artifact a " +
                		"inner join Commit_Artifact ca on ca.artifacts_id = a.id " +
                		"inner join Commit c on c.id = ca.commits_id " +
                		"inner join Author au on au.id = c.author_id " +
                		"where a.project_id=:id and a.kind='CODE'");
        query.setParameter("id", p.getId());
        List<Object[]> objectsList = query.list();
        ArrayList<ArtifactAndAuthor> artifactsAndAuthors = new ArrayList<ArtifactAndAuthor>();
        for (Object[] tuple : objectsList) {
            artifactsAndAuthors.add(new ArtifactAndAuthor((Integer)tuple[0], (Integer)tuple[1]));
        }
        return artifactsAndAuthors;
    }
}



