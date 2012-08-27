package org.metricminer.tasks.projectmetric.truckfactor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.metricminer.model.Project;

public class TruckFactorDao {

    private final Session session;

    public TruckFactorDao(Session session) {
        this.session = session;
    }

    public ArrayList<ArtifactAndAuthor> listAllArtifactsByAuthorAndCommitForProject(Project p) {
        @SuppressWarnings("deprecation")
        Connection c = session.connection();
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(
                    "select Artifact.id as artifact, Author.id as author from Artifact " +
                            "inner join Commit_Artifact ca on ca.artifacts_id = Artifact.id " +
                            "inner join Commit c on c.id = ca.commits_id " +
                            "inner join Author on Author.id = c.author_id " +
                            "where Artifact.project_id=? and Artifact.kind='CODE'"
            );
            ArrayList<ArtifactAndAuthor> artifactsAndAuthors = new ArrayList<ArtifactAndAuthor>();
            stmt.setLong(1, p.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ArtifactAndAuthor a = new ArtifactAndAuthor(Integer.valueOf(rs.getString("artifact")), Integer.valueOf(rs.getString("author")));
                artifactsAndAuthors.add(a);
            }
            return artifactsAndAuthors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        SessionFactory sf = new Configuration().configure()
                .buildSessionFactory();
        Session session = sf.openSession();
        Project p = (Project) session.load(Project.class, 1l);
        TruckFactorDao dao = new TruckFactorDao(session);
        dao.listAllArtifactsByAuthorAndCommitForProject(p);
    }
}



