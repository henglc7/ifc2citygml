package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Plancha extends Solido implements ISolido{

	private String id;
	
	// ObjectPlacement
	
	private Coordenada placementRelTo_placementRelTo;
	
	private Coordenada placementRelTo_relativePlacement;
	
	private Coordenada relativePlacement_location;
	
	// Representation
	
	private String representation_representationType;
	
	private String representation_representation_SweptAreaType;
	
	public String getRepresentation_representation_SweptAreaType() {
		return representation_representation_SweptAreaType;
	}

	public void setRepresentation_representation_SweptAreaType(
			String representation_representation_SweptAreaType) {
		this.representation_representation_SweptAreaType = representation_representation_SweptAreaType;
	}

	private Coordenada representation_position_location;
	
	private Coordenada representation_position_axis;
	
	private Coordenada representation_position_refDirection;
	
	private Coordenada representation_extruded_direction;
	
	// estos 3 atributos son mutuamente excluyentes
	
	private List<Coordenada> representation_points;
	
	private List<Segmento> representation_segmentos;
	
	private Rectangulo rectangulo;
	
	//este atributo contiene las coordenadas absolutas del perfil de la plancha
	//sin importar si se deriva de representation_points, representation_segmentos o rectangulo
	
	private List<Coordenada> coordenadasAbsolutas;
	
	
	

	
	public Coordenada getRepresentation_extruded_direction() {
		return representation_extruded_direction;
	}

	public void setRepresentation_extruded_direction(
			Coordenada representation_extruded_direction) {
		this.representation_extruded_direction = representation_extruded_direction;
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

	public Coordenada getRepresentation_position_location() {
		return representation_position_location;
	}

	public void setRepresentation_position_location(
			Coordenada representation_position_location) {
		this.representation_position_location = representation_position_location;
	}

	public Coordenada getRepresentation_position_axis() {
		return representation_position_axis;
	}

	public void setRepresentation_position_axis(
			Coordenada representation_position_axis) {
		this.representation_position_axis = representation_position_axis;
	}

	public Coordenada getRepresentation_position_refDirection() {
		return representation_position_refDirection;
	}

	public void setRepresentation_position_refDirection(
			Coordenada representation_position_refDirection) {
		this.representation_position_refDirection = representation_position_refDirection;
	}

	public String getRepresentation_representationType() {
		return representation_representationType;
	}

	public void setRepresentation_representationType(
			String representation_representationType) {
		this.representation_representationType = representation_representationType;
	}

	public Coordenada getRelativePlacement_location() {
		return relativePlacement_location;
	}

	public void setRelativePlacement_location(
			Coordenada relativePlacement_location) {
		this.relativePlacement_location = relativePlacement_location;
	}

	public Coordenada getPlacementRelTo_placementRelTo() {
		return placementRelTo_placementRelTo;
	}

	public void setPlacementRelTo_placementRelTo(
			Coordenada placementRelTo_placementRelTo) {
		this.placementRelTo_placementRelTo = placementRelTo_placementRelTo;
	}

	public Coordenada getPlacementRelTo_relativePlacement() {
		return placementRelTo_relativePlacement;
	}

	public void setPlacementRelTo_relativePlacement(
			Coordenada placementRelTo_relativePlacement) {
		this.placementRelTo_relativePlacement = placementRelTo_relativePlacement;
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
				double xActual = coordenadaActual.getX();
				
				if(representation_position_refDirection.getX()!=0){
					xActual = xActual * representation_position_refDirection.getX();	
				}
				
				xActual += placementRelTo_placementRelTo.getX();
				xActual += placementRelTo_relativePlacement.getX();
				xActual += relativePlacement_location.getX();
				
				xActual += representation_position_location.getX();
				
				
				double yActual = coordenadaActual.getY();
				
				if(representation_position_refDirection.getY()!=0){
					yActual = yActual * representation_position_refDirection.getY();	
				}
				
				yActual += placementRelTo_placementRelTo.getY();
				yActual += placementRelTo_relativePlacement.getY();
				yActual += relativePlacement_location.getY();
				
				yActual += representation_position_location.getY();
				
				
				Coordenada coord = new Coordenada();
				coord.setX(xActual);
				coord.setY(yActual);
				coordenadasAbsolutas.add(coord);
			}
		}else if(representation_segmentos!=null){

			coordenadasAbsolutas = new ArrayList();
			
			int contador = 0;
			
			for (Segmento segmentoActual : representation_segmentos) {
				
				Coordenada primeraCoordenadaDelPoligono = new Coordenada();
				Coordenada coordenadaActual;
				
				coordenadaActual = segmentoActual.getP0();
				
				double xActual = coordenadaActual.getX();
				
				if(representation_position_refDirection.getX()!=0){
					xActual = xActual * representation_position_refDirection.getX();	
				}
				
				xActual += placementRelTo_placementRelTo.getX();
				xActual += placementRelTo_relativePlacement.getX();
				xActual += relativePlacement_location.getX();
				
				xActual += representation_position_location.getX();
				
				
				double yActual = coordenadaActual.getY();
				
				if(representation_position_refDirection.getY()!=0){
					yActual = yActual * representation_position_refDirection.getY();	
				}
				
				yActual += placementRelTo_placementRelTo.getY();
				yActual += placementRelTo_relativePlacement.getY();
				yActual += relativePlacement_location.getY();
				
				yActual += representation_position_location.getY();
				
				
				Coordenada coord = new Coordenada();
				coord.setX(xActual);
				coord.setY(yActual);
				coordenadasAbsolutas.add(coord);	
				
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
				double xActual = coordenadaActual.getX();
				
				if(representation_position_refDirection.getX()!=0){
					xActual = xActual * representation_position_refDirection.getX();	
				}
				
				xActual += placementRelTo_placementRelTo.getX();
				xActual += placementRelTo_relativePlacement.getX();
				xActual += relativePlacement_location.getX();
				
				xActual += representation_position_location.getX();
				
				
				double yActual = coordenadaActual.getY();
				
				if(representation_position_refDirection.getY()!=0){
					yActual = yActual * representation_position_refDirection.getY();	
				}
				
				yActual += placementRelTo_placementRelTo.getY();
				yActual += placementRelTo_relativePlacement.getY();
				yActual += relativePlacement_location.getY();
				
				yActual += representation_position_location.getY();
				
				
				coord = new Coordenada();
				coord.setX(xActual);
				coord.setY(yActual);
				coordenadasAbsolutas.add(coord);
				
				if(c==0){
					primera = coord;
				}
				c++;
			}
			//se añade la primer para cerrar el poligono
			coordenadasAbsolutas.add(primera);

		}
		
		
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
		//ACA VOY
		
		// calcular las caras y fijar setCaras()
		
	}
	

}
