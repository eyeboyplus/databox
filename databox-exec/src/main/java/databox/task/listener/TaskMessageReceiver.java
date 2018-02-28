package databox.task.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dom4j.DocumentException;

import databox.task.AbstractTask;
import databox.task.TaskConfig;
import databox.task.TaskListXmlParser;
import databox.util.FileHelper;

class TaskMessageReceiveThread extends Thread {
	
	private Map<String, AbstractTask> workingTask;
	
	public TaskMessageReceiveThread(Map<String, AbstractTask> workingTask) {
		this.workingTask = workingTask;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Set<String> keys = workingTask.keySet();
			Iterator<String> keysIter = keys.iterator();
			while(keysIter.hasNext()) {
				AbstractTask task = workingTask.get(keysIter.next());
				if(task.isAlive()) {
					task.syncMessage();
				}
			}
		}	
	}
}

public class TaskMessageReceiver implements ServletContextListener {
	private TaskMessageReceiveThread taskMsgReceiveThread;
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		TaskListXmlParser tasklist = (TaskListXmlParser) event.getServletContext().getAttribute("tasklist");
		String tasklistXmlFile = TaskConfig.taskRootPath + "/" + "tasklist.xml";
		tasklist.saveToFile(tasklistXmlFile);
		
		if(taskMsgReceiveThread != null && taskMsgReceiveThread.isInterrupted()) {
			taskMsgReceiveThread.interrupt();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		TaskListXmlParser taskList = null;
		try {
			String tasklistXmlFile = TaskConfig.taskRootPath + "/" + "tasklist.xml";
			if(!FileHelper.exists(tasklistXmlFile)) {
				String tasklistTemplateFile = this.getClass().getClassLoader().getResource("/").getPath().replace("classes/", "tasklist.xml");
				FileHelper.copy(tasklistTemplateFile, tasklistXmlFile);
			}
				
			taskList = new TaskListXmlParser(tasklistXmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		event.getServletContext().setAttribute("tasklist", taskList);
		
		if(taskMsgReceiveThread == null) {
			Map<String, AbstractTask> workingTask = (Map<String, AbstractTask>) event.getServletContext().getAttribute("woringTask");
			if(workingTask == null) {
				workingTask = new HashMap<String, AbstractTask>();
				event.getServletContext().setAttribute("workingTask", workingTask);
			}
			taskMsgReceiveThread = new TaskMessageReceiveThread(workingTask);
			taskMsgReceiveThread.start();
		}
	}

}
