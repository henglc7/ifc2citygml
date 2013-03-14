package org.udistrital.ifc2citygmlv2.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.udistrital.ifc2citygmlv2.util.BuildingCreator;
import org.udistrital.ifc2citygmlv2.util.LatLonConvert;
import org.udistrital.ifc2citygmlv2.util.LectorPlanchas;
import org.udistrital.ifc2citygmlv2.sbm.Plancha;
import org.udistrital.ifc2citygmlv2.sbm.Edificio;
import org.udistrital.ifc2citygmlv2.sbm.Piso;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcBuildingStorey;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcOpeningElement;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcProduct;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSite;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSlab;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSlabTypeEnum;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.SET;
import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;
import openifctools.com.openifcjavatoolbox.step.parser.util.ProgressEvent;
import openifctools.com.openifcjavatoolbox.step.parser.util.StepParserProgressListener;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class Main {

	private static IfcModel ifcModel = null;
	private static Edificio edificio;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		edificio = new Edificio();
		edificio.setPisos(new ArrayList());

		int[] coordLatitud = null;
		int[] coordLongitud = null;
		
		String rutaArchivo = args[0]; 
    	//"C:\\Actual 2011\\escritorio XP\\modelos\\sabio caldas\\SabioCaldasSimplificado.ifc";

		// The IFC model is loaded in memory
		final File file = new File(rutaArchivo);
		ifcModel = new IfcModel();
		ifcModel.addStepParserProgressListener(new StepParserProgressListener() {
			@Override
			public void progressActionPerformed(final ProgressEvent event) {

				if (((int) event.getCurrentState() % 10 == 0)
						&& (event.getCurrentState() > 0)) {

					System.out.println(event.getMessage() + " - "
							+ event.getCurrentState() + "%");
				}
			}
		});
		try {
			ifcModel.readStepFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (IfcRelContainedInSpatialStructure currentRelation : (Collection<IfcRelContainedInSpatialStructure>) ifcModel
				.getCollection(IfcRelContainedInSpatialStructure.class)) {
			// solo interesa averiguar por los PISOS del edificio
			if (currentRelation.getRelatingStructure() instanceof IfcBuildingStorey) {
				IfcBuildingStorey storey = (IfcBuildingStorey) currentRelation
						.getRelatingStructure();
				// no se tienen en cuenta los pisos subterraneos ni el piso base
				// (elevation = 0)
				if (storey.getElevation().value > 0) {

					Piso pisoActual = new Piso();
					pisoActual.setId(storey.getGlobalId().toString());
					pisoActual.setElevacion(storey.getElevation().value);
					pisoActual.setNombre(storey.getName().toString());

					edificio.getPisos().add(pisoActual);

					System.out.println(pisoActual.getNombre() + " = "
							+ pisoActual.getElevacion() + " ("
							+ pisoActual.getId() + ")");

					SET<IfcProduct> relatedElements = currentRelation
							.getRelatedElements();
					// se buscan las planchas que tenga el piso
					for (IfcProduct product : relatedElements) {
						if (product instanceof IfcSlab) {
							IfcSlab currentSlab = (IfcSlab) product;
							// se descartan las planchas que sean de tipo ROOF
							// (techos)
							if (currentSlab.getPredefinedType().value == IfcSlabTypeEnum.IfcSlabTypeEnum_internal.FLOOR) {
								Plancha planchaActual = new Plancha();
								planchaActual.setId(currentSlab.getGlobalId()
										.toString());
								pisoActual.getPlanchas().add(planchaActual);
								// System.out.println("agregada plancha " +
								// planchaActual.getId());
							}
						}
					}
				}
			}
		}

		// SE LEEN LAS COORDENADAS GEOGRAFICAS
		// siempre devuelve un vector de una sola posicion
		Iterator i = ifcModel.getCollection(IfcSite.class).iterator();
		IfcSite site = (IfcSite) i.next();

		int valores = site.getRefLatitude().size();

		coordLatitud = new int[valores];
		boolean latitudNegativa = false;

		for (int n = 0; n < valores; n++) {

			int valor = site.getRefLatitude().get(n).value;

			if (n == 0 && valor < 0) {
				latitudNegativa = true;
			}
			// debido a que IFC guarda TODOS los valores de la latitud como
			// negativos
			// ese necesario ajustarlos
			// ejemplo IFC : -74°-3'-57.-56400'' (MAL !!!)
			// ajustado : -74° 3' 57.56400'' (OK !!!)
			if (n > 0 && latitudNegativa && valor < 0) {
				valor = valor * -1;
			}

			coordLatitud[n] = valor;

			// System.out.println("lat n = " + n + " valor = " +
			// coordLatitud[n]);
		}

		valores = site.getRefLongitude().size();

		coordLongitud = new int[valores];
		boolean longitudNegativa = false;

		for (int n = 0; n < valores; n++) {
			int valor = site.getRefLongitude().get(n).value;

			if (n == 0 && valor < 0) {
				longitudNegativa = true;
			}
			// debido a que IFC guarda TODOS los valores de la latitud como
			// negativos
			// ese necesario ajustarlos
			// ejemplo IFC : -74°-3'-57.-56400'' (MAL !!!)
			// ajustado : -74° 3' 57.56400'' (OK !!!)
			if (n > 0 && longitudNegativa && valor < 0) {
				valor = valor * -1;
			}

			coordLongitud[n] = valor;

			// System.out.println("lon n = " + n + " valor = " +
			// coordLongitud[n]);
		}

		// para ordenar los pisos de menor a mayor elevacion
		int posA = 0;
		int posB = 0;

		for (posA = 0; posA < (edificio.getPisos().size() - 1); posA++) {
			for (posB = posA + 1; posB < edificio.getPisos().size(); posB++) {
				Piso pisoA = edificio.getPisos().get(posA);
				Piso pisoB = edificio.getPisos().get(posB);
				if (pisoA.getElevacion() > pisoB.getElevacion()) {
					Piso aux = pisoA;
					edificio.getPisos()
							.set(posA, edificio.getPisos().get(posB));
					edificio.getPisos().set(posB, aux);
				}

			}
		}

		double mayorElevacionEncontrada = 0;

		for (posA = 0; posA < edificio.getPisos().size(); posA++) {
			Piso pisoA = edificio.getPisos().get(posA);
			if (pisoA.getElevacion() > mayorElevacionEncontrada)
				mayorElevacionEncontrada = pisoA.getElevacion();
		}

		// se borran los pisos que no tengan planchas
		List<Piso> borrar = new ArrayList();
		for (Piso pisoA : edificio.getPisos()) {
			if (pisoA.getPlanchas() == null || pisoA.getPlanchas().size() < 1) {
				borrar.add(pisoA);
			}
		}

		edificio.getPisos().removeAll(borrar);
		
		LectorPlanchas leerPlanchas = new LectorPlanchas();
        leerPlanchas.leerPlanchas(edificio.getPisos(), ifcModel);
        
        int pisoMinimo = edificio.getPisos().size() - 3;; 
        
/************************ CALCULO DE COORDENADAS GEOGRAFICAS UTM BASADO EN GRADOS MINUTOS Y SEGUNDOS ************************************************************/
        
		//se prueba con las coordenadas del edificio sabio caldas asignadas en el archivo IFC
		// la latitud y longitud reales del edificio son : 4.62787 -74.065849 pero IFC al parecer las trunca
        // se puede probar en http://netvicious.com/gps/
        
        double latitudGrados = coordLatitud[0];
        double latitudMinutos = coordLatitud[1];
        double latitudSegundos = (coordLatitud.length == 3) ? coordLatitud[2] : Double.parseDouble(coordLatitud[2] + "." + coordLatitud[3]);
        
        
        
        double longitudGrados = coordLongitud[0];
        double longitudMinutos = coordLongitud[1];
        double longitudSegundos = (coordLongitud.length == 3) ? coordLongitud[2] : Double.parseDouble(coordLongitud[2] + "." + coordLongitud[3]);
        
		
		LatLonConvert lat = new LatLonConvert(latitudGrados,latitudMinutos,latitudSegundos);
		//System.out.println(lat.getDecimal());
		
		LatLonConvert lon = new LatLonConvert(longitudGrados,longitudMinutos,longitudSegundos);
		//System.out.println(lon.getDecimal());
		
		
		LatLng ll = new LatLng(lat.getDecimal(), lon.getDecimal());
		UTMRef utm = ll.toUTMRef();
		//System.out.println(utm);
		
		// northing is the distance in metres to the equator
		//System.out.println(utm.getNorthing());
		
		// easting is the distance in metres to the false easting — a meridian that is uniquely defined for each UTM zone
		//System.out.println(utm.getEasting());
		

		
		/************************ CALCULO DE COORDENADAS GEOGRAFICAS UTM BASADO EN GRADOS MINUTOS Y SEGUNDOS ************************************************************/        
        
        GeometryFactory fact = new GeometryFactory();
        Geometry unionTodasLasPlanchas = fact.createGeometryCollection(null);
        
        for (Piso pisoA : edificio.getPisos()) {
        	if(edificio.getPisos().indexOf(pisoA) >= pisoMinimo){
        		pisoA.imprimir();
        		//para obtener las coordenadas de IFC se podria invocar pisoA.generarPoligonos(0, 0)
        		//MultiPolygon poligonosPisoActual = fact.createMultiPolygon(pisoA.generarPoligonos(0, 0));
        		MultiPolygon poligonosPisoActual = fact.createMultiPolygon(pisoA.generarPoligonos(utm.getEasting(), utm.getNorthing()));
        		//
        		
        		Geometry unionEstePiso = fact.createGeometryCollection(null);
        		unionEstePiso = unionEstePiso.union(poligonosPisoActual);
        		
        		//System.out.println(unionEstePiso);
        		
        		unionTodasLasPlanchas = unionTodasLasPlanchas.union(unionEstePiso);
        	}
		}
        
        //unionTodasLasPlanchas = unionTodasLasPlanchas.getEnvelope();
        
        System.out.println("GEOMETRIA UNION FINAL = " + unionTodasLasPlanchas);
        
        Coordinate[] coordenadas = unionTodasLasPlanchas.getCoordinates();
        

        BuildingCreator creador = new BuildingCreator();
        try {
			creador.crearModeloLOD1(coordenadas,mayorElevacionEncontrada,args[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
