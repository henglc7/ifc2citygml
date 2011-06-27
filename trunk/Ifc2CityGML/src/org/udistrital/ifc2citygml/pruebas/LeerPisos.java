package org.udistrital.ifc2citygml.pruebas;

import java.util.ArrayList;
import java.util.List;

import org.udistrital.ifc2citygml.ifc.Piso;
import org.udistrital.ifc2citygml.ifc.Plancha;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;


public class LeerPisos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Piso> pisos = new ArrayList();;
        ReleaseManager rm = new ReleaseManager();
      
        //String rutaArchivo = "C:\\Actual 2011\\escritorio XP\\modelos\\casa\\IFC\\AC11-FZK-Haus-IFC.ifc";
        String rutaArchivo = "C:\\Actual 2011\\escritorio XP\\modelos\\sabio caldas\\SabioCaldasSimplificado.ifc";
        //String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\muro y ventana\\miniExample20080731-CoordView-SweptSolid.ifc";
        
        try {

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
                    	//System.out.println(nameValor + " = " + elevationValor + " (" + globalIdValor + ")");
                    	
                    	Piso pisoActual = new Piso();
                    	pisoActual.setId(globalIdValor);
                    	pisoActual.setElevacion(elevationValor);
                    	pisoActual.setNombre(nameValor);
                    	
                    	pisos.add(pisoActual);
                    	
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
                            
                            Plancha planchaActual = new Plancha();
                            planchaActual.setId(planchaGlobalIdValor);
                            
                            pisoActual.getPlanchas().add(planchaActual);
                            
                            /*
                            indice[0] = "ObjectPlacement";
                            IDispatch objectPlacement = (IDispatch) planchaAtributos.method("Item", indice);
                            IDispatch objectPlacementValor = (IDispatch) objectPlacement.get("Value");
                            IDispatch objectPlacementAtributos = (IDispatch) objectPlacementValor.get("Attributes");
                            */
                            
                            
                        }

                        //pisoActual.imprimir();
                    }
                }
                                
            }
    
            

             

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }

        
        // para ordenar los pisos de menor a mayor elevacion
        int posA = 0;
        int posB = 0;
        
        for (posA = 0; posA < (pisos.size()-1); posA++) {
        	for (posB = posA+1; posB < pisos.size(); posB++) {
        		Piso pisoA = pisos.get(posA);
        		Piso pisoB = pisos.get(posB);
        		if (pisoA.getElevacion()>pisoB.getElevacion()){
        			Piso aux = pisoA;
        			pisos.set(posA, pisos.get(posB));
        			pisos.set(posB, aux);
        		}
    			
    		}	
		}
        
        
        LeerPlanchas leerPlanchas = new LeerPlanchas();
        
        leerPlanchas.leerPlanchas(pisos, rutaArchivo);
        
        
        for (Piso pisoA : pisos) {
        		
    		pisoA.imprimir();	
		}
        

	}

}
