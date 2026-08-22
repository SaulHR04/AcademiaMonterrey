package com.youtuber.noinyeccion;

public class PrincipalSinInyeccion {
    public static void main(String[] args) {
        System.out.println("===PROYECTO 3 SIN INYECCIÓN DE DEPENDENCIAS===\n");

        Youtuber ibai = new Youtuber("Ibai");
        ibai.grabarVideo();
    }
}