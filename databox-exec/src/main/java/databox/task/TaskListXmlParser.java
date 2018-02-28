package databox.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TaskListXmlParser {
	private Document doc;
	
	public TaskListXmlParser(String fileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		doc = reader.read(new File(fileName));
	}
	
	// test
	public Element getRootElement() {
		return doc.getRootElement();
	}
	
	public void saveToFile(String fileName) {
		try {
			FileWriter out = new FileWriter(fileName);
			doc.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Element getTaskGroup(String taskGroupName) {
		Element res = null;
		Element root = doc.getRootElement();
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element elem = it.next();
			Attribute attr = elem.attribute("name");
			if(attr != null) {
				if(taskGroupName.equals(attr.getValue())) {
					res = elem;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return res;
	}
	public List<Element> getAllTaskGroup() {
		Element root = doc.getRootElement();
		return root.elements();
	}
	
	public void insertTaskGroup(Element taskGroupElem) {
		Element root = doc.getRootElement();
		root.add(taskGroupElem);
	}
	
	public void insertTaskGroup(List<Element> taskGroupElems) {
		for(Iterator<Element> it = taskGroupElems.iterator(); it.hasNext();) {
			insertTaskGroup(it.next());
		}
	}
	
	public TaskInfo getTaskInfo(String taskGroupName, String taskName) {
		TaskInfo info = null;
		List<TaskInfo> infos = getTaskInfoByGroupName(taskGroupName);
		Iterator<TaskInfo> it = infos.iterator();
		while(it.hasNext()) {
			TaskInfo ti = it.next();
			if(ti.getTaskName().equals(taskName)) {
				info = ti;
				break;
			}
		}
		return info;
	}
	
	public List<String> getAllTaskGroupName() {
		List<String> res = new ArrayList<String>();
		Element root = doc.getRootElement();
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element element = it.next();
			if("taskgroup".equals(element.getName())) {
				Attribute attr = element.attribute("name");
				if(attr != null) {
					res.add(attr.getValue());
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return res;
	}
	
	private Element findTaskGroupElement(String groupName) {
		Element elem = null;
		Element root = doc.getRootElement();
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element e = it.next();
			Attribute attr = e.attribute("name");
			if(attr != null) {
				if(attr.getValue().equals(groupName))
					elem = e;
				else
					continue;
			}else {
				continue;
			}
		}
		return elem;
	}
	
	public boolean insertTaskInfoByGroupName(String groupName, List<TaskInfo> taskInfo) {
		Element element = findTaskGroupElement(groupName);
		Element taskGroupElem = null;
		if(element == null) {
			taskGroupElem = doc.getRootElement().addElement("taskgroup");
			taskGroupElem.addAttribute("name", groupName);
		} else {
			taskGroupElem = element;
		}
		
		Iterator<TaskInfo> it = taskInfo.iterator();
		while(it.hasNext()) {
			TaskInfo info = it.next();
			taskGroupElem.addElement("task")
			.addAttribute("name", info.getTaskName())
			.addAttribute("target", info.getTarget())
			.addAttribute("description", info.getDescription());
		}
		return true;
	}
	
	public List<TaskInfo> getAllTaskInfo() {
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		Element root = doc.getRootElement();
		for(Iterator<Element> it1 = root.elementIterator(); it1.hasNext();) {
			Element taskGroup = it1.next();
			for(Iterator<Element> it2 = taskGroup.elementIterator(); it2.hasNext();) {
				Element task = it2.next();
				TaskInfo info = new TaskInfo(taskGroup.attributeValue("name"),
						task.attributeValue("name"), task.attributeValue("target"),
						task.attributeValue("description"));
				taskInfos.add(info);
			}
		}
		return taskInfos;
	}
	
	public List<TaskInfo> getTaskInfoByGroupName(String taskGroupName) {
		List<TaskInfo> res = new ArrayList<TaskInfo>();
		Element root = doc.getRootElement();
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element elem = it.next();
			Attribute attr = elem.attribute("name");
			if(attr != null) {
				String value = attr.getValue();
				if(value.equals(taskGroupName)) {
					for(Iterator<Element> iter = elem.elementIterator(); iter.hasNext();) {
						Element e = iter.next();
						String taskName = e.attribute("name").getValue();
						String target = e.attribute("target").getValue();
						Attribute attrDesc = e.attribute("description");
						String description = "";
						if(attrDesc != null)
							description = attrDesc.getValue();
						TaskInfo info = new TaskInfo(taskGroupName, taskName, target, description);
						res.add(info);
					}
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return res;
	}
	
	public boolean containsTaskGroup(String taskGroupName) {
		Element root = doc.getRootElement();
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element element = it.next();
			Attribute attr = element.attribute("name");
			if(attr != null) {
				String value = attr.getValue();
				if(value.equals(taskGroupName))
					return true;
				else 
					continue;
			}
		}
		return false;
	}
	
	public boolean containsTask(String taskGroupName, String taskName) {
		Element root = doc.getRootElement();
		Element taskGroupElem = null;
		for(Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element element = it.next();
			Attribute attr = element.attribute("name");
			if(attr != null) {
				String value = attr.getValue();
				if(value.equals(taskGroupName))
					taskGroupElem = element;
				else 
					continue;
			}
		}
		
		if(taskGroupElem == null)
			return false;
		
		for(Iterator<Element> it = taskGroupElem.elementIterator(); it.hasNext();) {
			Element element = it.next();
			Attribute attr = element.attribute("name");
			if(attr != null) {
				String value = attr.getValue();
				if(value.equals(taskName))
					return true;
				else
					continue;
			}
		}
		return false;
	}
}
