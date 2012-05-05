package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class BlamedLine {
	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	private Author author;
	private int line;
	@ManyToOne
	private SourceCode sourceCode;

	public BlamedLine(Author author, int line, SourceCode sourceCode) {
		this.author = author;
		this.line = line;
		this.sourceCode = sourceCode;
	}

	protected BlamedLine() { }

	public int getId() {
		return id;
	}

	public Author getAuthor() {
		return author;
	}

	public int getLine() {
		return line;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

}
