package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import util.Util;

/**
 * Cliente Chat
 * arg[0]   Puerto del servidor
 *
 * Palabra clave de finalización: /fin
 *
 * @author Eduardo Barra Balao
 * @version 0.1
 */
public class ClienteChat {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            uso();
            System.exit(1);
        }
        InetAddress ip = InetAddress.getLocalHost();
        int puerto = Integer.parseInt(args[0]);
        try (
                Socket socket = new Socket(ip, puerto);
                PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entradaSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader entradaEstandar = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String entradaUsuario;
            while ((entradaUsuario = entradaEstandar.readLine()) != null) {
                salidaSocket.println(entradaUsuario);
                System.out.println("echo: " + entradaSocket.readLine());
                // Aunque el cliente envie la palabra de finalización, el hilo cliente no finaliza (se queda en este
                // bucle while).
                // Además no leemos del socket de entrada la despedida que nos envía el servidor tras enviarle la
                // palabra de finalización (ni la mostramos por pantalla)
                // Si escribimos en el teclado y el servidor ha finalizado la conexión, se intentará escribir en el
                // socket de salida que ya no está conectado al socket del servidor y ocurrirá el siguiente error:
                // Exception in thread "main" java.net.SocketException: Se ha anulado una conexión establecida por el
                // software en su equipo host.
            }
        }
    }

    private static void uso() {
        Util.uso("puerto");
    }
}
