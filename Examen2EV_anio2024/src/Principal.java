import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Principal {

    public static void main(String[] args) throws IOException {
        Oraculo oraculo = new Oraculo();
        Sacerdotisa sacerdotisa = new Sacerdotisa(oraculo);
        List<Griego> griegos = Fabrica.crearGriegos(oraculo);

        ExecutorService ex = Executors.newFixedThreadPool(4);
        ex.execute(sacerdotisa);
        for (Griego griego:griegos)
            ex.execute(griego);
        ex.shutdown();
        try {
            // Si el tiempo de espera de 20 segundos se supera salta la excepción
            // y finaliza todos los Hilos para que no se quede ninguno sin cerrar
            // en caso contrario ejecuta el now para que siga el programa main.
            if(!ex.awaitTermination(20, TimeUnit.SECONDS))
                ex.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
            ex.shutdownNow();
        }
        System.out.println("finalizacion del programa");
    }
}
