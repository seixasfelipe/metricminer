package tasks;

import org.junit.Test;

public class TasksRunnerTest {

	@Test
	public void shouldRunAValidTask() throws Exception {
		TasksRunner runner = new TasksRunner();
		runner.execute();
	}
}
