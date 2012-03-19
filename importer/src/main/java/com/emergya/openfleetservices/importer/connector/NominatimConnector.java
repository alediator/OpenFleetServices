package com.emergya.openfleetservices.importer.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;


public class NominatimConnector{

	private String url;
	private String format;
	private String json_callback;
	private String query;
	
	public NominatimConnector(String url) {
		this.url = url;
		this.format = null;
		this.json_callback = null;
		this.query = null;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getJson_callback() {
		return json_callback;
	}

	public void setJson_callback(String json_callback) {
		this.json_callback = json_callback;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getGeocode(){
		// Create the request
		String request = "";
		boolean res = false;
		request+= this.getUrl();
		if(this.getFormat()!=null){
			request+= "format=" + this.getFormat();
			res = true;
		}
		if(this.getQuery()!=null){
			if(res){
				request+= "&q=" + this.getQuery(); 
			}else{
				request+= "q=" + this.getQuery();
			}
		}
		return request;
	}
	
	public String getAddress(){
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(this.getGeocode());
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			// Using a buffer reader to read the response 
			InputStreamReader input = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
			BufferedReader reader = new BufferedReader(input);
			for (String line = null; (line = reader.readLine()) != null;) {
				System.out.println(line + "\n");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
