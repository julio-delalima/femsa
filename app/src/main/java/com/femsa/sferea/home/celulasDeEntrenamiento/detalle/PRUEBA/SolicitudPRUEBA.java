package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA;

/**
 * <p>Clase de prueba para generar datos dummy para el listado de Células de Entrenamiento.</p>
 */
public class SolicitudPRUEBA {

     // 0 -> Célula de Entrenamiento
     // 1 -> Inducción
    private int tipoSolicitud;

    // 0 -> Por Autorizar
    // 1 -> Por Programar
    // 2 -> Programado
    private int estatusSolicitud; //Por Autorizar-Por Programar-Programado

    // 0 -> Solicitante
    // 1 -> Participante
    // 2 -> Autorizador
    // 3 -> Facilitador
    private int tipoPersona;

    private String nombreSolicitud;
    private String nombrePersona;

    public SolicitudPRUEBA(int tipoSolicitud, int estatusSolicitud, int tipoPersona,
                           String nombreSolicitud, String nombrePersona) {
        this.tipoSolicitud = tipoSolicitud;
        this.estatusSolicitud = estatusSolicitud;
        this.tipoPersona = tipoPersona;
        this.nombreSolicitud = nombreSolicitud;
        this.nombrePersona = nombrePersona;
    }

    public int getTipoSolicitud() {
        return tipoSolicitud;
    }

    public int getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public int getTipoPersona() {
        return tipoPersona;
    }

    public String getNombreSolicitud() {
        return nombreSolicitud;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }
}
