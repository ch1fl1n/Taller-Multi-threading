package com.taller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

/**
 * Ejemplo propio: simulación del demo.py del taller.
 * Tres hilos corren en paralelo, cada uno imprime un contador
 * con pausas aleatorias, equivalente al threading de Python.
 */
public class CustomExample {
    private static final Random random = new Random();

    static void worker(String name) {
        for (int i = 1; i <= 5; i++) {
            System.out.println(name + " - Contador: " + i);
            try {
                // Pausa aleatoria entre 1 y 3 segundos (igual que el demo.py)
                Thread.sleep((random.nextInt(3) + 1) * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(name + " - Completado");
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Lanzar 3 hilos en paralelo
        executor.submit(() -> worker("Hilo 1"));
        executor.submit(() -> worker("Hilo 2"));
        executor.submit(() -> worker("Hilo 3"));

        executor.shutdown();

        // Esperar a que todos terminen
        while (!executor.isTerminated()) {
            Thread.sleep(500);
        }

        System.out.println("Hilos completados");
    }
}