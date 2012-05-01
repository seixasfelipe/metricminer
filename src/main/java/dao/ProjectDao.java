package dao;

import java.util.List;

import model.Project;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ProjectDao {
    private final Session session;

    public ProjectDao(Session session) {
        this.session = session;
    }

    public void save(Project project) {
        session.save(project);
        project.setupInitialConfigurationsEntries();
        session.save(project);
    }

    @SuppressWarnings("unchecked")
    public List<Project> listAll() {
        return session.createCriteria(Project.class).list();
    }

    public Project findProjectBy(Long id) {
        return (Project) session.load(Project.class, id);
    }
    
    public Long commitCountFor(Project project) {
        Query query = session.createQuery("select count(commit.id) from Commit commit "
                + "join commit.project as project where project.id = :project_id");
        query.setParameter("project_id", project.getId());
        return (Long) query.uniqueResult();
    }
    
    public Long commitersCountFor(Project project) {
        Query query = session.createQuery("select count(id) from Author where id " +
        		"in (select author.id from Commit commit join commit.author author " +
        		"where commit.project.id = :id group by author.id)");
        query.setParameter("id", project.getId());
        return (Long) query.uniqueResult();
    }
    
    public Commit firstCommitFor(Project project) {
        Query query = session.createQuery("select commit From Commit as commit where " +
                "commit.project.id=:id order by date asc ");
        query.setMaxResults(1);
        query.setParameter("id", project.getId());
        Commit commit = (Commit) query.uniqueResult();
        return commit;
    }
    
    public Commit lastCommitFor(Project project) {
        Query query = session.createQuery("select commit From Commit as commit where " +
                "commit.project.id=:id order by date desc ");
        query.setMaxResults(1);
        query.setParameter("id", project.getId());
        Commit commit = (Commit) query.uniqueResult();
        return commit;
    }

    public Session getSession() {
        return this.session;
    }

	public void update(Project project) {
		session.update(project);
	}

    public void delete(Long id) {
        Project project = findProjectBy(id);
        session.delete(project);
    }
    
}
