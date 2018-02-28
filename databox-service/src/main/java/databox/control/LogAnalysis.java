package databox.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

public class LogAnalysis {
	private CloseableHttpClient client;
	private RequestConfig config;
	private String hostName;
	
	public LogAnalysis(String ip, int port) {
		client = HttpClients.createDefault();
		config = RequestConfig.custom()
				.setSocketTimeout(5000000)
				.setConnectTimeout(50000)
				.setConnectionRequestTimeout(500000000)
				.build();
		
		this.hostName = "http://" + ip + ":" + port;
	}
	
	public boolean init() {
		CloseableHttpResponse response = get("/" + "init");
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode == 200) {
			HttpEntity entity = response.getEntity();
			try {
				InputStream in = entity.getContent();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while(-1 != (n = in.read(buffer)))
					out.write(buffer, 0, n);
				
				byte[] result = out.toByteArray();
				Gson gson = new Gson();
				AnalysisResult aRes = gson.fromJson(new String(result), AnalysisResult.class);
				if(aRes == null) {
					System.err.println("error: analysis result's status is null");
					return false;
				}
				
				if("success".equals(aRes.status)) {
					return true;
				} else {	//deny
					// log
					return false;
				}
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
				return false;
			} catch ( IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public boolean control(Log log) {
		String logJsonString = LogHelper.logToJson(log);
		HttpPost httpPost = new HttpPost(this.hostName + "/service");
		httpPost.setHeader("Content-Type", "application/json");
		try {	
			httpPost.setEntity(new StringEntity(logJsonString));
			CloseableHttpResponse response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while(-1 != (n = in.read(buffer)))
					out.write(buffer, 0, n);
				
				byte[] result = out.toByteArray();
				Gson gson = new Gson();
				AnalysisResult aRes = gson.fromJson(new String(result), AnalysisResult.class);
				if(aRes == null) {
					System.err.println("error: analysis result's status is null");
					return false;
				}
				
				if("pass".equals(aRes.status)) {
					return true;
				} else {	//deny
					// log
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private CloseableHttpResponse get(final String url) {
		HttpGet httpGet = new HttpGet(this.hostName + url);
		httpGet.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
}
