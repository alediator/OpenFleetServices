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
package com.emergya.openfleetservices.importer.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Describe a dataset
 * 
 * @author marias
 * 
 */
public class DataSetDescriptor {

	private String tablename = "default";
	private String columnAddress;
	private String geoColumnName;

	/**
	 * @return the columnAddress
	 */
	public String getColumnAddress() {
		return columnAddress;
	}

	/**
	 * @param columnAddress the columnAddress to set
	 */
	public void setColumnAddress(String columnAddress) {
		this.columnAddress = columnAddress;
	}

	/**
	 * @return the geoColumnName
	 */
	public String getGeoColumnName() {
		return geoColumnName;
	}

	/**
	 * @param geoColumnName the geoColumnName to set
	 */
	public void setGeoColumnName(String geoColumnName) {
		this.geoColumnName = geoColumnName;
	}

	public File file = null;

	public DataSetDescriptor(File file) {
		this.file = file;
	}

	/**
	 * @return the tablename
	 */
	public String getTablename() {
		return tablename;
	}

	/**
	 * @param tablename
	 *            the tablename to set
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	private List<Column> fields = new ArrayList<Column>();

	public void addField(Column c) {
		fields.add(c);
	}

	public List<Column> getFields() {
		return fields;
	}

}

class Column {
	private String name;
	private String type;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}