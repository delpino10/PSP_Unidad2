package util;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public class Util {

    private Util() {}

    public static void error(EchoError error, Object... dato) {
        switch (error) {
            case ERROR_PUERTO_INVALIDO:
                System.out.printf("El puerto debe ser un número en el rango [1,65535] (puerto=%s)", dato[0]);
                break;
            case  ERROR_HOST_INVALIDO:
                System.out.println("Dirección IP o nombre de host inválido: " + dato[0]);
                break;
            case ERROR_PUERTO_OCUPADO:
                System.out.println("No se pudo enlazar al puerto 8080. El puerto ya está en uso: ");
                break;
            case ERROR_PUERTO_TIMEOUT:
                System.out.println("Timeout al esperar una conexión. Volviendo a intentar...");
                break;
            case ERROR_PUERTO_NOTVALID:
                System.out.println("Puerto inválido especificado:");
                break;
            case ERROR_PUERTO_IO:
                System.out.println("Error de E/S al crear o usar el ServerSocket:");
                break;
            case ERROR_HOST_DESCONOCIDO:
                System.out.println("Host deconocido");
                break;
            default:
                System.err.println("Código de error desconocido: "+error);
                System.exit(1);
        }
    }

    public static String mensaje(@NotNull String apodo, @NotNull String mensaje, @NotNull Socket clienteSocket) {
        // TODO: 17/12/2023 Agregar información sobre el socket del cliente antes del apodo: [ip:puerto]
        String ip = clienteSocket.getLocalAddress().getHostAddress();
        int puerto = clienteSocket.getPort();

        String prefijo = clienteSocket == null
                ? ""
                :String.format("%s:%d", ip, puerto);
        //equivale al operador Elvis de arriba => String.format("%s:%d", ip, puerto);
        return String.format("%s %s:%s", prefijo, apodo, mensaje);
    }

    public static void uso(@NotNull String mensaje) {
        System.out.printf("Uso: java %s %s", nombreClasePrincipal(), mensaje);
    }

    public static String nombreClasePrincipal() {
        // Obtenemos la pila de llamadas (stack trace)
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // El último elemento es el método main de la clase principal
        StackTraceElement penultimoElemento = stackTrace[stackTrace.length - 1];
        return extraerNombreClase(penultimoElemento.getClassName());
    }

    public static String extraerNombreClase(@NotNull String nombreClaseCualificado) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(nombreClaseCualificado);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz.getSimpleName();
    }
    // T04: Crear un método llamado finalizar que reciba un código de error y lo use como
    //  código de salida del proceso para el sistema operativo.
    //  Ejemplo del cuerpo del método finalizar: System.exit(e.getNumero())
    public static void finalizar(int e) {
        System.exit(e);
    }
}
