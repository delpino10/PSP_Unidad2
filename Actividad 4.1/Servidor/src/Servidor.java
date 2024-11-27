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

        int puerto = 0;  // Obtenemos el puerto desde los parámetros de la línea de comando (args[0])
        try {
            puerto = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.printf("El puerto debe ser un número [puerto = %s]", args[0]);
            System.exit(1);
        }

        System.out.println("Servidor escuchando en el puerto " + puerto);

        // A partir de aquí se incluiría la lógica para iniciar el servidor
        try (
                // Crea un socket de tipo servidor (ServerSocket en Java)
                ServerSocket servidor = new ServerSocket(puerto);
                // El serivdor establece la conexión con el cliente
                // Si queremos más conexiones de más clientes, crear más socket cliente
                Socket cliente = servidor.accept();
                // InputStream del cliente al Servidor
                BufferedReader flujoEntrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                // OutputStream del servidor al cliente
                PrintWriter flujoSalida = new PrintWriter(cliente.getOutputStream(), true);
        ) {
            String mensaje;
            while ((mensaje = flujoEntrada.readLine()) != null) {
                // Salida del texto por consola
                System.out.println(mensaje);
                // Salida del texto por pantalla del cliente
                flujoSalida.println(mensaje);
                // Terminamos la conexión si escribimos "fin"
                if(mensaje.equals("fin")){
                    // Mensaje que aparece en la consola
                    System.out.println("Palabra clave recibida. Cerrando servidor...");
                    // Mensaje que aparece en el cliente
                    flujoSalida.println("Servidor cerrado. Adios.");
                    break;
                }else{
                    // Mensaje escrito por pantalla
                    flujoSalida.println(mensaje);
                }
            }
        }catch (IOException e) {
            System.out.println("Error al escuchar el puerto " + puerto);
            System.err.println(e.getMessage());
        }
    }
}
