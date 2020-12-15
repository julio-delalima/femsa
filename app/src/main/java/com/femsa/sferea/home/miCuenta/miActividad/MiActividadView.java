package com.femsa.sferea.home.miCuenta.miActividad;

import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.ObtenerAllLogrosData;
import com.femsa.requestmanager.Response.MiActividad.MiActividadResponseData;
import com.femsa.requestmanager.Response.Ranking.ProgramRankingResponseData;
import com.femsa.requestmanager.Response.Ranking.RankingResponseData;

public interface MiActividadView {
    void onViewMostrarLogros(ObtenerAllLogrosData data); //WS: obtenerAllLogros
    void onViewMostrarDatos(RankingResponseData data);  //WS: obtenerDatosRanking
    void onViewMostrarMedidoresProgresoYProgramas(ProgramRankingResponseData data); //WS: obtenerProgramasRankingMiActividad
    void onViewMostrarLoader();
    void onViewOcultarLoader();
    void onViewMostrarMensaje(int messageType);
    void onViewMiActividadData(MiActividadResponseData data);
    /**Cuando el token del usuario expire se mandará a llamar este método*/
    void onViewNoAuth();
    void onViewNoInternet();
}
