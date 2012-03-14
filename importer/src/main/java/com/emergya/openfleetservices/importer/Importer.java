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

import com.emergya.openfleetservices.importer.connector.ShapefileConnector;

/**
 * 
 * 
 * @author marias
 * 
 */

public class Importer {

	public static String doAll(File file, FileType filetype, String columnName,
			String geoColumnName) {
		String tablename = doImport(file, filetype, geoColumnName);
		doGeocoding(tablename, columnName, geoColumnName);
		return tablename;
	}

	public static String doImport(File file, FileType filetype,
			String geoColumnName) {
		String tablename = null;

		// TODO
		switch (filetype) {
		case SHAPEFILE:
			// TODO meterle file
			ShapefileConnector sc = new ShapefileConnector(null, filetype);
			Iterator<Object[]> it = sc.getIterator();
			//TODO
			
			
			tablename = sc.getDescriptor().getTablename();
			break;
		default:
			throw new RuntimeException();
		}

		return tablename;
	}

	public static void doGeocoding(String tablename, String columnName,
			String geoColumnName) {

		// TODO
		//jdbc->geocode
	}
}
