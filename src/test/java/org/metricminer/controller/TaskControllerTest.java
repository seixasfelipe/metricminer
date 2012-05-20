package org.metricminer.controller;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.metricminer.dao.ProjectDao;
import org.metricminer.dao.TaskDao;

import br.com.caelum.vraptor.util.test.MockResult;

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

}
