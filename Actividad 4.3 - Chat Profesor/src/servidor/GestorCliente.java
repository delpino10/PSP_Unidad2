package servidor;

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
public class GestorCliente implements Runnable {
    private static final Set<PrintWriter> clientesChat = new HashSet<PrintWriter>();
    private Socket clienteSocket;
    private String apodoServidor;

    public GestorCliente(@NotNull Socket clienteSocket, @NotNull String apodoServidor) {
        this.clienteSocket = clienteSocket;
        this.apodoServidor = apodoServidor;
    }

    @Override
    public void run() {
        try (PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);
             BufferedReader entradaSocket = new BufferedReader(
                     new InputStreamReader(clienteSocket.getInputStream())
             );
        ) {
            // Añadir el cliente a la lista
            synchronized (clientesChat) {
                clientesChat.add(salida);
            }

            System.out.println("Servidor esperando a que el cliente envíe una línea de texto");
            String entradaUsuario;
            String linea;
            while ((entradaUsuario = entradaSocket.readLine()) != null) {
                System.out.println(entradaUsuario);
                salida.println(entradaUsuario);
                linea=mensaje(apodoServidor, entradaUsuario, clienteSocket);
                System.out.println(linea);

                synchronized (clientesChat) {
                    for(PrintWriter cliente : clientesChat) {
                        cliente.println(linea);
                    }
                }

                if (entradaUsuario.equals("/fin")) {
                    //String despedida = "¡Adios querido cliente!";
                    /*T09: El formato en línea de comandos sería el siguiente:
                    ServidorChat [puerto] [despedida] -> Por defecto usar parámetros de Conf
                    (definir DESPEDIDA en Conf)*/
                    System.out.println(mensaje(apodoServidor, DESPEDIDA.s(), clienteSocket));
                    salida.println(PUERTO.s() + " " +DESPEDIDA.s());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
                System.out.println("El servidor ha cerrado la conexión con el cliente " + clienteSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}