package databox.task;

public class TaskInfo {
    private String lang;
    private String uid;
    private String groupName;
	private String taskName;
	private String target;
	private String filePath;
	private String description;

	public TaskInfo(String taskGroupName, String taskName, String target, String description) {
		super();
		this.groupName = taskGroupName;
		this.taskName = taskName;
		this.target = target;
		this.description = description;
	}

	public TaskInfo() {

    }

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
