package org.udistrital.ifc2citygmlv2.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/*
import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;
*/
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.DOUBLE;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCartesianPoint;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCompositeCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCompositeCurveSegment;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcDirection;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcLocalPlacement;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPolyline;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPositiveLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcProductDefinitionShape;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcProductRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSlab;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.LIST;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.SET;
import openifctools.com.openifcjavatoolbox.ifcmodel.IfcModel;

import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Piso;
import org.udistrital.ifc2citygmlv2.sbm.Plancha;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Segmento;

public class LectorPlanchas {
	
	public void leerPlanchas(List<Piso> pisos, IfcModel ifcModel){
		
		try {
			
            for (Piso pisoActual : pisos) {
            	for (Plancha planchaActual : pisoActual.getPlanchas()){
            		
            		planchaActual.setPlacementRelTo_placementRelTo(new Coordenada());
            		planchaActual.setPlacementRelTo_relativePlacement(new Coordenada());
            		planchaActual.setRelativePlacement_location(new Coordenada());
            		planchaActual.setRepresentation_position_location(new Coordenada());
            		planchaActual.setRepresentation_position_axis(new Coordenada());
            		planchaActual.setRepresentation_position_refDirection(new Coordenada());
            		
            		IfcSlab planchaEncontrada = (IfcSlab) ifcModel.getIfcObjectByID(planchaActual.getId());
            		
                   
                    //Se ubica en el nodo objectPlacement->placementRelTo de la plancha 
                    IfcLocalPlacement objectPlacement = (IfcLocalPlacement) planchaEncontrada.getObjectPlacement();
            		IfcLocalPlacement placementRelToA = (IfcLocalPlacement) objectPlacement.getPlacementRelTo();
            		

            		
            		//Se lee PlacementRelTo
            		IfcLocalPlacement placementRelToB = (IfcLocalPlacement) placementRelToA.getPlacementRelTo();
            		IfcAxis2Placement3D relativePlacementA = (IfcAxis2Placement3D) placementRelToB.getRelativePlacement();
            		IfcCartesianPoint locationA = relativePlacementA.getLocation();
            		LIST<IfcLengthMeasure> coordinatesA = locationA.getCoordinates();
                    
                    int coordenadasA = coordinatesA.size();
                    
                    Coordenada coordA = new Coordenada();
                    for(int n = 0; n<coordenadasA; n++){

                        double valor = (Double) coordinatesA.get(n).value;
                        
                        switch (n) {
						case 0: coordA.setX(valor); break;
						case 1: coordA.setY(valor); break;
						case 2: coordA.setZ(valor); break;
						}
                        
                    }
                    planchaActual.setPlacementRelTo_placementRelTo(coordA);

                    
                    
                    //Se lee RelativePlacement
                    
                    IfcAxis2Placement3D relativePlacementB = (IfcAxis2Placement3D) placementRelToA.getRelativePlacement();
                    IfcCartesianPoint locationB = relativePlacementB.getLocation();
                    LIST<IfcLengthMeasure> coordinatesB = locationB.getCoordinates();
                    
                    int coordenadasB = coordinatesB.size();
                    
                    Coordenada coordB = new Coordenada();
                    for(int n = 0; n<coordenadasB;n++){
                    	
                        double valor = (Double) coordinatesB.get(n).value;
                        
                        switch (n) {
						case 0: coordB.setX(valor); break;
						case 1: coordB.setY(valor); break;
						case 2: coordB.setZ(valor); break;
						}

                    }
                    planchaActual.setPlacementRelTo_relativePlacement(coordB);
                    
                    //Se lee location
                    
                    IfcAxis2Placement3D relativePlacementC = (IfcAxis2Placement3D) objectPlacement.getRelativePlacement();
                    IfcCartesianPoint locationC = relativePlacementC.getLocation();
                    LIST<IfcLengthMeasure> coordinatesC = locationC.getCoordinates();
                    
                    int coordenadasC = coordinatesC.size();
                    
                    Coordenada coordC = new Coordenada();
                    for(int n = 0; n<coordenadasC;n++){
                    	double valor = (Double) coordinatesC.get(n).value;
                        
                        switch (n) {
						case 0: coordC.setX(valor); break;
						case 1: coordC.setY(valor); break;
						case 2: coordC.setZ(valor); break;
						}
                        
                    }
                    planchaActual.setRelativePlacement_location(coordC);
                    
                    
                    //Se lee Representation
                    
                    IfcProductDefinitionShape representation = (IfcProductDefinitionShape) planchaEncontrada.getRepresentation();
                    LIST<IfcRepresentation> representations = representation.getRepresentations();
                    //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE LA POSICION 0)
                    IfcRepresentation representationActual = representations.get(0);
                    String representationType = representationActual.getRepresentationType().toString();
                    planchaActual.setRepresentation_representationType(representationType);
                    
                    SET<IfcRepresentationItem> items = representationActual.getItems();
                    Iterator it = items.iterator();
                    //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE EL PRIMER ITEM)
                    IfcExtrudedAreaSolid itemActual = (IfcExtrudedAreaSolid) it.next();
                    IfcAxis2Placement3D position = itemActual.getPosition();
                    IfcCartesianPoint location = position.getLocation();
                    LIST<IfcLengthMeasure> coordinatesD = location.getCoordinates();
                    
                    int coordenadas = coordinatesD.size();
                    
                    Coordenada coord = new Coordenada();
                    for(int n = 0; n<coordenadas;n++){
                        double valor = (Double) coordinatesD.get(n).value;
                        
                        switch (n) {
						case 0: coord.setX(valor); break;
						case 1: coord.setY(valor); break;
						case 2: coord.setZ(valor); break;
						}
                        
                    }
                    
                    planchaActual.setRepresentation_position_location(coord);
                    
                    //System.out.println(pisoActual.getNombre());
                    //planchaActual
                    
                    //Definition from IAI: If the attribute values for Axis and RefDirection are not given, the placement defaults to P[1] (x-axis) as [1.,0.,0.], P[2] (y-axis) as [0.,1.,0.] and P[3] (z-axis) as [0.,0.,1.]. 
                    if(position.getAxis() != null){
                    	
                    	IfcDirection axis = position.getAxis();
                        LIST<DOUBLE> directionRatios = axis.getDirectionRatios();
                        
                        coordenadas = directionRatios.size();
                        
                        coord = new Coordenada();
                        
                        for(int n = 0; n<coordenadas;n++){
                            double valor = (Double) directionRatios.get(n).value;
                            
                            switch (n) {
    						case 0: coord.setX(valor); break;
    						case 1: coord.setY(valor); break;
    						case 2: coord.setZ(valor); break;
    						}
                            
                        }
                        
                        planchaActual.setRepresentation_position_axis(coord);
                        
                        IfcDirection refDirection = position.getRefDirection();
                        directionRatios = refDirection.getDirectionRatios();
                        
                        coordenadas = directionRatios.size();
                        
                        coord = new Coordenada();
                        
                        for(int n = 0; n<coordenadas;n++){
                            double valor = (Double) directionRatios.get(n).value;
                            
                            switch (n) {
    						case 0: coord.setX(valor); break;
    						case 1: coord.setY(valor); break;
    						case 2: coord.setZ(valor); break;
    						}
                            
                        }
                        
                        planchaActual.setRepresentation_position_refDirection(coord);
                    }
                    
                    
                    
                    if(itemActual.getSweptArea() instanceof IfcArbitraryClosedProfileDef){
                    	
                    	IfcArbitraryClosedProfileDef sweptArea = (IfcArbitraryClosedProfileDef) itemActual.getSweptArea();
                        //int at = (Integer) sweptAreaAtributos.get("Count");
                        //sobra?
                        planchaActual.setRepresentation_representation_SweptAreaType(sweptArea.getClass().getSimpleName());
                        Object outerCurve = sweptArea.getOuterCurve();
                    	
                        if(outerCurve instanceof IfcPolyline){
                        	
                        	planchaActual.setRepresentation_points(new ArrayList());
                        	IfcPolyline outerCurveActual = (IfcPolyline) outerCurve; 
                        	LIST<IfcCartesianPoint> points = outerCurveActual.getPoints();
                        	
                        	int puntos = points.size();
                            
                            for(int n = 0; n<puntos;n++){
                            	
                            	
                            	IfcCartesianPoint pointsActual = points.get(n);
                            	LIST<IfcLengthMeasure> coordinatesE = pointsActual.getCoordinates();
                                int coordenadasE = coordinatesE.size();
                                Coordenada coordenada = new Coordenada();
                                
                                for(int i = 0; i<coordenadasE;i++){
                                	
                                    double valor = coordinatesE.get(i).value;
                                    switch (i) {
									case 0: coordenada.setX(valor); break;
									case 1: coordenada.setY(valor); break;
									}
                                }
                                
                                planchaActual.getRepresentation_points().add(coordenada);
                                
                            }                            
                        	
                        }
                    	
                        
                        if(outerCurve instanceof IfcCompositeCurve){
                        	
                        	planchaActual.setRepresentation_segmentos(new ArrayList());
                        	IfcCompositeCurve outerCurveActual = (IfcCompositeCurve) outerCurve;
                        	LIST<IfcCompositeCurveSegment> segments = outerCurveActual.getSegments();
                        	int segmentos = segments.size();
                            
                            for(int n = 0; n<segmentos;n++){
                            	
                            	Segmento segmento = new Segmento();
                            	
                            	IfcCompositeCurveSegment segmentoActual = segments.get(n);
                            	IfcCurve parentCurve = segmentoActual.getParentCurve();
                            	
                                if(parentCurve instanceof IfcPolyline){
                               	
                                	IfcPolyline parentCurveActual = (IfcPolyline) parentCurve;
                                	LIST<IfcCartesianPoint> points = parentCurveActual.getPoints();
                                	int puntos = points.size();
                                    
                                    Coordenada p0 = new Coordenada();
                                    Coordenada p1 = new Coordenada();
                                    
                                    for(int i = 0; i<puntos;i++){
                                    	
                                    	IfcCartesianPoint puntoActual = points.get(i);
                                    	LIST<IfcLengthMeasure> coordinates = puntoActual.getCoordinates(); 
                                        coordenadas = coordinates.size();
                                        
                                        for(int m = 0; m<coordenadas;m++){
                                        	
                                            double valor = coordinates.get(m).value;
                                            
                                            if(i==0){
                                            	switch (m) {
            									case 0: p0.setX(valor); break;
            									case 1: p0.setY(valor); break;
            									}
                                            	
                                            }
                                            if(i==1){
                                            	switch (m) {
            									case 0: p1.setX(valor); break;
            									case 1: p1.setY(valor); break;
            									}
                                            	
                                            }
                                            
                                        }
                                        
                                        
                                    }
                                    
                                    segmento.setP0(p0);
                                    segmento.setP1(p1);
                                    
                                    planchaActual.getRepresentation_segmentos().add(segmento);
                                    
                                }
                                
                                if(parentCurve instanceof IfcTrimmedCurve){
                                	
                                	//no se tiene en cuenta IfcTrimmedCurve
                                }
                            }
                        }
                    }
                    
                    if(itemActual.getSweptArea() instanceof IfcRectangleProfileDef){
                        
                    	
                    	IfcRectangleProfileDef sweptArea = (IfcRectangleProfileDef) itemActual.getSweptArea();
                    	planchaActual.setRepresentation_representation_SweptAreaType(sweptArea.getClass().getSimpleName());
                    	
                    	Rectangulo rec = new Rectangulo();
                    	
                    	IfcPositiveLengthMeasure xdim = sweptArea.getXDim();
                        rec.setXDim(xdim.value);
                        
                        IfcPositiveLengthMeasure ydim = sweptArea.getYDim();
                        rec.setYDim(ydim.value);
                        
                        IfcAxis2Placement2D positionD = sweptArea.getPosition();
                        IfcCartesianPoint locationD = positionD.getLocation();
                        LIST<IfcLengthMeasure> coordinates = locationD.getCoordinates();
                        coordenadas = coordinates.size();
                        
                        Coordenada coordenada = new Coordenada();
                        
                        for(int i = 0; i<coordenadas;i++){
                        	
                        	double valor = coordinates.get(i).value;
                            switch (i) {
							case 0: coordenada.setX(valor); break;
							case 1: coordenada.setY(valor); break;
							}
                        }
                        
                        rec.setPosition_location(coordenada);

                        IfcDirection refDirectionD = positionD.getRefDirection();
                        LIST<DOUBLE> directionRatiosD = refDirectionD.getDirectionRatios();
                        coordenadas = directionRatiosD.size();
                        
                        coordenada = new Coordenada();
                        
                        for(int i = 0; i<coordenadas;i++){
                        	
                        	double valor = directionRatiosD.get(i).value;
                            switch (i) {
							case 0: coordenada.setX(valor); break;
							case 1: coordenada.setY(valor); break;
							}
                        }
                        
                        rec.setPosition_refDirection(coordenada);
                        
                        planchaActual.setRectangulo(rec);
                        
                        
                    }
                     
                    //se calculan las coordenadas absolutas de la geometria que define el perfil de la plancha
                    planchaActual.calcularCoordenadasAbsolutas();
                    
            	}
            	
			}
			
			
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            //rm.release();
        }
		
	}

}
