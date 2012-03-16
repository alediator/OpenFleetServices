package com.emergya.openfleetservices.importer.connector;

import java.io.File;
import java.util.Iterator;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

import com.emergya.openfleetservices.importer.data.DataSetDescriptor;
import com.emergya.openfleetservices.importer.ddbb.JDBCConnector;

public class CSVFileConnectorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String pathname = "/home/usuario/Escritorio/exportacion/csv/r:rp.csv";
		File f = new File(pathname);
		CSVfileConnector csvfile = new CSVfileConnector(f);
		Iterator<Object[]> it = csvfile.getIterator();
		while(it.hasNext()){
			String[] obj = (String[])it.next();
			String cadena = "";
			for(int i=0;i<obj.length;i++){
				cadena += " " + obj[i] + " ";
			}
			System.out.println(cadena);
		}
		DataSetDescriptor dsd = csvfile.getDescriptor();
		
		JDBCConnector jdbc = new JDBCConnector();
		jdbc .createTable(dsd);
	}

}
