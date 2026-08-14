package com.curso.gymv0;

public class Entrenador extends Usuario {

	public Entrenador(String nombre) {
		super(nombre);
		
	}
	
@Override
public void permisosUsuario() {
	System.out.println("Nombre:" + getNombre() + "Permisos: Entrenar Usuarios" );
}
}
