import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Por favor, ingrese el número de puerto.");
            System.exit(1);
        }
        int puerto = Integer.parseInt(args[0]);  // Obtenemos el puerto desde los parámetros de la línea de comando
//        int puerto = 8000;  // Obtenemos el puerto desde los parámetros de la línea de comando
        System.out.println("Servidor escuchando en el puerto " + puerto);
        // Aquí se incluiría la lógica para iniciar el servidor (por ejemplo, un servidor de sockets)
        try (
                ServerSocket servidor = new ServerSocket(puerto);
                Socket cliente = servidor.accept();
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        ) {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                //salida.println(mensaje);
                System.out.println(mensaje);
                // Terminamos la conexión si escribimos "fin"
                if(mensaje.equals("fin")){
                    // Mensaje que aparece en la consola
                    System.out.println("Palabra clave recibida. Cerrando servidor...");
                    // Mensaje que aparece en el cliente
                    salida.println("Servidor cerrado. Adiós.");
                    break;
                }else{
                    //
                    salida.println(mensaje);
                }
            }
        }catch (IOException e) {
            System.out.println("Error al escuchar el puerto " + puerto);
        }
    }
}
