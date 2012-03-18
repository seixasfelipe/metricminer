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
	private TaskStatus status;

	public Task() {
	}

	public Task(Project project, String name, Class taskRunnerClass) {
		this.project = project;
		this.name = name;
		this.runnableTaskFactoryClass = taskRunnerClass;
		this.submitDate = new GregorianCalendar();
		this.status = TaskStatus.QUEUED;
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

	public void start() {
		this.status = TaskStatus.STARTED;
	}

	public void finish() {
		this.status = TaskStatus.FINISHED;
	}

	@Override
	public String toString() {
		return this.name + " - " + this.status;
	}
}
