package org.example;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	
    // 1. Contador INSEGURO: Un 'int' normal sufre de Race Condition o perdidas cuando entran vistas simultáneas
    private static int vistasInseguras = 0;

    // 2. Contador SEGURO: 'AtomicInteger' garantiza que cada vista se cuente a nivel de hardware
    private static AtomicInteger vistasSeguras = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        int hilosServidores = 10;
        int vistasPorServidor = 1000;       // Visitas por servidor 
        int totalEsperado = hilosServidores * vistasPorServidor;

        System.out.println("=== ANALÍTICAS DE YOUTUBE EN TIEMPO REAL ===");
        System.out.println("Simulando " + hilosServidores + " servidores recibiendo " + vistasPorServidor + " vistas simultáneas cada uno...\n");

        //  ExecutorService para el manejo del pool de hilos 
        ExecutorService poolServidores = Executors.newFixedThreadPool(hilosServidores);

        //  simulacion de pool de hilos
        for (int i = 1; i <= hilosServidores; i++) {
            final int idServidor = i;
            poolServidores.submit(() -> {
                for (int j = 0; j < vistasPorServidor; j++) {
                    // Operación insegura 
                    vistasInseguras++;

                    // Operación segura 
                    vistasSeguras.incrementAndGet();
                }
                System.out.println("[Servidor " + idServidor + "] Terminó de registrar sus 1,000 vistas.");
            });
        }

      
        poolServidores.shutdown();
        poolServidores.awaitTermination(5, TimeUnit.SECONDS);

        // comparacion de resultados, sin AtomicInteger - con AtomicInteger y visitas esperadas
        System.out.println("\n=== RESULTADO FINAL DE VISTAS EN EL CANAL ===");
        System.out.println("Vistas reales recibidas (Esperado): " + totalEsperado);
        System.out.println("Contador INSEGURO (int normal):    " + vistasInseguras );
        System.out.println("Contador SEGURO (AtomicInteger):   " + vistasSeguras.get() + " Resultado real con AtomicInteger");
    }
}