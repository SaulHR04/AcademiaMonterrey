package com.youtuber.noinyeccion;


public class Youtuber {
	 private String nombre;
	 private Microfonoyeti miMicrofono;
	
	 
	 public Youtuber(String nombre) {
		this.nombre = nombre;	
		this.miMicrofono = new Microfonoyeti();
		}
public void grabarVideo() {
	System.out.print(nombre + " esta grabando su video y ");
	miMicrofono.usar(); 	    
	}
}
