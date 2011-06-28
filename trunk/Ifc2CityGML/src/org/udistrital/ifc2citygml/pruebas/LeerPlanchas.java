package org.udistrital.ifc2citygml.pruebas;

import java.util.ArrayList;
import java.util.List;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.udistrital.ifc2citygml.ifc.Coordenada;
import org.udistrital.ifc2citygml.ifc.Piso;
import org.udistrital.ifc2citygml.ifc.Plancha;
import org.udistrital.ifc2citygml.ifc.Segmento;

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
            		
            		planchaActual.setPlacementRelTo_placementRelTo(new ArrayList());
            		planchaActual.setPlacementRelTo_relativePlacement(new ArrayList());
            		planchaActual.setRelativePlacement_location(new ArrayList());
            		planchaActual.setRepresentation_position_location(new ArrayList());
            		planchaActual.setRepresentation_position_axis(new ArrayList());
            		planchaActual.setRepresentation_position_refDirection(new ArrayList());
            		
            		
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
                        planchaActual.getPlacementRelTo_placementRelTo().add(valor);
                    }
                    
                  //Se lee RelativePlacement
                    
                    obj[0] = "ObjectPlacement";
                    objectPlacement = (IDispatch) planchaAtributos.method("Item", obj);
                    objectPlacementValor = (IDispatch) objectPlacement.get("Value");
                    objectPlacementAtributos = (IDispatch) objectPlacementValor.get("Attributes");
                    
                    obj[0] = "PlacementRelTo";
                    placementRelTo = (IDispatch) objectPlacementAtributos.method("Item", obj);
                    placementRelToValor = (IDispatch) placementRelTo.get("Value");
                    placementRelToAtributos = (IDispatch) placementRelToValor.get("Attributes");
                    
                    obj[0] = "RelativePlacement";
                    relativePlacement = (IDispatch) placementRelToAtributos.method("Item", obj);
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
                        planchaActual.getPlacementRelTo_relativePlacement().add(valor);
                    }
                    
                    //Se lee location
                    
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
                        planchaActual.getRelativePlacement_location().add(valor);
                    }
                    
  
                    //Se lee Representation
                    
                    obj[0] = "Representation";
                    IDispatch representation = (IDispatch) planchaAtributos.method("Item", obj);
                    IDispatch representationValor = (IDispatch) representation.get("Value");
                    IDispatch representationAtributos = (IDispatch) representationValor.get("Attributes");
                    
                    
                    obj[0] = "Representations";
                    IDispatch representations = (IDispatch) representationAtributos.method("Item", obj);
                  //aca podria ir un FOR
                    Object[] posicionVector = new Object[1];
                    posicionVector[0] = 1;
                    //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE LA POSICION 1)
                    IDispatch representationsActualValor = (IDispatch) representations.method("GetItem", posicionVector);
                    IDispatch representationsActualAtributos = (IDispatch) representationsActualValor.get("Attributes");

                    obj[0] = "RepresentationType";
                    IDispatch representationType = (IDispatch) representationsActualAtributos.method("Item", obj);
                    String representationTypeValor = (String) representationType.get("Value");
                    planchaActual.setRepresentation_representationType(representationTypeValor);
                    

                    obj[0] = "Items";
                    IDispatch items = (IDispatch) representationsActualAtributos.method("Item", obj);
                  //aca podria ir un FOR
                    Object[] posicionVector2 = new Object[1];
                    posicionVector2[0] = 1;
                    //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE LA POSICION 1)
                    IDispatch itemsActualValor = (IDispatch) items.method("GetItem", posicionVector2);
                    IDispatch itemsActualAtributos = (IDispatch) itemsActualValor.get("Attributes");
                    
                    obj[0] = "Position";
                    IDispatch position = (IDispatch) itemsActualAtributos.method("Item", obj);
                    IDispatch positionValor = (IDispatch) position.get("Value");
                    IDispatch positionAtributos = (IDispatch) positionValor.get("Attributes");
                    
                    obj[0] = "Location";
                    location = (IDispatch) positionAtributos.method("Item", obj);
                    locationValor = (IDispatch) location.get("Value");
                    locationAtributos = (IDispatch) locationValor.get("Attributes");
                    
                    obj[0] = "Coordinates";
                    coordinates = (IDispatch) locationAtributos.method("Item", obj);
                    
                    coordenadas = Integer.valueOf((coordinates.get("Size").toString()));
                    for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                    	posicionVector = new Object[1];
                        posicionVector[0] = n;
                        double valor = (Double) coordinates.method("GetItem", posicionVector);
                        planchaActual.getRepresentation_position_location().add(valor);
                    }
                    
                    obj[0] = "Axis";
                    IDispatch axis = (IDispatch) positionAtributos.method("Item", obj);
                    IDispatch axisValor = (IDispatch) axis.get("Value");
                    IDispatch axisAtributos = (IDispatch) axisValor.get("Attributes");
                    
                    obj[0] = "DirectionRatios";
                    IDispatch directionRatios = (IDispatch) axisAtributos.method("Item", obj);
                    
                    coordenadas = Integer.valueOf((directionRatios.get("Size").toString()));
                    for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                    	posicionVector = new Object[1];
                        posicionVector[0] = n;
                        double valor = (Double) directionRatios.method("GetItem", posicionVector);
                        planchaActual.getRepresentation_position_axis().add(valor);
                    }
                    
                    obj[0] = "RefDirection";
                    IDispatch refDirection = (IDispatch) positionAtributos.method("Item", obj);
                    IDispatch refDirectionValor = (IDispatch) refDirection.get("Value");
                    IDispatch refDirectionAtributos = (IDispatch) refDirectionValor.get("Attributes");
                    
                    obj[0] = "DirectionRatios";
                    directionRatios = (IDispatch) refDirectionAtributos.method("Item", obj);
                    
                    coordenadas = Integer.valueOf((directionRatios.get("Size").toString()));
                    for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                    	posicionVector = new Object[1];
                        posicionVector[0] = n;
                        double valor = (Double) directionRatios.method("GetItem", posicionVector);
                        planchaActual.getRepresentation_position_refDirection().add(valor);
                    }
                    
                    obj[0] = "SweptArea";
                    IDispatch sweptArea = (IDispatch) itemsActualAtributos.method("Item", obj);
                    IDispatch sweptAreaValor = (IDispatch) sweptArea.get("Value");
                    IDispatch sweptAreaAtributos = (IDispatch) sweptAreaValor.get("Attributes");
                    
                    int at = (Integer) sweptAreaAtributos.get("Count");



                    /*
                    System.out.println(at + " Plancha : " + planchaActual.getId());
                    System.out.println(at + " Tipo : " + sweptAreaValor.get("Type"));
                    */
                    
                    //No se incluyen planchas de tipo IfcRectangleProfileDef
                    if(sweptAreaValor.get("Type").equals("IfcArbitraryClosedProfileDef")){
                        obj[0] = "OuterCurve";
                        IDispatch outerCurve = (IDispatch) sweptAreaAtributos.method("Item", obj);
                        IDispatch outerCurveValor = (IDispatch) outerCurve.get("Value");
                        IDispatch outerCurveAtributos = (IDispatch) outerCurveValor.get("Attributes");
                        
                        if(outerCurveValor.get("Type").equals("IfcPolyline")){
                        	
                        	planchaActual.setRepresentation_points(new ArrayList());
                        	
                        	obj[0] = "Points";
                            IDispatch points = (IDispatch) outerCurveAtributos.method("Item", obj);
                            int puntos = Integer.valueOf((points.get("Size").toString()));
                            
                            for(int n = 1; n<=Integer.valueOf(puntos);n++){
                            	posicionVector = new Object[1];
                                posicionVector[0] = n;
                                IDispatch pointsActualValor = (IDispatch) points.method("GetItem", posicionVector);
                                IDispatch pointsActualAtributos = (IDispatch) pointsActualValor.get("Attributes");
                                
                                obj[0] = "Coordinates";
                                coordinates = (IDispatch) pointsActualAtributos.method("Item", obj);
                                
                                coordenadas = Integer.valueOf((coordinates.get("Size").toString()));
                                
                                Coordenada coordenada = new Coordenada();
                                
                                for(int i = 1; i<=Integer.valueOf(coordenadas);i++){
                                	
                                	Object[] posicionVectorCoordenadas = new Object[1];
                                	posicionVectorCoordenadas[0] = i;
                                    double valor = (Double) coordinates.method("GetItem", posicionVectorCoordenadas);
                                    switch (i) {
									case 1: coordenada.setX(valor); break;
									case 2: coordenada.setY(valor); break;
									}
                                }
                                
                                planchaActual.getRepresentation_points().add(coordenada);
                                
                            }                            
                        	
                        }
                        
                        if(outerCurveValor.get("Type").equals("IfcCompositeCurve")){
                        	
                        	planchaActual.setRepresentation_segmentos(new ArrayList());
                        	
                        	obj[0] = "Segments";
                            IDispatch segments = (IDispatch) outerCurveAtributos.method("Item", obj);
                            int segmentos = Integer.valueOf((segments.get("Size").toString()));
                            
                            for(int n = 1; n<=Integer.valueOf(segmentos);n++){
                            	
                            	Segmento segmento = new Segmento();
                            	
                            	posicionVector = new Object[1];
                                posicionVector[0] = n;
                                IDispatch segmentoActualValor = (IDispatch) segments.method("GetItem", posicionVector);
                                IDispatch segmentoActualAtributos = (IDispatch) segmentoActualValor.get("Attributes");
                                
                                obj[0] = "ParentCurve";
                                IDispatch parentCurve = (IDispatch) segmentoActualAtributos.method("Item", obj);
                                IDispatch parentCurveValor = (IDispatch) parentCurve.get("Value");
                                IDispatch parentCurveAtributos = (IDispatch) parentCurveValor.get("Attributes");
                                
                                if(parentCurveValor.get("Type").equals("IfcPolyline")){
                                	obj[0] = "Points";
                                	IDispatch points = (IDispatch) parentCurveAtributos.method("Item", obj);
                                    
                                    int puntos = Integer.valueOf((points.get("Size").toString()));
                                    
                                    Coordenada p0 = new Coordenada();
                                    Coordenada p1 = new Coordenada();
                                    
                                    for(int i = 1; i<=Integer.valueOf(puntos);i++){
                                    	
                                    	posicionVector = new Object[1];
                                        posicionVector[0] = i;
                                        //IDispatch puntoActual = (IDispatch) points.method("GetItem", posicionVector);
                                        IDispatch puntoActualValor = (IDispatch) points.method("GetItem", posicionVector);
                                        IDispatch puntoActualAtributos = (IDispatch) puntoActualValor.get("Attributes");
                                        
                                        coordenadas = Integer.valueOf(puntoActualAtributos.get("Count").toString());
                                        
                                        obj[0] = "Coordinates";
                                        coordinates = (IDispatch) puntoActualAtributos.method("Item", obj);
                                        
                                        coordenadas = Integer.valueOf((coordinates.get("Size").toString()));
                                        
                                        for(int m = 1; m<=Integer.valueOf(coordenadas);m++){
                                        	
                                        	Object[] posicionVectorCoordenadas = new Object[1];
                                        	posicionVectorCoordenadas[0] = m;
                                            double valor = (Double) coordinates.method("GetItem", posicionVectorCoordenadas);
                                            if(i==1){
                                            	switch (m) {
            									case 1: p0.setX(valor); break;
            									case 2: p0.setY(valor); break;
            									}
                                            	
                                            }
                                            if(i==2){
                                            	switch (m) {
            									case 1: p1.setX(valor); break;
            									case 2: p1.setY(valor); break;
            									}
                                            	
                                            }
                                            
                                        }
                                        
                                        
                                    }
                                    
                                    segmento.setP0(p0);
                                    segmento.setP1(p1);
                                    
                                    planchaActual.getRepresentation_segmentos().add(segmento);
                                    
                                }
                                //no se tiene en cuenta IfcTrimmedCurve
                                if(parentCurveValor.get("Type").equals("IfcTrimmedCurve")){
                                	
                                	
                                }
                                
                                
                            }
                        }
                        	
                        	
                        
                        
                        
                        

                    	
                    }
                    
                    
                    
                    
                    /*
                    IDispatch outerCurveAtributos = (IDispatch) outerCurveValor.get("Attributes");
                    */
                    /*
                    obj[0] = "Segments";
                    IDispatch segments = (IDispatch) outerCurveAtributos.method("Item", obj);
                    */
                    
                    //int segmentos = Integer.valueOf((segments.get("Size").toString()));
                    /*
                    for(int n = 1; n<=Integer.valueOf(segmentos);n++){
                    	posicionVector = new Object[1];
                        posicionVector[0] = n;
                        IDispatch segmentoActual = (IDispatch) segments.method("GetItem", posicionVector);
                        

                    }
                    */
            	}
				
			}
			
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }
		
	}

}
