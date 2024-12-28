package Servidor;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import util.Conf;
import util.Util;

import static util.Conf.*;
import static util.EchoError.*;
import static util.Util.error;
import static util.Util.finalizar;

/**
 * Servidor Chat
 *
 * En el servidor creamos un hilo para cada cliente conectado y hacemos eco del mensaje del cliente.
 * Un hilo asociado a un objeto GestorCliente gestiona cada cliente.
 *
 * @version 0.3
 */
public class ServidorChatProfe_CorreccionChatGPT {
    private final int puerto;                            // Puerto del servidor
    private final int maxClientes;                       // Máximo número de clientes conectados
    private final ThreadGroup clientes;                  // Grupo de hilos de clientes
    private final AtomicInteger contadorClientes;        // Contador de clientes conectados

    /**
     * Constructor del ServidorChat
     *
     * @param puerto        Puerto del servidor
     * @param maxClientes   Máximo número de clientes conectados
     */
    public ServidorChatProfe_CorreccionChatGPT(int puerto, int maxClientes) {
        this.puerto = puerto;
        this.maxClientes = maxClientes;
        this.clientes = new ThreadGroup("Clientes");
        this.contadorClientes = new AtomicInteger(0);
    }

    /**
     * Método principal
     *
     * @param args Argumentos: [0] Puerto del servidor
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            uso();
            System.exit(1);
        }

        try {
            int puerto = Integer.parseInt(args[0]);
            if (puerto < MIN_PUERTO.n() || puerto > MAX_PUERTO.n()) {
                error(ERROR_PUERTO_INVALIDO, puerto);
                uso();
                finalizar(1);
            }

            new ServidorChatProfe_CorreccionChatGPT(puerto, Conf.MAX_CLIENTES.n()).iniciar();
        } catch (NumberFormatException e) {
            error(ERROR_PUERTO_INVALIDO, 0);
            uso();
            finalizar(1);
        }
    }

    /**
     * Inicia el servidor
     */
    public void iniciar() {
        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            System.out.printf("Servidor iniciado en el puerto %d. Máximo clientes: %d%n", puerto, maxClientes);

            while (true) {
                if (contadorClientes.get() >= maxClientes) {
                    System.out.println("Máximo número de clientes alcanzado. Esperando desconexiones...");
                    Thread.sleep(1000);
                    continue;
                }

                Socket clienteSocket = servidorSocket.accept();
                int clienteId = contadorClientes.incrementAndGet();
                System.out.printf("Cliente %d conectado desde %s%n", clienteId, clienteSocket.getInetAddress());

                // Crear un hilo para manejar al cliente
                new Thread(clientes, new GestorClienteChatProfe_CorreccionChatGpt(clienteSocket, APODO_SERVIDOR.s()), APODO_SERVIDOR.s()).start();
            }
        } catch (BindException e) {
            error(ERROR_PUERTO_OCUPADO, puerto);
        } catch (IOException e) {
            error(ERROR_PUERTO_IO, puerto);
        } catch (InterruptedException e) {
            System.err.println("Servidor interrumpido: " + e.getMessage());
        } finally {
            System.out.println("Servidor cerrado.");
        }
    }

    /**
     * Reduce el contador de clientes al desconectarse uno.
     */
    public void clienteDesconectado() {
        contadorClientes.decrementAndGet();
    }

    private static void uso() {
        Util.uso("ServidorChatProfe <puerto>");
    }
}


