package org.udistrital.ifc2citygml.ifc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Piso {
	
	private String id;
	private String nombre;
	private double elevacion;
	private List<Plancha> planchas;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getElevacion() {
		return elevacion;
	}

	public void setElevacion(double elevacion) {
		this.elevacion = elevacion;
	}

	public List<Plancha> getPlanchas() {
		return planchas;
	}

	public void setPlanchas(List<Plancha> planchas) {
		this.planchas = planchas;
	}

	
	public Piso(){
		planchas = new ArrayList();
	}
	
	public void imprimir(){
		String cadena = "";
		cadena += "\nPiso \"" + getNombre() + "\"  ( id=" + getId() + " , elevacion=" + getElevacion() + " )";
		
		cadena += "\n  " + getPlanchas().size() + " Planchas:";
		for (Plancha planchaActual : getPlanchas()) {
			cadena += "\n  |__ Plancha ( id=" + planchaActual.getId() + " , ";
			cadena += "placementRelTo=[ ";
			for (Double coordenadaActual : planchaActual.getPlacementRelTo()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			cadena += " )";	
		}
		
		
		
		System.out.println(cadena);
	}

}
