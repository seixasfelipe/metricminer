package model;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String scmUrl;

	public Project() {
	}

	public Project(String name, String scmUrl) {
		this.name = name;
		this.scmUrl = scmUrl;
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

	public HashMap<String, String> listInitialConfigurationsEntries() {
		HashMap<String, String> configurations = new HashMap<String, String>();
		String metricMinerHome = "/home/csokol/ime/tcc/MetricMinerHome";

		configurations
				.put("scm", "br.com.caelum.revolution.scm.git.GitFactory");
		configurations.put("scm.repository", metricMinerHome + "/" + this.id
				+ "/" + this.name);
		configurations.put("changesets",
				"br.com.caelum.revolution.changesets.AllChangeSetsFactory");
		configurations.put("build",
				"br.com.caelum.revolution.builds.nobuild.NoBuildFactory");
		configurations.put("build",
				"br.com.caelum.revolution.builds.nobuild.NoBuildFactory");

		return configurations;
	}
}
