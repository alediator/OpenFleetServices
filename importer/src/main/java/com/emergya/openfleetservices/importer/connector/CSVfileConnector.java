package com.emergya.openfleetservices.importer.connector;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.emergya.openfleetservices.importer.IConnector;
import com.emergya.openfleetservices.importer.data.Column;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;
import com.emergya.openfleetservices.importer.ddbb.DataBaseType;

public class CSVfileConnector implements IConnector {

	private DataSetDescriptor dsd = null;

	public CSVfileConnector(File f) {
		this.initialize(f);
	}

	public void initialize(File f) {
		dsd = new DataSetDescriptor(f);
		String name = f.getName();
		String alphaOnly = name.replaceAll("[^a-zA-Z]+","");
		dsd.setTablename(alphaOnly);
		this.readColumnsFromCSV(f, dsd);
	}

	public Iterator<Object[]> getIterator() {
		return new CSVIterator(dsd);
	}

	public DataSetDescriptor getDescriptor() {
		return dsd;
	}
	/**
	 * Method readColumnsFromCSV: method to read the name and the type of the column
	 * from a csv file
	 * 
	 * @param f: file to read
	 * @param dsd: data set where we put the column
	 */

	private void readColumnsFromCSV(File f, DataSetDescriptor dsd) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(f);
			
			CSVReader csvFile = new CSVReader(fileReader);
			String[] headerLine = csvFile.readNext();
			String[] firstDataLine = csvFile.readNext();
			// Add the column names
			for (int i = 0; i < headerLine.length; i++) {
				Column c = new Column();
				c.setName(headerLine[i]);
				dsd.addField(c);
			}
			
			this.checkColumnType(dsd.getFields(), firstDataLine);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method checkColumnType: method to check the column type
	 *	1.- Look at first row.
	 *	2.- Checking each type attributes.
	 *	3.- Add this one to column type.
	 *
	 * @param list: column list
	 * @param firstDataLine: first line of the csv file
	 */
	private void checkColumnType(List<Column> list, String[] firstDataLine) {
		for (int i = 0; i < firstDataLine.length; i++) {
			String data = firstDataLine[i];
			Column c = list.get(i);
			try {
				Double.valueOf(data);
				c.setType(DataBaseType.decimal);
			} catch (NumberFormatException e) {
				c.setType(DataBaseType.text);
			}
		}

	}

}

class CSVIterator implements Iterator<Object[]> {

	private static final Log LOG = LogFactory.getLog(CSVIterator.class);
	private DataSetDescriptor datasetdescriptor;
	private CSVReader csvFile;
	private String[] element;

	public CSVIterator(DataSetDescriptor dsd) {
		FileReader fileReader = null;
		File f = null;
		try {
			datasetdescriptor = dsd;
			f = datasetdescriptor.getFile();
			fileReader = new FileReader(f);
			csvFile = new CSVReader(fileReader);
			// Skip the first line
			csvFile.readNext();
			element = csvFile.readNext();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public boolean hasNext() {
		return element != null;
	}

	public Object[] next() {
		Object[] res = element;
		try {
			element = csvFile.readNext();
		} catch (IOException e) {
			element = null;
		}
		return res;
	}

	public void remove() {

	}

}