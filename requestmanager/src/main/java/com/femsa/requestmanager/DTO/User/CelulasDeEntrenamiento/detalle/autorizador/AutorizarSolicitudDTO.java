package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.autorizador;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Clase que permite encapsular los elementos que se enviarán en la petición para autorizar una solicitud.</p>
 */
public class AutorizarSolicitudDTO implements Serializable {

    private int mIdSolicitud;
    private int mIdEmpleado;
    private ArrayList<ParticipanteDTO> mListParticipantes;

    public AutorizarSolicitudDTO(int idSolicitud, int idEmpleado, ArrayList<ParticipanteDTO> listParticipantes){
        mIdSolicitud = idSolicitud;
        mIdEmpleado = idEmpleado;
        mListParticipantes = listParticipantes;
    }

    public AutorizarSolicitudDTO(AutorizarSolicitudDTO autorizadorSolicitud){
        mIdSolicitud = autorizadorSolicitud.mIdSolicitud;
        mIdEmpleado = autorizadorSolicitud.mIdEmpleado;
        mListParticipantes = autorizadorSolicitud.mListParticipantes;
    }

    public int getIdSolicitud() {
        return mIdSolicitud;
    }

    public int getIdEmpleado() {
        return mIdEmpleado;
    }

    public ArrayList<ParticipanteDTO> getListParticipantes() {
        return mListParticipantes;
    }
}
