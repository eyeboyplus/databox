package databox.task;

import java.io.IOException;

public class PyTask extends AbstractTask {
	public PyTask(TaskInfo info) {
		super(info);
	}

	@Override
	public boolean execute() {
		String cmd = "python " + SOURCE_ROOT_PATH + "/" + info.getTaskGroupName() + "/" + info.getTarget();
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
