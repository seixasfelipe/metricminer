package org.metricminer;

public class MetricMinerExeption extends RuntimeException {

	public MetricMinerExeption() {
		super();
	}

	public MetricMinerExeption(Exception e) {
		super(e);
	}

	public MetricMinerExeption(String message, Exception e) {
		super(message, e);
	}

	public MetricMinerExeption(String message) {
		super(message);
	}

}
