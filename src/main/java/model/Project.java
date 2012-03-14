package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.caelum.revolution.config.MapConfig;
import br.com.caelum.revolution.domain.Artifact;

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
		String metricMinerHome = "/home/csokol/ime/tcc/MetricMinerHome";

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
}
