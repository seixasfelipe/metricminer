package org.metricminer.sandbox;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.junit.Ignore;
import org.junit.Test;
import org.metricminer.model.Commit;
import org.metricminer.model.SourceCode;


public class Sandbox {
    @Test
    public void testStatelessSession() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        StatelessSession statelessSession = sf.openStatelessSession();
        Query query = statelessSession.createQuery("select source from SourceCode source "
                + "join source.artifact as artifact where artifact.project.id = :project_id");
        query.setParameter("project_id", 1L);
        ScrollableResults results = query.scroll();
        while (results.next()) {
            SourceCode source = (SourceCode) results.get(0);
            System.out.println(source.getName());
        }
    }

    @Test
    public void testCommitersCount() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        Query query = session
                .createQuery("select count(id) from Author where id in (select author.id from Commit commit "
                        + "join commit.author author where commit.project.id = :id "
                        + "group by author.id)");

        query.setParameter("id", 4l);

        ScrollableResults results = query.scroll();
        while (results.next()) {
            Long count = (Long) results.get(0);
            System.out.println(count);
        }
    }

    @Test
    public void testCommitCountForInterval() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        Query query = session
                .createQuery("select count(id) from Commit as commit "
                        + "where commit.project.id=:id and (commit.date >= :start AND commit.date <= :end)");

        query.setParameter("id", 4l);
        query.setParameter("start", new GregorianCalendar(2012, 03, 24));
        query.setParameter("end", new GregorianCalendar(2012, 03, 26));

        ScrollableResults results = query.scroll();
        while (results.next()) {
            Long count = (Long) results.get(0);
            System.out.println(count);
        }
    }
    
    @Test
    public void testCommitFileCountForInterval() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        Query query = session
                .createQuery("select count(source.id),commit from SourceCode as source "
                        + " join source.commit as commit "
                        + "where commit.project.id=:id and (commit.date >= :start AND commit.date <= :end) " +
                        "group by commit.id");
        
        query.setParameter("id", 1l);
        query.setParameter("start", new GregorianCalendar(2012, 01, 24));
        query.setParameter("end", new GregorianCalendar(2012, 03, 26));
        
        ScrollableResults results = query.scroll();
        while (results.next()) {
            Long count = (Long) results.get(0);
            Commit commit = (Commit) results.get(1);
            System.out.println(count);
            System.out.println(commit.getCommitId());
        }
    }

    @Test
    public void testDateIntervals() throws Exception {

        int lastMonth = 3;
        int lastYear = 2011;

        GregorianCalendar lastDate = new GregorianCalendar(lastYear, lastMonth, 1);

        GregorianCalendar start = lastDate;

        for (int i = 0; i < 6; i++) {
            int startMonth = start.get(Calendar.MONTH);
            int startYear = start.get(Calendar.YEAR);
            int nextMonth, nextYear;
            GregorianCalendar end = new GregorianCalendar(startYear, startMonth,
                    start.getActualMaximum(Calendar.DAY_OF_MONTH));

            System.out.println(start.get(Calendar.MONTH) + "/" + start.get(Calendar.YEAR) + "/" + start.get(Calendar.DAY_OF_MONTH) + " - "
                    + end.get(Calendar.MONTH) + "/" + end.get(Calendar.YEAR) + "/" + end.get(Calendar.DAY_OF_MONTH));

            if (startMonth == 1) {
                nextMonth = 12;
                nextYear = startYear - 1;
            } else {
                nextMonth = startMonth - 1;
                nextYear = startYear;
            }
            //System.out.println(nextYear + "/" + nextMonth);
            start = new GregorianCalendar(nextYear, nextMonth, 1);

        }

    }

    @Ignore
    @Test
    public void testFirstCommit() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        Query query = session.createQuery("select commit From Commit as commit where "
                + "commit.project.id=:id order by date asc ");

        query.setMaxResults(1);

        query.setParameter("id", 4l);

        Commit commit = (Commit) query.uniqueResult();

        System.out.println(commit.getDate().get(Calendar.YEAR));
    }

}
