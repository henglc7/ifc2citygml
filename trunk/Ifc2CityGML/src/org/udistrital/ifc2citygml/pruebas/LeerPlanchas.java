package org.udistrital.ifc2citygml.pruebas;

import java.util.List;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.udistrital.ifc2citygml.ifc.Piso;

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

            //se cuenta el numero de entidades
            obj[0] = "IfcSlab";
            IDispatch planchas = (IDispatch) design.method("FindObjects", obj);
            System.out.println("\n Conteo de " + "IfcSlab" + " = " + planchas.get("Count"));
            
			
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }
		
	}

}
