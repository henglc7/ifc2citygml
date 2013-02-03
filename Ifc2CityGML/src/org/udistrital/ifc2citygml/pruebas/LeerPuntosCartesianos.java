package org.udistrital.ifc2citygml.pruebas;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;


public class LeerPuntosCartesianos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

        ReleaseManager rm = new ReleaseManager();
        try {

            //Se declara una instancia del componente ActiveX pasando el nombre como parametro
            IDispatch object = new IDispatch(rm, "IFCsvr.R300");

            //String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\casa\\IFC\\AC11-FZK-Haus-IFC.ifc";
            //String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\sabio caldas\\SabioCaldasSimplificado.ifc";
            String rutaArchivo = "C:\\Documents and Settings\\Administrator\\Desktop\\escritorio\\modelos\\muro y ventana\\miniExample20080731-CoordView-SweptSolid.ifc";
            
            //String tipoEntidad = "ifcBoundingBox";
            String tipoEntidad = "ifcCartesianPoint";

            Object[] obj = new Object[1];
            obj[0] = rutaArchivo;

            //Se abre un diseno IFC
            IDispatch design = (IDispatch) object.method("OpenDesign", obj);

            //Se lee una propiedad del diseno
            Object o = design.get("Name");
            String name = String.valueOf(o);
            System.out.println(name + "\n");


            //se cuenta el numero de ventanas
            obj[0] = tipoEntidad;
            //obj[0] = "ifcWallStandardCase";
            o = design.method("CountEntities", obj);
            String conteo = String.valueOf(o);
            System.out.println("Conteo de " + tipoEntidad + " = " + conteo + "\n");

            
            //se obtiene el listado de entidades ventana
            obj[0] = tipoEntidad;
            IDispatch entities = (IDispatch) design.method("FindObjects", obj);
            o = entities.get("Count");
            conteo = String.valueOf(o);
            System.out.println("Vector de entidades " + tipoEntidad + " = " + conteo + "\n");

            double maxX = 0;
            double maxY = 0;
            double maxZ = 0;
            
            double minX = 0;
            double minY = 0;
            double minZ = 0;
            
            //se lee la propiedad GUID de la ventana 1
            for(int i = 1; i<=Integer.valueOf(conteo);i++){
            	obj[0] = new Integer(i);
                IDispatch cartesianPoint = (IDispatch) entities.method("Item", obj);//entities.invoke("Item",IDispatch.METHOD,obj);//
                //o = entity.get("GUID");
                //System.out.println("GUID " + i + " = " + String.valueOf(o) + "\n");  

                //se cuenta el numero de atributos de la ventana
                IDispatch attributes = (IDispatch) cartesianPoint.get("Attributes");
                o = attributes.get("Count");
                //System.out.println("Atributos = " + String.valueOf(o) + "\n");
                
                Object[] actual = new Object[1];
                actual[0] = 1;
                IDispatch punto = (IDispatch) attributes.method("Item", actual);//entities.invoke("Item",IDispatch.METHOD,obj);//
                /*
                System.out.println("\n");
                System.out.println("Punto " + i);
                System.out.println("Tipo = " + punto.get("Type"));
                System.out.println("Tamano = " + punto.get("Size"));
                System.out.println("Nombre = " + punto.get("Name"));
                System.out.print("Coordenadas = ( ");
                */
                int coordenadas = Integer.valueOf((punto.get("Size").toString()));
                
                
                for(int n = 1; n<=Integer.valueOf(coordenadas);n++){
                	
                	Object[] posicionVector = new Object[1];
                    posicionVector[0] = n;
                    double valor = (Double) punto.method("GetItem", posicionVector);//entities.invoke("Item",IDispatch.METHOD,obj);//
                    // System.out.print(valor);
                    
                    
                    if( n==1 && valor > maxX){
                    	maxX = valor;
    					System.out.print("\n nuevo maximo X = " + valor);
                    }else if( n==2 && valor > maxY){
                    	maxY = valor;
    					System.out.print("\n nuevo maximo Y = " + valor);
                    }else if( n==3 && valor > maxZ){
                    	maxZ = valor;
    					System.out.print("\n nuevo maximo Z = " + valor);
                    }
                    
                    if( n==1 && valor < minX){
                    	minX = valor;
    					System.out.print("\n nuevo minimo X = " + valor);
                    }else if( n==2 && valor < minY){
                    	minY = valor;
    					System.out.print("\n nuevo minimo Y = " + valor);
                    }else if( n==3 && valor < minZ){
                    	minZ = valor;
    					System.out.print("\n nuevo minimo Z = " + valor);
                    }
                    
                    if(n==Integer.valueOf(coordenadas)) break;
                    //System.out.print(" , ");
                    	
                	
                }
                
                //System.out.println(" ) ");
                
                                
            }
            
            
            System.out.println("\n");
            System.out.print("MAXIMOS X = " + maxX + " Y = " + maxY + " Z = " + maxZ);
            System.out.println("\n");
            System.out.print("MINIMOS X = " + minX + " Y = " + minY + " Z = " + minZ);
            

             

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rm.release();
        }



	}

}
