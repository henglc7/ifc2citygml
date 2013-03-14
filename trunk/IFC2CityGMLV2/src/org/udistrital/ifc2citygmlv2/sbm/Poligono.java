package org.udistrital.ifc2citygmlv2.sbm;

import java.util.ArrayList;
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

}
