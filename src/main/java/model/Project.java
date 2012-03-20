package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.domain.Artifact;
import config.MetricMinerConfigs;

@Entity
public class Project {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String scmUrl;
	@OneToMany(mappedBy = "project")
	private List<ConfigurationEntry> configurationEntries;
	@OneToMany(mappedBy = "project")
	private List<Artifact> artifacts;
	@OneToMany(mappedBy = "project")
	private List<Task> tasks;
	private String scmRootDirectoryName;

	public Project() {
	}

	public Project(String name, String scmUrl) {
		this.name = name;
		this.scmUrl = scmUrl;
		this.configurationEntries = new ArrayList<ConfigurationEntry>();
	}

	public String getName() {
		return name;
	}

	public String getScmUrl() {
		return scmUrl;
	}

	public Long getId() {
		return id;
	}

	public List<ConfigurationEntry> getConfigurationEntries() {
		return configurationEntries;
	}

	public void setupInitialConfigurationsEntries() {
		this.configurationEntries = new ArrayList<ConfigurationEntry>();
		String metricMinerHome = MetricMinerConfigs.metricMinerHome;

		configurationEntries.add(new ConfigurationEntry("scm",
				"br.com.caelum.revolution.scm.git.GitFactory", this));
		configurationEntries.add(new ConfigurationEntry("scm.repository",
				metricMinerHome + "/projects/" + this.id + "/" + this.name,
				this));
		configurationEntries.add(new ConfigurationEntry("changesets",
				"br.com.caelum.revolution.changesets.AllChangeSetsFactory",
				this));
		configurationEntries
				.add(new ConfigurationEntry(
						"build",
						"br.com.caelum.revolution.builds.nobuild.NoBuildFactory",
						this));

	}

	public MapConfig getMapConfig() {
		HashMap<String, String> configurations = new HashMap<String, String>();
		for (ConfigurationEntry entry : this.configurationEntries)
			configurations.put(entry.getKey(), entry.getValue());
		return new MapConfig(configurations);
	}

	public List<Task> getTasks() {
		Collections.sort(tasks, new Comparator<Task>() {
			public int compare(Task o1, Task o2) {
				return o1.compareTo(o2);
			};
		});
		return tasks;
	}

	public String getScmRootDirectoryName() {
		return scmRootDirectoryName;
	}

	public void setScmRootDirectoryName(String scmRootDirectoryName) {
		this.scmRootDirectoryName = scmRootDirectoryName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
	}

}
