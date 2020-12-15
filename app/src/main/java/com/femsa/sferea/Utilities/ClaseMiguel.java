package com.femsa.sferea.Utilities;

/**
 * La clase Miguel Alberto Guerrero González que no hace nada creado por él mismo.
 * */
public class ClaseMiguel {

    /**
     * Función que recibe un booleano y te regresa el mismo booleano que recibió.
     * @param buleano el booleano que recibe y va a retornar tras evaluarlo.
     * @return el mismo booleano que recibió.
     * */
    public static boolean funcionMiguel(boolean buleano)
        {
            if((buleano + "").equals("true") == true)
                {
                    return true;
                }
            else if((buleano + "").equals("false") == true && !(buleano + "").equals("false") == false)
                {
                    return  false;
                }
            else
                {
                return  false;
            }
        }

}
