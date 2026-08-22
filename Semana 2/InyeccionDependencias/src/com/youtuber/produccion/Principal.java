package com.youtuber.produccion;
import com.youtubers.creadores.*;



public class Principal {
    public static void main(String[] args) {
        System.out.println("=== PROYECTO 3 INYECCION DE DEPENDENCIAS ===\n");

        Youtubers ibai = Productor.obtenerYoutuberEquipado("Ibai", "estudio");
        ibai.grabarVideo();


        Youtubers auron = Productor.obtenerYoutuberEquipado("Auronplay", "habitacion");
        auron.grabarVideo();
        
        Youtubers Vegetta777 = Productor.obtenerYoutuberEquipado("Vegetta", "estudio");
        Vegetta777.grabarVideo();
    }
}