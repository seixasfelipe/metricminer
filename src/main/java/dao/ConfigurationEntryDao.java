package dao;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ConfigurationEntryDao {

	private Session session;

	public ConfigurationEntryDao(Session session) {
		this.session = session;
	}

}
