package org.udistrital.ifc2citygmlv2.util;

import java.util.ArrayList;
import java.util.Iterator;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.DOUBLE;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcArbitraryClosedProfileDef;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcBooleanClippingResult;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcBooleanOperand;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCartesianPoint;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCompositeCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCompositeCurveSegment;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcDirection;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcHalfSpaceSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPolyline;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPositiveLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.LIST;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.SET;

import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Segmento;

public class LectorRepresentationMuro {

	public static void procesarSweptSolid(IfcRepresentation representationActual, Muro muroActual){
		String representationType = representationActual.getRepresentationType().toString();
        muroActual.representation.setRepresentation_representationType(representationType);
        //System.err.println("seteado a = " + representationActual.getStepLineNumber());
        muroActual.representation.setStepLineNumber(representationActual.getStepLineNumber());
        
        SET<IfcRepresentationItem> items = representationActual.getItems();
        Iterator it = items.iterator();
        //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE EL PRIMER ITEM)
        IfcExtrudedAreaSolid itemActual = (IfcExtrudedAreaSolid) it.next();
        
        extraerCoordenadasDeExtrudedAreaSolid(itemActual, muroActual);
        
	}
	
	
	public static void procesarClipping(IfcRepresentation representationActual, Muro muroActual){
		
		SET<IfcRepresentationItem> items = representationActual.getItems();
        Iterator it = items.iterator();
        
        //se asume que siempre va a existir UNA sola representacion (SOLO SE LEE EL PRIMER ITEM)
        IfcBooleanClippingResult clipping = (IfcBooleanClippingResult) it.next();
        
        IfcBooleanOperand opA = clipping.getFirstOperand();
        IfcBooleanOperand opB = clipping.getSecondOperand();
        
    	if(opA instanceof IfcExtrudedAreaSolid && opB instanceof IfcHalfSpaceSolid){

        	//se asume que siempre va a existir UNA sola representacion (SOLO SE LEE EL PRIMER ITEM)
        	IfcExtrudedAreaSolid itemActual = (IfcExtrudedAreaSolid)opA;
        	
        	
        	String representationType = representationActual.getRepresentationType().toString();
            muroActual.representation.setRepresentation_representationType(representationType);
            //System.err.println("seteado a = " + representationActual.getStepLineNumber());
            muroActual.representation.setStepLineNumber(representationActual.getStepLineNumber());
            
            
            extraerCoordenadasDeExtrudedAreaSolid(itemActual, muroActual);
            
		}else if(opA instanceof IfcBooleanClippingResult && opB instanceof IfcHalfSpaceSolid){
			
			System.err.println("PENDIENTE DE IMPLEMENTAR" );
			
		}else{
			
			System.err.println("ESTO NUNCA DEBERIA SALIR" );
		}
		
		
		
		
		

		
	}
	
	
	public static void extraerCoordenadasDeExtrudedAreaSolid(IfcExtrudedAreaSolid itemActual, Muro muroActual){
		
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
        
        muroActual.representation.setRepresentation_position_location(coord);
        
        
        
        IfcDirection direction = itemActual.getExtrudedDirection();
        LIST<DOUBLE> coordinatesF = direction.getDirectionRatios();
        
        coordenadas = coordinatesF.size();
        
        coord = new Coordenada();
        for(int n = 0; n<coordenadas;n++){
            double valor = (Double) coordinatesF.get(n).value;
            
            switch (n) {
			case 0: coord.setX(valor); break;
			case 1: coord.setY(valor); break;
			case 2: coord.setZ(valor); break;
			}
            
        }
        
        muroActual.representation.setRepresentation_extruded_direction(coord);
        
        
        
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
            
            muroActual.representation.setRepresentation_position_axis(coord);
            
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
            
            muroActual.representation.setRepresentation_position_refDirection(coord);
        }
        
        
        
        
        if(itemActual.getSweptArea() instanceof IfcArbitraryClosedProfileDef){
        	
        	IfcArbitraryClosedProfileDef sweptArea = (IfcArbitraryClosedProfileDef) itemActual.getSweptArea();
            //int at = (Integer) sweptAreaAtributos.get("Count");
            //sobra?
        	muroActual.representation.setRepresentation_representation_SweptAreaType(sweptArea.getClass().getSimpleName());
            Object outerCurve = sweptArea.getOuterCurve();
        	
            if(outerCurve instanceof IfcPolyline){
            	
            	muroActual.setRepresentation_points(new ArrayList());
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
                    
                    muroActual.getRepresentation_points().add(coordenada);
                    
                }                            
            	
            }
        	
            
            if(outerCurve instanceof IfcCompositeCurve){
            	
            	muroActual.setRepresentation_segmentos(new ArrayList());
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
                        
                        muroActual.getRepresentation_segmentos().add(segmento);
                        
                    }
                    
                    if(parentCurve instanceof IfcTrimmedCurve){
                    	
                    	//no se tiene en cuenta IfcTrimmedCurve
                    }
                }
            }
        }
        
        
        if(itemActual.getSweptArea() instanceof IfcRectangleProfileDef){
            
        	
        	IfcRectangleProfileDef sweptArea = (IfcRectangleProfileDef) itemActual.getSweptArea();
        	muroActual.representation.setRepresentation_representation_SweptAreaType(sweptArea.getClass().getSimpleName());
        	
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
            
            muroActual.setRectangulo(rec);
            
            
        }
         
        //se calculan las coordenadas absolutas de la geometria que define el perfil de la plancha
        muroActual.calcularCoordenadasAbsolutas();
		
	}
	
}
