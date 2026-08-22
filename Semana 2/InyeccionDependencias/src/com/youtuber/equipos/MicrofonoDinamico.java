package com.youtuber.equipos;
import com.youtuber.abstraccion.*;

public class MicrofonoDinamico implements Microfono{
	 private String modelo;

	 public MicrofonoDinamico(String modelo) {
		super();
		this.modelo = modelo;
	 }
	 @Override
	    public void capturarAudio() {
	        System.out.println("capturando audio aislado  (Dinamico: " + modelo + ") 🎙");
	    }
}
