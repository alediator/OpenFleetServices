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
package com.emergya.openfleetservices.importer.connector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader.Row;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import com.emergya.openfleetservices.importer.IConnector;
import com.emergya.openfleetservices.importer.data.Column;
import com.emergya.openfleetservices.importer.data.DataSetDescriptor;
import com.emergya.openfleetservices.importer.ddbb.DataBaseType;
import com.emergya.openfleetservices.importer.exception.NotYetImplementedException;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author marias
 * 
 */
public class ShapefileConnector implements IConnector {

	private DataSetDescriptor dsd = null;

	public ShapefileConnector(File f) {
		initialize(f);
	}

	public void initialize(File f) {
		dsd = new DataSetDescriptor(f);
		this.readColumnsFromShapeFile(f, dsd);
	}
	
	private void readColumnsFromShapeFile(File f, DataSetDescriptor dsd){
		ShpFiles shpFile = null;
		//boolean strict = true;
		boolean useMemoryMapped = false;
		//GeometryFactory gf = new GeometryFactory();
		DbaseFileReader shapeParams = null;
		DbaseFileHeader shapeHeader = null;
		try {
			shpFile = new ShpFiles(f);
			shapeParams = new DbaseFileReader(shpFile, useMemoryMapped, Charset.defaultCharset());
			shapeHeader = shapeParams.getHeader();
			int numFields  = shapeHeader.getNumFields();
			String nameField = "";
			int decimalCountField = 0;
			int lengthField = 0;
			String classField = "";
			for(int i = 0; i<numFields; i++){
				 nameField = shapeHeader.getFieldName(i);
				 classField = shapeHeader.getFieldClass(i).getSimpleName();
				 // It depends of the number size
				 lengthField = shapeHeader.getFieldLength(i);
				 decimalCountField = shapeHeader.getFieldDecimalCount(i);
				 Column c = new Column();
				 c.setName(nameField);
				 c.setType(this.getDBTypeFromShape(classField));
				 dsd.addField(c);
				 classField.toString();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}finally {
			try {
				shapeParams.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getDBTypeFromShape(String classField){
		String type = "";
		try {
			if(classField.equals("Long") ||
					classField.equals("Integer") ||
					classField.equals("Double")){
				//TODO Hacer la correspondencia entre tipos numericos
				type = String.valueOf(DataBaseType.decimal);
			}else if(classField.equals("String")){
				type = String.valueOf(DataBaseType.text);
			}else if(classField.equals("Boolean")){
				type = String.valueOf(DataBaseType.bool);
			}else if(classField.equals("Date")){
				type = String.valueOf(DataBaseType.date);
			}else if(classField.equals("Timestamp")){
				type = String.valueOf(DataBaseType.timestamp);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return type;
	}

	public Iterator<Object[]> getIterator() {
		throw new NotYetImplementedException();
	}

	public DataSetDescriptor getDescriptor() {
		return dsd;
	}

}
