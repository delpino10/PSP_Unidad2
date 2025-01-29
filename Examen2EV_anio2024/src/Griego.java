import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;

public class Griego implements Runnable{
    private String Apodo;
    private final SecureRandom random;
    private Oraculo oraculo;

    public Griego(String apodo,Oraculo oraculo) {
        Apodo = apodo;
        this.oraculo = oraculo;
        random = new SecureRandom();
    }

    @Override
    public void run() {
        synchronized (oraculo) {
            System.out.printf("Soy %s y espero que el oraculo me sea propicio. \n", Apodo);
            try (Socket socket = new Socket("localhost", 5000);
                 PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                //imprime la respuesta del oraculo que le da la sacerdotisa
                salidaSocket.println(oraculo.getPreguntas(random.nextInt(3)));
                System.out.printf("[Oraculo]: %s \n", socketIn.readLine());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
