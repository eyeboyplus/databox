package databox.task.listener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import databox.task.TaskConfig;

public class InitGlobalParamListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties properties = new Properties();
		try {
			String fileName = this.getClass().getClassLoader().getResource("/").getPath().replace("classes/", "task-cfg.properties");
			System.out.println(fileName);
			properties.load(new BufferedInputStream(new FileInputStream(fileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TaskConfig.taskLibPath = properties.getProperty("taskLibPath");
		TaskConfig.taskRootPath = properties.getProperty("taskRootPath");
		TaskConfig.downloadPath = properties.getProperty("downloadPath");
		TaskConfig.tempPath = properties.getProperty("tempPath");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {	
	}
}
