package org.udistrital.ifc2citygmlv2.util;

import java.util.ArrayList;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcOpeningElement;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Poligono;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Vacio;
import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

public class Transformador {

	
	
	public Coordenada convertirEnAbsoluta(Coordenada coordOriginal, Solido pSolido){
		
		Coordenada coordAbsoluta = aplicarObjectRepresentation(coordOriginal, pSolido);
		coordAbsoluta = aplicarObjectPlacement(coordAbsoluta, pSolido);
		/*
		if(pSolido.getIfcModel().getIfcObjectByID(pSolido.getId()) instanceof IfcOpeningElement){
			coordAbsoluta = rotarCoordenadaVacio(coordAbsoluta, pSolido);
		}*/
		
		return coordAbsoluta;
	}
		
	public static Coordenada rotarCoordenadaVacio(Coordenada original, Solido pSolido) {
		/*
		 * 
		 * 
		 * 
         
        if(esVacio && pSolido.objectPlacement.relativePlacement.axis == null){
			original = ...
		}
		
		Coordenada r = rotarCoordenada(
				original
				, pSolido.representation.position.axis
				, pSolido.representation.position.refDirection
				);
		
		return r;

		
		
		LECTOR VACIOS

		 //Se lee RelativePlacement
				                    
				                    IfcAxis2Placement3D relativePlacementB = (IfcAxis2Placement3D) placementRelToA.getRelativePlacement();
				                    vacioActual.objectPlacement.setPlacementRelTo_relativePlacement(LectorCoordenada.Leer(relativePlacementB.getLocation()));
				                    
				                    if(relativePlacementB.getAxis()!=null && relativePlacementB.getRefDirection()!=null){
				                    	
					                    //vacioActual.objectPlacement.setPlacementRelTo_relativePlacement_axis(LectorCoordenada.Leer(relativePlacementB.getAxis()));
					                    //vacioActual.objectPlacement.setPlacementRelTo_relativePlacement_refDirection(LectorCoordenada.Leer(relativePlacementB.getRefDirection()));

				                    	
				                    }


		PLACEMENT

		// inicia para vacios
			
			public Coordenada placementRelTo_relativePlacement_axis;
			
			public Coordenada placementRelTo_relativePlacement_refDirection;
			


		*/
		
		//////////
		

		Vacio vacio = (Vacio) pSolido;
		
		Coordenada locationMuro = vacio.getMuroAlQueVacia().objectPlacement.getRelativePlacement_location();
		Coordenada axisMuro = vacio.getMuroAlQueVacia().objectPlacement.getRelativePlacement_axis();
		Coordenada refDirectionMuro = vacio.getMuroAlQueVacia().objectPlacement.getRelativePlacement_refDirection();
		Coordenada origen = new Coordenada(0,0,0);
		
		Vector3D vectorOrigen = Vector3D.ZERO;
		Vector3D diferenciaConOrigen = vectorOrigen.subtract(locationMuro.toVector3D());
		
		Coordenada axisVacio = pSolido.representation.position.axis;
		Coordenada refDirectionVacio = pSolido.representation.position.refDirection;
		
		Vector3D coordenadaTrasladada = original.toVector3D().add(diferenciaConOrigen);

		Coordenada coordenadaRotada; 
		
		if(axisMuro!=null && refDirectionMuro!=null){
			
			//se aplica la rotacion que indica el muro padre
			coordenadaRotada = rotarCoordenada(new Coordenada(coordenadaTrasladada), axisMuro, refDirectionMuro);
			
		}else{
			
			//se aplica la rotacion que indica el vacio
			coordenadaRotada = rotarCoordenada(new Coordenada(coordenadaTrasladada), axisVacio, refDirectionVacio);
		}
		
		Vector3D coordenadaRotadaReubicada = coordenadaRotada.toVector3D().subtract(diferenciaConOrigen); 
		
		return new Coordenada(coordenadaRotadaReubicada);

	}

	private Coordenada aplicarObjectRepresentation(Coordenada coordOriginal, Solido pSolido){
		
		coordOriginal = rotarCoordenada(
				coordOriginal
				, pSolido.representation.position.axis
				, pSolido.representation.position.refDirection 
				);

		Coordenada location = pSolido.representation.position.location;
		
		double xActual = coordOriginal.getX();
		xActual += location.getX();
		
		double yActual = coordOriginal.getY();
		yActual += location.getY();
		
		double zActual = coordOriginal.getZ();
		zActual += location.getZ();
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	
	private Coordenada aplicarObjectPlacement(Coordenada original, Solido pSolido){
		//Hay que rotar primero, o no funciona bien
		Coordenada conRotacion = rotarCoordenada(
				original
				, pSolido.objectPlacement.relativePlacement.axis
				, pSolido.objectPlacement.relativePlacement.refDirection
				);
		
		double xActual = conRotacion.getX();
		
		
		xActual += pSolido.objectPlacement.placementRelTo_placementRelTo.getX();
		xActual += pSolido.objectPlacement.placementRelTo_relativePlacement.getX();
		xActual += pSolido.objectPlacement.relativePlacement.location.getX();
		
		double yActual = conRotacion.getY();
		
		
		yActual += pSolido.objectPlacement.placementRelTo_placementRelTo.getY();
		yActual += pSolido.objectPlacement.placementRelTo_relativePlacement.getY();
		yActual += pSolido.objectPlacement.relativePlacement.location.getY();
		
		double zActual = conRotacion.getZ();
		
		zActual += pSolido.objectPlacement.placementRelTo_placementRelTo.getZ();
		zActual += pSolido.objectPlacement.placementRelTo_relativePlacement.getZ();
		zActual += pSolido.objectPlacement.relativePlacement.location.getZ();
		
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	
	public static Coordenada rotarCoordenada(Coordenada coordOriginal, Coordenada axis, Coordenada refDirection){
		
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
