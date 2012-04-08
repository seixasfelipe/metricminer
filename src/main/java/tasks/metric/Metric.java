package tasks.metric;

import java.io.InputStream;

public interface Metric {
	String header();
	String content(String path, String project);
	void calculate(InputStream is);
}
