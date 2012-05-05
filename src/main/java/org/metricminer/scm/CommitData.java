package org.metricminer.scm;

import java.util.ArrayList;
import java.util.List;

public class CommitData {
	private String commitId;
	private String author;
	private String email;
	private String date;
	private String message;
	private String diff;
	private List<DiffData> diffs;
	private String priorCommit;

	public CommitData() {
		diffs = new ArrayList<DiffData>();
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public void addDiff(DiffData diffData) {
		if (diffs == null) {
			diffs = new ArrayList<DiffData>();
		}
		diffs.add(diffData);
	}

	public List<DiffData> getDiffs() {
		return (diffs == null ? new ArrayList<DiffData>() : diffs);
	}

	public String getPriorCommit() {
		return priorCommit;
	}

	public void setPriorCommit(String priorCommit) {
		this.priorCommit = priorCommit;
	}

}
