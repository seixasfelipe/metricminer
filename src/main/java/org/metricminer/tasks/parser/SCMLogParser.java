package org.metricminer.tasks.parser;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;
import org.metricminer.changesets.ChangeSet;
import org.metricminer.changesets.ChangeSetCollection;
import org.metricminer.model.PersistedCommitConverter;
import org.metricminer.model.Project;
import org.metricminer.scm.CommitData;
import org.metricminer.scm.SCM;


public class SCMLogParser {

    private Session session;
    private PersistedCommitConverter converter;
    private SCM scm;
    private final ChangeSetCollection collection;
    private static Logger log = Logger.getLogger(SCMLogParser.class);
    private final Project project;

    public SCMLogParser(PersistedCommitConverter converter, SCM scm,
            ChangeSetCollection collection, Session session, Project project) {
        this.converter = converter;
        this.scm = scm;
        this.collection = collection;
        this.session = session;
        this.project = project;
    }

    public void start() {
        for (ChangeSet changeSet : collection.get()) {
            log.info("--------------------------");
            CommitData commitData;
            session.clear();
            Transaction transaction = session.getTransaction();
            if (!transaction.isActive()) {
                session.beginTransaction();
            }
            try {
                commitData = scm.detail(changeSet.getId());
                log.info("Persisting change set " + changeSet.getId());
                log.info("Author: " + commitData.getAuthor() + " on " + commitData.getDate());
                converter.toDomain(commitData, session, project);
                session.getTransaction().commit();
            } catch (ParseException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
            	log.error("Too big changeset " + changeSet.getId() + " in project " + project.getName() + ", out of memory", e);
                session.getTransaction().rollback();
                commitData = null;
                System.gc();
            } catch (GenericJDBCException e) {
                session.getTransaction().rollback();
                log.error("Too big changeset, unable to persist", e);
                commitData = null;
                System.gc();
            } catch(Throwable e) {
            	//morre geral
            }

        }
        log.info("");
        log.info("--------------------------");
        log.info("Finished persisting commit data.");
    }
}
