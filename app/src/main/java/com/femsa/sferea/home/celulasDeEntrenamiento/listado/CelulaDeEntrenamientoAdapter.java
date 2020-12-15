package com.femsa.sferea.home.celulasDeEntrenamiento.listado;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.listado.SolicitudDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.SwipeRevealLayout;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CelulaDeEntrenamientoAdapter extends RecyclerView.Adapter<CelulaDeEntrenamientoAdapter.CelulaDeEntrenamientoViewHolder> {

    private ArrayList<SolicitudDTO> mList;

    private ArrayList<PaisesDTO.PaisData> mListadoPaises;


    private Context mContext;

    private OnItemClickListener mListener;

    private OnCelulaListadoClick mListenerCelula;

    public void setListener(OnCelulaListadoClick listener)
        {
            mListenerCelula = listener;
        }

    public CelulaDeEntrenamientoAdapter(Context context, ArrayList<SolicitudDTO> list, ArrayList<PaisesDTO.PaisData> listadoPaises){
        mContext = context;
        mList = list;
        mListadoPaises = listadoPaises;
    }

    public interface OnCelulaListadoClick
        {
            void onEliminarcelula(int idCelula);
        }

    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public CelulaDeEntrenamientoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_celula_entrenamiento, viewGroup, false);
        return new CelulaDeEntrenamientoAdapter.CelulaDeEntrenamientoViewHolder(view);
    }

    /**
     * <p>Método utilizado para asignar los datos de una celula de entrenamiento de un evento a un holder.</p>
     * @param celulaDeEntrenamientoViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull CelulaDeEntrenamientoViewHolder celulaDeEntrenamientoViewHolder, int i) {
        celulaDeEntrenamientoViewHolder.mCelulaContainer.setOnClickListener(v -> mListener.onClickDetalleCelulaEntrenamiento(mList.get(i)));

        SolicitudDTO solicitud = mList.get(i);

        //Ruta completa para obtener la imagen de perfil.
        String fullPath = RequestManager.IMAGE_BASE_URL+"/"+ mList.get(i).getFotoPerfil();

        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        //Asignando el texto del tipo de la solicitud
        celulaDeEntrenamientoViewHolder.tipoCelulaEntrenamiento.setText(
                solicitud.getTipoSolicitud().equals("2") ?
                        mContext.getResources().getString(R.string.Celulas_label_singular)
                        :
                            (solicitud.getTipoSolicitud().equals("1")
                                    ?
                                        mContext.getResources().getString(R.string.induccion_label)
                                    :
                                    solicitud.getTipoSolicitud()
                                )
                );
        /*switch (solicitud.getTipoSolicitud()){
            case CelulaEntrenamiento.INDUCCION:
                celulaDeEntrenamientoViewHolder.tipoCelulaEntrenamiento.setText(R.string.celula_entrenamiento_item_tipo_induccion);
                break;
            case CelulaEntrenamiento.CELULA:
                celulaDeEntrenamientoViewHolder.tipoCelulaEntrenamiento.setText(R.string.celula_entrenamiento_item_tipo_celula_entrenamiento);
                break;
        }*/

        //Asignando el background y texto al TextView dependiendo del estatus de la solicitud.
        if (solicitud.getStatusSolicitud().equals(CelulaEntrenamiento.POR_AUTORIZAR)){
            celulaDeEntrenamientoViewHolder.estado.setBackgroundResource(R.drawable.background_celula_entrenamiento_item_por_autorizar);
            celulaDeEntrenamientoViewHolder.estado.setText(R.string.celula_entrenamiento_item_estado_por_autorizar);
        }
        else if (solicitud.getStatusSolicitud().equals(CelulaEntrenamiento.POR_PROGRAMAR)){
            celulaDeEntrenamientoViewHolder.estado.setBackgroundResource(R.drawable.background_celula_entrenamiento_item_por_programar);
            celulaDeEntrenamientoViewHolder.estado.setText(R.string.celula_entrenamiento_item_estado_por_programar);
        }
        else{
            celulaDeEntrenamientoViewHolder.estado.setBackgroundResource(R.drawable.background_celula_entrenamiento_item_programado);
            celulaDeEntrenamientoViewHolder.estado.setText(R.string.celula_entrenamiento_item_estado_programado);
        }

        //Asignando el tipo de rol de la solicitud.
        switch (solicitud.getRolSolicitud()){
            case CelulaEntrenamiento.AUTORIZADOR:
                celulaDeEntrenamientoViewHolder.tipoPersona.setText(R.string.celula_entrenamiento_item_categoria_autorizador);
                break;
            case CelulaEntrenamiento.SOLICITANTE:
                celulaDeEntrenamientoViewHolder.tipoPersona.setText(R.string.celula_entrenamiento_item_categoria_solicitante);
                break;
            case CelulaEntrenamiento.FACILITADOR:
                celulaDeEntrenamientoViewHolder.tipoPersona.setText(R.string.celula_entrenamiento_item_categoria_facilitador);
                break;
            case CelulaEntrenamiento.PARTICIPANTE:
                celulaDeEntrenamientoViewHolder.tipoPersona.setText(R.string.celula_entrenamiento_item_categoria_participante);
                break;
        }

        //Asignando los datos restantes

        celulaDeEntrenamientoViewHolder.nombrePersona.setText(solicitud.getNombreSolicitante());
        celulaDeEntrenamientoViewHolder.tituloCelulaEntrenamiento.setText(solicitud.getTemaGeneral());
        for(PaisesDTO.PaisData paisActual : mListadoPaises)
            {
                if(paisActual.getIdPais() == solicitud.getIdioma())
                    {
                        GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(celulaDeEntrenamientoViewHolder.banderaPais);
                        break;
                    }
            }

        GlideApp.with(mContext).load(glideUrl).error(R.mipmap.ic_circled_user).into(celulaDeEntrenamientoViewHolder.imagenPersona);
        //celulaDeEntrenamientoViewHolder.mFullSwipe.close(true);
        //celulaDeEntrenamientoViewHolder.mBorrarContainer.setVisibility(View.GONE);
    }

    public void setPaisAdapter(ArrayList<PaisesDTO.PaisData> listadoPaises)
        {
            mListadoPaises.addAll(listadoPaises);
            notifyDataSetChanged();
        }

    /**
     * <p>Método que devuelve la cantidad de items que se encuentran en una lista.</p>
     * @return Número de elementos de la lista.
     */
    @Override
    public int getItemCount() {
        return mList!=null ? mList.size() : 0;
    }


    /**
     * <p>Método que define un listener, ya que una lista basada en un RecyclerView no contiene
     * un listener por defecto.</p>
     * @param listener Listener asignado.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
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

    public class CelulaDeEntrenamientoViewHolder extends RecyclerView.ViewHolder{

        TextView tipoCelulaEntrenamiento; //TextView donde se puede mostrar "Inducción" o "Célula de Entrenamiento".
        CircleImageView imagenPersona;
        TextView nombrePersona;
        TextView tituloCelulaEntrenamiento;
        ImageView banderaPais;
        TextView estado; //TextView donde se puede mostrar "Por Programar", "Programado" o "Por Autorizar".
        TextView tipoPersona; //TextView donde se puede mostrar "Autorizador", "Solicitante" o "Facilitador".
        //ConstraintLayout mCelulaContainer;
        CardView mCelulaContainer;
        SwipeRevealLayout mFullSwipe;

        public CelulaDeEntrenamientoViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoCelulaEntrenamiento = itemView.findViewById(R.id.tv_celula_entrenamiento_item_tipo);
            imagenPersona = itemView.findViewById(R.id.civ_celula_entrenamiento_item_imagen);
            nombrePersona = itemView.findViewById(R.id.tv_celula_entrenamiento_item_nombre_persona);
            tituloCelulaEntrenamiento = itemView.findViewById(R.id.tv_celula_entrenamiento_item_titulo);
            banderaPais = itemView.findViewById(R.id.iv_celula_entrenamiento_bandera);
            estado = itemView.findViewById(R.id.tv_celula_entrenamiento_estado);
            tipoPersona = itemView.findViewById(R.id.tv_celula_entrenamiento_tipo_persona);
            mCelulaContainer = itemView.findViewById(R.id.celula_container_listado);
        }
    }

    public interface OnItemClickListener{
        void onClickDetalleCelulaEntrenamiento(SolicitudDTO celula);
    }
}

