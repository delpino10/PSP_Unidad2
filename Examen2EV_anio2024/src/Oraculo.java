import java.util.HashMap;

public class Oraculo {

    private String[] preguntas = {"¿Cuál es el destino que nos aguarda como sociedad y como podemos prepararnos " +
            "para enfrentarlo?","¿Cómo podemos encontrar el camino hacia la verdad y la comprensión más profunda " +
            "en un undo lleno de incertidumbres?","¿Cual es el significado más profundo de la vida individual y cómo podemos vivir " +
            "de acuerdo con nuestro destino personal"};
    private HashMap<String,String> pyr = new HashMap<>();
    public Oraculo(){
        pyr.put(preguntas[0], "El destino de la sociedad está entrelazado con sus acciones y decisiones colectivas. " +
                "Adoptar la virtud, la sabiduría y la colaboración contribuirá a un futuro próspero.");
        pyr.put(preguntas[1], "Buscad el conocimiento con humildad y mantened una mente abuerta. La verdad se revela " +
                "a aquellos que buscan con sinceridad y perseverancia.");
        pyr.put(preguntas[2], "Cada vida tiene un propósito único. Reflexionad sobre vuestras acciones, cultivad la " +
                "virtud y buscad el equilibrio para vivir en armonía con vuestro destino.");
    }

    public String getPreguntas(int i) {
        return preguntas[i];
    }
    public String getRespuesta(String pregunta) {
        return pyr.get(pregunta);
    }
}
