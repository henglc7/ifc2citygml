
package org.udistrital.ifc2citygmlv2.sbm.ifc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcHalfSpaceSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPlane;
import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.SubLine;
import org.apache.commons.math3.geometry.euclidean.threed.SubPlane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Piso;
import org.udistrital.ifc2citygmlv2.sbm.Poligono;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Segmento;
import org.udistrital.ifc2citygmlv2.util.LectorCoordenada;
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
	
	//solo se usa para imprimir y probar datos
	public List<PlanoDeCorte> planosDeCorte;
	
	
	//este atributo contiene las coordenadas absolutas del perfil de la plancha
	//sin importar si se deriva de representation_points, representation_segmentos o rectangulo
	
	public List<PlanoDeCorte> getPlanosDeCorte() {
		return planosDeCorte;
	}

	public void setPlanosDeCorte(List<PlanoDeCorte> planosDeCorte) {
		this.planosDeCorte = planosDeCorte;
	}

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
	
	public void cortarCaras(){
		
		List<Poligono> nuevasCaras = new ArrayList();
		
		PolyhedronsSet boundingBox = obtenerBoundingBox();
		
		Poligono nuevaCaraSuperior = new Poligono();
		
		for (Poligono caraActual : caras) {
			
			//System.err.println("CARA " + c + " = " +  caraActual);
			
			for(PlanoDeCorte planoActual : planosDeCorte){
				
				planoActual.getCarasACortar().add(new Poligono(caraActual.getCoordenadas()));
				
				Poligono caraCortada = caraActual.cortar(/*caraActual, */planoActual.getPlanoApache()/*, boundingBox*/);
				
				//cualquier poligono debe tener al menos 4 puntos (minimo 3 más el primero repetido para cerrar el poligono)
				if(caraCortada.getCoordenadas().size() >= 4){
					
					nuevasCaras.add(caraCortada);
					
					//se guarda el historico de las caras que se cortaron solo para imprimir en pantalla
					planoActual.getCarasResultado().add(new Poligono(caraCortada.getCoordenadas()));
					
				}
				
				//System.err.println("CARA CORTADA = " + caraCortada);
				
			}
			//System.err.println("\n");
		}
		
		if(this.getId().equals("3Ttjr$59XEWfWN1WUHjelZ")){
			
			//la cara superior deberia calcularse con el metodo intersect del plano de APACHE (intersectar 3 planos)
			//http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/geometry/euclidean/threed/Plane.html#intersection(org.apache.commons.math3.geometry.euclidean.threed.Plane, org.apache.commons.math3.geometry.euclidean.threed.Plane, org.apache.commons.math3.geometry.euclidean.threed.Plane)
			
			//System.err.println("NUEVA CARA SUPERIOR = " + nuevaCaraSuperior);
		}
			
		this.caras = nuevasCaras;
		
	}
	
	public PolyhedronsSet obtenerBoundingBox(){
		
		/*
		List<SubHyperplane> subPlanos = new ArrayList(); 
		 
		 for (Poligono caraActual : caras){
			 
			 Iterator<Coordenada> i = caraActual.getCoordenadas().iterator();
			 
			 int c = 0;
			 
			 Coordenada pa = caraActual.getCoordenadas().get(0);
			 Coordenada pb = caraActual.getCoordenadas().get(1);
			 Coordenada pc = caraActual.getCoordenadas().get(2);
			 
			 Plane planoActual = new Plane(pa.toVector3D(), pb.toVector3D(), pc.toVector3D());
			 
			 SubPlane hiperPlano = planoActual.wholeHyperplane();
			 
			 subPlanos.add(hiperPlano);
			 
		 }
		 
		 PolyhedronsSet poliHedros = new PolyhedronsSet((BSPTree<Euclidean3D>) subPlanos);
		 
		 return poliHedros;
		 
		 */
		
		
		
		Double xMin = null;
		Double yMin = null;
		Double zMin = null;
        
		Double xMax = null;
		Double yMax = null;
		Double zMax = null;
        
        
		 for (Poligono caraActual : caras){
			 
			 Iterator<Coordenada> i = caraActual.getCoordenadas().iterator();
			 
			 int c = 0;
			 
			 while(i.hasNext()){
				 
				 Coordenada coordenadaActual = i.next();
				 
				//minimos
				 
				 if(xMin == null || coordenadaActual.getX() < xMin){
					 xMin = coordenadaActual.getX(); 
				 }
				 
				 if(yMin == null || coordenadaActual.getY() < yMin){
					 yMin = coordenadaActual.getY(); 
				 }
				 
				 if(zMin == null || coordenadaActual.getZ() < zMin){
					 zMin = coordenadaActual.getZ(); 
				 }
				 
				 //maximos
				 
				 if(xMax == null || coordenadaActual.getX() > xMax){
					 xMax = coordenadaActual.getX(); 
				 }
				 
				 if(yMax == null || coordenadaActual.getY() > yMax){
					 yMax = coordenadaActual.getY(); 
				 }
				 
				 if(zMax == null || coordenadaActual.getZ() > zMax){
					 zMax = coordenadaActual.getZ(); 
				 }
			 }
			 
			 
		 }
		 
		 PolyhedronsSet r = new PolyhedronsSet(xMin, xMax, yMin, yMax, zMin, zMax);
		 
		 return r;

		 
	}

}
