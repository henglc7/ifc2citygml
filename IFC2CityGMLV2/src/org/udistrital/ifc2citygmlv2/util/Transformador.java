package org.udistrital.ifc2citygmlv2.util;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

public class Transformador {

	public Coordenada aplicarObjectRepresentation(Coordenada coordOriginal, Solido muro){
		Coordenada location = muro.representation.representation_position_location;
		Coordenada refDirection = muro.representation.representation_position_refDirection;
		return aplicarObjectRepresentation(coordOriginal, location, refDirection);
	}
	
	public Coordenada aplicarObjectRepresentation(Coordenada coordOriginal, Coordenada location, Coordenada refDirection){
		
		/*
		if(this.getTipo().equals("ROOF")){
			coordOriginal = aplicarRotacionSegunRepresentation(coordOriginal, muro);
		}
		*/
		double xActual = coordOriginal.getX();
		
		if(refDirection!=null && refDirection.getX()!=0){
			xActual = xActual * refDirection.getX();	
		}
		xActual += location.getX();
		
		double yActual = coordOriginal.getY();
		
		if(refDirection!=null && refDirection.getY()!=0){
			yActual = yActual * refDirection.getY();	
		}
		
		yActual += location.getY();
		
		double zActual = coordOriginal.getZ();
		
		if(refDirection!=null && refDirection.getZ()!=0){
			zActual = zActual * refDirection.getZ();	
		}
		
		zActual += location.getZ();
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	
	public Coordenada aplicarRotacionSegunRepresentation(Coordenada coordOriginal, Solido muro){
		
		Coordenada r = new Coordenada();
		
		Coordenada axis = muro.representation.representation_position_axis;
		Coordenada refDirection = muro.representation.representation_position_refDirection;
		
		
		if(axis != null && refDirection != null){
			
			Vector3D axisX = new Vector3D(1, 0, 0);
			//Vector3D axisY = new Vector3D(0, 1, 0);
			Vector3D axisZ = new Vector3D(0, 0, 1);

			Vector3D deseadoX = new Vector3D(axis.getX(), axis.getY(), axis.getZ()); //(AXIS, eje Z)
			//Vector3D deseadoZ = new Vector3D(refDirection.getX(), refDirection.getY(), refDirection.getZ()); //(refdirection, eje X)
			//Vector3D deseadoY = Vector3D.crossProduct(deseadoX, deseadoZ); // producto cruz
			
			//Rotation rotacionX = new Rotation(axisX,deseadoZ);
			//Rotation rotacionY = new Rotation(axisY,deseadoY);
			Rotation rotacionZ = new Rotation(axisZ,deseadoX);
			
			
			Vector3D punto = new Vector3D(coordOriginal.getX(), coordOriginal.getY(), coordOriginal.getZ());
			//Vector3D puntoRotado = rotacionX.applyTo(punto);
			//puntoRotado = rotacionY.applyTo(puntoRotado);
			Vector3D puntoRotado = rotacionZ.applyTo(punto);
			
			r.setX(puntoRotado.getX());
			r.setY(puntoRotado.getY());
			r.setZ(puntoRotado.getZ());
		}else{
			r = coordOriginal;
		}
		

		return r;
	}
	
	public Coordenada aplicarObjectPlacement(Coordenada original, Solido muro){
		//Hay que rotar primero, o no funciona bien
		Coordenada conRotacion = aplicarRotacionSegunObjectPlacement(original, muro);
		
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
	

	
	
	public Coordenada aplicarRotacionSegunObjectPlacement(Coordenada coordOriginal, Solido muro){
		
		Coordenada r = new Coordenada();
		
		Coordenada axis = muro.objectPlacement.relativePlacement_axis;
		Coordenada refDirection = muro.objectPlacement.relativePlacement_refDirection;
		
		if(axis != null && refDirection != null){
			
			Vector3D axisX = new Vector3D(1, 0, 0);
			//Vector3D axisY = new Vector3D(0, 1, 0);
			Vector3D axisZ = new Vector3D(0, 0, 1);

			Vector3D deseadoX = new Vector3D(axis.getX(), axis.getY(), axis.getZ()); //(AXIS, eje Z)
			Vector3D deseadoZ = new Vector3D(refDirection.getX(), refDirection.getY(), refDirection.getZ()); //(refdirection, eje X)
			//Vector3D deseadoY = Vector3D.crossProduct(deseadoX, deseadoZ); // producto cruz
			
			Rotation rotacionX = new Rotation(axisX,deseadoZ);
			//Rotation rotacionY = new Rotation(axisY,deseadoY);
			Rotation rotacionZ = new Rotation(axisZ,deseadoX);
			
			
			Vector3D punto = new Vector3D(coordOriginal.getX(), coordOriginal.getY(), coordOriginal.getZ());
			Vector3D puntoRotado = rotacionX.applyTo(punto);
			//puntoRotado = rotacionY.applyTo(puntoRotado);
			puntoRotado = rotacionZ.applyTo(puntoRotado);
			
			r.setX(puntoRotado.getX());
			r.setY(puntoRotado.getY());
			r.setZ(puntoRotado.getZ());
		}else{
			r = coordOriginal;
		}
		

		return r;
	}
	
	/*
	public Coordenada aplicarPositionLocationRectangulo(Coordenada coordOriginal, Rectangulo rec){
		
		//Coordenada r = coordOriginal;
		
		Coordenada location = rec.getPosition_location();
		Coordenada refDirection = rec.getPosition_refDirection();
		
		//coordOriginal.setX( coordOriginal.getX() + location.getX());
		//coordOriginal.setY( coordOriginal.getY() + location.getY());
		
		
		if(location != null && refDirection != null){
			
			Vector3D axisZ = new Vector3D(0, 0, 1);

			Vector3D deseadoZ = new Vector3D(refDirection.getX(), refDirection.getY(), refDirection.getZ()); //(AXIS, eje Z)

			Rotation rotacionZ = new Rotation(axisZ,deseadoZ);
			
			
			Vector3D punto = new Vector3D(location.getX(), location.getY(), location.getZ());
			Vector3D locationRotado = rotacionZ.applyTo(punto);
			
			coordOriginal.setX(locationRotado.getX());
			coordOriginal.setY(locationRotado.getY());
			//coordOriginal.setZ(locationRotado.getZ());
		}
		

		return coordOriginal;
		
	}
	*/
	
	/*
	public Coordenada rotarLocationRectangulo(Rectangulo rec){
		
		
		
		
		
	}*/
}
