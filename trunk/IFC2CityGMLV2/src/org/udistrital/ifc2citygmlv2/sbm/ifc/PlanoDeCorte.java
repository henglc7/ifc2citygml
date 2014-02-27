package org.udistrital.ifc2citygmlv2.sbm.ifc;

import java.util.ArrayList;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcHalfSpaceSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPlane;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Poligono;

public class PlanoDeCorte {

	IfcPlane planoIfc;
	Coordenada locationAbsolutaIfc;
	Coordenada normalAbsolutaIfc;
	
	Plane planoApache;
	
	
	//caras que se van a cortar
	private List<Poligono> carasACortar = new ArrayList();
	
	//caras resultado del corte
	private List<Poligono> carasResultado = new ArrayList();
	
	public PlanoDeCorte(IfcPlane pOriginal, Plane pApache) {
		
		planoIfc = pOriginal;
		planoApache = pApache;
	}
	
	
	public Coordenada getLocationAbsolutaIfc() {
		return locationAbsolutaIfc;
	}


	public void setLocationAbsolutaIfc(Coordenada locationAbsolutaIfc) {
		this.locationAbsolutaIfc = locationAbsolutaIfc;
	}


	public Coordenada getNormalAbsolutaIfc() {
		return normalAbsolutaIfc;
	}


	public void setNormalAbsolutaIfc(Coordenada normalAbsolutaIfc) {
		this.normalAbsolutaIfc = normalAbsolutaIfc;
	}


	
	
	
	public IfcPlane getPlanoIfc() {
		return planoIfc;
	}


	public void setPlanoIfc(IfcPlane planoIfc) {
		this.planoIfc = planoIfc;
	}


	public Plane getPlanoApache() {
		return planoApache;
	}


	public void setPlanoApache(Plane planoApache) {
		this.planoApache = planoApache;
	}
	
	public List<Poligono> getCarasACortar() {
		return carasACortar;
	}


	public void setCarasACortar(List<Poligono> carasACortar) {
		this.carasACortar = carasACortar;
	}


	public List<Poligono> getCarasResultado() {
		return carasResultado;
	}


	public void setCarasResultado(List<Poligono> carasResultado) {
		this.carasResultado = carasResultado;
	}

}
