package org.udistrital.ifc2citygmlv2.sbm;

public class Coordenada {
	
	double x;
	double y;
	double z;
	
	public Coordenada(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Coordenada(double X, double Y, double Z){
		x = X;
		y = Y;
		z = Z;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	@Override
	public String toString(){
		return "[ " + getX() + ", " + getY() + ", " + getZ() + " ]";
	}

}
