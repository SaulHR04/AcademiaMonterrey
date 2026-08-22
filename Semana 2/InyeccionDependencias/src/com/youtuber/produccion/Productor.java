package com.youtuber.produccion;
import com.youtuber.abstraccion.*;
import com.youtuber.equipos.*;
import com.youtubers.creadores.*;


public class Productor {

    public static Youtubers obtenerYoutuberEquipado(String nombre, String ambiente) {
        Microfono microParaInyectar;

        if (ambiente.equalsIgnoreCase("estudio")) {
            microParaInyectar = new microfonocondensado ("Audio-Technica AT2020");
        } else {
            microParaInyectar = new MicrofonoDinamico("Shure");
        }

        return new Youtubers(nombre, microParaInyectar);
    }
}