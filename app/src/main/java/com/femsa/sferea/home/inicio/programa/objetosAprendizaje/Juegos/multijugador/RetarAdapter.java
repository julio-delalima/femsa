package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RetarAdapter extends RecyclerView.Adapter<RetarAdapter.RetarViewHolder> {

    private Context mContext;

    private OnClickRetador mListener;

    private ArrayList<ParticipanteDTO> mListadoRetadores, mListadoFiltrado, mLitadoCompleto;

    public interface OnClickRetador{
        /**
         * <p>Método que se ejecuta cuando hago click sobre un retador</p>
         * @param id id del retador que elegí.
         * @param nombre nombre del jugador que estoy retando.
         * */
        void onClickRetador(int id, String nombre);
    }

    public void setData(RetadorResponseData data){
        mListadoRetadores.addAll(data.getRetador().getListadoJugadores());
        mLitadoCompleto.addAll(mListadoRetadores);
        notifyDataSetChanged();
    }

    public void resetListado(){
        mListadoRetadores.clear();
        mListadoRetadores.addAll(mLitadoCompleto);
        notifyDataSetChanged();
    }

    public void filtraListado(String filtro){
        mListadoFiltrado.clear();
        for(ParticipanteDTO dto: mLitadoCompleto){
            if(dto.getNombreParticipante().toLowerCase().contains(filtro.toLowerCase())){
                mListadoFiltrado.add(dto);
            }
        }
        mListadoRetadores.clear();
        mListadoRetadores.addAll(mListadoFiltrado);
        notifyDataSetChanged();
    }

    public RetarAdapter(Context context, OnClickRetador listener) {
        mContext = context;
        mListener = listener;
        mListadoRetadores = new ArrayList<>();
        mListadoFiltrado = new ArrayList<>();
        mLitadoCompleto = new ArrayList<>();
    }

    @NonNull
    @Override
    public RetarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_retar_participante, viewGroup, false);
        return new RetarAdapter.RetarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetarViewHolder retarViewHolder, int i) {
        retarViewHolder.mNombre.setText(mListadoRetadores.get(i).getNombreParticipante());
        retarViewHolder.mPuesto.setText(mListadoRetadores.get(i).getAreaFuncionalParticipante());

        GlideUrl glideUrl = new GlideUrl(RequestManager.IMAGE_BASE_URL + "/" + mListadoRetadores.get(i).getFotoPerfil(),
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + mListadoRetadores.get(i).getNombrePais()).error(mContext.getDrawable(R.drawable.sin_imagen)).into(retarViewHolder.mBandera);
        GlideApp.with(mContext).load(glideUrl).error(mContext.getDrawable(R.drawable.sin_imagen)).into(retarViewHolder.mFoto);
    }

    @Override
    public int getItemCount() {
        return mListadoRetadores != null ? mListadoRetadores.size(): 0;
    }

    class RetarViewHolder extends RecyclerView.ViewHolder{
        TextView mNombre, mPuesto;
        CircleImageView mFoto;
        ImageView mBandera;
        RetarViewHolder(@NonNull View itemView) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.retar_participante_nombre_usuario);
            mPuesto = itemView.findViewById(R.id.retar_participante_puesto);
            mFoto = itemView.findViewById(R.id.retar_participante_foto_perfil);
            mBandera = itemView.findViewById(R.id.retar_participante_bandera);
            itemView.setOnClickListener(v ->
                    mListener.onClickRetador(
                            mListadoRetadores.get(getAdapterPosition()).getIdParticipante(),
                            mListadoRetadores.get(getAdapterPosition()).getNombreParticipante()
            ));
        }
    }
}
