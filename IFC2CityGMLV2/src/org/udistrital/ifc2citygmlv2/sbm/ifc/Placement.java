package org.udistrital.ifc2citygmlv2.sbm.ifc;

import org.udistrital.ifc2citygmlv2.sbm.Coordenada;

public class Placement {

	// ObjectPlacement
	
	public Coordenada placementRelTo_placementRelTo;
	
	public Coordenada placementRelTo_relativePlacement;
	
	public Coordenada relativePlacement_location;
	
	public Coordenada relativePlacement_axis;
	
	public Coordenada relativePlacement_refDirection;
	
	
	
	public Coordenada getRelativePlacement_axis() {
		return relativePlacement_axis;
	}

	public void setRelativePlacement_axis(Coordenada relativePlacement_axis) {
		this.relativePlacement_axis = relativePlacement_axis;
	}

	public Coordenada getRelativePlacement_refDirection() {
		return relativePlacement_refDirection;
	}

	public void setRelativePlacement_refDirection(
			Coordenada relativePlacement_refDirection) {
		this.relativePlacement_refDirection = relativePlacement_refDirection;
	}

	public Coordenada getRelativePlacement_location() {
		return relativePlacement_location;
	}

	public void setRelativePlacement_location(
			Coordenada relativePlacement_location) {
		this.relativePlacement_location = relativePlacement_location;
	}

	public Coordenada getPlacementRelTo_placementRelTo() {
		return placementRelTo_placementRelTo;
	}

	public void setPlacementRelTo_placementRelTo(
			Coordenada placementRelTo_placementRelTo) {
		this.placementRelTo_placementRelTo = placementRelTo_placementRelTo;
	}

	public Coordenada getPlacementRelTo_relativePlacement() {
		return placementRelTo_relativePlacement;
	}

	public void setPlacementRelTo_relativePlacement(
			Coordenada placementRelTo_relativePlacement) {
		this.placementRelTo_relativePlacement = placementRelTo_relativePlacement;
	}

}
