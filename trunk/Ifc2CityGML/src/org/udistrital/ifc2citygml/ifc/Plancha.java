package org.udistrital.ifc2citygml.ifc;

import java.util.List;

public class Plancha {

	private String id;
	
	// ObjectPlacement
	
	private List<Double> placementRelTo;
	
	private List<Double> relativePlacement;
	
	

	public List<Double> getRelativePlacement() {
		return relativePlacement;
	}

	public void setRelativePlacement(List<Double> relativePlacement) {
		this.relativePlacement = relativePlacement;
	}

	public List<Double> getPlacementRelTo() {
		return placementRelTo;
	}

	public void setPlacementRelTo(List<Double> placementRelTo) {
		this.placementRelTo = placementRelTo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
