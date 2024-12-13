package servidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;


/**
 * GestorCliente
 * Palabra clave de finalización: /fin
 */
public class GestorCliente implements Runnable {
    // Lista de clientes conectados
    private static final Set<PrintWriter> clientesChat = new HashSet<PrintWriter>();
    private final Socket clienteSocket;

    // constructor
    public GestorCliente( Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    @Override
    public void run() {
        try (PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader( new InputStreamReader(clienteSocket.getInputStream())
             )
        ) {
            // Añadir el cliente a la lista
            synchronized (clientesChat) {
                clientesChat.add(salida);
            }

            System.out.println("Servidor esperando a que el cliente envíe una línea de texto");
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.printf("clientSocket %s mensaje recibido: %s%n", clienteSocket, mensaje);
                salida.println(mensaje);

                synchronized (clientesChat) {
                    for(PrintWriter cliente : clientesChat) {
                        cliente.println(mensaje);
                    }
                }

                if (mensaje.equals("fin")) {
                    String despedida = "¡Adios querido cliente!";
                    salida.println(despedida);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage() );
        } finally {
            try {
                // Cerrar el socket porque no podemos dejar recursos abiertos
                clienteSocket.close();
                System.out.println("El servidor ha cerrado la conexión con el cliente " + clienteSocket);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage() );
            }
        }
    }
}