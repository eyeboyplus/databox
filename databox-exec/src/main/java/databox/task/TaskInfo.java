package databox.task;

public class TaskInfo {
	private String taskGroupName;
	private String taskName;
	private String target;
	private String description;
	
	public TaskInfo(String taskGroupName, String taskName, String target, String description) {
		super();
		this.taskGroupName = taskGroupName;
		this.taskName = taskName;
		this.target = target;
		this.description = description;
	}
 
	public String getTaskGroupName() {
		return taskGroupName;
	}

	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
