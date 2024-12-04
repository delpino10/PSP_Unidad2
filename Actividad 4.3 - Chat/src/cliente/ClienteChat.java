package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import util.Util;

/**
 * Cliente Chat
 * arg[0]   Puerto del servidor
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
                // Salida Servidor
                PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
                // Entrada desde el servidor
                BufferedReader entradaServidor = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                // Entrada desde el cliente
                BufferedReader entradaUser = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            // hilo para leer los mensajes del servidor
            Thread hiloLectura = new Thread(() -> {
                String mensajeDesdeServidor;
                try{
                    while ((mensajeDesdeServidor = entradaServidor.readLine()) != null){
                        System.out.println("Mensaje: " + mensajeDesdeServidor);
                    }
                } catch (IOException e) {
                   System.err.println("Error: " + e.getMessage());
                }
            });
            hiloLectura.start();

            String mensajeDesdeUsuario;
            while ((mensajeDesdeUsuario = entradaUser.readLine()) != null){
                salidaServidor.println(mensajeDesdeUsuario);
                // Si el cliente envió "/fin", el servidor enviará la despedida
                if (mensajeDesdeUsuario.equals("fin")) {
                    String x = entradaUser.readLine();
                    System.out.println("El servidor dice: " + x);
                    break;
                }
            }

        }catch (SocketException e){
            // Manejar la excepción si el servidor ha cerrado la conexión
            System.err.println("El servidor ha cerrado la conexión.");
        }catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void uso() {
        Util.uso("puerto");
    }
}