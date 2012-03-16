package com.emergya.openfleetservices.importer.data;

import com.emergya.openfleetservices.importer.ddbb.DataBaseType;

public class Column {
	private String name;
	private DataBaseType type;

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
	public DataBaseType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DataBaseType type) {
		this.type = type;
	}
}