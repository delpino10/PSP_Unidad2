package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import util.Util;


import static util.Configuracion.*;
import static util.EchoError.ERROR_PUERTO_INVALIDO;
import static util.Util.error;

/**
 * Servidor Chat
 * En el servidor creamos un hilo para cada cliente conectado y simplemente hacemos eco del mensaje del cliente.
 * Es decir, una vez recibido el mensaje del cliente, lo mostramos por pantalla y se lo enviamos de vuelta al cliente.
 * Esta tarea la realiza el hilo asociado a un objeto GestorCliente.
 *
 * @author Eduardo Barra Balao
 * @version 0.1
 */
public class ServidorChat {
    private static final int MAX_CLIENTES = 2;
    //private static final int MIN_PUERTO = 1;
    // private static final int MAX_PUERTO = 65535;
    private final int puerto;                         // Puerto del servidor
    private final int maxClientes;                    // Máximo número de clientes conectados
    private final ThreadGroup clientes;               // Clientes de chat

    /**
     * ServidorChat
     *
     * @param puerto        Puerto del servidor
     * @param maxCLientes   Máximo número de clientes conectados
     */
    public ServidorChat(int puerto, int maxCLientes) {
        this.puerto = puerto;
        this.maxClientes = maxCLientes;
        this.clientes = new ThreadGroup("Clientes");
    }

    /**
     * main
     *
     * @param args  [0] Puerto del servidor
     */
    public static void main(String[] args) {
        final int MAX_CLIENTES=9;
        int puerto=0;

        if (args.length != 1) {
            uso();
            System.exit(1);
        }

        // Verificar que el puerto está en un rango determinado
        try {
            puerto = Integer.parseInt(args[0]);
            if (puerto < MIN_PUERTO.n()|| puerto > MAX_PUERTO.n()) {
                error(ERROR_PUERTO_INVALIDO, puerto);
                uso();
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            error(ERROR_PUERTO_INVALIDO, puerto);
            uso();
            System.exit(1);
        }
        // Iniciamos el Servidor
        new ServidorChat(puerto, MAX_CLIENTES).iniciar();
        System.out.println("Fin del Servidor: " + APODO_SERVIDOR.s());
    }

    public void iniciar() {
        int contador=0;
        // Iniciamos el servidor en un determinado puerto
        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            // Mensaje por consola
            //System.out.println(version("ServidorChat ", + VERSION_SERVIDOR.s()));
            System.out.println("Servidor iniciado en puerto "+puerto);
            // Según van accediendo los clientes
            while (++contador<=MAX_CLIENTES) {
                /*
                El servidorSocket es el encargado de aceptar las
                solicitudes de conexión de los clientes y,
                cuando un cliente solicita una conexión, servidorSocket establece la conexión
                */
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Se ha establecido la conexión con el cliente "+contador);
                // Esperar a que un cliente se conecte
                // Creamos un hilo para manejar los mensajes del cliente
                new Thread(clientes, new GestorCliente(clienteSocket), APODO_SERVIDOR.s()).start();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void uso() {
        Util.uso("puerto");
    }
}