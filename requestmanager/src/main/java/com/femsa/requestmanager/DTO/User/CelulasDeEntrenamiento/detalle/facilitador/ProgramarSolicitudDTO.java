package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Clase que permite encapsular los elementos que se enviarán en la petición para programar una solicitud.</p>
 */
public class ProgramarSolicitudDTO implements Serializable {

    private int mIdFacilitador;
    private int mIdSolicitud;
    private ArrayList<FechaDTO> mListFechas;

    public ProgramarSolicitudDTO(int idFacilitador, int idSolicitud, ArrayList<FechaDTO> listFechas){
        mIdFacilitador = idFacilitador;
        mIdSolicitud = idSolicitud;
        mListFechas = listFechas;
    }

    public int getIdFacilitador() {
        return mIdFacilitador;
    }

    public int getIdSolicitud() {
        return mIdSolicitud;
    }

    public ArrayList<FechaDTO> getListFechas() {
        return mListFechas;
    }
}
