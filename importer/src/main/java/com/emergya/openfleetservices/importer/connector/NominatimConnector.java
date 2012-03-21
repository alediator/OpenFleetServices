package com.emergya.openfleetservices.importer.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import com.emergya.openfleetservices.importer.data.DataSetDescriptor;


public class NominatimConnector{
	private static final Log LOG = LogFactory.getLog(NominatimConnector.class);
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
		HttpClient httpclient = new HttpClient();
		GetMethod method = new GetMethod(this.getUrl());
		
		NameValuePair[] parameters = new NameValuePair[2]; 
        parameters[0] = new NameValuePair("format", this.getFormat()); 
        parameters[1] = new NameValuePair("q", this.getQuery());
        method.setQueryString(parameters);

		int response = 0;
		String lonLat = "";
		try {
			response = httpclient.executeMethod(method);
			if (response == HttpStatus.SC_OK) {
				lonLat = this.getLonLatfromJson(method.getResponseBodyAsStream());	
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return lonLat;
	}
	
	private String getLonLatfromJson(InputStream response){
		String lonlat = "";
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			line = reader.readLine();
			JSONArray jsonArray = null;
			JSONObject jsonObject = null;
			while(line!=null) {
				// Parser the json
				jsonArray =  (JSONArray) JSONSerializer.toJSON(line);
				if(jsonArray.size()!=0){
					jsonObject = jsonArray.getJSONObject(0);
				}
				String lon = null;
				String lat = null;
				if(jsonObject != null && jsonObject.get("lon")!=null){
					lon = (String)jsonObject.get("lon");
				}
				if(jsonObject != null && jsonObject.get("lat")!=null){
					lat = (String) jsonObject.get("lat");
				}
				if(lon == null && lat == null){
					lonlat = null;
				}else{
					lonlat = lon + "," + lat;
				}
				line = reader.readLine();
			}
		} catch (Throwable e) {
			LOG.debug("");
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lonlat;
	}
	
	public void getColumnsToGeom(List<String> columns, String address){
		while(address.contains("{")){
			String column = address.substring(address.indexOf("{")+1, address.indexOf("}"));
			columns.add(column);
			address = address.substring(address.indexOf("}")+1);
			getColumnsToGeom(columns, address);
		}
	}
	
	public Map<String, String> getPKAndAddress(DataSetDescriptor dsd, List<Map<String, Object>> paramMap, String address) {
		Map<String, String> pkAddress = new HashMap<String, String>();
		for(Map<String, Object> m: paramMap){
			String dir = address;
			Object pk = m.get(dsd.getNamePK());
			//Replace the parameter in the address by the map parameters
			for(String key: m.keySet()){
				dir = dir.replace("{"+ key + "}", String.valueOf(m.get(key)));
			}
			address.toString();
			pkAddress.put(String.valueOf(pk), String.valueOf(dir));
		}
		return pkAddress;
	}

	public Map<String, String> getPKAndGeom(Map<String, String> pkAddress) {
		Map<String, String> pkGeom = new HashMap<String, String>();
		Set<String> pks = pkAddress.keySet();
		for(String primaryKey:pks){
			String dirToGeo = pkAddress.get(primaryKey);
			this.setQuery(dirToGeo);
			pkGeom.put(primaryKey, this.getAddress());
		}
		return pkGeom;
	}
}
