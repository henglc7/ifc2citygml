package org.udistrital.ifc2citygmlv2.util;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

public class Transformador {

		
	public Coordenada aplicarObjectRepresentation(Coordenada coordOriginal, Solido muro){
		
		coordOriginal = rotarCoordenada(
				coordOriginal
				, muro.representation.representation_position_axis
				, muro.representation.representation_position_refDirection 
				);

		Coordenada location = muro.representation.representation_position_location;
		
		double xActual = coordOriginal.getX();
		xActual += location.getX();
		
		double yActual = coordOriginal.getY();
		yActual += location.getY();
		
		double zActual = coordOriginal.getZ();
		zActual += location.getZ();
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	
	public Coordenada aplicarObjectPlacement(Coordenada original, Solido muro){
		//Hay que rotar primero, o no funciona bien
		Coordenada conRotacion = rotarCoordenada(
				original
				, muro.objectPlacement.relativePlacement_axis
				, muro.objectPlacement.relativePlacement_refDirection
				);
		
		double xActual = conRotacion.getX();
		
		
		xActual += muro.objectPlacement.placementRelTo_placementRelTo.getX();
		xActual += muro.objectPlacement.placementRelTo_relativePlacement.getX();
		xActual += muro.objectPlacement.relativePlacement_location.getX();
		
		double yActual = conRotacion.getY();
		
		
		yActual += muro.objectPlacement.placementRelTo_placementRelTo.getY();
		yActual += muro.objectPlacement.placementRelTo_relativePlacement.getY();
		yActual += muro.objectPlacement.relativePlacement_location.getY();
		
		double zActual = conRotacion.getZ();
		
		zActual += muro.objectPlacement.placementRelTo_placementRelTo.getZ();
		zActual += muro.objectPlacement.placementRelTo_relativePlacement.getZ();
		zActual += muro.objectPlacement.relativePlacement_location.getZ();
		
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	

	
	public Coordenada rotarCoordenada(Coordenada coordOriginal, Coordenada axis, Coordenada refDirection){
		
		Coordenada r = new Coordenada();
		
		if(axis != null && refDirection != null){
			
			Vector3D axisX = new Vector3D(1, 0, 0);
			Vector3D axisZ = new Vector3D(0, 0, 1);

			Vector3D deseadoX = new Vector3D(axis.getX(), axis.getY(), axis.getZ()); //(AXIS, eje Z)
			Vector3D deseadoZ = new Vector3D(refDirection.getX(), refDirection.getY(), refDirection.getZ()); //(refdirection, eje X)
			
			Rotation rotacionX = new Rotation(axisX,deseadoZ);
			Rotation rotacionZ = new Rotation(axisZ,deseadoX);
			
			
			Vector3D punto = new Vector3D(coordOriginal.getX(), coordOriginal.getY(), coordOriginal.getZ());
			Vector3D puntoRotado = rotacionX.applyTo(punto);
			puntoRotado = rotacionZ.applyTo(puntoRotado);
			
			r.setX(puntoRotado.getX());
			r.setY(puntoRotado.getY());
			r.setZ(puntoRotado.getZ());
		}else{
			r = coordOriginal;
		}
		

		return r;
	}
	
}
