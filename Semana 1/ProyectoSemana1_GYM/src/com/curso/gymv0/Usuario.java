package com.curso.gymv0;

public class Usuario {
private static int matricula = 0;
protected int id; 
private String nombre;


public Usuario(String nombre) {
	matricula++;
	this.id = matricula;
	this.nombre = nombre;
}

public int matricula() {return matricula; }
public String getNombre() {return nombre;}

public void permisosUsuario() {
	System.out.println("Usuario:" + getNombre()+ " Acceso exitoso");
}

}
