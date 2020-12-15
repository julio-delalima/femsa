package com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.SolicitudDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Insomnia: detalleSolicitud{}
 */
public class DetalleSolicitudDTO extends SolicitudDTO implements Serializable {

    private static final String ID_PAIS_KEY = "idPais";
    private static final String CORREO_KEY = "correo";
    private static final String NOMBRE_KEY = "nombre";
    private static final String FOTO_PERFIL_KEY = "fotoPerfil";
    private static final String PUESTO_KEY = "puesto";
    private static final String AUTORIZACION_KEY = "autorizacion";
    private static final String PARTICIPANTES_LIST_KEY = "participantesList";
    private static final String JEFE_LIST_KEY = "jefeList";
    private static final String INFORMACION_LIST_KEY = "informacionList";
    private static final String FECHA_INICIO_KEY = "fechaInicio";
    private static final String FECHA_FIN_KEY = "fechaFin";
    private static final String NUMERO_RED_KEY = "numeroRed";
    private static final String PAIS_SOLICITANTE_KEY = "paisSolicitante";
    private static final String PAIS_RECEPTOR_KEY = "paisReceptor";
    private static final String DESC_POSICION_KEY = "descPosicion";
    private static final String FECHA_SOLICITUD_KEY = "fechaSolicitud";
    private static final String ADMIN_SOLICITANTE_KEY = "adminPaisSolicitante";
    private static final String ADMIN_RECEPTOR_KEY = "adminPaisReceptor";

    private int mIdPais;
    private String mCorreo;
    private ArrayList<ParticipanteDTO> mListaParticipantes;
    private ArrayList<JefeDTO> mListaAutorizacion;
    private ArrayList<InformacionDeInduccionDTO> mListaTemas;
    private String mFechaInicio;
    private String mFechaFin;
    private int mNumeroRed;
    private int mPaisSolicitante;
    private int mPaisReceptor;
    private String mDescPosicion;
    private String mFechaSolicitud;

    public DetalleSolicitudDTO(JSONObject data) {
        super(data);
        mListaParticipantes = new ArrayList<>();
        mListaTemas = new ArrayList<>();
        mListaAutorizacion = new ArrayList<>();
        mIdPais = data.optInt(ID_PAIS_KEY);
        mCorreo = data.optString(CORREO_KEY);
        crearListaParticipantes(data.optJSONArray(PARTICIPANTES_LIST_KEY));
        crearListaAutorizacion(data.optJSONArray(JEFE_LIST_KEY));
        agregarAdminPaisReceptor(data.optJSONObject(ADMIN_RECEPTOR_KEY));
        agregarAdminPaisSolicitante(data.optJSONObject(ADMIN_SOLICITANTE_KEY));
        crearListaTemas(data.optJSONArray(INFORMACION_LIST_KEY));
        mFechaInicio = data.optString(FECHA_INICIO_KEY);
        mFechaFin = data.optString(FECHA_FIN_KEY);
        mNumeroRed = data.optInt(NUMERO_RED_KEY);
        mPaisSolicitante = data.optInt(PAIS_SOLICITANTE_KEY);
        mPaisReceptor = data.optInt(PAIS_RECEPTOR_KEY);
        mDescPosicion = data.optString(DESC_POSICION_KEY);
        mFechaSolicitud = data.optString(FECHA_SOLICITUD_KEY);
    }

    private void agregarAdminPaisReceptor(JSONObject admin)
        {
            if(admin != null)
                {
                    mListaAutorizacion.add(new JefeDTO(
                            admin.optString(NOMBRE_KEY),
                            admin.optString(FOTO_PERFIL_KEY),
                            admin.optString(PUESTO_KEY),
                            admin.optBoolean(AUTORIZACION_KEY),
                            admin.optInt(ID_PAIS_KEY),
                            JefeDTO.PAIS_RECEPTOR
                    ));
                }
        }

    private void agregarAdminPaisSolicitante(JSONObject admin)
        {
            if(admin != null)
            {
                mListaAutorizacion.add(new JefeDTO(
                        admin.optString(NOMBRE_KEY),
                        admin.optString(FOTO_PERFIL_KEY),
                        admin.optString(PUESTO_KEY),
                        admin.optBoolean(AUTORIZACION_KEY),
                        admin.optInt(ID_PAIS_KEY),
                        JefeDTO.PAIS_SOLICITANTE
                ));
            }
        }

    /**
     * <p>Método que crea la lista de Participantes que hay en la solicitud.</p>
     * @param array
     */
    private void crearListaParticipantes(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListaParticipantes.add(new ParticipanteDTO(array.optJSONObject(i)));
            }
        }
    }

    /**
     * <p>Método que crea la lista de autorizaciones que hay en la solicitud.</p>
     * @param array Array obtenido de la petición.
     */
    private void crearListaAutorizacion(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListaAutorizacion.add(new JefeDTO(array.optJSONObject(i)));
            }
        }
    }

    /**
     * <p>Método que crea la lista de Temas que hay en la solicitud.</p>
     * @param array
     */
    private void crearListaTemas(JSONArray array){
        if (array!=null){
            for (int i=0; i<array.length(); i++){
                mListaTemas.add(new InformacionDeInduccionDTO(array.optJSONObject(i)));
            }
        }
    }

    public int getIdPais() {
        return mIdPais;
    }

    public String getCorreo() {
        return mCorreo;
    }

    public ArrayList<ParticipanteDTO> getListaParticipantes() {
        return mListaParticipantes;
    }

    public ArrayList<InformacionDeInduccionDTO> getListaTemas() {
        return mListaTemas;
    }

    public ArrayList<JefeDTO> getListaAutorizacion(){
        return mListaAutorizacion;
    }

    public String getFechaInicio() {
        return mFechaInicio;
    }

    public String getFechaFin() {
        return mFechaFin;
    }

    public int getNumeroRed(){
        return mNumeroRed;
    }

    public int getPaisSolicitante(){
        return mPaisSolicitante;
    }

    public int getPaisReceptor(){
        return mPaisReceptor;
    }

    public String getDescPosicion() {
        return mDescPosicion;
    }

    public String getFechaSolicitud() {
        return mFechaSolicitud;
    }
}
