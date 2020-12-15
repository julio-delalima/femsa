package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ParticipanteDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantesAdapter extends RecyclerView.Adapter<ParticipantesAdapter.ParticipantesViewHolder> {

    private ArrayList<ParticipanteDTO> mList;
    private Context mContext;

    //Bandera para determinar si se debe mostrar el texto "Rechazar/Aceptar" y el SwitchButton.
    private boolean mostrarSwitch;

    //Bandera para determinar si se debe mostrar el ImageButton de cruz para eliminar participante.
    private boolean eliminarParticipante;

    private ArrayList<PaisesDTO.PaisData> mListPaises;

    private ParticipantesListener mListener; //Listener para eliminar un participante.

    public ParticipantesAdapter(Context context, boolean mostrarSwitch, boolean eliminarParticipante, ArrayList<PaisesDTO.PaisData> paises){
        mList = new ArrayList<>();
        mListPaises = new ArrayList<>();
        mContext = context;
        this.mostrarSwitch = mostrarSwitch;
        this.eliminarParticipante = eliminarParticipante;
        mListPaises.addAll(paises);
    }

    public void setPaisesListado(ArrayList<PaisesDTO.PaisData> listadoPaises)
        {
            mListPaises = listadoPaises;
            notifyDataSetChanged();
        }

    /**
     * <p>Método que permite agregar la lista de participantes al Adapter.</p>
     * @param list Lista de participantes que se va a agregar al Adapter.
     */
    public void addParticipantes(ArrayList<ParticipanteDTO> list){
        mList = list;
    }

    /**
     * <p>Método que elimina los elementos de una lista.</p>
     */
    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * <p>Método que determina si se debe mostrar las cruces para eliminar participantes. Esto con
     * base en el tipo de solicitud.</p>
     * @param tipoSolicitud String que contiene el tipo de la solicitud.
     */
    public void determinarTipoSolicitud(String tipoSolicitud){
        eliminarParticipante = tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION);
    }

    /**
     * <p>Método que asigna un listener al adapter para eliminar a un Participante del listado.</p>
     * @param listener Listener que se asigna desde un Fragment/Activity que implementa la interfaz que aquí se define.
     */
    public void setListener(ParticipantesListener listener){
        mListener = listener;
    }

    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public ParticipantesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_participantes, viewGroup, false);
        ParticipantesViewHolder viewHolder = new ParticipantesViewHolder(view);
        if (!mostrarSwitch){
            viewHolder.aceptarRechazarTexto.setVisibility(View.GONE);
            viewHolder.aceptarRechazar.setVisibility(View.GONE);
        }
        return viewHolder;
    }

    /**
     * <p>Método utilizado para asignar los datos de un participante a un holder.</p>
     * @param participantesViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull ParticipantesViewHolder participantesViewHolder, int i) {
        final ParticipanteDTO participante = mList.get(i);
        final int idJefe = SharedPreferencesUtil.getInstance().getIdEmpleado();

        //Ruta para obtener las imágenes de perfil.
        String fullPath = RequestManager.IMAGE_BASE_URL+"/"+ participante.getFotoPerfil();

        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(mContext)
                .load(glideUrl)
                .error(R.mipmap.ic_circled_user)
                .into(participantesViewHolder.fotoPerfil);

        participantesViewHolder.nombre.setText(participante.getNombreParticipante());

        String numeroEmpleado = String.format(mContext.getString(R.string.activity_detalle_celula_entrenamiento_numero_empleado_2), String.valueOf(participante.getNumeroRed()));
        participantesViewHolder.numeroEmpleado.setText(numeroEmpleado);

        participantesViewHolder.posicionSolicitante.setText(participante.getDescripcionPosicion());

        String area = String.format(mContext.getString(R.string.activity_detalle_celula_entrenamiento_area_funcional), participante.getArea());

        participantesViewHolder.areaFuncional.setText(area);

        participantesViewHolder.subareaProceso.setText(participante.getSubarea());

        if (mostrarSwitch){
            //Verificando que el id del jefe del participante sea igual al id del usuario que usa la aplicación para mostrar el switch.
            if (participante.getIdEmpleadoSuperior()==idJefe){
                participantesViewHolder.aceptarRechazar.setVisibility(View.VISIBLE);
                participantesViewHolder.aceptarRechazarTexto.setVisibility(View.VISIBLE);
                //Asignando un listener al SwitchButton para saber cuando se enciende o apaga.
                participantesViewHolder.aceptarRechazar.setOnCheckedChangeListener((view, isChecked) -> {
                    if (isChecked){
                        mListener.autorizarParticipante(participante);
                    } else {
                        mListener.noAutorizarParticipante(participante);
                    }
                });
            }
        }

        if (participante.isAutorizacion()){
            participantesViewHolder.aceptarRechazarTexto.setVisibility(View.GONE);
            participantesViewHolder.aceptarRechazar.setVisibility(View.GONE);
        }

        if(eliminarParticipante){
            participantesViewHolder.eliminarParticipante.setVisibility(View.VISIBLE);
            participantesViewHolder.eliminarParticipante.setOnClickListener(view -> mListener.eliminarParticipante(participante, i));
        }

        for(PaisesDTO.PaisData paisActual : mListPaises)
        {
            if(paisActual.getIdPais() == participante.getIdPais())
            {
                GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(participantesViewHolder.banderaParticipante);
                break;
            }
        }
    }

    /**
     * <p>Método que devuelve la cantidad de items que se encuentran en una lista.</p>
     * @return El número de elemento que hay en la lista.
     */
    @Override
    public int getItemCount() {
        return mList!=null? mList.size(): 0;
    }

    class ParticipantesViewHolder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView nombre;
        TextView numeroEmpleado;
        TextView posicionSolicitante;
        TextView areaFuncional;
        TextView subareaProceso;
        TextView aceptarRechazarTexto;
        SwitchButton aceptarRechazar;
        ImageButton eliminarParticipante;
        ImageView banderaParticipante;

        public ParticipantesViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoPerfil = itemView.findViewById(R.id.civ_participantes_layout_foto_perfil);
            nombre = itemView.findViewById(R.id.tv_participantes_layout_nombre);
            numeroEmpleado = itemView.findViewById(R.id.tv_participantes_layout_numero_empleado);
            posicionSolicitante = itemView.findViewById(R.id.tv_participantes_layout_posicion_solicitante);
            areaFuncional = itemView.findViewById(R.id.tv_participantes_layout_area_funcional);
            subareaProceso = itemView.findViewById(R.id.tv_participantes_layout_subarea_proceso);
            aceptarRechazarTexto = itemView.findViewById(R.id.tv_rechazar_aceptar);
            aceptarRechazar = itemView.findViewById(R.id.switch_participantes_layout_aceptar_rechazar);
            eliminarParticipante = itemView.findViewById(R.id.ib_participantes_layout_close);
            banderaParticipante = itemView.findViewById(R.id.iv_participantes_layout_flag);
        }
    }

    /**
     * <p>Interface que contiene los métodos para eliminar y/o aceptar/rechazar a un participante.</p>
     */
    public interface ParticipantesListener {
        void noAutorizarParticipante(ParticipanteDTO participante);
        void autorizarParticipante(ParticipanteDTO participante);

        /**
         * <p>Método para eliminar a un participante al presionar la imagen de cruz.</p>
         * @param participante Información del participante que se va a eliminar.
         * @param posicion Posición del participante en la lista para eliminarlo.
         */
        void eliminarParticipante(ParticipanteDTO participante, int posicion);
    }

}
