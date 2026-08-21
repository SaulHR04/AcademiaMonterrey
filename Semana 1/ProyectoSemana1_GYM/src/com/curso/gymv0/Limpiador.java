package com.curso.gymv0;

public class Limpiador extends Usuario {

	public Limpiador(String nombre) {
		super(nombre);
	}
public void permisosUsuario() {
	System.out.println("Matricula | "+id +"|Limpieza: "+ getNombre() + "|Acceso a instalaciones|");
}
}
