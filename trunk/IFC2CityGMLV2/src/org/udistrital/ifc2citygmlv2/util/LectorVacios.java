package org.udistrital.ifc2citygmlv2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcBuildingStorey;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcOpeningElement;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcProduct;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRelVoidsElement;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcWall;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcWallStandardCase;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.SET;
import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import org.udistrital.ifc2citygmlv2.sbm.Edificio;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Piso;

public class LectorVacios {

	public void cargarDatosBasicos(IfcModel ifcModel, Edificio edificio){

		
		HashMap<String, List<String>> murosYVacios = new HashMap<String, List<String>>();
		
		//Se carga la relacion que indica los vacios y a cual muro pertenecen
		for (IfcRelVoidsElement currentRelation : (Collection<IfcRelVoidsElement>) ifcModel
				.getCollection(IfcRelVoidsElement.class)) {
			
			
			// se va a indagar por los vacios que pertenezcan exclusivamente a muros
			if (currentRelation.getRelatingBuildingElement() instanceof IfcWallStandardCase) {
				
				IfcWallStandardCase muro = (IfcWallStandardCase) currentRelation.getRelatingBuildingElement();
				String idMuro = muro.getGlobalId().toString();
				
				IfcOpeningElement vacio = (IfcOpeningElement) currentRelation.getRelatedOpeningElement();

				//si el muro aun no esta registrado
				if(!murosYVacios.containsKey(idMuro)){
					
					List<String> listaDeVacios = new ArrayList();
					listaDeVacios.add(vacio.getGlobalId().toString());
					
					murosYVacios.put(idMuro, listaDeVacios );
					
				}else{
					
					List<String> listaDeVacios = murosYVacios.get(idMuro);
					listaDeVacios.add(vacio.getGlobalId().toString());

				}
			}
		}
		
		
		//se asignan a cada muro sus vacios respectivos
		for (Piso pisoActual : edificio.getPisos()) {
			
			for (Muro muroActual : pisoActual.getMuros()) {
				
				muroActual.setVacios(murosYVacios.get(muroActual.getId()));
				
			} 
			
		}
	}
	
	
}
