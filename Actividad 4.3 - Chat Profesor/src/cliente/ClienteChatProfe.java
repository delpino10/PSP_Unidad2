package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.jetbrains.annotations.NotNull;
import util.EchoError;
import util.Util;

import static util.Conf.*;
import static util.EchoError.ERROR_PUERTO_INVALIDO;
import static util.Util.*;

/**
 * Cliente Chat
 * arg[0]   Apodo del cliente
 * arg[1]   IP del servidor
 * arg[2]   Puerto del servidor
 * Palabra clave de finalización: /fin
 *
 * @author Eduardo Barra Balao
 * @version 0.2
 */
public class ClienteChatProfe {
    private String apodo;               // Apodo del cliente
    private InetAddress ipServidor;     // IP del servidor
    private int puertoServidor;         // Puerto del servidor

    public ClienteChatProfe(@NotNull String apodo, InetAddress ipServidor, @NotNull int puertoServidor) {
        this.apodo = apodo;
        this.ipServidor = ipServidor;
        this.puertoServidor = puertoServidor;
    }

    public static void main(String[] args)  {
        if (args.length != 3) {
            uso();
            System.exit(1);
        }
        try{
            
          /*  int n = 0;
            String[] args1 = args;
            
        switch (args[n]) {
            case 1:
                String apodo = args[0];
                break;
                
            case 2:
                InetAddress ipServidor = null;
                ipServidor = InetAddress.getByName(args[1]);
                break;
            case 3:
                
                break;  
        }*/


        // TODO: 17/12/2023 Usar parámetro args[1] como IP del servidor. Por ahora se usa localhost
        } catch (UnknownHostException e) {
            error(EchoError.ERROR_HOST_INVALIDO,args[1]);
            uso();
            System.exit(1);
        }

        int puertoServidor = 0;         // TODO: 17/12/2023 T05, T08
        try {
            //T08: Controlar el rango válido para un puerto (1-65535)
            try {
            puertoServidor = Integer.parseInt(args[2]);
                if (puertoServidor < MIN_PUERTO.n() || puertoServidor > MAX_PUERTO.n()) {
                    error(ERROR_PUERTO_INVALIDO, puertoServidor);
                    uso();
                    System.exit(1);
                }
            //T05: Controlar que el número de puerto servidor sea entero
            }catch (NumberFormatException e) {
                System.err.println(e.getMessage());
                error(ERROR_PUERTO_INVALIDO, puertoServidor);
                uso();
                System.exit(1);
            }

            // Creamos el Cliente
            new ClienteChatProfe(apodo, ipServidor, puertoServidor).iniciar();
            System.out.println("Fin del cliente: "+apodo);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void iniciar(String [] args) throws IOException {
        try (
                // Se crea un socket que se conecta al servidor especificado por la dirección
                Socket Cliente = new Socket(String.valueOf(ipServidor), puertoServidor);
                // Se utiliza un PrintWriter para enviar datos al servidor.
                PrintWriter salidaServidor = new PrintWriter(Cliente.getOutputStream(), true);
                // Se utiliza un BufferedReader para recibir datos del servidor.
                BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(Cliente.getInputStream()));
                // Este BufferedReader permite leer datos ingresados por el usuario desde la consola.
                BufferedReader entradaPorTeclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            //T02: Modificar el programa del cliente para que reciba la despedida del servidor y la muestre por pantalla
            // Mensaje Servidor
            String mensajeServidor;
            try{
                while ((mensajeServidor = entradaServidor.readLine()) != null) {
                    System.out.printf("Mensaje del %s: %s", APODO_SERVIDOR.s(),mensajeServidor);
                }
            } catch (IOException e) {
                System.err.println("Error:" + e.getMessage());
            }

            System.out.println("Manda un mensaje o escribe 'fin' para salir ");
            System.out.println("Escribe 'info' para info de la conexión ");
            // Mensaje Cliente
            String mensajeUsuario;
            while ((mensajeUsuario = entradaPorTeclado.readLine()) != null) {
                System.out.println(mensaje(apodo, mensajeUsuario, Cliente));     // Mensaje del cliente por pantalla
                salidaServidor.println(mensaje(apodo, mensajeUsuario, Cliente));   // Mensaje del cliente al servidor

                // Mostrar el mensaje del servidor por pantalla
                System.out.println(mensaje(APODO_SERVIDOR.s(), entradaServidor.readLine(), Cliente));
                if (mensajeUsuario.equals("/fin")) {
                    String x = entradaServidor.readLine();
                    System.out.printf("El %s dice: " + DESPEDIDA.s(), PUERTO.s());
                    break;
                }
                // M01: Modificar el programa para que sea
                // configurable la visualización (o no) de la información del socket en un mensaje del cliente y/o del servidor
                if (mensajeUsuario.equals("info")) {
                    System.out.println("Conectado al servidor: " + Cliente.getInetAddress() + ":" + Cliente.getPort());
                }
            }
        /*T07: Cazar las distintas excepciones (ver documentación de la API de Java) que pueden ocurrir en el bloque
               try del método iniciar de la clase ClienteChat. Agregar errores asociados a EchoError.
               Usar ls métodos error y finalizar */
        }catch (SocketException e) {
            System.err.println("El servidor ha cerrado la conexión.");
            System.err.println(e.getMessage());
            error(EchoError.ERROR_HOST_INVALIDO, args[1]);
            finalizar(1);
        }catch (IOException e){
            System.err.println("Error:" + e.getMessage());
        }
    }

    private static void uso() {
        Util.uso("apodo puerto");
    }
}
