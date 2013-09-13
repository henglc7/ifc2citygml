
package org.udistrital.ifc2citygmlv2.sbm.ifc;

import java.util.ArrayList;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Piso;
import org.udistrital.ifc2citygmlv2.sbm.Poligono;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Segmento;
import org.udistrital.ifc2citygmlv2.util.Transformador;

public class Solido {
	
	private IfcModel ifcModel;
	
	private Piso pisoPadre;

	private String id;
	
	private String tipo;
	
	public Transformador transformador = new Transformador();
	
	
	
	public Placement objectPlacement = new Placement();
	
	public Representation representation = new Representation();
	
	//el listado de caras se inicializa, aunque puede que permanezca siempre vacío
	private List<Poligono> caras = new ArrayList();

	
	// estos 3 atributos son mutuamente excluyentes
	
	protected List<Coordenada> representation_points;
	
	protected List<Segmento> representation_segmentos;
	
	protected Rectangulo rectangulo;
	
	
	//este atributo contiene las coordenadas absolutas del perfil de la plancha
	//sin importar si se deriva de representation_points, representation_segmentos o rectangulo
	
	protected List<Coordenada> coordenadasAbsolutas;
	

	
	
	public List<Coordenada> getCoordenadasAbsolutas() {
		return coordenadasAbsolutas;
	}

	public void setCoordenadasAbsolutas(List<Coordenada> coordenadasAbsolutas) {
		this.coordenadasAbsolutas = coordenadasAbsolutas;
	}
	
	
	public Rectangulo getRectangulo() {
		return rectangulo;
	}

	public void setRectangulo(Rectangulo rectangulo) {
		this.rectangulo = rectangulo;
	}

	public List<Segmento> getRepresentation_segmentos() {
		return representation_segmentos;
	}

	public void setRepresentation_segmentos(List<Segmento> representation_segmentos) {
		this.representation_segmentos = representation_segmentos;
	}

	public List<Coordenada> getRepresentation_points() {
		return representation_points;
	}

	public void setRepresentation_points(List<Coordenada> representation_points) {
		this.representation_points = representation_points;
	}

	

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
