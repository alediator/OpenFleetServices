package com.emergya.openfleetservices.importer.connector;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		String pathname = "/home/usuario/Escritorio/exportacion/csv/cm.csv";
		File f = new File(pathname);
		CSVfileConnector csvfile = new CSVfileConnector(f);
	}

}
