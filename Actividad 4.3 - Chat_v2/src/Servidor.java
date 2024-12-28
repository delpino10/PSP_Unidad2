import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private ServerSocket servidor;

    public Servidor(ServerSocket servidor) {
        this.servidor = servidor;
    }



    public static void main(String[] args) throws IOException {

        // Creamos el Socket del Servidor
        // Es el puerto por el que estará escuchando a los clientes
        ServerSocket servidorSocket = new ServerSocket(1234);
        // Instaciamos el objeto servidor y le pasamos el servidorSocket
        Servidor servidor = new Servidor(servidorSocket);
        // Iniciamos el Servidor
        servidor.iniciarServidor();

    }



    //Mantiene el Servidor encendido
    public void iniciarServidor() {

        try{
            // Mientras el Servidor este encendido
            while(!servidor.isClosed()){
                // Prepara al servidor para que los clientes se conecten
                // El programa se detendrá esté punto hasta que un cliente se conecte
                Socket socket = servidor.accept();
                System.out.printf("Un nuevo cliente se ha conectado: %s\n", socket.getInetAddress());
                // Administra los hilos en la clase GestorCliente
                GestorCliente gestorCliente = new GestorCliente(socket);
                //
                Thread hilo = new Thread(gestorCliente);
                // Iniciamos el hilo
                hilo.start();

            }
        }catch (IOException e){
            CerrarServidor();
        }
    }

    public void CerrarServidor() {
        try{
            if(servidor != null){
                servidor.close();
            }
        } catch (IOException e) {
            System.out.printf("Servidor desconectado\n %s\n", e.getMessage());
        }
    }


}
