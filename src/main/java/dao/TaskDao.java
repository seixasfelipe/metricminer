package dao;

import java.util.List;

import model.Task;
import model.TaskStatus;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
        List tasks = session.createCriteria(Task.class).add(
                Restrictions.eq("status", TaskStatus.QUEUED)).addOrder(Order.asc("position"))
                .setMaxResults(1).list();
        if (tasks.isEmpty())
            return null;
        return (Task) tasks.get(0);

    }

    public void update(Task task) {
        session.saveOrUpdate(task);
    }
}
