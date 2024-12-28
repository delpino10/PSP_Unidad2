package util;

public enum EchoError {
    ERROR_PUERTO_INVALIDO(1),       // Puerto fuera de rango [1,65535]
    ERROR_HOST_INVALIDO(2),        // Nombre o dirección IP de máquina (host) no válido
    ERROR_PUERTO_OCUPADO(3),
    ERROR_PUERTO_TIMEOUT(4),
    ERROR_PUERTO_NOTVALID(5),
    ERROR_PUERTO_IO(6),
    ERROR_HOST_DESCONOCIDO(7);

    private int numero;                    // Código numérico de error para ser usado como salida del programa

    EchoError(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }
}
