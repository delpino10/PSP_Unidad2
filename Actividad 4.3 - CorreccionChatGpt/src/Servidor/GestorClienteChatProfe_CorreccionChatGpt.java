package Servidor;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import static util.Conf.DESPEDIDA;
import static util.Conf.PUERTO;
import static util.Util.mensaje;


/**
 * GestorCliente
 *
 * Palabra clave de finalización: /fin
 */
public class GestorClienteChatProfe_CorreccionChatGpt implements Runnable {
    private static final Set<PrintWriter> clientesChat = new HashSet<>();
    private final Socket clienteSocket;
    private final String apodoServidor;

    public GestorClienteChatProfe_CorreccionChatGpt( Socket clienteSocket,  String apodoServidor) {
        this.clienteSocket = clienteSocket;
        this.apodoServidor = apodoServidor;
    }

    @Override
    public void run() {
        try (PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);
             BufferedReader entradaSocket = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()))
        ) {
            // Añadir el cliente a la lista
            synchronized (clientesChat) {
                clientesChat.add(salida);
            }

            System.out.println("Servidor esperando a que el cliente envíe una línea de texto");
//            String entradaUsuario;
            String message;
            while ((message = entradaSocket.readLine()) != null) {
                System.out.printf("clientSocket %s mensaje recibido: %s%n", clienteSocket, message);
                salida.println(message);
                message = mensaje(apodoServidor, message, clienteSocket);
                System.out.println(message);

                synchronized (clientesChat) {
                    for (PrintWriter cliente : clientesChat) {
                        cliente.println(message);
                    }
                }

                /*if (message.equals("/fin")) {
                    System.out.println(mensaje(apodoServidor, DESPEDIDA.s(), clienteSocket));
                    salida.println(PUERTO.s() + " " + DESPEDIDA.s());
                    break;
                }*/
            }
        } catch (IOException e) {
            System.err.println("Error en la gestión del cliente: " + e.getMessage());
        } finally {
            try {
                if (clienteSocket != null && !clienteSocket.isClosed()) {
                    clienteSocket.close();
                    System.out.println("El servidor ha cerrado la conexión con el cliente " + clienteSocket);
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }
}
