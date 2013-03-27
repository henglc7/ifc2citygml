package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSlab;
import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Plancha extends Solido implements ISolido{
	
	private IfcModel ifcModel;
	
	private Piso pisoPadre;

	private String id;
	
	private String tipo;
	

	// estos 3 atributos son mutuamente excluyentes
	
	private List<Coordenada> representation_points;
	
	private List<Segmento> representation_segmentos;
	
	private Rectangulo rectangulo;
	
	//este atributo contiene las coordenadas absolutas del perfil de la plancha
	//sin importar si se deriva de representation_points, representation_segmentos o rectangulo
	
	private List<Coordenada> coordenadasAbsolutas;
	

	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public IfcModel getIfcModel() {
		return ifcModel;
	}

	public void setIfcModel(IfcModel ifcModel) {
		this.ifcModel = ifcModel;
	}
	
	public Piso getPisoPadre() {
		return pisoPadre;
	}

	public void setPisoPadre(Piso pisoPadre) {
		this.pisoPadre = pisoPadre;
	}
	
	public List<Coordenada> getCoordenadasAbsolutas() {
		return coordenadasAbsolutas;
	}

	public void setCoordenadasAbsolutas(List<Coordenada> coordenadasAbsolutas) {
		this.coordenadasAbsolutas = coordenadasAbsolutas;
	}

	public Rectangulo getRectangulo() {
		return rectangulo;
	}

	public void setRectangulo(Rectangulo rectangulo) {
		this.rectangulo = rectangulo;
	}

	public List<Segmento> getRepresentation_segmentos() {
		return representation_segmentos;
	}

	public void setRepresentation_segmentos(List<Segmento> representation_segmentos) {
		this.representation_segmentos = representation_segmentos;
	}

	public List<Coordenada> getRepresentation_points() {
		return representation_points;
	}

	public void setRepresentation_points(List<Coordenada> representation_points) {
		this.representation_points = representation_points;
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void calcularCoordenadasAbsolutas(){
		
		if(representation_points!=null){
			//coordenadasAbsolutas.addAll(getRepresentation_points());
			
			coordenadasAbsolutas = new ArrayList();
			
			for (Coordenada coordenadaActual : representation_points) {
				
				Coordenada coordAbsoluta = aplicarObjectRepresentation(coordenadaActual);
				coordAbsoluta = aplicarObjectPlacement(coordAbsoluta);
				coordenadasAbsolutas.add(coordAbsoluta);
				
			}
		}else if(representation_segmentos!=null){

			coordenadasAbsolutas = new ArrayList();
			
			int contador = 0;
			
			for (Segmento segmentoActual : representation_segmentos) {
				
				Coordenada primeraCoordenadaDelPoligono = new Coordenada();
				Coordenada coordenadaActual;
				
				coordenadaActual = segmentoActual.getP0();
				
				Coordenada coordAbsoluta = aplicarObjectRepresentation(coordenadaActual);
				coordAbsoluta = aplicarObjectPlacement(coordAbsoluta);
				coordenadasAbsolutas.add(coordAbsoluta);
				
				contador++;
				
				if(contador==representation_segmentos.size()){
					coordenadasAbsolutas.add(coordenadasAbsolutas.get(0));
				}
			}
			
			
		}else if(rectangulo!=null){
			
			List<Coordenada> cuatroEsquinas = new ArrayList();
			
			double ancho = 0;
			double alto = 0;
			
			//eje X relativo igual a ejex X real
			if(rectangulo.getPosition_refDirection().getX()!=0){
				ancho = rectangulo.getXDim();
				alto = rectangulo.getYDim();
			}
			
			//eje X relativo igual a eje Y real
			if(rectangulo.getPosition_refDirection().getY()!=0){
				ancho = rectangulo.getYDim();
				alto = rectangulo.getXDim();
			}
			
			Coordenada coord = new Coordenada();
			coord.setX((ancho / 2) * -1);
			coord.setY((alto / 2) * -1);
			cuatroEsquinas.add(coord); //inferior ixzquierda
			
			coord = new Coordenada();
			coord.setX((ancho / 2) * -1);
			coord.setY((alto / 2));
			cuatroEsquinas.add(coord);//superior izquierda
			
			coord = new Coordenada();
			coord.setX((ancho / 2));
			coord.setY((alto / 2));
			cuatroEsquinas.add(coord);//superior derecha
			
			coord = new Coordenada();
			coord.setX((ancho / 2));
			coord.setY((alto / 2) * -1);
			cuatroEsquinas.add(coord);//superior derecha
			
			coordenadasAbsolutas = new ArrayList();
			
			int c = 0;
			Coordenada primera = null;
			for (Coordenada coordenadaActual : cuatroEsquinas) {
				
				Coordenada coordAbsoluta = aplicarObjectRepresentation(coordenadaActual);
				coordAbsoluta = aplicarObjectPlacement(coordAbsoluta);
				coordenadasAbsolutas.add(coordAbsoluta);
				
				if(c==0){
					primera = coordAbsoluta;
				}
				c++;
			}
			//se a�ade la primer para cerrar el poligono
			coordenadasAbsolutas.add(primera);

			
		}
		
		//aplicarObjectPlacement(/*coordenadasAbsolutas*/);
		
	}
	
	
	public Coordenada aplicarObjectPlacement(Coordenada original){
		//Hay que rotar primero, o no funciona bien
		Coordenada conRotacion = aplicarRotacionSegunObjectPlacement(original);
		
		double xActual = conRotacion.getX();
		
		
		xActual += objectPlacement.placementRelTo_placementRelTo.getX();
		xActual += objectPlacement.placementRelTo_relativePlacement.getX();
		xActual += objectPlacement.relativePlacement_location.getX();
		
		double yActual = conRotacion.getY();
		
		
		yActual += objectPlacement.placementRelTo_placementRelTo.getY();
		yActual += objectPlacement.placementRelTo_relativePlacement.getY();
		yActual += objectPlacement.relativePlacement_location.getY();
		
		double zActual = conRotacion.getZ();
		
		zActual += objectPlacement.placementRelTo_placementRelTo.getZ();
		zActual += objectPlacement.placementRelTo_relativePlacement.getZ();
		zActual += objectPlacement.relativePlacement_location.getZ();
		
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		return coord;
		
	}
	
	public Coordenada aplicarObjectRepresentation(Coordenada coordOriginal){
		
		//Coordenada conRotacion = aplicarRotacionSegunRepresentation(coordOriginal);
		
		double xActual = coordOriginal.getX();
		
		if(representation.representation_position_refDirection.getX()!=0){
			xActual = xActual * representation.representation_position_refDirection.getX();	
		}
		xActual += representation.representation_position_location.getX();
		
		double yActual = coordOriginal.getY();
		
		if(representation.representation_position_refDirection.getY()!=0){
			yActual = yActual * representation.representation_position_refDirection.getY();	
		}
		
		yActual += representation.representation_position_location.getY();
		
		double zActual = coordOriginal.getZ();
		
		if(representation.representation_position_refDirection.getZ()!=0){
			zActual = zActual * representation.representation_position_refDirection.getZ();	
		}
		
		zActual += representation.representation_position_location.getZ();
		
		Coordenada coord = new Coordenada(xActual, yActual, zActual);
		
		
		
		return coord;
		
	}
	
	
	public Coordenada aplicarRotacionSegunObjectPlacement(Coordenada coordOriginal){
		
		Coordenada r = new Coordenada();
		
		Coordenada axis = this.objectPlacement.relativePlacement_axis;
		Coordenada refDirection = this.objectPlacement.relativePlacement_refDirection;
		
		
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
			
		

		return r;
	}
	
	public Coordenada aplicarRotacionSegunRepresentation(Coordenada coordOriginal){
		
		Coordenada r = new Coordenada();
		
		Coordenada axis = this.representation.representation_position_axis;
		Coordenada refDirection = this.representation.representation_position_refDirection;
		
		
		Vector3D axisX = new Vector3D(1, 0, 0);
		//Vector3D axisY = new Vector3D(0, 1, 0);
		Vector3D axisZ = new Vector3D(0, 0, 1);

		Vector3D deseadoX = new Vector3D(axis.getX(), axis.getY(), axis.getZ()); //(AXIS, eje Z)
		//Vector3D deseadoX = new Vector3D(0, 0, 1); //(AXIS, eje Z)
		Vector3D deseadoZ = new Vector3D(refDirection.getX(), refDirection.getY(), refDirection.getZ()); //(refdirection, eje X)
		
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
			
		

		return r;
	}
	
	
	public Polygon generarPoligono(double easting, double northing){
	    // create a factory using default values (e.g. floating precision)
	    GeometryFactory fact = new GeometryFactory();

	    Coordinate[] coordenadas = new Coordinate[coordenadasAbsolutas.size()];
	    int c=0;
	    	
	    for (Coordenada coordenadaActual : coordenadasAbsolutas){
	    	Coordinate coord = 
	    		new Coordinate(coordenadaActual.getX() + easting, coordenadaActual.getY() + northing, 0);
	    	coordenadas[c] = coord;
	    	c++;
	    }
	    
	     
	    LinearRing anillo = fact.createLinearRing(coordenadas);
	    
	    Polygon poligono = fact.createPolygon(anillo, null);
	    
	    return poligono;
	}

	@Override
	public void generarCaras() {
		
		//inicialmente se obtiene la instancia de la plancha en el modelo IFC
		IfcSlab planchaIfc = (IfcSlab) getIfcModel().getIfcObjectByID(getId());
		IfcRepresentation item = planchaIfc.getRepresentation().getRepresentations().get(0);
		
		//la profundidad de la extrusion, que va a afectar las coordenadas en Z
		Double profundidad = 0d;
		
		//System.err.println("Piso = " + getPisoPadre().getNombre());
		//System.err.println("Plancha = " + this.getId());

		if(item.getRepresentationType().toString().equals("SweptSolid")){
			IfcExtrudedAreaSolid repItem = (IfcExtrudedAreaSolid) item.getItems().iterator().next();
			profundidad = repItem.getDepth().value;
		}
		
				
		Poligono caraSuperior = new Poligono();
		
		for (Coordenada coordenadaActual : coordenadasAbsolutas) {
			
			Coordenada c = new Coordenada(coordenadaActual.getX(), coordenadaActual.getY(), coordenadaActual.getZ() );
			caraSuperior.getCoordenadas().add(c);
			
		}
		
		Poligono caraInferior = new Poligono();
		
		for (Coordenada coordenadaActual : coordenadasAbsolutas) {
			
			Coordenada c = new Coordenada(coordenadaActual.getX(), coordenadaActual.getY(), coordenadaActual.getZ() + profundidad);
			caraInferior.getCoordenadas().add(c);
			
		}
		
		List<Poligono> carasLaterales = new ArrayList();
		
		Integer puntos = caraSuperior.getCoordenadas().size();
		
		for(int i=0; i<puntos-1; i++){
			Poligono estaCara = new Poligono();
			
			estaCara.getCoordenadas().add(caraSuperior.getCoordenadas().get(i));
			estaCara.getCoordenadas().add(caraSuperior.getCoordenadas().get(i+1));
			
			estaCara.getCoordenadas().add(caraInferior.getCoordenadas().get(i+1));
			estaCara.getCoordenadas().add(caraInferior.getCoordenadas().get(i));
			
			estaCara.getCoordenadas().add(caraSuperior.getCoordenadas().get(i)); //se a�ade nuevamente la primera para cerrar
			
			carasLaterales.add(estaCara);
		}
		
		List<Poligono> todasLasCaras = new ArrayList();
		
		todasLasCaras.add(caraSuperior);
		todasLasCaras.addAll(carasLaterales);
		todasLasCaras.add(caraInferior);
		
		//se aplica la primera rotacion
				
		
		this.setCaras(todasLasCaras);
		
	}
	

}