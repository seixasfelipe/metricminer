package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tasks.CalculateMetricTaskFactory;

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
    public void shouldReturnAvaiableMetrics() throws Exception {
        Project project = new Project();
        setupProject(project);

        List<RegisteredMetric> avaiable = project.avaiableMetricsToAddBasedOn(Arrays.asList(
                new RegisteredMetric("CC metric", "classname1"), new RegisteredMetric("CC metric",
                        "classname2"), new RegisteredMetric("CC metric", "classname3")));

        assertEquals(1, avaiable.size());
        assertEquals("classname3", avaiable.get(0).getMetricFactoryClass());
    }
    
    @Test
    public void shouldReturnNoneAvaiableMetrics() throws Exception {
        Project project = new Project();
        setupProject(project);

        List<RegisteredMetric> avaiable = project.avaiableMetricsToAddBasedOn(Arrays.asList(
                new RegisteredMetric("CC metric", "classname1"), new RegisteredMetric("CC metric",
                        "classname2")));

        assertTrue(avaiable.isEmpty());
    }

    private void setupProject(Project project) {
        Task task = new Task(project, "calculate", CalculateMetricTaskFactory.class, 1);
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS, "classname1");
        project.addTask(task);
        task = new Task(project, "calculate", CalculateMetricTaskFactory.class, 2);
        task.addTaskConfigurationEntry(TaskConfigurationEntryKey.METRICFACTORYCLASS, "classname2");
        project.addTask(task);
    }

    
}
