package com.emergya.openfleetservices.importer.connector;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShapeFileConnectorTest {

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
		String pathname = "/home/usuario/teleatlas_split/network_split.shp";
		File f = new File(pathname);
		ShapefileConnector sfc = new ShapefileConnector(f);
	}

}
