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
			cadena += "\n  |__ Plancha";
			cadena += "\n      id = " + planchaActual.getId();
			cadena += "\n      placementRelTo";
			cadena += "\n          placementRelTo = [ ";
			for (Double coordenadaActual : planchaActual.getPlacementRelTo_placementRelTo()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			
			cadena += "\n          relativePlacement = [ ";
			for (Double coordenadaActual : planchaActual.getPlacementRelTo_relativePlacement()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			
			cadena += "\n      relativePlacement";
			cadena += "\n          location = [ ";
			for (Double coordenadaActual : planchaActual.getRelativePlacement_location()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			
			cadena += "\n      Representation";
			cadena += "\n          RepresentationType = " + planchaActual.getRepresentation_representationType();
			cadena += "\n          Position ";
			cadena += "\n              Location = [ ";
			for (Double coordenadaActual : planchaActual.getRepresentation_position_location()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			
			cadena += "\n              Axis = [ ";
			for (Double coordenadaActual : planchaActual.getRepresentation_position_axis()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			
			cadena += "\n              RefDirection = [ ";
			for (Double coordenadaActual : planchaActual.getRepresentation_position_refDirection()) {
				cadena += coordenadaActual + " ";
			}
			cadena += "]";
			

		}
		
		
		
		System.out.println(cadena);
	}

}
