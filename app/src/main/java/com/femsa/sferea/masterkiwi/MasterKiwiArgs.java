package com.femsa.sferea.masterkiwi;

/**
 * <p>Clase que permite empaquetar la información que se entrega a los listeners que estan registrados</p>
 */
public class MasterKiwiArgs
{
    /**
     * <p>Cantidad de assets a descargar.</p>
     */
    public int assetsToDownload;
    /**
     * <p>Cantidad de assets que falta por descargar.</p>
     */
    public int remainigAssets;
    /**
     * <p>Los datos del usuario que hizo login.</p>
     */
    public String userData;
    /**
     * <p>Mensaje de error que regresa la comunicación con la red.</p>
     */
    public String netErrorMessage;
    /**
     * <p>Mensaje de error que regresa el portal en caso de fallar.</p>
     */
    public String masterKiwiNetErrorMssage;
    /**
     * <p>Constructor base.</p>
     */
    MasterKiwiArgs()
        {
        }
    /**
     * <p>Constructor que sólo guarda los datos del usuario.</p>
     *
     * @param uData Datos del usuario.
     */
    MasterKiwiArgs(String uData)
        {
        userData = uData;
        }
    /**
     * <p>Constructor para guardar los errores con la red que sucedan.</p>
     *
     * @param netErr Errores de la red.
     * @param mkErr Errores de MasterKiwi.
     */
    MasterKiwiArgs(String netErr, String mkErr)
        {
        netErrorMessage = netErr;
        masterKiwiNetErrorMssage = mkErr;
        }
    /**
     * <p>Constructor que guarda los archivos que se descargaran.</p>
     *
     * @param remainig Archivos que faltan por descargar.
     * @param totalCount Archivos que se deben de descargar.
     */
    MasterKiwiArgs(int remainig, int totalCount)
        {
        remainigAssets = remainig;
        if(totalCount > 0)
            {
            assetsToDownload = totalCount;
            }
        }
    }
