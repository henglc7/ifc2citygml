
package org.udistrital.ifc2citygmlv2.sbm.ifc;

import java.util.List;

import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import org.udistrital.ifc2citygmlv2.sbm.Piso;
import org.udistrital.ifc2citygmlv2.sbm.Poligono;

public class Solido {
	
	private IfcModel ifcModel;
	
	private Piso pisoPadre;

	private String id;
	
	private String tipo;
	
	
	
	
	
	public Placement objectPlacement = new Placement();
	
	public Representation representation = new Representation();
	
	private List<Poligono> caras;

	
	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public IfcModel getIfcModel() {
		return ifcModel;
	}

	public void setIfcModel(IfcModel ifcModel) {
		this.ifcModel = ifcModel;
	}
	
	public Piso getPisoPadre() {
		return pisoPadre;
	}

	public void setPisoPadre(Piso pisoPadre) {
		this.pisoPadre = pisoPadre;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public List<Poligono> getCaras() {
		return caras;
	}

	public void setCaras(List<Poligono> caras) {
		this.caras = caras;
	}

}
