package org.udistrital.ifc2citygml.util;

/*
 * This file is part of citygml4j.
 * Copyright (c) 2007 - 2010
 * Institute for Geodesy and Geoinformation Science
 * Technische Universitaet Berlin, Germany
 * http://www.igg.tu-berlin.de/
 *
 * The citygml4j library is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see 
 * <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.JAXBBuilder;
import org.citygml4j.factory.CityGMLFactory;
import org.citygml4j.factory.GMLFactory;
import org.citygml4j.factory.geometry.GMLGeometryFactory;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.building.BoundarySurfaceProperty;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.gml.geometry.complexes.CompositeSurface;
import org.citygml4j.model.gml.geometry.primitives.AbstractSurface;
import org.citygml4j.model.gml.geometry.primitives.Polygon;
import org.citygml4j.model.gml.geometry.primitives.Solid;
import org.citygml4j.model.gml.geometry.primitives.SurfaceProperty;
import org.citygml4j.model.module.citygml.CityGMLVersion;
import org.citygml4j.util.gmlid.DefaultGMLIdManager;
import org.citygml4j.util.gmlid.GMLIdManager;
import org.citygml4j.xml.io.CityGMLOutputFactory;
import org.citygml4j.xml.io.writer.CityGMLWriter;
import org.udistrital.ifc2citygml.modelo.Piso;

import com.vividsolutions.jts.geom.Coordinate;


public class BuildingCreator {
	private CityGMLFactory citygml;
	private GMLFactory gml;

	/*
	public static void main(String[] args) throws Exception {
		new BuildingCreator().doMain();
	}
	*/

	public void doMain(Coordinate[] coordenadas, double elevacion, String archivoSalida) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] "); 

		System.out.println(df.format(new Date()) + "setting up citygml4j context and JAXB builder");
		CityGMLContext ctx = new CityGMLContext();
		JAXBBuilder builder = ctx.createJAXBBuilder();

		System.out.println(df.format(new Date()) + "creating LOD2 building as citygml4j in-memory object tree");
		GMLGeometryFactory geom = new GMLGeometryFactory();
		citygml = new CityGMLFactory();
		gml = new GMLFactory();

		GMLIdManager gmlIdManager = DefaultGMLIdManager.getInstance();

		Building building = citygml.createBuilding();

		List<AbstractSurface> shell = new ArrayList<AbstractSurface>();
		
		for(int c=0; c < coordenadas.length -1 ; c++){
        	Coordinate coordenadaActual = coordenadas[c];
        	Coordinate coordenadaSiguiente = coordenadas[c+1];
        	
        	Polygon poligonoActual = geom.createLinearPolygon(
        			new double[]
        			           {
        					coordenadaActual.x, coordenadaActual.y,0,
        					coordenadaActual.x, coordenadaActual.y,elevacion,
        					coordenadaSiguiente.x, coordenadaSiguiente.y, elevacion,
        					coordenadaSiguiente.x, coordenadaSiguiente.y, 0,
        					coordenadaActual.x, coordenadaActual.y, 0
        					}
        			, 3
        			);
        	
        	shell.add(poligonoActual);
        }
		
		

		double[] tapa = new double[coordenadas.length*3];
		int c = 0;		
		for(int n=coordenadas.length -1; n >=0  ; n--){
        	Coordinate coordenadaActual = coordenadas[n];
			
			tapa[c] = coordenadaActual.x;
        	tapa[c+1] = coordenadaActual.y;
        	tapa[c+2] = elevacion;
        	
        	c=c+3;
		}

		Polygon poligonoTapa = geom.createLinearPolygon(tapa, 3);
		
		shell.add(poligonoTapa);
		

		
		CompositeSurface exterior = gml.createCompositeSurface(shell);
		Solid solid = gml.createSolid();
		solid.setExterior(gml.createSurfaceProperty(exterior));

		building.setLod1Solid(gml.createSolidProperty(solid));


		CityModel cityModel = citygml.createCityModel();
		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));

		System.out.println(df.format(new Date()) + "writing citygml4j object tree");
		CityGMLOutputFactory out = builder.createCityGMLOutputFactory(CityGMLVersion.v1_0_0);
		CityGMLWriter writer = out.createCityGMLWriter(new File(archivoSalida));

		writer.setPrefixes(CityGMLVersion.v1_0_0);
		writer.setSchemaLocations(CityGMLVersion.v1_0_0);
		writer.setIndentString("  ");
		writer.write(cityModel);
		writer.close();	
		
		System.out.println(df.format(new Date()) + "CityGML file archivoSalida written");
		System.out.println(df.format(new Date()) + "sample citygml4j application successfully finished");
	}

}