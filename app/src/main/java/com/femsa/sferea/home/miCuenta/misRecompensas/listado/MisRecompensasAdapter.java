package com.femsa.sferea.home.miCuenta.misRecompensas.listado;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.MisRecompensas.listado.RecompensaDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MisRecompensasAdapter extends RecyclerView.Adapter<MisRecompensasAdapter.MisRecompensasViewHolder>{

    private ArrayList<RecompensaDTO> mList;
    private OnClickRecompensaListener mListener;

    public MisRecompensasAdapter(ArrayList<RecompensaDTO> list, OnClickRecompensaListener listener){
        mList = list;
        mListener = listener;
    }

    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public interface OnClickRecompensaListener{
        void onClickRecompensa(RecompensaDTO recompensa);
    }

    /**
     * <p>Método que permite agregar un listener al adapter para poder visualizar el detalle de una
     * recompensa.</p>
     * @param listener Listener que contiene el método para obtener el detalle de una recompensa.
     */
    public void setOnClickRecompensaListener(OnClickRecompensaListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public MisRecompensasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recompensa,viewGroup, false);
        return new MisRecompensasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisRecompensasViewHolder misRecompensasViewHolder, int i) {
        misRecompensasViewHolder.itemView.setOnClickListener(view -> mListener.onClickRecompensa(mList.get(i)));

        RecompensaDTO recompensa = mList.get(i);
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL+"/"+recompensa.getNombreImagenRecompensa();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        misRecompensasViewHolder.nombre.setText(recompensa.getTituloRecompensa());
        misRecompensasViewHolder.costo.setText(Integer.toString(recompensa.getValorRecompensa()));


        GlideApp.with(AppTalentoRHApplication.getApplication())
                .load(glideUrl)
                .error(R.drawable.sin_imagen)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(misRecompensasViewHolder.imagen);

        //Picasso.with(AppTalentoRHApplication.getApplication()).load(fullPath).error(R.mipmap.ic_circled_user).into(misRecompensasViewHolder.imagen);
    }

    @Override
    public int getItemCount() {
        return mList!=null? mList.size():0;
    }

    class MisRecompensasViewHolder extends RecyclerView.ViewHolder{

        TextView costo;
        TextView nombre;
        ImageView imagen;

        public MisRecompensasViewHolder(@NonNull View itemView) {
            super(itemView);
            costo = itemView.findViewById(R.id.tv_item_recompensa_puntos);
            nombre = itemView.findViewById(R.id.tv_item_recompensa_nombre);
            imagen = itemView.findViewById(R.id.iv_item_recompensa_imagen);
        }
    }
}
