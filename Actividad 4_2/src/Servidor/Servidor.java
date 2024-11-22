package Servidor;

import Util.Util;

public class Servidor {

    public static void Uso(){
        Util.uso("hola");
    }

    int puerto = 8000;
    public int iniciainicializacion (int puerto){
        if (puerto < 1 || puerto > 65535){
            //Util.Error
        }
        return puerto;
    }
}
