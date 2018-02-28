package databox.control;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import databox.db.RecordedData;

public class LogAnalysisProxy {
	public static Object getProxy(Object obj, LogAnalysis logAnalysis) {
		LogAnalysisHandler handler = new LogAnalysisHandler(obj, logAnalysis);	
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);
	}
}

class LogAnalysisHandler implements InvocationHandler {

	private Object object;
	private LogAnalysis logAnalysis;
	
	public LogAnalysisHandler(Object obj, LogAnalysis logAnalysis) {
		this.object = obj;
		this.logAnalysis = logAnalysis;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//System.out.println("------proxy handler start------");
		//System.out.println("------" + method.getName() + "------");
		
		Object result = method.invoke(this.object, args);
		RecordedData recordedData = (RecordedData) result;
		
		Log log = recordedData.getLog();
		//Gson gson = new Gson();
		//System.out.println(gson.toJson(log));
		
		//! control
		boolean flag = logAnalysis.control(log);
		recordedData.setAllowFlag(flag);
		
		// System.out.println(flag ? "pass" : "deny");
		
		//System.out.println("------proxy handler end------");
		
		return recordedData;
	}
}
