package org.udistrital.ifc2citygml.pruebas;

import java.util.ArrayList;
import java.util.List;

import org.udistrital.ifc2citygml.ifc.Piso;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;


public class LeerPisos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Piso> pisos = new ArrayList();;
        ReleaseManager rm = new ReleaseManager();
        try {

            //Se declara una instancia del componente ActiveX pasando el nombre como parametro
            IDispatch object = new IDispatch(rm, "IFCsvr.R300");

            //String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\casa\\IFC\\AC11-FZK-Haus-IFC.ifc";
            String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\sabio caldas\\SabioCaldasSimplificado.ifc";
            //String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\muro y ventana\\miniExample20080731-CoordView-SweptSolid.ifc";
            
            //String tipoEntidad = "IfcBuildingStorey";
            //String tipoEntidad = "IfcSlab";
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
                    
                    //no se tienen en cuenta los pisos subterraneoa
                    if(elevationValor>=0){
                    	//System.out.println(nameValor + " = " + elevationValor + " (" + globalIdValor + ")");
                    	
                    	Piso pisoActual = new Piso();
                    	pisoActual.setId(globalIdValor);
                    	pisoActual.setElevacion(elevationValor);
                    	pisoActual.setNombre(nameValor);
                    	
                    	pisos.add(pisoActual);
                    	
                    	pisoActual.imprimir();
                    	
                    	indice = new Object[1];
                        indice[0] = "RelatedElements";
                        IDispatch relatedElements = (IDispatch) atributos.method("Item", indice);
                        
                        IDispatch relatedElementsValue = (IDispatch) relatedElements.get("Value");
                        
                        //se buscan las planchas que tenga el piso
                        indice = new Object[1];
                        indice[0] = "IfcSlab";
                        IDispatch planchas = (IDispatch) relatedElementsValue.method("FindObjects", indice);
                        


                        
                        System.out.println(planchas.get("Count") + " planchas");
                    }
                }
                                
            }
    
            

             

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }



	}

}
