import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private Socket socket;
    // Lee los mensajes que han sido enviados desde el gestorCliente
    private BufferedReader bufferedReader;
    // Manda mensajes a los clientes, que a su vez son mensajes enviados por otros clientes
    private BufferedWriter bufferedWriter;
    // Nombre del Usuario
    private String nombreUsuario;

    public Cliente(Socket socket, String nombreUsuario) {
        try{
            // Conexión
            this.socket = socket;
            // Los mensajes que manda
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Los mensajes que recibe
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Nombre de Usuario
            this.nombreUsuario = nombreUsuario;
        } catch (IOException e) {
            cierraConex(socket, bufferedReader, bufferedWriter);
        }
    }

    public void mandarMensaje(){
        try {
            // escribe texto en un flujo de salida
            bufferedWriter.write(nombreUsuario);
            // Este método inserta un salto de línea en el archivo
            bufferedWriter.newLine();
            /* Este método vacía el buffer escribiendo todos los datos acumulados en él al destino.
             Esto asegura que todo lo escrito esté disponible en el archivo o flujo subyacente,
             incluso si no se cierra todavía*/
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String mensajeParaEnviar = sc.nextLine();
                bufferedWriter.write(nombreUsuario + " : " + mensajeParaEnviar);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            cierraConex(socket, bufferedReader, bufferedWriter);
        }
    }

    public void escucharMensajes(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                String mensajeParaGrupo;

                while (socket.isConnected()) {
                    try {
                        mensajeParaGrupo = bufferedReader.readLine();
                        System.out.println(mensajeParaGrupo);
                    }catch (IOException e) {
                        cierraConex(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void cierraConex(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un nombre del usuario para el chat: ");
        String nombreUsuario = sc.nextLine();

        // Creamos la conexión con el servidor
        Socket socketCliente = new Socket("localhost", 1234);

        // Creamos el Cliente
        Cliente cliente = new Cliente(socketCliente, nombreUsuario);
        // Escucha Mensajes
        cliente.escucharMensajes();
        // Manda mensajes
        cliente.mandarMensaje();
    }
}


