package org.udistrital.ifc2citygmlv2.sbm;

import org.udistrital.ifc2citygmlv2.sbm.ifc.Solido;

public class Vacio extends Solido{
	
	Muro muroAlQueVacia = null;

	public Muro getMuroAlQueVacia() {
		return muroAlQueVacia;
	}

	public void setMuroAlQueVacia(Muro muroAlQueVacia) {
		this.muroAlQueVacia = muroAlQueVacia;
	}

}
