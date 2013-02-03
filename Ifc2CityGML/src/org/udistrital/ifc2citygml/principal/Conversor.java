package org.udistrital.ifc2citygml.principal;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.udistrital.ifc2citygml.modelo.Coordenada;
import org.udistrital.ifc2citygml.modelo.Edificio;
import org.udistrital.ifc2citygml.modelo.Piso;
import org.udistrital.ifc2citygml.modelo.Plancha;
import org.udistrital.ifc2citygml.util.BuildingCreator;
import org.udistrital.ifc2citygml.util.LatLonConvert;
import org.udistrital.ifc2citygml.util.LectorPlanchas;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;


public class Conversor {
	
	private static Edificio edificio; 

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {

		//List<Piso> pisos = new ArrayList();;
		edificio = new Edificio();
		edificio.setPisos(new ArrayList());
        ReleaseManager rm = new ReleaseManager();
      
        int[] coordLatitud = null;
        int[] coordLongitud = null;
        
        String rutaArchivo = args[0]; 
        	//"C:\\Actual 2011\\escritorio XP\\modelos\\sabio caldas\\SabioCaldasSimplificado.ifc";

        
        try {
        	
        	//loadDll();

            //Se declara una instancia del componente ActiveX pasando el nombre como parametro
            IDispatch object = new IDispatch(rm, "IFCsvr.R300");
            
            String tipoEntidad = "IfcRelContainedInSpatialStructure";

            Object[] obj = new Object[1];
            obj[0] = rutaArchivo;

            //Se abre un diseno IFC
            IDispatch design = (IDispatch) object.method("OpenDesign", obj);

            Object o;

            //se cuenta el numero de entidades
            obj[0] = tipoEntidad;
            o = design.method("CountEntities", obj);
            String conteo = String.valueOf(o);
            //System.out.println("Conteo de " + tipoEntidad + " = " + conteo + "\n");

            
            //se obtiene el listado de entidades
            obj[0] = tipoEntidad;
            IDispatch entities = (IDispatch) design.method("FindObjects", obj);
            o = entities.get("Count");
            conteo = String.valueOf(o);
            //System.out.println("Vector de entidades " + tipoEntidad + " = " + conteo + "\n");


            for(int i = 1; i<=Integer.valueOf(conteo);i++){
            	obj[0] = new Integer(i);
                IDispatch entidad = (IDispatch) entities.method("Item", obj);
                
                IDispatch atributos = (IDispatch) entidad.get("Attributes");
                
                Object[] indice = new Object[1];
                indice[0] = "RelatingStructure";
                IDispatch relatingStructure = (IDispatch) atributos.method("Item", indice);
                
                IDispatch relatingStructureValue = (IDispatch) relatingStructure.get("Value");
                
                //solo interesa averiguar por los PISOS del edificio
                if(relatingStructureValue.get("Type").equals("IfcBuildingStorey")){
                	
                	IDispatch relatingStructureAtributos = (IDispatch) relatingStructureValue.get("Attributes");
                    
                    indice[0] = "Elevation";
                    IDispatch elevation = (IDispatch) relatingStructureAtributos.method("Item", indice);
                    double elevationValor = (Double) elevation.get("Value");
                    
                    indice[0] = "Name";
                    IDispatch name = (IDispatch) relatingStructureAtributos.method("Item", indice);
                    String nameValor = (String) name.get("value");
                    
                    indice[0] = "GlobalId";
                    IDispatch globalId = (IDispatch) relatingStructureAtributos.method("Item", indice);
                    String globalIdValor = (String) globalId.get("value");
                    
                    //no se tienen en cuenta los pisos subterraneos ni el piso base (elevation = 0)
                    if(elevationValor>0){
                    	System.out.println(nameValor + " = " + elevationValor + " (" + globalIdValor + ")");
                    	
                    	Piso pisoActual = new Piso();
                    	pisoActual.setId(globalIdValor);
                    	pisoActual.setElevacion(elevationValor);
                    	pisoActual.setNombre(nameValor);
                    	
                    	edificio.getPisos().add(pisoActual);
                    	
                    	indice = new Object[1];
                        indice[0] = "RelatedElements";
                        IDispatch relatedElements = (IDispatch) atributos.method("Item", indice);
                        
                        IDispatch relatedElementsValue = (IDispatch) relatedElements.get("Value");
                        
                        //se buscan las planchas que tenga el piso
                        indice = new Object[1];
                        indice[0] = "IfcSlab";
                        IDispatch planchas = (IDispatch) relatedElementsValue.method("FindObjects", indice);
                        
                        //Se leen las planchas del piso
                        String nPlanchas = String.valueOf(planchas.get("Count"));
                        for(int p = 1; p<=Integer.valueOf(nPlanchas);p++){
                        	obj[0] = new Integer(p);
                            IDispatch plancha = (IDispatch) planchas.method("Item", obj);
                            //IDispatch planchaValue = (IDispatch) plancha.get("Value");
                            IDispatch planchaAtributos = (IDispatch) plancha.get("Attributes");
                            
                            indice[0] = "GlobalId";
                            IDispatch planchaGlobalId = (IDispatch) planchaAtributos.method("Item", indice);
                            String planchaGlobalIdValor = (String) planchaGlobalId.get("value");
                            
                            indice[0] = "PredefinedType";
                            IDispatch predefinedType = (IDispatch) planchaAtributos.method("Item", indice);
                            String predefinedTypeValor = (String) predefinedType.get("value");
                            
                            //se descartan las planchas que sean de tipo ROOF (techos)
                            if(predefinedTypeValor.equals("FLOOR")){
                            	Plancha planchaActual = new Plancha();
                                planchaActual.setId(planchaGlobalIdValor);
                                pisoActual.getPlanchas().add(planchaActual);	
                            }
                            
                        }

                        //pisoActual.imprimir();
                    }
                }
                                
            }
    
            //SE LEEN LAS COORDENADAS GEOGRAFICAS
            //siempre devuelve un vector de una sola posicion
            obj[0] = "IfcSite";
            IDispatch vectorSite = (IDispatch) design.method("FindObjects", obj);
            
            obj[0] = new Integer(1);
            IDispatch site = (IDispatch) vectorSite.method("Item", obj);
            IDispatch siteAtributos = (IDispatch) site.get("Attributes");

            obj[0] = "RefLatitude";
            IDispatch refLatitude = (IDispatch) siteAtributos.method("Item", obj);
            int valores = Integer.valueOf((refLatitude.get("Size").toString()));
            
            coordLatitud = new int[valores];
            boolean latitudNegativa = false;
            
            for(int n = 1; n<=Integer.valueOf(valores);n++){
            	Object[] posicionVector = new Object[1];
                posicionVector[0] = n;
                int valor = (Integer) refLatitude.method("GetItem", posicionVector);
                
                if(n==1 && valor<0){
                	latitudNegativa = true;
                }
                // debido a que IFC guarda TODOS los valores de la latitud como negativos
                // ese necesario ajustarlos
                // ejemplo IFC : -74°-3'-57.-56400'' (MAL !!!)
                // ajustado :    -74° 3' 57.56400''  (OK !!!)
                if(n>1 && latitudNegativa && valor<0){
                	valor = valor * -1;
                }
                
                coordLatitud[n-1] = valor;
            }
            
            obj[0] = "RefLongitude";
            IDispatch refLongitude = (IDispatch) siteAtributos.method("Item", obj);
            valores = Integer.valueOf((refLatitude.get("Size").toString()));
            
            coordLongitud = new int[valores];
            boolean longitudNegativa = false;
            
            for(int n = 1; n<=Integer.valueOf(valores);n++){
            	Object[] posicionVector = new Object[1];
                posicionVector[0] = n;
                int valor = (Integer) refLongitude.method("GetItem", posicionVector);
                
                if(n==1 && valor<0){
                	longitudNegativa = true;
                }
                // debido a que IFC guarda TODOS los valores de la latitud como negativos
                // ese necesario ajustarlos
                // ejemplo IFC : -74°-3'-57.-56400'' (MAL !!!)
                // ajustado :    -74° 3' 57.56400''  (OK !!!)
                if(n>1 && longitudNegativa && valor<0){
                	valor = valor * -1;
                }
                
                coordLongitud[n-1] = valor;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }

        
        // para ordenar los pisos de menor a mayor elevacion
        int posA = 0;
        int posB = 0;
        
        
        for (posA = 0; posA < (edificio.getPisos().size()-1); posA++) {
        	for (posB = posA+1; posB < edificio.getPisos().size(); posB++) {
        		Piso pisoA = edificio.getPisos().get(posA);
        		Piso pisoB = edificio.getPisos().get(posB);
        		if (pisoA.getElevacion()>pisoB.getElevacion()){
        			Piso aux = pisoA;
        			edificio.getPisos().set(posA, edificio.getPisos().get(posB));
        			edificio.getPisos().set(posB, aux);
        		}
    			
    		}	
		}
        
        double mayorElevacionEncontrada = 0;
        
        for (posA = 0; posA < edificio.getPisos().size(); posA++) {
        	Piso pisoA = edificio.getPisos().get(posA);
        	if(pisoA.getElevacion()>mayorElevacionEncontrada)
        	mayorElevacionEncontrada = pisoA.getElevacion();
        }
        
        //se borran los pisos que no tengan planchas
        List<Piso> borrar = new ArrayList();
        for (Piso pisoA : edificio.getPisos()) {
        	if(pisoA.getPlanchas()==null || pisoA.getPlanchas().size()<1){
        		borrar.add(pisoA);
        	}
		}
        
        edificio.getPisos().removeAll(borrar);
        
        LectorPlanchas leerPlanchas = new LectorPlanchas();
        leerPlanchas.leerPlanchas(edificio.getPisos(), rutaArchivo);
        
        int pisoMinimo = edificio.getPisos().size() - 3; 
        
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
        

        /*
                
        String gml = "";
        for(int c=0; c < coordenadas.length -1 ; c++){
        	Coordinate coordenadaActual = coordenadas[c];
        	Coordinate coordenadaSiguiente = coordenadas[c+1];
        	
        	gml+= "\n                             <gml:surfaceMember>";
        	gml+= "\n                                <gml:Polygon gml:id=\"PolyID" + c + "\">";
        	gml+= "\n                                    <gml:exterior>";
        	gml+= "\n                                        <gml:LinearRing gml:id=\"PolyID" + c + "\">";
        	gml+= "\n                                            <gml:pos>" + coordenadaActual.x + " " + coordenadaActual.y + " 0 </gml:pos>";
        	gml+= "\n                                            <gml:pos>" + coordenadaActual.x + " " + coordenadaActual.y + " 35 </gml:pos>";
        	gml+= "\n                                            <gml:pos>" + coordenadaSiguiente.x + " " + coordenadaSiguiente.y + " 35 </gml:pos>";
        	gml+= "\n                                            <gml:pos>" + coordenadaSiguiente.x + " " + coordenadaSiguiente.y + " 0 </gml:pos>";
        	gml+= "\n                                            <gml:pos>" + coordenadaActual.x + " " + coordenadaActual.y + " 0 </gml:pos>";
        	gml+= "\n                                        </gml:LinearRing>";
        	gml+= "\n                                    </gml:exterior>";
        	gml+= "\n                                </gml:Polygon>";
        	gml+= "\n                            </gml:surfaceMember>";
        }

        
    	gml+= "\n                             <gml:surfaceMember>";
    	gml+= "\n                                <gml:Polygon gml:id=\"PolyIDasdasd\">";
    	gml+= "\n                                    <gml:exterior>";
    	gml+= "\n                                        <gml:LinearRing gml:id=\"PolyIDfghfg\">";
        for(int c=coordenadas.length-1; c >=0 ; c--){
        	Coordinate coordenadaActual = coordenadas[c];
        	gml+= "\n                                            <gml:pos>" + coordenadaActual.x + " " + coordenadaActual.y + " 35 </gml:pos>";
        }
    	gml+= "\n                                        </gml:LinearRing>";
    	gml+= "\n                                    </gml:exterior>";
    	gml+= "\n                                </gml:Polygon>";
    	gml+= "\n                            </gml:surfaceMember>";

        
        System.out.println(gml);
        */
        
        BuildingCreator creador = new BuildingCreator();
        try {
			creador.doMain(coordenadas,mayorElevacionEncontrada,args[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}

	/*
	private static void loadDll() {
        try {

                InputStream in = Conversor.class.getResourceAsStream("/jcom.dll");

                File fileOut = new File("jcom.dll");

                OutputStream out = FileUtils.openOutputStream(fileOut);
                IOUtils.copy(in, out);
                in.close();
                out.close();
                System.load(fileOut.toString());//loading goes here
        } catch (Exception e) {
               //handle
        }
        
}	*/
}
