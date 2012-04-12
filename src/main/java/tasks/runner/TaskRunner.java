package tasks.runner;

import model.Task;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import dao.TaskDao;

@PrototypeScoped
@Scheduled(cron = "0/10 * * * * ?")
public class TaskRunner implements br.com.caelum.vraptor.tasks.Task {

    private static Logger log = Logger.getLogger(TaskRunner.class);
    private TaskDao taskDao;
    private Task taskToRun;
    private Session session;

    @Override
    public void execute() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        taskDao = new TaskDao(session);
        tx.commit();
        taskToRun = taskDao.getFirstQueuedTask();
        if (taskToRun != null) {
            if (!taskToRun.isDependenciesFinished()) {
                log.info("Waiting for task to finish...");
                closeSessions();
                return;
            }
            log.info("Starting task: " + taskToRun.getName());
            taskToRun.start();
            taskDao.update(taskToRun);
            try {
                runTask(session);
            } catch (Exception e) {
                handleError(e);
            } finally {
                closeSessions();
            }
        }
        closeSessions();
    }

    private void runTask(Session taskSession) throws InstantiationException, IllegalAccessException {
        RunnableTaskFactory runnableTaskFactory = (RunnableTaskFactory) taskToRun
                .getRunnableTaskFactoryClass().newInstance();
        taskSession.beginTransaction();
        runnableTaskFactory.build(taskToRun, taskSession).run();
        Transaction transaction = taskSession.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        finishTask(transaction);
    }

    private void finishTask(Transaction transaction) {
        taskToRun.finish();
        taskDao.update(taskToRun);
        transaction.commit();
        log.info("Finished running task: " + taskToRun.getName());
    }

    private void handleError(Exception e) {
        taskToRun.fail();
        taskDao.update(taskToRun);
        log.error("Error when running a task", e);
    }

    private void closeSessions() {
        if (session.isOpen()) {
            session.disconnect();
            session.close();
        }
    }

}
