package com.youtuber.equipos;
import com.youtuber.abstraccion.*;


public class microfonocondensado implements Microfono{
private String modelo;

public microfonocondensado(String modelo) {
	this.modelo = modelo;
}
@Override
public void capturarAudio() {
    System.out.println("capturando audio  (microfono: " + modelo + ") ");
}

}
