package Servidor;

import Util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ServidorMulti {
    private static int puerto; // Variable de clase para el puerto
    private static final int MAX_CLIENTES = 3; // Número máximo de clientes simultáneos

    /**
     * Método de inicialización para validar y asignar el puerto.
     * @param args Argumentos de la línea de comandos.
     */
    public static void inicializacion(String[] args) {
        if (args.length != 1) {
            Util.uso(Arrays.toString(args));
            System.exit(1);
        }

        try {
            int puertoCandidato = Integer.parseInt(args[0]);
            if (puertoCandidato < 1 || puertoCandidato > 65535) {
                Util.error();
                System.exit(1);
            }
            puerto = puertoCandidato;
        } catch (NumberFormatException e) {
            Util.error(EchoError.PUERTO_INVALIDO);
            System.exit(1);
        }
    }

    /**
     * Método principal para iniciar el servidor.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        inicializacion(args);

        System.out.println("Servidor iniciado en puerto " + puerto);

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            int contador = 0;

            while (true) {
                // Espera hasta que el número de clientes simultáneos sea menor o igual a MAX_CLIENTES.
                if (contador < MAX_CLIENTES) {
                    // System.out.println("Esperando peticiones de conexión del cliente " + contador); // Mensaje opcional
                    Socket socket = serverSocket.accept();
                    contador++;
                    System.out.println("Se ha establecido la conexión con el cliente " + contador);

                    // Crear un hilo para manejar al cliente
                    new Thread(() -> {
                        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

                            System.out.println("Servidor esperando a que el cliente envíe una línea de texto");
                            String linea;
                            while ((linea = entrada.readLine()) != null) {
                                salida.println("Echo: " + linea);
                                System.out.println(linea);
                            }
                        } catch (Exception e) {
                            System.err.println("Error en la conexión con el cliente.");
                        } finally {
                            contador--;
                        }
                    }).start();
                }
            }
        } catch (Exception e) {
            System.err.println("Error iniciando el servidor: " + e.getMessage());
        }
    }


}
