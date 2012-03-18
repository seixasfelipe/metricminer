package dao;

import model.Task;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

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

	public Task getNewestTask() {
		return (Task) session.createCriteria(Task.class)
				.addOrder(Order.desc("submitDate")).setMaxResults(1).list()
				.get(0);

	}
}
