package org.udistrital.ifc2citygml.ifc;

import java.util.List;

public class Plancha {

	private String id;
	
	// ObjectPlacement
	
	private List<Double> placementRelTo_placementRelTo;
	
	private List<Double> placementRelTo_relativePlacement;
	
	private List<Double> relativePlacement_location;
	
	public List<Double> getRelativePlacement_location() {
		return relativePlacement_location;
	}

	public void setRelativePlacement_location(
			List<Double> relativePlacement_location) {
		this.relativePlacement_location = relativePlacement_location;
	}

	public List<Double> getPlacementRelTo_placementRelTo() {
		return placementRelTo_placementRelTo;
	}

	public void setPlacementRelTo_placementRelTo(
			List<Double> placementRelTo_placementRelTo) {
		this.placementRelTo_placementRelTo = placementRelTo_placementRelTo;
	}

	public List<Double> getPlacementRelTo_relativePlacement() {
		return placementRelTo_relativePlacement;
	}

	public void setPlacementRelTo_relativePlacement(
			List<Double> placementRelTo_relativePlacement) {
		this.placementRelTo_relativePlacement = placementRelTo_relativePlacement;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
