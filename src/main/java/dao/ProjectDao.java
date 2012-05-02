package dao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Project;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
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
        Query query = session.createQuery("select count(id) from Author where id "
                + "in (select author.id from Commit commit join commit.author author "
                + "where commit.project.id = :id group by author.id)");
        query.setParameter("id", project.getId());
        return (Long) query.uniqueResult();
    }

    public Commit firstCommitFor(Project project) {
        Query query = session.createQuery("select commit From Commit as commit where "
                + "commit.project.id=:id order by date asc ");
        query.setMaxResults(1);
        query.setParameter("id", project.getId());
        Commit commit = (Commit) query.uniqueResult();
        return commit;
    }

    public Commit lastCommitFor(Project project) {
        Query query = session.createQuery("select commit From Commit as commit where "
                + "commit.project.id=:id order by date desc ");
        query.setMaxResults(1);
        query.setParameter("id", project.getId());
        Commit commit = (Commit) query.uniqueResult();
        return commit;
    }

    public Map<Calendar, Long> commitCountForLastMonths(Project project) {
        Map<Calendar, Long> map = new HashMap<Calendar, Long>();
        Commit lastCommit = lastCommitFor(project);
        if (lastCommit == null)
            return map;
        int totalMonths = 12;

        int lastMonth = lastCommit.getDate().get(Calendar.MONTH);
        int lastYear = lastCommit.getDate().get(Calendar.YEAR);

        GregorianCalendar start = new GregorianCalendar(lastYear, lastMonth, 1, 0, 0, 0);

        for (int i = 0; i < totalMonths; i++) {
            int nextMonth, nextYear;
            int startMonth = start.get(Calendar.MONTH);
            int startYear = start.get(Calendar.YEAR);
            GregorianCalendar end = new GregorianCalendar(startYear, startMonth,
                    start.getActualMaximum(Calendar.DAY_OF_MONTH), 24, 59, 59);

            Long count = getCommitCountForInterval(project, start, end);
            
            map.put(start, count);

            if (startMonth == 1) {
                nextMonth = 12;
                nextYear = startYear - 1;
            } else {
                nextMonth = startMonth - 1;
                nextYear = startYear;
            }
            start = new GregorianCalendar(nextYear, nextMonth, 1);
        }

        return map;
    }
    
    public Map<Commit, Long> fileCountPerCommitForLastSixMonths(Project project) {
        Commit lastCommit = lastCommitFor(project);
        if (lastCommit == null)
            return new HashMap<Commit, Long>();

        int lastMonth = lastCommit.getDate().get(Calendar.MONTH);
        int lastYear = lastCommit.getDate().get(Calendar.YEAR);

        Calendar end = lastCommit.getDate();
        
        int startMonth = lastMonth - 6;
        int startYear = lastYear; 
        if (startMonth <= 0) {
            startMonth += 12;
            startYear--;
        }
        
        Calendar start = new GregorianCalendar(startYear, startMonth, 1);
        
        return fileCountPerCommitByInterval(end, start);
    }

    private Map<Commit, Long> fileCountPerCommitByInterval(Calendar end, Calendar start) {
        Map<Commit, Long> map = new HashMap<Commit, Long>();
        Query query = session
                .createQuery("select count(source.id),commit from SourceCode as source "
                        + " join source.commit as commit "
                        + "where commit.project.id=:id and (commit.date >= :start AND commit.date <= :end) " +
                        "group by commit.id");
        
        query.setParameter("id", 1l);
        query.setParameter("start", start);
        query.setParameter("end", end);
        
        ScrollableResults results = query.scroll();
        
        while (results.next()) {
            Long count = (Long) results.get(0);
            Commit commit = (Commit) results.get(1);
            map.put(commit, count);
        }
        
        return map;
    }

    private Long getCommitCountForInterval(Project project, GregorianCalendar start,
            GregorianCalendar end) {
        Query query = session
                .createQuery("select count(id) from Commit as commit "
                        + "where commit.project.id=:id and (commit.date >= :start AND commit.date <= :end)");
        query.setParameter("id", project.getId());
        query.setParameter("start", start);
        query.setParameter("end", end);
        Long count = (Long) query.uniqueResult();
        return count;
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
