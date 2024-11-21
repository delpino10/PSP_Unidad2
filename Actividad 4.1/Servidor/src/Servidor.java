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
        System.out.println("Servidor escuchando en el puerto " + puerto);
        // Aquí se incluiría la lógica para iniciar el servidor (por ejemplo, un servidor de sockets)
        try (
                ServerSocket echoSocket = new ServerSocket(puerto);
                Socket clientSocket = echoSocket.accept();
                PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = socketIn.readLine()) != null) {
                socketOut.println(inputLine);
                System.out.println(inputLine);
            }
        }catch (IOException e) {
            System.out.println("Error al escuchar el puerto " + puerto);
        }
    }
}
