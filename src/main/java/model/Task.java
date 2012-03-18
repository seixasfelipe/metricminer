package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Task {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Project project;
	private String name;
	@SuppressWarnings({ "rawtypes" })
	private Class runnableTaskFactoryClass;
	private Calendar submitDate;

	public Task() {
	}

	public Task(Project project, String name, Class taskRunnerClass) {
		this.project = project;
		this.name = name;
		this.runnableTaskFactoryClass = taskRunnerClass;
		this.submitDate = new GregorianCalendar();
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings({ "rawtypes" })
	public Class getRunnableTaskFactoryClass() {
		return runnableTaskFactoryClass;
	}

	public Project getProject() {
		return project;
	}

}
