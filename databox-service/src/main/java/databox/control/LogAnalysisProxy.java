package databox.control;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import databox.db.RecordedData;
import databox.db.SeqLogDB;

public class LogAnalysisProxy {
	public static Object getProxy(Object obj, LogAnalysis logAnalysis) {
		LogAnalysisHandler handler = new LogAnalysisHandler(obj, logAnalysis);	
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);
	}
}

class LogAnalysisHandler implements InvocationHandler {

	private Object object;
	private LogAnalysis logAnalysis;
	private SeqLogDB seqLogDB;
	
	public LogAnalysisHandler(Object obj, LogAnalysis logAnalysis) {
		this.object = obj;
		this.logAnalysis = logAnalysis;

		// TODO 导出配置文件
		String ip = "localhost";
		int port = 27017;
		String dbName = "databox_analysis";
		String collectionName = "seq_log";
		this.seqLogDB = new SeqLogDB(ip, port, dbName, collectionName);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("------proxy handler start------");
		//System.out.println("------" + method.getName() + "------");
		
		Object result = method.invoke(this.object, args);
		RecordedData recordedData = (RecordedData) result;
		
		Log log = recordedData.getLog();
		//Gson gson = new Gson();
		//System.out.println(gson.toJson(log));
		
		//! control
        seqLogDB.insert(log);
		boolean flag = logAnalysis.control(log);
		recordedData.setAllowFlag(flag);
		
		// System.out.println(flag ? "pass" : "deny");
		
		//System.out.println("------proxy handler end------");
		
		return recordedData;
	}
}
