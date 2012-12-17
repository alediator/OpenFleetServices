package com.emergya.openfleetservices.importer.connector;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShapeFileConnectorTest {
	
	public static final String TEST_ONE = "target/test-classes/files/shapefiles/points_23030.shp";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String pathname = TEST_ONE;
		File f = new File(pathname);
		ShapefileConnector sfc = new ShapefileConnector(f);
	}

}
