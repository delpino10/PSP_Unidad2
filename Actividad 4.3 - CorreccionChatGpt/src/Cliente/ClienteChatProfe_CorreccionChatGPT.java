package Cliente;

import java.io.*;
import java.net.*;


import org.jetbrains.annotations.NotNull;
import util.EchoError;

import static util.Conf.APODO_SERVIDOR;
import static util.Util.error;


/**
 * Cliente Chat
 * arg[0]   Apodo del cliente
 * arg[1]   IP del servidor
 * arg[2]   Puerto del servidor
 * Palabra clave de finalización: /fin
 *
 * @author Eduardo Barra Balao
 * @version 0.3
 */
public class ClienteChatProfe_CorreccionChatGPT {
    private final String apodo;               // Apodo del cliente
    private final InetAddress ipServidor;     // IP del servidor
    private final int puertoServidor;         // Puerto del servidor
    private static boolean mostrarConf = false; // Mostrar configuración del socket
    private static String exitword = "/fin"; // Palabra clave de finalización

    public ClienteChatProfe_CorreccionChatGPT(String apodo, InetAddress ipServidor, int puertoServidor) {
        this.apodo = apodo;
        this.ipServidor = ipServidor;
        this.puertoServidor = puertoServidor;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            uso();
            System.exit(1);
        }

        String apodo = args[0];
        InetAddress ipServidor;
        int puertoServidor;

        try {
            ipServidor = InetAddress.getLocalHost();
            puertoServidor = Integer.parseInt(args[1]);

            if (puertoServidor < 1 || puertoServidor > 65535) {
                error(EchoError.ERROR_PUERTO_INVALIDO, puertoServidor);
                uso();
                System.exit(1);
            }

            if (args.length > 3 && args[3].equalsIgnoreCase("mostrarConf")) {
                mostrarConf = true;
            }

            new ClienteChatProfe_CorreccionChatGPT(apodo, ipServidor, puertoServidor).iniciar();

        } catch (UnknownHostException e) {
            //error(EchoError.ERROR_HOST_DESCONOCIDO, ipServidor);
            System.out.printf("[ERROR] Unknown host: %s%n", e.getMessage());
            uso();
            System.exit(1);
        } catch (NumberFormatException e) {
            error(EchoError.ERROR_PUERTO_INVALIDO, args[1]);
            uso();
            System.exit(1);
        }
    }

    private void iniciar() {
        try (
                // Creas el socket del Cliente (ipCliente, Puerto)
                Socket cliente = new Socket(ipServidor, puertoServidor);
                // Lo que escribe el cliente que sale al servidor
                PrintWriter salidaServidor = new PrintWriter(cliente.getOutputStream(), true);
                // Lo que entra del servidor, procedente de otro cliente
                BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                // Lo que ek cliente escribe a otro cliente por teclado
                BufferedReader entradaPorTeclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado al servidor. Escribe '/fin' para salir.");

            // Hilo para escuchar mensajes del servidor
            new Thread(() -> {
                try {
                    String mensajeServidor;
                    while ((mensajeServidor = entradaServidor.readLine()) != null) {
                        System.out.printf("[%s]: %s%n", apodo, mensajeServidor);
                    }
                } catch (IOException e) {
                    System.err.println("Conexión cerrada por el servidor.");
                }
            }).start();

            // Enviar mensajes al servidor
            System.out.println("Manda un mensaje o escribe 'fin' para salir ");
            String mensajeUsuario;
            while ((mensajeUsuario = entradaPorTeclado.readLine()) != null) {
                if (mensajeUsuario.equalsIgnoreCase(exitword)) {
                    salidaServidor.println(mensajeUsuario);
                    System.out.println("Desconectando...");
                    break;
                }

                if (mensajeUsuario.equalsIgnoreCase("info") && mostrarConf) {
                    System.out.printf("Conexión: %s:%d%n", cliente.getInetAddress(), cliente.getPort());
                } else {
                    salidaServidor.println(mensajeUsuario);
                }
            }
        } catch (ConnectException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        } catch (SocketException e) {
            System.err.println("El servidor cerró la conexión: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void uso() {
        System.out.println("Uso: ClienteChatProfe <apodo> <ipServidor> <puerto> [mostrarConf]");
    }
}

