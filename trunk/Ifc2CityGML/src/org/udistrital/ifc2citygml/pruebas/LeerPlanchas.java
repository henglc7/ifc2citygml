package org.udistrital.ifc2citygml.pruebas;

import java.util.ArrayList;
import java.util.List;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.udistrital.ifc2citygml.ifc.Piso;
import org.udistrital.ifc2citygml.ifc.Plancha;

public class LeerPlanchas {
	
	public void leerPlanchas(List<Piso> pisos, String rutaArchivo){
		
		ReleaseManager rm = new ReleaseManager();
		
		try {
			//Se declara una instancia del componente ActiveX pasando el nombre como parametro
            IDispatch object = new IDispatch(rm, "IFCsvr.R300");
			
			Object[] obj = new Object[1];
            obj[0] = rutaArchivo;

            //Se abre un diseno IFC
            IDispatch design = (IDispatch) object.method("OpenDesign", obj);
            
            for (Piso pisoActual : pisos) {
            	for (Plancha planchaActual : pisoActual.getPlanchas()){
            		
            		planchaActual.setPlacementRelTo(new ArrayList());
            		planchaActual.setRelativePlacement(new ArrayList());
            		
            		//se cuenta el numero de entidades
                    obj = new Object[4];
                    obj[0] = "IfcSlab";
                    obj[1] = "GlobalId";
                    obj[2] = planchaActual.getId();
                    obj[3] = 0;
                    //se busca la plancha especifica
                    IDispatch planchaEncontrada = (IDispatch) design.method("FindObjectsByValue", obj);
                    //System.out.println("\n Conteo de " + "IfcSlab" + " = " + plancha.get("Count"));
                    
                    //siempre se encontrara UNA UNICA plancha, debido a que se busca por GlobalID
                    obj = new Object[1];
                    obj[0] = new Integer(1);
                    IDispatch plancha = (IDispatch) planchaEncontrada.method("Item", obj);
                    IDispatch planchaAtributos = (IDispatch) plancha.get("Attributes");
                    
                    //Se lee PlacementRelTo
                    
                    obj[0] = "ObjectPlacement";
                    IDispatch objectPlacement = (IDispatch) planchaAtributos.method("Item", obj);
                    IDispatch objectPlacementValor = (IDispatch) objectPlacement.get("Value");
                    IDispatch objectPlacementAtributos = (IDispatch) objectPlacementValor.get("Attributes");

                    obj[0] = "PlacementRelTo";
                    IDispatch placementRelTo = (IDispatch) objectPlacementAtributos.method("Item", obj);
                    IDispatch placementRelToValor = (IDispatch) placementRelTo.get("Value");
                    IDispatch placementRelToAtributos = (IDispatch) placementRelToValor.get("Attributes");
                    
                    obj[0] = "PlacementRelTo";
                    IDispatch placementRelTo_A_ = (IDispatch) placementRelToAtributos.method("Item", obj);
                    IDispatch placementRelTo_A_Valor = (IDispatch) placementRelTo_A_.get("Value");
                    IDispatch placementRelTo_A_Atributos = (IDispatch) placementRelTo_A_Valor.get("Attributes");
                    
                    obj[0] = "RelativePlacement";
                    IDispatch relativePlacement = (IDispatch) placementRelTo_A_Atributos.method("Item", obj);
                    IDispatch relativePlacementValor = (IDispatch) relativePlacement.get("Value");
                    IDispatch relativePlacementAtributos = (IDispatch) relativePlacementValor.get("Attributes");
                    
                    obj[0] = "Location";
                    IDispatch location = (IDispatch) relativePlacementAtributos.method("Item", obj);
                    IDispatch locationValor = (IDispatch) location.get("Value");
                    IDispatch locationAtributos = (IDispatch) locationValor.get("Attributes");
                    
                    obj[0] = "Coordinates";
                    IDispatch coordinates = (IDispatch) locationAtributos.method("Item", obj);
                    
                    int coordenadas = Integer.valueOf((coordinates.get("Size").toString()));
                    for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                    	Object[] posicionVector = new Object[1];
                        posicionVector[0] = n;
                        double valor = (Double) coordinates.method("GetItem", posicionVector);
                        planchaActual.getPlacementRelTo().add(valor);
                    }
                    
                  //Se lee RelativePlacement
                    
                    obj[0] = "ObjectPlacement";
                    objectPlacement = (IDispatch) planchaAtributos.method("Item", obj);
                    objectPlacementValor = (IDispatch) objectPlacement.get("Value");
                    objectPlacementAtributos = (IDispatch) objectPlacementValor.get("Attributes");
                    
                    obj[0] = "RelativePlacement";
                    relativePlacement = (IDispatch) objectPlacementAtributos.method("Item", obj);
                    relativePlacementValor = (IDispatch) relativePlacement.get("Value");
                    relativePlacementAtributos = (IDispatch) relativePlacementValor.get("Attributes");
                    
                    obj[0] = "Location";
                    location = (IDispatch) relativePlacementAtributos.method("Item", obj);
                    locationValor = (IDispatch) location.get("Value");
                    locationAtributos = (IDispatch) locationValor.get("Attributes");
                    
                    obj[0] = "Coordinates";
                    coordinates = (IDispatch) locationAtributos.method("Item", obj);
                    
                    coordenadas = Integer.valueOf((coordinates.get("Size").toString()));
                    
                    for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                    	Object[] posicionVector = new Object[1];
                        posicionVector[0] = n;
                        double valor = (Double) coordinates.method("GetItem", posicionVector);
                        planchaActual.getRelativePlacement().add(valor);
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
