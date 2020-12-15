package com.femsa.requestmanager.Response;
import java.io.Serializable;

/**
 * <p>Clase que representa la estructuro del objeto error proveniente de la respuesta del servidor.</p>
 */
public class ErrorDTO implements Serializable {


    private int mCodigoError;

    public ErrorDTO(int errorCode)
        {
            mCodigoError = errorCode;
        }

    /**
     * <p>Método que permite obtener el código de error de la petición.</p>
     * @return Código de error.
     */
    public int getCodigoError() {
        return mCodigoError;
    }
}
