package org.udistrital.ifc2citygmlv2.util;

import java.awt.Toolkit;
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
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPlane;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPolyline;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPositiveLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcTrimmedCurve;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.LIST;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.SET;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.udistrital.ifc2citygmlv2.sbm.Coordenada;
import org.udistrital.ifc2citygmlv2.sbm.Muro;
import org.udistrital.ifc2citygmlv2.sbm.Rectangulo;
import org.udistrital.ifc2citygmlv2.sbm.Segmento;
import org.udistrital.ifc2citygmlv2.sbm.ifc.PlanoDeCorte;

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
        
        IfcBooleanOperand operadorA = clipping.getFirstOperand();
        IfcBooleanOperand operadorB = clipping.getSecondOperand();
        
        
    	if(operadorA instanceof IfcExtrudedAreaSolid && operadorB instanceof IfcHalfSpaceSolid){
    		
    		//operadorA es un Solido y operadorB un Plano

    		extraerCoordenadasYAgregarPlano(representationActual, muroActual, operadorA, operadorB);
        
    		
		}else if(operadorA instanceof IfcBooleanClippingResult && operadorB instanceof IfcHalfSpaceSolid){ 
    	    
			//operadorA es un Clipping 3D y operadorB un Plano
    	
			
	        IfcBooleanOperand opC = ((IfcBooleanClippingResult)operadorA).getFirstOperand();
	        IfcBooleanOperand opD = ((IfcBooleanClippingResult)operadorA).getSecondOperand();
			
	        //opC es un Solido
	        if(opC instanceof IfcExtrudedAreaSolid){

	        	extraerCoordenadasYAgregarPlano(representationActual, muroActual, opC, opD);

	        	//FALTA IMPLEMENTAR EL CORTE CUANDO HAY MAS DE UN PLANO
	        	agregarPlanoDeCorte(muroActual, (IfcHalfSpaceSolid) operadorB);
	        		

	        	
	        }else{
				
				System.err.println("PENDIENTE DE IMPLEMENTAR !" + muroActual.getId() + "( opC = " + opC.getClass() + " opD = " + opD.getClass() + " )");
				
			}
	        	
	        
	        
			//System.err.println("REVISAR MURO " + muroActual.getId() );
			
		}else{
			
			System.err.println("ESTO NUNCA DEBERIA SALIR" );
		}
		
	}
	
	public static void extraerCoordenadasYAgregarPlano(IfcRepresentation representationActual, Muro muroActual, IfcBooleanOperand opA, IfcBooleanOperand opB){
		
			//se asume que siempre va a existir UNA sola representacion (SOLO SE LEE EL PRIMER ITEM)
	    	IfcExtrudedAreaSolid itemActual = (IfcExtrudedAreaSolid)opA;
	    	
	    	String representationType = representationActual.getRepresentationType().toString();
	        muroActual.representation.setRepresentation_representationType(representationType);
	        //System.err.println("seteado a = " + representationActual.getStepLineNumber());
	        muroActual.representation.setStepLineNumber(representationActual.getStepLineNumber());
	        
	        extraerCoordenadasDeExtrudedAreaSolid(itemActual, muroActual);

	        //despues de calcular las caras se agrega el plano de corte
	        agregarPlanoDeCorte(muroActual, (IfcHalfSpaceSolid)opB);
			
	}
	
	public static void agregarPlanoDeCorte(Muro muroActual, IfcHalfSpaceSolid halfSpace){
		
		if(muroActual.getPlanosDeCorte() == null){
			muroActual.setPlanosDeCorte(new ArrayList());
		}
		
		IfcPlane plano = (IfcPlane) halfSpace.getBaseSurface();
		
		Coordenada locationIfc = LectorCoordenada.Leer(plano.getPosition().getLocation());
		Coordenada axisIfc = null;
		Coordenada refDirectionIfc = null;
		Coordenada normalIfc = new Coordenada(0,0,0);
		
		Transformador t = new Transformador();

		if(plano.getPosition().getAxis()!= null && plano.getPosition().getRefDirection()!= null){
			
			axisIfc = LectorCoordenada.Leer(plano.getPosition().getAxis());
			refDirectionIfc = LectorCoordenada.Leer(plano.getPosition().getRefDirection());

		}

		locationIfc = t.convertirEnAbsoluta(locationIfc, muroActual);
		normalIfc = t.convertirEnAbsoluta(normalIfc, muroActual);
		if(axisIfc!=null) axisIfc = t.convertirEnAbsoluta(axisIfc, muroActual);
		
		try {
			
			Coordenada axisIfcParaApache = new Coordenada(axisIfc.toVector3D().subtract(normalIfc.toVector3D()));
			
			//se define el plano de apache commons
			//dicho plano contiene al origen (0,0,0) y es normal a axisIfcParaApache 
			Plane planoDeCorteApacheCommons = new Plane(axisIfcParaApache.toVector3D());
			
			//el plano de apache commons se translada a la posicion original definida en locationIfc para que concuerde
			Plane planoTransladado = planoDeCorteApacheCommons.translate(locationIfc.toVector3D());
			PlanoDeCorte p = new PlanoDeCorte(plano, planoTransladado);
			
			p.setAgreementFlagIfc(halfSpace.getAgreementFlag().value);
			
			p.setLocationAbsolutaIfc(locationIfc);
			p.setNormalAbsolutaIfc(normalIfc);
			
			//finalmente el plano se agrega al listado de planos de corte
			muroActual.getPlanosDeCorte().add(p);
			
		} catch (Exception e) {
			System.err.println(" NO SE PUEDE CREAR EL PLANO PORQUE TIENE NORMA = 0" );
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
