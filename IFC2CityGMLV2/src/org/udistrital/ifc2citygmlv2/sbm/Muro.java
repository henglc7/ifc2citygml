package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcBooleanClippingResult;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSlab;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcWallStandardCase;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.LIST;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.udistrital.ifc2citygmlv2.sbm.ifc.ISolido;
import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class Muro  extends Solido implements ISolido{
	
	/*
	//Se declara una lista de representations para tener la referencia porque en IFC esta entidad no tiene id global
	//Almacena el STEP line number de cada representation
	private List<String> representationsList;

	public List<String> getRepresentationsList() {
		return representationsList;
	}


	public void setRepresentationsList(List<String> representationsList) {
		this.representationsList = representationsList;
	}
	*/


	public void calcularCoordenadasAbsolutas(){
		
		if(representation_points!=null){
			//coordenadasAbsolutas.addAll(getRepresentation_points());
			
			coordenadasAbsolutas = new ArrayList();
			
			for (Coordenada coordenadaActual : representation_points) {
				
				Coordenada coordAbsoluta = transformador.convertirEnAbsoluta(coordenadaActual, this);
				coordenadasAbsolutas.add(coordAbsoluta);
				
			}
		}else if(representation_segmentos!=null){

			coordenadasAbsolutas = new ArrayList();
			
			int contador = 0;
			
			for (Segmento segmentoActual : representation_segmentos) {
				
				Coordenada primeraCoordenadaDelPoligono = new Coordenada();
				Coordenada coordenadaActual;
				
				coordenadaActual = segmentoActual.getP0();
				
				Coordenada coordAbsoluta = transformador.convertirEnAbsoluta(coordenadaActual, this);
				coordenadasAbsolutas.add(coordAbsoluta);
				
				contador++;
				
				if(contador==representation_segmentos.size()){
					coordenadasAbsolutas.add(coordenadasAbsolutas.get(0));
				}
			}
			
			
		}else if(rectangulo!=null){
			
			//TODO: SUMAR VALORES QUE FALTAN A LAS COORDENADAS DEL RECTANGULO
			List<Coordenada> cuatroEsquinas = new ArrayList();
			
			double ancho = 0;
			double alto = 0;
			
			//eje X relativo igual a ejex X real
			//if(rectangulo.getPosition_refDirection().getX()!=0){
				ancho = rectangulo.getXDim();
				alto = rectangulo.getYDim();
			//}
			
			//eje X relativo igual a eje Y real
			//if(rectangulo.getPosition_refDirection().getY()!=0){
				//ancho = rectangulo.getYDim();
				//alto = rectangulo.getXDim();
			//}
			
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
			
			Coordenada locationRectangulo = rectangulo.getPosition_location();
			//Coordenada refDirectionRectangulo = rectangulo.getPosition_refDirection();
			
			int c = 0;
			Coordenada primera = null;
			for (Coordenada coordenadaActual : cuatroEsquinas) {
				
				Coordenada coordAbsoluta = coordenadaActual;
				
				if(locationRectangulo != null /*&& refDirectionRectangulo != null*/){
					
					double X = locationRectangulo.getX();
					double Y = locationRectangulo.getY();
					
					coordAbsoluta.setX(coordAbsoluta.getX() + X);
					coordAbsoluta.setY(coordAbsoluta.getY() + Y);
					
				}
				
				
				/*Coordenada*/ coordAbsoluta = transformador.convertirEnAbsoluta(coordenadaActual, this);
				
				//coordAbsoluta = transformador.aplicarPositionLocationRectangulo(coordAbsoluta, rectangulo);
				
				
				coordenadasAbsolutas.add(coordAbsoluta);
				
				if(c==0){
					primera = coordAbsoluta;
				}
				c++;
			}
			//se añade la primer para cerrar el poligono
			coordenadasAbsolutas.add(primera);

			
		}
		
		//aplicarObjectPlacement(/*coordenadasAbsolutas*/);
		
	}
	
	
	/*
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
	*/
	
	@Override
	public void generarCaras() {
		
		if(getIfcModel().getIfcObjectByID(getId()) instanceof IfcWallStandardCase){
			
		
		//inicialmente se obtiene la instancia de la plancha en el modelo IFC
		IfcWallStandardCase muroIfc = (IfcWallStandardCase) getIfcModel().getIfcObjectByID(getId());
		
		Iterator<IfcRepresentation> it = muroIfc.getRepresentation().getRepresentations().iterator();
		
		IfcRepresentation item = null;
		
		//System.err.println("Muro = " + this.getId());
		while(it.hasNext()){
			IfcRepresentation representationActual = it.next();
			if(this.representation.getStepLineNumber() == representationActual.getStepLineNumber() ){
				
				item = representationActual;
				//System.err.println("COINCIDEN            = " + this.representation.getStepLineNumber() + " existente = " + representationActual.getStepLineNumber());
				break;
			}else{
				//System.err.println("No coinciden buscado = " + this.representation.getStepLineNumber() + " existente = " + representationActual.getStepLineNumber());
			}
		}
		
		
		
		//la profundidad de la extrusion, que va a afectar las coordenadas en Z
		Double profundidad = 0d;
		
		//System.err.println("Piso = " + getPisoPadre().getNombre());
		//System.err.println("Plancha = " + this.getId());
		
		if(this.representation.getStepLineNumber() > 0){
			
			if(item.getRepresentationType().toString().equals("SweptSolid")){
				IfcExtrudedAreaSolid repItem = (IfcExtrudedAreaSolid) item.getItems().iterator().next();
				profundidad = repItem.getDepth().value;
			}
			
			if(item.getRepresentationType().toString().equals("Clipping")){
				IfcExtrudedAreaSolid repItem = (IfcExtrudedAreaSolid) ((IfcBooleanClippingResult)item.getItems().iterator().next()).getFirstOperand();
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
				
				estaCara.getCoordenadas().add(caraSuperior.getCoordenadas().get(i)); //se añade nuevamente la primera para cerrar
				
				carasLaterales.add(estaCara);
			}
			
			List<Poligono> todasLasCaras = new ArrayList();
			
			todasLasCaras.add(caraSuperior);
			todasLasCaras.addAll(carasLaterales);
			todasLasCaras.add(caraInferior);
			
			//se aplica la primera rotacion
					
			
			this.setCaras(todasLasCaras);			
		}
		
		}// else otros tipos de muro diferentes a IfcWallStandardCase
	
	}
	
	



}
