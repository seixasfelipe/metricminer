package org.metricminer.infra.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.metricminer.model.Query;
import org.metricminer.model.Task;
import org.metricminer.model.TaskBuilder;
import org.metricminer.model.TaskConfigurationEntryKey;
import org.metricminer.model.TaskStatus;
import org.metricminer.tasks.query.ExecuteQueryTaskFactory;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class TaskDao {

    private Session session;

    public TaskDao(Session session) {
        this.session = session;
    }

    public void save(Task task) {
        session.save(task);
    }

    @SuppressWarnings("rawtypes")
    public Task getFirstQueuedTask() {
        List tasks = session.createCriteria(Task.class)
                .add(Restrictions.eq("status", TaskStatus.QUEUED))
                .addOrder(Order.asc("submitDate"))
                .addOrder(Order.asc("position")).setMaxResults(1).list();
        if (tasks.isEmpty())
            return null;
        return (Task) tasks.get(0);

    }

    public void update(Task task) {
        session.saveOrUpdate(task);
    }

    @SuppressWarnings("unchecked")
    public List<Task> listTasks() {
        return session.createCriteria(Task.class).addOrder(Order.desc("endDate")).list();
    }

    @SuppressWarnings("unchecked")
    public List<Task> lastTasks(int total) {
        org.hibernate.Query query = session
                .createQuery(
                        "select task from Task as task join fetch task.project order by endDate desc")
                .setMaxResults(total);
        return query.list();

    }

    public void saveTaskToExecuteQuery(Query query) {
        Task task = new TaskBuilder()
                .withName("Execute query " + query.getName())
                .withRunnableTaskFactory(new ExecuteQueryTaskFactory()).build();
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.QUERY_ID,
                query.getId().toString());
        save(task);
    }
}
