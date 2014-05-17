package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.udistrital.ifc2citygmlv2.sbm.ifc.PlanoDeCorte;
import org.udistrital.ifc2citygmlv2.util.ComparadorAngulos;

import com.vividsolutions.jts.algorithm.CentroidPoint;
import com.vividsolutions.jts.geom.Coordinate;

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
	
	public Poligono cortar(/*Poligono caraActual,*/ PlanoDeCorte pPlano /*, PolyhedronsSet cajaFrontera*/){
		
		
		Plane planoDeCorte = pPlano.getPlanoApache();
		
		List<Coordenada> coordenadasDeCorte = new ArrayList();
		
		
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
				
				coordenadasDeCorte.add(coordenadaB);
			}
			
			//A debajo y B encima el plano
			if(posicionA < 0 && posicionB > 0){
				
				Coordenada i = new Coordenada(interseccion);
				
				caraCortada.coordenadas.add(coordenadaA);
				caraCortada.coordenadas.add(i);
				
				//if(!caraSuperior.contieneCoordenada(i)) caraSuperior.getCoordenadas().add(i);
				
				coordenadasDeCorte.add(new Coordenada(interseccion));
			}
			
			//A en y B encima el plano
			if(posicionA == 0 && posicionB > 0){
				
				caraCortada.coordenadas.add(coordenadaA);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaA)) caraSuperior.getCoordenadas().add(coordenadaA);
				
				coordenadasDeCorte.add(coordenadaA);

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
				
				coordenadasDeCorte.add(coordenadaA);

			}
			
			
			//ahora para B primero y A despues


			
			//B debajo y A en el plano
			if(posicionB < 0 && posicionA == 0){
				//caraCortada.coordenadas.add(coordenadaA);
				if(ultimaCoordenadaB) caraCortada.coordenadas.add(coordenadaA);
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaA)) caraSuperior.getCoordenadas().add(coordenadaA);
				
				coordenadasDeCorte.add(coordenadaA);
			}
			
			//B debajo y A encima el plano
			if(posicionB < 0 && posicionA > 0){
				
				Coordenada i = new Coordenada(interseccion);
				
				caraCortada.coordenadas.add(i);
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(i)) caraSuperior.getCoordenadas().add(i);
				
				coordenadasDeCorte.add(new Coordenada(interseccion));
			}
			
			//B en y A encima el plano
			if(posicionB == 0 && posicionA > 0){
				
				caraCortada.coordenadas.add(coordenadaB);
				
				//if(!caraSuperior.contieneCoordenada(coordenadaB)) caraSuperior.getCoordenadas().add(coordenadaB);
				
				coordenadasDeCorte.add(coordenadaB);

			}
			
					
		}
		
		//caraCortada.setTipo(this.getTipo());
		
		
		if(pPlano.getCaraDeCorte() == null){
			pPlano.setCaraDeCorte(new Poligono());
		}
		
		for (Coordenada coordenada : coordenadasDeCorte) {
			//se evitan insertar coordenadas repetidas, las coordenadas
			//se consideran iguales mediante el metodo "public boolean equals(Object otraCoordenada)" de la clase Coordenada
			if(!pPlano.getCaraDeCorte().getCoordenadas().contains(coordenada)){
				pPlano.getCaraDeCorte().getCoordenadas().add(coordenada);
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
	
	
	
	public void ordenarVerticesRespectoACentroide(){
		
		//se crea un plano que contiene las 3 primeras coordenadas del poligono
		
		Plane plano = null; 

		int n = this.getCoordenadas().size();
		try {
			
			plano = new Plane(this.getCoordenadas().get(0).toVector3D(), this.getCoordenadas().get(1).toVector3D(), this.getCoordenadas().get(2).toVector3D());
			
		} catch (Exception e) {

			//si las 3 primeras coordenadas generan error se escogen las 3 ultimas
			//esto podria hacerse mas sofisticado verificando que las 3 coordenadas no compartan la misma linea que es cuando no se puede crear el pano
			//y se genera la excepcion
			plano = new Plane(this.getCoordenadas().get(n-1).toVector3D(), this.getCoordenadas().get(n-2).toVector3D(), this.getCoordenadas().get(n-3).toVector3D());
		}
		
		
		Vector3D ejeZ = new Vector3D(0,0,1); //Vector que representa al eje Z 
		Rotation rotacionRespectoAZ = new Rotation(plano.getNormal(),ejeZ);
		Rotation rotacionOriginal = new Rotation(ejeZ,plano.getNormal());
		
		List<Coordenada> coordenadasRotadas = new ArrayList<Coordenada>();
		
		CentroidPoint cp = new CentroidPoint();
		double coordenadaZ = 0;
		
		//System.out.println("Puntos Rotados a plano en Z");
		
		for (Coordenada coordActual : this.getCoordenadas()) {
			
			Coordenada rotada = new Coordenada(rotacionRespectoAZ.applyTo(coordActual.toVector3D()));
			coordenadasRotadas.add(rotada);
			
			//System.out.println(rotada);
			
			cp.add(new Coordinate(rotada.getX(), rotada.getY()));
			
			//se puede tomar de cualquier coordenada porque todas comparten el mismo plano en Z
			coordenadaZ = rotada.getZ();
			
		}
		
		Coordinate centroideRotado = cp.getCentroid();
		
		//ordena las coordenadas respecto al centroide para que el poligono se genere correctamente, evitando cruces de bordes por ejemplo
		Collections.sort(coordenadasRotadas, new ComparadorAngulos(centroideRotado));
		
		//se limpian las coordenadas originales del poligono, porque pueden estar en desorden
		this.setCoordenadas(new ArrayList<Coordenada>());
		
		//se devuelven las coordenadas a su ubicacion original
		for (Coordenada coordActual : coordenadasRotadas){
			
			Coordenada original = new Coordenada(rotacionOriginal.applyTo(coordActual.toVector3D()));
			
			this.getCoordenadas().add(original);
		}
		
		
		//el centroide ubicado en su posicion absoluta seria este
		//Vector3D centroideReal = rotacionOriginal.applyTo(new Vector3D(centroideRotado.x, centroideRotado.y, coordenadaZ));
		
	}

}
