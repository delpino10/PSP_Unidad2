import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

public class Sacerdotisa implements Runnable{
    private ServerSocket servidor;
    private final String nombre = "PITIA";
    private Oraculo oraculo;

    public Sacerdotisa(Oraculo oraculo) throws IOException {
        this.oraculo = oraculo;
        servidor = new ServerSocket(5000);
    }
    public void run(){
        for (int i =0 ; i< 3;i++) {
            try {
                SecureRandom random = new SecureRandom();
                int TIEMPO1 = random.nextInt(5000);
                Socket clientSocket = servidor.accept();
                new Thread(() -> {
                    try (PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                        Thread.sleep(TIEMPO1);
                        //Lee la pregunta y envia la respuesta del oráculo
                        socketOut.println(oraculo.getRespuesta(socketIn.readLine()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                ).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}