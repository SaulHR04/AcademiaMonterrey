package com.youtubers.creadores;
import com.youtuber.abstraccion.*;

public class Youtubers {
	private String nombre;
    private final Microfono miMicrofono; //Dependencia
    
	public Youtubers(String nombre, Microfono miMicrofono) {
		this.nombre = nombre;
		this.miMicrofono = miMicrofono;
	} 
    
	public void grabarVideo() {
        System.out.print(nombre + " esta en transmicion en vivo y ");
        miMicrofono.capturarAudio(); 
    }
}
