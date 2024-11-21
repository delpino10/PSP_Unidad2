import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Por favor, ingrese el número de puerto.");
            System.exit(1);
        }

        int puerto = Integer.parseInt(args[0]);
        System.out.println("Servidor escuchando en el puerto " + puerto);
        int contador=0;
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            while (++contador<=1) {
                Socket cliente = servidor.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
                             BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        ) {
                            String mensaje;
                            while ((mensaje = entrada.readLine()) != null) {
                                System.out.printf("cliente %s echo: %s%n", cliente, mensaje);
                                salida.println(mensaje);
                                if (mensaje.equals("fin")) {
                                    salida.println("ADIOS QUERIDO CLIENTE");
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                cliente.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}