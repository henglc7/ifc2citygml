package org.udistrital.ifc2citygml.ifc;

import java.util.ArrayList;
import java.util.List;

public class Edificio {
	
	private String id;
	private List<Piso> pisos;
	
	public Edificio(){
		pisos = new ArrayList();
	}

}
