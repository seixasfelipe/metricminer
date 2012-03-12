package dao;

import java.util.HashMap;
import java.util.Map.Entry;

import model.Project;
import model.ConfigurationEntry;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ConfigurationEntryDao {

	private Session session;

	public ConfigurationEntryDao(Session session) {
		this.session = session;
	}

	public void saveInitialProjectConfigurations(Project p) {
		HashMap<String, String> entries = p.listInitialConfigurationsEntries();
		for (Entry<String, String> entry : entries.entrySet()) {
			ConfigurationEntry configurationEntry = new ConfigurationEntry(
					entry.getKey(), entry.getValue(), p);
			session.save(configurationEntry);
		}

	}
}
