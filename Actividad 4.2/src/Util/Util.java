package Util;

import org.jetbrains.annotations.NotNull;

public class Util {

    private Util() {}

    public static void error(Error error){
        switch (error){
            case ERROR_PUERTO_INVALIDO:
                System.err.printf("el puerto debe ser un número en el rango [1, 65535] (puerto = %d)", dato[0])
                break;
            default:
                System.err.println("código de error desconocido: " + error);
        }
    }

    public static void uso(@NotNull String mensaje){
        System.err.printf("Uso: java %s %s", nombreClasePrincipal(), mensaje );
    }

    public static String nombreClasePrincipal(){
        // Obtenemos la pila de llamada (stack Trace)
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // El último elemento es el método main de la clase principal
        StackTraceElement penultimoElemento = stackTrace[stackTrace.length - 1];
        return extraerNombreClase(penultimoElemento.getClassName());
    }

    public static String extraerNombreClase(@NotNull String nombreClaseCualificado) {
        Class<?> clazz = null;
        try{
            clazz = Class.forName(nombreClaseCualificado);
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        return clazz.getSimpleName();
    }
}
