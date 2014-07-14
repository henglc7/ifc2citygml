package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;
import org.udistrital.ifc2citygmlv2.util.Transformador;

public class Vacio extends Solido{
	
	Muro muroAlQueVacia = null;

	public Muro getMuroAlQueVacia() {
		return muroAlQueVacia;
	}

	public void setMuroAlQueVacia(Muro muroAlQueVacia) {
		this.muroAlQueVacia = muroAlQueVacia;
	}

	public void calcularCarasInternas(){
		
		if(this.getTipo()!= null && this.getTipo().equals("ventana")){
			
			for (Poligono caraVacioActual : this.getCaras()) {
				
				for (Poligono caraMuroActual : muroAlQueVacia.getCaras()) {
					
					if(caraMuroActual.compartePlanoCon(caraVacioActual)){
						
						caraVacioActual.setInterno(true);
						
						caraMuroActual.getCarasInternas().add(caraVacioActual);
						
						//si la cara interna toca alguno de los bordes del muro hay que recortar la silueta del mur
						//se puede hacer con JTS ? creo que si ! (funcion diff del polygon)
					}
					
				}
				
			}
			
		}
		
		
	}
	
	public void rotarCoordenadasVacio(){
		
		//inicialmente se rotan las coordenadas absolutas
		//esto simplemente sirve para hacer que coincidan con la ubicacion final de las caras
		//las caras ya estaban calculadas anteriormente con base en las coordenadas absolutas sin rotar
		List absolutasOriginales = this.coordenadasAbsolutas;
		List absolutasRotadas = new ArrayList();
		
		for (Coordenada coordenadaActual : coordenadasAbsolutas) {
			
			Coordenada rotada = Transformador.rotarCoordenadaVacio(coordenadaActual, this);
			absolutasRotadas.add(rotada);
			
		}
		
		this.setCoordenadasAbsolutas(absolutasRotadas);
		
		
		//posteriormente se rota cada una de las coordenadas que somponen las caras del vacio

		List<Poligono> carasOriginales = this.getCaras();
		List<Poligono> carasRotadas = new ArrayList();
		
		for (Poligono caraActual : carasOriginales) {
			
			Poligono caraRotada = new Poligono();
			
			for (Coordenada coordenadaActual : caraActual.getCoordenadas()) {
				
				Coordenada rotada = Transformador.rotarCoordenadaVacio(coordenadaActual, this);
				
				caraRotada.getCoordenadas().add(rotada);
				
			}
			
			carasRotadas.add(caraRotada);
		}
		
		this.setCaras(carasRotadas);
		
	}
}
