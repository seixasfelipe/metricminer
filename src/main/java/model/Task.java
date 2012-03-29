package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@SuppressWarnings("rawtypes")
@Entity
public class Task implements Comparable {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Project project;
	private String name;
	private Class runnableTaskFactoryClass;
	private Calendar submitDate;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	private Integer position;

	public Task() {
	}

	public Task(Project project, String name, Class runnableTaskFactoryClass,
			Integer position) {
		this.project = project;
		this.name = name;
		this.runnableTaskFactoryClass = runnableTaskFactoryClass;
		this.submitDate = new GregorianCalendar();
		this.status = TaskStatus.QUEUED;
		this.position = position;
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

	public void fail() {
		this.status = TaskStatus.FAILED;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public Calendar getSubmitDate() {
		return submitDate;
	}

	@Override
	public int compareTo(Object o) {
		return ((Task) o).getPosition().compareTo(this.position);
	}

	public Integer getPosition() {
		return position;
	}
}
