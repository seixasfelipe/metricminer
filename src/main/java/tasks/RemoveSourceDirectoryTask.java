package tasks;

import model.Project;
import br.com.caelum.revolution.executor.CommandExecutor;

public class RemoveSourceDirectoryTask implements RunnableTask {

	private final Project project;
	private final CommandExecutor exec;

	public RemoveSourceDirectoryTask(Project project, CommandExecutor exec) {
		this.project = project;
		this.exec = exec;
	}

	@Override
	public void run() {
		String localPath = project.getLocalPath();
		exec.execute("rm -rf " + localPath, localPath);
	}
}
