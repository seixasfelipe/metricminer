package org.metricminer.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.metricminer.stats.r.RNumberListGenerator;

@Entity
public class StatisticalTest {

	@Id @GeneratedValue
	private int id;
	private String name;
	private String algorithm;
	@ManyToOne(fetch=FetchType.EAGER)
	private User user;
	@Transient
	private RNumberListGenerator listGenerator;

	
	public StatisticalTest(String name, String algorithm, User user) {
		this.listGenerator = new RNumberListGenerator();
		this.name = name;
		this.algorithm = algorithm;
		this.user = user;
	}

	public StatisticalTest(int id) {
		this.id = id;
	}
	
	protected StatisticalTest() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public String getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	public String algorithmFor(List<Double> set1, List<Double> set2) {
		return algorithm
				.replace("#set1#", listGenerator.generate(set1))
				.replace("#set2#", listGenerator.generate(set2));
	}
	
}
