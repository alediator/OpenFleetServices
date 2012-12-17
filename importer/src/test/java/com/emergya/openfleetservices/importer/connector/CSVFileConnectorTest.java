package com.emergya.openfleetservices.importer.connector;

import java.io.File;
import java.util.Iterator;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.openfleetservices.importer.data.DataSetDescriptor;
import com.emergya.openfleetservices.importer.ddbb.JDBCConnector;

@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
@Transactional
public class CSVFileConnectorTest extends AbstractJUnit4SpringContextTests{
	
	public static final String TEST_ONE = "target/test-classes/files/csv/r:rp.csv";
	
	@Autowired
	private ApplicationContext applicationContext;
	
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
		String pathname = TEST_ONE;
		File f = new File(pathname);
		CSVfileConnector csvfile = new CSVfileConnector(f);
		DataSource ds = (DataSource)applicationContext.getBean("dataSource");
		DataSetDescriptor dsd = csvfile.getDescriptor();
		JDBCConnector jdbc = new JDBCConnector();
		jdbc.setDataSource(ds);
		jdbc.createTable(dsd);
		Iterator<Object[]> it = csvfile.getIterator();
		while(it.hasNext()){
			jdbc.addData(dsd, it.next());
		}
		String address = "{poblacion},España";
		jdbc.geocode(dsd, address);
	}

}
