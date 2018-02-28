package databox.task;

import java.io.IOException;

import databox.util.FileHelper;
public class JTask extends AbstractTask {
	public JTask(TaskInfo info) {
		super(info);
	}
	
	@Override
	public boolean execute() {
//		String fileSeparator = System.getProperty("file.separator");
		String fileSeparator = "/";
		String pathSeparator = System.getProperty("path.separator");
		String cmd = "java -classpath " 
		+ TaskConfig.taskRootPath + fileSeparator + info.getTaskGroupName() + fileSeparator + pathSeparator
		+ TaskConfig.taskRootPath + fileSeparator + info.getTaskGroupName() + fileSeparator + "*" + pathSeparator 
		+ TaskConfig.taskLibPath + fileSeparator + "*" + pathSeparator 
		+ " " + FileHelper.getFileName(info.getTarget());
		System.out.println(cmd);
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
