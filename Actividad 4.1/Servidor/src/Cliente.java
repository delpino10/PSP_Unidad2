import java.io.IOException;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Por favor, ingrese la dirección IP y el puerto.");
            return;
        }
        String ip = args[0];   // Dirección IP del servidor
        int puerto = Integer.parseInt(args[1]);  // Puerto del servidor
        try {
            Socket socket = new Socket(ip, puerto);
            System.out.println("Conectado al servidor en " + ip + " en el puerto " + puerto);
            // Lógica para interactuar con el servidor
        } catch (IOException e) {
            System.out.println("Error al conectar al servidor: " + e.getMessage());
        }
    }
}
