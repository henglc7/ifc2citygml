package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

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
}
