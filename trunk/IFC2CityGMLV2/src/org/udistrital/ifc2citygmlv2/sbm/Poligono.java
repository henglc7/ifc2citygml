package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Poligono {
	
	private List<Coordenada> coordenadas;

	public Poligono(){
		coordenadas = new ArrayList();
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

}
