package com.femsa.sferea.home.inicio.programa.detallePrograma;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectResponseData;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;

public interface KOFSectionView
    {
        void getProgramDetailSuccess(ProgramDetailResponseData data);

        void likeSuccess();

        void showLoader();

        void hideLoader();

        void getObjectsSuccess(ObjectResponseData data);

        /**
         * Muetsra un SnackBar con el mensaje de error
         * */
        void onMostrarMensaje(int tipoMensaje);

        /**
         * Muetsra un SnackBar con el mensaje de error
         * */
        void onMostrarMensajeInesperado(int tipoMensaje, int codigoError);

        /**
         * Método a llamar cuando se realize una correcta inscrpción al programa.
         * */
        void onInscripcionExitosa();

        /**
         * Método que cierra la sesión del usuario tars ercibir un 401
         * */
        void onNoAuth();

        void onNoInternet();
    }
