package databox.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.nustaq.serialization.FSTConfiguration;

public class DataBoxHttpClient {
	private CloseableHttpClient client;
	private RequestConfig config;
	private FSTConfiguration fstConfiguration;
	
	private String hostName;
	
	public DataBoxHttpClient(final String hostName) {
		client = HttpClients.createDefault();
		config = RequestConfig.custom()
				.setSocketTimeout(5000000)
				.setConnectTimeout(5000000)
				.setConnectionRequestTimeout(50000000)
				.build();
		fstConfiguration = FSTConfiguration.createDefaultConfiguration();
		
		this.hostName = hostName;
	}
	
	public DataBoxHttpClient(String ip, int port) {
		client = HttpClients.createDefault();
		config = RequestConfig.custom()
				.setSocketTimeout(5000000)
				.setConnectTimeout(5000000)
				.setConnectionRequestTimeout(50000000)
				.build();
		fstConfiguration = FSTConfiguration.createDefaultConfiguration();
		
		this.hostName = "http://" + ip + ":" + port;
	}
	
	public Object readObject(CloseableHttpResponse response) {
		Object obj = null;
		
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
				FSTConfiguration fstConfiguration = FSTConfiguration.createJsonConfiguration();
				obj = fstConfiguration.asObject(result);
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	public long readLong(CloseableHttpResponse response) {
		long res = 0;
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
				res = Long.valueOf(new String(result));
			} catch (UnsupportedOperationException e ) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public double readDouble(CloseableHttpResponse response) {
		double res = 0;
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
				res = Double.valueOf(new String(result));
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
		
	}
	
	public CloseableHttpResponse post(final String url, Object obj) {
		HttpPost httpPost = new HttpPost(this.hostName + url);
		byte[] bytes = fstConfiguration.asByteArray(obj);
		ByteArrayEntity entity = new ByteArrayEntity(bytes);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public CloseableHttpResponse post(final String url) {
		HttpPost httpPost = new HttpPost(this.hostName + url);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public CloseableHttpResponse get(final String url) {
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
