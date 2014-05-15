package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.Region;

public class Poligono {
	
	private List<Coordenada> coordenadas;

	public Poligono(){
		coordenadas = new ArrayList();
	}
	
	public Poligono(List<Coordenada> coord){
		
		//se clonan las coordenadas
		coordenadas = new ArrayList<Coordenada>(coord);
	}
	
	public List<Coordenada> getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(List<Coordenada> coordenadas) {
		this.coordenadas = coordenadas;
	}
	
	@Override 
	public String toString(){
		
		
		String cadena = " Polygon[";
		
		Iterator<Coordenada> i = this.getCoordenadas().iterator();
		while(i.hasNext()) {
			
			Coordenada coordenadaActual = i.next();
			cadena += "(" + coordenadaActual.getX() + ", " + coordenadaActual.getY() + ", " + coordenadaActual.getZ() + ")" ;
			
			if(i.hasNext()){
				
				cadena += " ,";
				
			}
			
		}
		
		cadena += "]"; 
			
		return cadena;
		
	}
	
	public Poligono cortar(/*Poligono caraActual,*/ Plane planoDeCorte /*, PolyhedronsSet cajaFrontera*/){
		
		
		Vector3D normal = planoDeCorte.getNormal();
		
		Poligono caraCortada = new Poligono();
		
		Iterator<Coordenada> iA = this.getCoordenadas().iterator();
		Iterator<Coordenada> iB = this.getCoordenadas().iterator();
		
		iB.next();
		
		PolyhedronsSet planoComoSolido = planoDeCorte.wholeSpace();
		
		boolean agregarA = true;
		
		//System.err.println("ORIGEN = " + new Coordenada(planoEvaluado.getOrigin()) );
		
		
		
		while(iB.hasNext()) {
			
			Coordenada coordenadaA = iA.next();
			Coordenada coordenadaB = iB.next();
			

			boolean ultimaCoordenadaB = !iB.hasNext();
			
			double posicionA = planoDeCorte.getOffset(coordenadaA.toVector3D());
			double posicionB = planoDeCorte.getOffset(coordenadaB.toVector3D());
			
		
			
			Line lineaDeAHastaB = new Line(coordenadaA.toVector3D(), coordenadaB.toVector3D());
			
			Vector3D interseccion = planoDeCorte.intersection(lineaDeAHastaB);
			
			/*
			Boolean interseccionDentroDeCajaFrontera = null;
			
			if( interseccion != null){
				if(cajaFrontera.checkPoint(interseccion)!= Region.Location.OUTSIDE){
					//la interseccion de la linea con el plano esta DENTRO de la caja frontera del solido
					interseccionDentroDeCajaFrontera = true;
				}else{
					//la interseccion de la linea con el plano esta FUERA de la caja frontera del solido
					interseccionDentroDeCajaFrontera = false;
				}
			}else{
				//la interseccion de la linea con el plano es INEXISTENTE porque son paralelos
			}
			
			if(interseccionDentroDeCajaFrontera!= null && interseccionDentroDeCajaFrontera){
				
				Coordenada i = new Coordenada(interseccion);
				
				if(!caraSuperior.contieneCoordenada(i)) caraSuperior.getCoordenadas().add(i);
			}
			*/
			

			//A y B debajo del plano
			if(posicionA < 0 && posicionB < 0){
				caraCortada.coordenadas.add(coordenadaA);
				
				if(ultimaCoordenadaB) caraCortada.coordenadas.add(coordenadaB);
			}
			
			//A debajo y B en el plano
			if(posicionA < 0 && posicionB == 0){
				caraCortada.coordenadas.add(coordenadaA);
				//caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaB)) caraSuperior.getCoordenadas().add(coordenadaB);
			}
			
			//A debajo y B encima el plano
			if(posicionA < 0 && posicionB > 0){
				
				Coordenada i = new Coordenada(interseccion);
				
				caraCortada.coordenadas.add(coordenadaA);
				caraCortada.coordenadas.add(i);
				
				//if(!caraSuperior.contieneCoordenada(i)) caraSuperior.getCoordenadas().add(i);
			}
			
			//A en y B encima el plano
			if(posicionA == 0 && posicionB > 0){
				
				caraCortada.coordenadas.add(coordenadaA);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaA)) caraSuperior.getCoordenadas().add(coordenadaA);

			}
			
			//A encima y B encima del plano
			if(posicionA > 0 && posicionB > 0){

				//nada

			}
			
			//A y B en el plano
			if(posicionA == 0 && posicionB == 0){

				caraCortada.coordenadas.add(coordenadaA);
				//caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaA)) caraSuperior.getCoordenadas().add(coordenadaA);
				//if(!caraSuperior.contieneCoordenada(coordenadaB)) caraSuperior.getCoordenadas().add(coordenadaB);
				

			}
			
			
			//ahora para B primero y A despues


			
			//B debajo y A en el plano
			if(posicionB < 0 && posicionA == 0){
				//caraCortada.coordenadas.add(coordenadaA);
				if(ultimaCoordenadaB) caraCortada.coordenadas.add(coordenadaA);
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaA)) caraSuperior.getCoordenadas().add(coordenadaA);
			}
			
			//B debajo y A encima el plano
			if(posicionB < 0 && posicionA > 0){
				
				Coordenada i = new Coordenada(interseccion);
				
				caraCortada.coordenadas.add(i);
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(i)) caraSuperior.getCoordenadas().add(i);
				
			}
			
			//B en y A encima el plano
			if(posicionB == 0 && posicionA > 0){
				
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaB)) caraSuperior.getCoordenadas().add(coordenadaB);

			}
			
					
		}
		
		return caraCortada;
		
		
	}
	
	public boolean contieneCoordenada(Coordenada c){
		boolean r = false;
		
		for (Coordenada coordenadaActual : coordenadas) {
			
			if(coordenadaActual.esIgual(c)){
				r = true;
				break;
			}
			
		}
		
		return r;
	}

}
