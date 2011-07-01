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
			cadena += planchaActual.getPlacementRelTo_placementRelTo().getX() + " ";
			cadena += planchaActual.getPlacementRelTo_placementRelTo().getY() + " ";
			cadena += planchaActual.getPlacementRelTo_placementRelTo().getZ() + " ";
			cadena += "]";
			
			cadena += "\n          relativePlacement = [ ";
			cadena += planchaActual.getPlacementRelTo_relativePlacement().getX() + " ";
			cadena += planchaActual.getPlacementRelTo_relativePlacement().getY() + " ";
			cadena += planchaActual.getPlacementRelTo_relativePlacement().getZ() + " ";
			cadena += "]";
			
			cadena += "\n      relativePlacement";
			cadena += "\n          location = [ ";
			cadena += planchaActual.getRelativePlacement_location().getX() + " ";
			cadena += planchaActual.getRelativePlacement_location().getY() + " ";
			cadena += planchaActual.getRelativePlacement_location().getZ() + " ";
			cadena += "]";
			
			cadena += "\n      Representation";
			cadena += "\n          RepresentationType = " + planchaActual.getRepresentation_representationType();
			cadena += "\n          SweptAreaType = " + planchaActual.getRepresentation_representation_SweptAreaType();
			cadena += "\n          Position ";
			cadena += "\n              Location = [ ";
			cadena += planchaActual.getRepresentation_position_location().getX() + " ";
			cadena += planchaActual.getRepresentation_position_location().getY() + " ";
			cadena += planchaActual.getRepresentation_position_location().getZ() + " ";
			cadena += "]";
			
			cadena += "\n              Axis = [ ";
			cadena += planchaActual.getRepresentation_position_axis().getX() + " ";
			cadena += planchaActual.getRepresentation_position_axis().getY() + " ";
			cadena += planchaActual.getRepresentation_position_axis().getZ() + " ";
			cadena += "]";
			
			cadena += "\n              RefDirection = [ ";
			cadena += planchaActual.getRepresentation_position_refDirection().getX() + " ";
			cadena += planchaActual.getRepresentation_position_refDirection().getY() + " ";
			cadena += planchaActual.getRepresentation_position_refDirection().getZ() + " ";
			cadena += "]";
			
			if(planchaActual.getRepresentation_points()!=null){
				cadena += "\n          Puntos (" + planchaActual.getRepresentation_points().size() + ") = [ ";
				for (Coordenada coordenadaActual : planchaActual.getRepresentation_points()) {
					cadena += coordenadaActual.getX() + " " + coordenadaActual.getY() + " , ";
				}
				cadena += "]";
			}
			
			if(planchaActual.getRepresentation_segmentos()!=null){
				cadena += "\n          Segmentos (" + planchaActual.getRepresentation_segmentos().size() + ") = [ ";
				for (Segmento segmentoActual : planchaActual.getRepresentation_segmentos()) {
					cadena += segmentoActual.getP0().getX() + " " + segmentoActual.getP0().getY() + " , ";
					cadena += segmentoActual.getP1().getX() + " " + segmentoActual.getP1().getY() + " , ";
				}
				cadena += "]";
			}
			
			if(planchaActual.getRectangulo()!=null){
				Rectangulo rec = planchaActual.getRectangulo(); 
				cadena += "\n          Rectangulo";
				cadena += "\n              Location = [ " + rec.getPosition_location().getX() + " " + rec.getPosition_location().getY() + " ]";
				cadena += "\n              RefDirection = [ " + rec.getPosition_refDirection().getX() + " " + rec.getPosition_refDirection().getY() + " ]";
				cadena += "\n              XDim = " + rec.getXDim();
				cadena += "\n              YDim = " + rec.getYDim();
			}

		}
		
		
		
		System.out.println(cadena);
	}

}
