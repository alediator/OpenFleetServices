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

import com.emergya.openfleetservices.importer.connector.NominatimConnector;
import com.emergya.openfleetservices.importer.data.Column;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;

/**
 * @author marias
 * 
 */
@Repository
public class JDBCConnector {
	
	private static final Log LOG = LogFactory.getLog(JDBCConnector.class);
	protected JdbcTemplate simpleJdbcTemplate = null;
	protected NamedParameterJdbcTemplate namedJdbcTemplate = null;

	@Autowired
	final public void setDataSource(final DataSource dataSource) {
		this.simpleJdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
		String pk = dsd.getNamePK();
		String sqlCreateTable = "CREATE TABLE ";
		List<Column> columns = dsd.getFields();
		Iterator<Column> it = columns.iterator();
		Column c = (Column)it.next();
		columnsToTable += c.getName() + " " + c.getType();
		while(it.hasNext()){
			c = (Column)it.next();
			columnsToTable += ", " + c.getName() + " " + c.getType();
		}
		LOG.debug("Creating table: " + tableName);
		sqlCreateTable = sqlCreateTable + tableName + 
				" (" + pk + " SERIAL PRIMARY KEY, " +
				columnsToTable + ")";
		this.simpleJdbcTemplate.execute(sqlCreateTable);
		LOG.debug("Table " + tableName + " created");
		
		return tableName;
	}

	/**
	 * Add one row to the table
	 * 
	 * @param dsd
	 * @param it
	 */
	public void addData(DataSetDescriptor dsd, Object[] it) {
		
		String sqlInsert = "INSERT INTO ";
		String tableName = dsd.getTablename();
		List<Column> fields = dsd.getFields();
		
		sqlInsert += tableName + "(" + fields.get(0).getName();
		for(int i=1; i<fields.size(); i++){
			sqlInsert += ", " + fields.get(i).getName();
		}
		sqlInsert += ") VALUES ('" + it[0] + "'";
		
		for(int j=1; j<it.length; j++){
			sqlInsert += ", '" + it[j] + "'";
		}
		sqlInsert += ")";
		
		this.simpleJdbcTemplate.execute(sqlInsert);
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
		// TODO Crear una instancia de NominatimConnector
		// Del dsd vamos a requerir:
		String columnAddress = dsd.getColumnAddress();
		String geoColumnName = dsd.getGeoColumnName();
		// De la instancia de Nominatim extraeremos el geocoding
		NominatimConnector nm = new NominatimConnector("http://nominatim.openstreetmap.org/search.php?");
		nm.setFormat("json");
		nm.setQuery("sevilla");
		nm.getAddress();
		return false;
	}

}
