package com.curso.gymv0;

final class PlanUsuario {
	
private final String nombre;
private final double precio;		//inmutabilidad
private final boolean PermisoSPA;			//Final para que no pueda modificarse despúes


public PlanUsuario(String nombre, double precio, boolean PermisoSPA) {
	this.nombre = nombre; 
	this.precio = precio;     		//Constructor del plan usuario
	this.PermisoSPA = PermisoSPA;
}


public boolean getPermisoSPA(){return PermisoSPA;}
public double getPrecio() {return precio;}     //Getters no necesito Setters ya que es final
public String getNombre() {return nombre;}


}


