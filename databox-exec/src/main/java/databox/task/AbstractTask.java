package databox.task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractTask {
	protected static final String SOURCE_ROOT_PATH = "D:/Class/task";	
	protected Process process = null;
	protected TaskInfo info;
	
	protected StringBuilder errorStringBuilder;
	protected StringBuilder messageStringBuilder;
	
	protected AbstractTask(TaskInfo info) {
		
		this.info = info;
		
		this.errorStringBuilder = new StringBuilder();
		this.messageStringBuilder = new StringBuilder();
	}
	
	public TaskInfo getTaskInfo()
	{
		return this.info;
	}
	
	public String getMessage() {
		syncMessage();
		return this.messageStringBuilder.toString();
	}
	
	public String getError() {
		syncMessage();
		return this.errorStringBuilder.toString();
	}
	
	public abstract boolean execute();
	
	
	public boolean isAlive() {
		return process.isAlive();
	}	
	
	public void destroy() {
		process.destroy();
	}
	
	public int exitCode() {
		return process.exitValue();
	}
	
	public void syncMessage() {
		try {
			BufferedReader msgReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "utf-8"));
			
			String msg = msgReader.readLine();
			while(msg != null) {
				messageStringBuilder.append(msg).append("\n");
				msg = msgReader.readLine();
			}
			String err = errReader.readLine();
			while(err != null) {
				errorStringBuilder.append(err).append("\n");
				err = errReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void debug() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while(process.isAlive()) {
			try {
				System.out.println(reader.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
