package dao;

import model.Task;

import org.hibernate.Session;

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

}
