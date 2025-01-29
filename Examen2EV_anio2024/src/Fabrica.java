import java.util.ArrayList;

public class Fabrica {

    public static ArrayList<Griego> crearGriegos(Oraculo oraculo){
        ArrayList<Griego> griegos = new ArrayList<>();
        griegos.add(new Griego("ATENEA",oraculo));
        griegos.add(new Griego("HOMERO",oraculo));
        griegos.add(new Griego("PLATON",oraculo));
        return griegos;
    }
}
