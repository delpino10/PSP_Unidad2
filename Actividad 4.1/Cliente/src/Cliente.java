import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static void main(String[] args) throws UnknownHostException {
        if (args.length != 1) {
            System.out.println("Por favor, ingrese la dirección IP y el puerto.");
            return;
        }
        String ip = args[0];   // Dirección IP del servidor
        InetAddress inetAddress = InetAddress.getLocalHost();
        int puerto = Integer.parseInt(args[0]);  // Puerto del servidor
        try (
                Socket socket = new Socket(inetAddress, puerto);
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                socketOut.println(userInput);
                System.out.println("echo: " + socketIn.readLine());
            }
        }catch (IOException e) {
            System.out.println("Error al conectar al servidor: " + e.getMessage());
        }
    }
}
