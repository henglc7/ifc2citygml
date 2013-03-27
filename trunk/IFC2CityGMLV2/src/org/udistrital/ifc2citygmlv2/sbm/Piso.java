
package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

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
			
			cadena += planchaActual.objectPlacement.getPlacementRelTo_placementRelTo().getX() + " ";
			cadena += planchaActual.objectPlacement.getPlacementRelTo_placementRelTo().getY() + " ";
			cadena += planchaActual.objectPlacement.getPlacementRelTo_placementRelTo().getZ() + " ";
			cadena += "]";
			
			cadena += "\n          relativePlacement = [ ";
			cadena += planchaActual.objectPlacement.getPlacementRelTo_relativePlacement().getX() + " ";
			cadena += planchaActual.objectPlacement.getPlacementRelTo_relativePlacement().getY() + " ";
			cadena += planchaActual.objectPlacement.getPlacementRelTo_relativePlacement().getZ() + " ";
			cadena += "]";
			
			cadena += "\n      relativePlacement";
			cadena += "\n          location = [ ";
			cadena += planchaActual.objectPlacement.getRelativePlacement_location().getX() + " ";
			cadena += planchaActual.objectPlacement.getRelativePlacement_location().getY() + " ";
			cadena += planchaActual.objectPlacement.getRelativePlacement_location().getZ() + " ";
			cadena += "]";
			
			cadena += "\n          Axis = [ ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_axis() !=null) ? planchaActual.objectPlacement.getRelativePlacement_axis().getX() : "null") + " ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_axis() !=null) ? planchaActual.objectPlacement.getRelativePlacement_axis().getY() : "null") + " ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_axis() !=null) ? planchaActual.objectPlacement.getRelativePlacement_axis().getZ() : "null") + " ";
			cadena += "]";
			
			cadena += "\n          RefDirection = [ ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_refDirection() !=null) ? planchaActual.objectPlacement.getRelativePlacement_refDirection().getX() : "null") + " ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_refDirection() !=null) ? planchaActual.objectPlacement.getRelativePlacement_refDirection().getY() : "null") + " ";
			cadena += (( planchaActual.objectPlacement.getRelativePlacement_refDirection() !=null) ? planchaActual.objectPlacement.getRelativePlacement_refDirection().getZ() : "null") + " ";
			cadena += "]";
			
			
			cadena += "\n      Representation";
			cadena += "\n          RepresentationType = " + planchaActual.representation.getRepresentation_representationType();
			cadena += "\n          SweptAreaType = " + planchaActual.representation.getRepresentation_representation_SweptAreaType();
			cadena += "\n          Position ";
			cadena += "\n              Location = [ ";
			cadena += planchaActual.representation.getRepresentation_position_location().getX() + " ";
			cadena += planchaActual.representation.getRepresentation_position_location().getY() + " ";
			cadena += planchaActual.representation.getRepresentation_position_location().getZ() + " ";
			cadena += "]";

			

			cadena += "\n              Axis = [ ";
			cadena += (( planchaActual.representation.getRepresentation_position_axis()!=null) ? planchaActual.representation.getRepresentation_position_axis().getX() : "null") + " ";
			cadena += (( planchaActual.representation.getRepresentation_position_axis()!=null) ? planchaActual.representation.getRepresentation_position_axis().getY() : "null") + " ";
			cadena += (( planchaActual.representation.getRepresentation_position_axis()!=null) ? planchaActual.representation.getRepresentation_position_axis().getZ() : "null") + " ";
			cadena += "]";
			
			cadena += "\n              RefDirection = [ ";
			cadena += (( planchaActual.representation.getRepresentation_position_refDirection()!=null) ? planchaActual.representation.getRepresentation_position_refDirection().getX() : "null") + " ";
			cadena += (( planchaActual.representation.getRepresentation_position_refDirection()!=null) ? planchaActual.representation.getRepresentation_position_refDirection().getY() : "null") + " ";
			cadena += (( planchaActual.representation.getRepresentation_position_refDirection()!=null) ? planchaActual.representation.getRepresentation_position_refDirection().getZ() : "null") + " ";
			cadena += "]";
			
			
			cadena += "\n          ExtrudedDirection = [ ";
			cadena += planchaActual.representation.getRepresentation_extruded_direction().getX() + " ";
			cadena += planchaActual.representation.getRepresentation_extruded_direction().getY() + " ";
			cadena += planchaActual.representation.getRepresentation_extruded_direction().getZ() + " ";
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
			
			if(planchaActual.getCoordenadasAbsolutas()!=null){
				cadena += "\n          Absolutas (" + planchaActual.getCoordenadasAbsolutas().size() + ") = [ ";
				for (Coordenada coordenadaActual : planchaActual.getCoordenadasAbsolutas()) {
					cadena += coordenadaActual.getX() + " " + coordenadaActual.getY() + " " + coordenadaActual.getZ() + " , ";
				}
				cadena += "]";
			}
			
			cadena += "\n          Piso Padre";
			cadena += "\n              Id = " + planchaActual.getPisoPadre().getId();
			cadena += "\n              Nombre = " + planchaActual.getPisoPadre().getNombre();
			cadena += "\n              Elevacion = " + planchaActual.getPisoPadre().getElevacion();

		}
		
		
		
		System.out.println(cadena);
	}
	
	public Polygon[] generarPoligonos(double easting, double northing){
		
		int planchasTipoFloor = 0;
		for (Plancha planchaActual : getPlanchas()){
			if(planchaActual.getTipo().equals("FLOOR") || planchaActual.getTipo().equals("BASESLAB")){
				planchasTipoFloor++;
			}
		}
		
		
		Polygon[] poligonos = new Polygon[planchasTipoFloor];
		int c=0;
		for (Plancha planchaActual : getPlanchas()){
			//solo se toman en cuenta las planchas que sea de tipo "FLOOR,BASESLAB", se descartan "ROOF,LANDING,USERDEFINED,NOTDEFINED"
			if(planchaActual.getTipo().equals("FLOOR") || planchaActual.getTipo().equals("BASESLAB")){
				
				Polygon poligonoActual = planchaActual.generarPoligono(easting, northing);
				poligonos[c] = poligonoActual;
				c++;
				
			}
			
		}
		
		return poligonos;
	}

}
