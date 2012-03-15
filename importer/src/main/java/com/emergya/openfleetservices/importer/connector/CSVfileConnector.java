package com.emergya.openfleetservices.importer.connector;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import com.emergya.openfleetservices.importer.IConnector;
import com.emergya.openfleetservices.importer.data.Column;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;

public class CSVfileConnector implements IConnector {

	private DataSetDescriptor dsd = null;
	
	public CSVfileConnector(File f) {
		this.initialize(f);
	}

	public void initialize(File f) {
		dsd = new DataSetDescriptor(f);
		this.readColumnsFromCSV(f, dsd);
	}

	public Iterator<Object[]> getIterator() {
		return null;
	}

	public DataSetDescriptor getDescriptor() {
		return null;
	}
	
	private void readColumnsFromCSV(File f, DataSetDescriptor dsd){
		FileReader fileReader;
		try {
			fileReader = new FileReader(f);
			CSVReader csvFile = new CSVReader(fileReader);
			String[] line = csvFile.readNext();
			for(int i=0; i<line.length; i++){
				Column c = new Column();
				c.setName(line[i]);
				//TODO column type
				dsd.addField(c);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
