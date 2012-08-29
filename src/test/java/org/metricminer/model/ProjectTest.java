package org.metricminer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.metricminer.tasks.metric.CalculateAllMetricsTaskFactory;
import org.metricminer.tasks.metric.cc.CCMetricFactory;
import org.metricminer.tasks.metric.lcom.LComMetricFactory;
import org.metricminer.tasks.metric.methods.MethodsCountMetricFactory;

public class ProjectTest {

	@Test
	public void shouldAddTags() {
		Project project = new Project();
		assertEquals(0, project.getTags().size());

		project.addTag(new Tag("mvc"));
		assertEquals(new Tag("mvc"), project.getTags().get(0));
	}

	@Test
	public void shouldRemoveTags() {
		Project project = new Project();
		project.addTag(new Tag("mvc"));
		project.removeTag("mvc");

		assertEquals(0, project.getTags().size());
	}
	
	@Test
	public void shouldAddACalculatedMetricProperly() throws Exception {
		Project project = new Project();
		project.addCalculatedMetric(new CalculatedMetric(project, CCMetricFactory.class));
		assertTrue(project.alreadyCalculated(new RegisteredMetric("cc", CCMetricFactory.class)));
	}

	@Test
	public void shouldReturnAvaiableMetrics() throws Exception {
		Project project = new Project();
		setupProject(project);

		List<RegisteredMetric> avaiable = project.avaiableMetricsToAddBasedOn(Arrays.asList(
				new RegisteredMetric("CC metric", LComMetricFactory.class), new RegisteredMetric(
						"CC metric", CCMetricFactory.class), new RegisteredMetric("CC metric",
						MethodsCountMetricFactory.class)));

		assertEquals(1, avaiable.size());
		assertEquals(new RegisteredMetric("cc", CCMetricFactory.class).getMetricFactoryClassName(),
				avaiable.get(0).getMetricFactoryClassName());
	}

	@Test
	public void shouldReturnNoneAvaiableMetrics() throws Exception {
		Project project = new Project();
		setupProject(project);

		List<RegisteredMetric> avaiable = project.avaiableMetricsToAddBasedOn(Arrays.asList(
				new RegisteredMetric("CC metric", LComMetricFactory.class), new RegisteredMetric(
						"CC metric", MethodsCountMetricFactory.class)));

		assertTrue(avaiable.isEmpty());
	}
	
	@Test
    public void shouldVerifyIfWillCalculateAllMetrics() throws Exception {
	    Project project = new Project();
	    Task calculateAllMetricsTask = new TaskBuilder().
	            withRunnableTaskFactory(new CalculateAllMetricsTaskFactory()).build();
        project.addTask(calculateAllMetricsTask);
        assertTrue(project.willCalculateAllMetrics());
    }
	
	@Test
	public void shouldNotConsiderThatWillCalculateAllMetricsIfThatTaskIsFinished() throws Exception {
	    Project project = new Project();
	    Task calculateAllMetricsTask = new TaskBuilder().
	            withRunnableTaskFactory(new CalculateAllMetricsTaskFactory()).build();
	    project.addTask(calculateAllMetricsTask);
	    calculateAllMetricsTask.setFinished();
	    assertFalse(project.willCalculateAllMetrics());
	}

	private void setupProject(Project project) {
		project.addCalculatedMetric(new CalculatedMetric(project, LComMetricFactory.class));
		project.addCalculatedMetric(new CalculatedMetric(project, MethodsCountMetricFactory.class));
	}

}
