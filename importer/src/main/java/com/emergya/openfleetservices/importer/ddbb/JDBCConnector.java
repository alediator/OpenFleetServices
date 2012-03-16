/*
 * Copyright (C) 2012, Emergya (http://www.emergya.com)
 *
 * @author <a href="mailto:marias@emergya.com">María Arias de Reyna</a>
 *
 * This file is part of GoFleetLS
 *
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
package com.emergya.openfleetservices.importer.ddbb;

import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.emergya.openfleetservices.importer.data.Column;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;

/**
 * @author marias
 * 
 */
@Repository
public class JDBCConnector {
	
	private static final Log LOG = LogFactory.getLog(JDBCConnector.class);
	protected NamedParameterJdbcTemplate simpleJdbcTemplate = null;

	@Autowired
	final public void setDataSource(final DataSource dataSource) {
		this.simpleJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * Create a Table that suits the {@link DataSetDescriptor} given.
	 * 
	 * The tablename attribute of the datasetdescriptor is also updated.
	 * 
	 * @param dsd
	 * @return the table name
	 */
	public String createTable(DataSetDescriptor dsd) {
		String tableName = dsd.getTablename();
		String columnsToTable = "";
		List<Column> columns = dsd.getFields();
		Iterator<Column> it = columns.iterator();
		Column c = (Column)it.next();
		columnsToTable += c.getName() + " " + c.getType();
		while(it.hasNext()){
			c = (Column)it.next();
			columnsToTable += ", " + c.getName() + " " + c.getType();
		}
		String sqlCreateTable = "CREATE TABLE " + tableName + " (" + columnsToTable + ")";
		
//		HashMap<String, String> paramMap = new HashMap<String, String>();
//		PreparedStatementCallback<Object> action = new;
//		this.simpleJdbcTemplate.execute(sqlCreateTable, paramMap, action );
		
		JdbcTemplate jdbc = new JdbcTemplate();
		jdbc.execute(sqlCreateTable);
		
		return tableName;
	}

	/**
	 * Add one row to the table
	 * 
	 * @param dsd
	 * @param it
	 */
	public void addData(DataSetDescriptor dsd, Object[] it) {
		// TODO
	}

	/**
	 * Add multiple rows to the table (usually calling
	 * {@link #addData(DataSetDescriptor, Object[])}
	 * 
	 * @param dsd
	 * @param it
	 */
	public void addAllData(DataSetDescriptor dsd, Iterator<Object[]> it) {
		while (it.hasNext()) {
			this.addData(dsd, it.next());
		}
	}

	/**
	 * Given a tablename and the column where the address lies, for each row, it
	 * geocodes the address and saves the geometry on the geocolumn
	 * 
	 * @param dsd
	 * @return
	 */
	public Boolean geocode(DataSetDescriptor dsd) {
		// TODO
		return false;
	}

}
