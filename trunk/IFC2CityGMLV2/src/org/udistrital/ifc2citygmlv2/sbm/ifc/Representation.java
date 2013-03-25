package org.udistrital.ifc2citygmlv2.sbm.ifc;

import org.udistrital.ifc2citygmlv2.sbm.Coordenada;

public class Representation {

// Representation
	
	public String representation_representationType;
	
	public String representation_representation_SweptAreaType;

	public Coordenada representation_position_location;
	
	public Coordenada representation_position_axis;
	
	public Coordenada representation_position_refDirection;
	
	public Coordenada representation_extruded_direction;

	
	public String getRepresentation_representation_SweptAreaType() {
		return representation_representation_SweptAreaType;
	}

	public void setRepresentation_representation_SweptAreaType(
			String representation_representation_SweptAreaType) {
		this.representation_representation_SweptAreaType = representation_representation_SweptAreaType;
	}

	public Coordenada getRepresentation_extruded_direction() {
		return representation_extruded_direction;
	}

	public void setRepresentation_extruded_direction(
			Coordenada representation_extruded_direction) {
		this.representation_extruded_direction = representation_extruded_direction;
	}

	public Coordenada getRepresentation_position_location() {
		return representation_position_location;
	}

	public void setRepresentation_position_location(
			Coordenada representation_position_location) {
		this.representation_position_location = representation_position_location;
	}

	public Coordenada getRepresentation_position_axis() {
		return representation_position_axis;
	}

	public void setRepresentation_position_axis(
			Coordenada representation_position_axis) {
		this.representation_position_axis = representation_position_axis;
	}

	public Coordenada getRepresentation_position_refDirection() {
		return representation_position_refDirection;
	}

	public void setRepresentation_position_refDirection(
			Coordenada representation_position_refDirection) {
		this.representation_position_refDirection = representation_position_refDirection;
	}

	public String getRepresentation_representationType() {
		return representation_representationType;
	}

	public void setRepresentation_representationType(
			String representation_representationType) {
		this.representation_representationType = representation_representationType;
	}

}
