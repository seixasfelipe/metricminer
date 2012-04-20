package controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import model.Project;
import model.Task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import tasks.GitCloneTaskFactory;
import br.com.caelum.vraptor.util.test.MockResult;
import config.MetricMinerConfigs;
import dao.ProjectDao;
import dao.TaskDao;

public class TaskControllerTest {

	private TaskDao taskDao;
	private ProjectDao projectDao;
	private MockResult result;
	private TaskController controller;

	@Before
	public void setUp() {
		this.result = new MockResult();
		this.projectDao = mock(ProjectDao.class);
		this.taskDao = mock(TaskDao.class);
	}

	@Test
	public void shouldAddTaskToProject() throws ClassNotFoundException {
        Project p = new Project(new MetricMinerConfigs());
		ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
		when(projectDao.findProjectBy(1L)).thenReturn(p);
		controller = new TaskController(result, projectDao, taskDao);
		controller.addTaskTo(1L, "tasks.GitCloneTaskFactory", "clone");

		verify(taskDao).save(argument.capture());
		assertEquals(GitCloneTaskFactory.class, argument.getValue()
				.getRunnableTaskFactoryClass());
	}

	@Test
	public void shouldNotAddUnexistingTaskToProject() {
		controller = new TaskController(result, projectDao, taskDao);
		controller.addTaskTo(1L, "tasks.GitCloneTaskFactoy", "clone");
		verifyZeroInteractions(taskDao);
	}
}
