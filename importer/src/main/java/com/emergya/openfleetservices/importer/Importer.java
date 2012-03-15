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
package com.emergya.openfleetservices.importer;

import java.io.File;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.emergya.openfleetservices.importer.connector.ShapefileConnector;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;
import com.emergya.openfleetservices.importer.ddbb.JDBCConnector;

/**
 * 
 * 
 * @author marias
 * 
 */
@Repository
public class Importer {

	@Autowired
	public JDBCConnector jdbcConnector;

	/**
	 * @param jdbcConnector
	 *            the jdbcConnector to set
	 */
	public void setJdbcConnector(JDBCConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}

	public DataSetDescriptor doAll(File file, FileType filetype,
			String columnName, String geoColumnName) {
		return doGeocoding(doImport(file, filetype, geoColumnName));
	}

	/**
	 * Import the file given
	 * 
	 * @param file
	 * @param filetype
	 * @param geoColumnName
	 * @return
	 */
	public DataSetDescriptor doImport(File file, FileType filetype,
			String geoColumnName) {

		IConnector con = null;

		switch (filetype) {
		case SHAPEFILE:
			con = new ShapefileConnector(file);
			break;
		default:
			throw new RuntimeException();
		}

		Iterator<Object[]> it = con.getIterator();
		this.jdbcConnector.addAllData(con.getDescriptor(), it);
		return con.getDescriptor();
	}

	/**
	 * Geocode the table described on the {@link DataSetDescriptor}
	 * @param dsd
	 * @return
	 */
	public DataSetDescriptor doGeocoding(DataSetDescriptor dsd) {
		this.jdbcConnector.geocode(dsd);
		return dsd;
	}
}
