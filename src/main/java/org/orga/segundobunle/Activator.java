package org.orga.segundobunle;

import java.util.Scanner;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private Scanner scanner;
    private Thread thread;
    private volatile boolean running = true;

    public void start(BundleContext context) throws Exception {
        System.out.println("Iniciando el bundle");

        thread = new Thread(() -> {
            scanner = new Scanner(System.in);

            while (running) {
                mostrarMenu();
                int opcion = obtenerOpcionUsuario();

                switch (opcion) {
                    case 1:
                        ejecutarMultiplicacion();
                        break;
                    case 2:
                        ejecutarDivision();
                        break;
                    case 0:
                        System.out.println("Saliendo del bundle");
                        running = false;
                        break;
                    default:
                        System.out.println("Opción inválida. Inténtalo de nuevo.");
                }
            }

            stopInternal();
        });

        thread.start();
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Deteniendo el bundle");

        running = false;

        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (scanner != null) {
            scanner.close();
            scanner = null;
        }

        System.out.println("El bundle ha sido detenido.");

        if (context != null) {
            context.getBundle().stop();
        }
    }

    private void stopInternal() {
        try {
            stop(null);
            System.out.println("El bundle ha sido detenido completamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMenu() {
        System.out.println("----- Calculadora OSGi -----");
        System.out.println("1. Multiplicación");
        System.out.println("2. División");
        System.out.println("0. Salir");
        System.out.println("-----------------------------");
        System.out.print("Selecciona una opción: ");
    }

    private int obtenerOpcionUsuario() {
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    private void ejecutarMultiplicacion() {
        System.out.print("Ingresa el primer número: ");
        int num1 = scanner.nextInt();
        System.out.print("Ingresa el segundo número: ");
        int num2 = scanner.nextInt();
        int resultado = num1 * num2;
        System.out.println("El resultado de la multiplicación es: " + resultado);
    }

    private void ejecutarDivision() {
        System.out.print("Ingresa el primer número: ");
        int num1 = scanner.nextInt();
        System.out.print("Ingresa el segundo número: ");
        int num2 = scanner.nextInt();
        if (num2 != 0) {
            double resultado = (double) num1 / num2;
            System.out.println("El resultado de la división es: " + resultado);
        } else {
            System.out.println("No se puede dividir por cero.");
        }
    }
}