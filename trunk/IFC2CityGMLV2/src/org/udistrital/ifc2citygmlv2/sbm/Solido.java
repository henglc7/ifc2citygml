
package org.udistrital.ifc2citygmlv2.sbm;

import java.util.List;

import org.udistrital.ifc2citygmlv2.sbm.ifc.Placement;
import org.udistrital.ifc2citygmlv2.sbm.ifc.Representation;

public class Solido {
	
	public Placement placement = new Placement();
	
	public Representation representation = new Representation();
	
	private List<Poligono> caras;

	public List<Poligono> getCaras() {
		return caras;
	}

	public void setCaras(List<Poligono> caras) {
		this.caras = caras;
	}

}
