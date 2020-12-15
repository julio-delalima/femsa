package com.femsa.sferea.home.miCuenta.miActividad.programas;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProgramasAdapter extends RecyclerView.Adapter<ProgramasAdapter.ProgramasViewHolder>{

    private Context mContext;
    private ArrayList<ProgramRankingData> mData;

    public ProgramasAdapter(Context context, ArrayList<ProgramRankingData> data){
        mContext = context;
        mData = data;
    }

    /**
     * <p>Método que elimina los elementos de una lista.</p>
     */
    public void clear(){
        if (mData!=null){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public ProgramasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_programas, viewGroup, false);
        return new ProgramasViewHolder(view);
    }

    /**
     * <p>Método utilizado para asignar los datos de un programa a un holder.</p>
     * @param programasViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull ProgramasViewHolder programasViewHolder, int i) {
        int bonoActual = mData.get(i).getBonus();
        int bonoTotal = mData.get(i).getTotalBonus();
        int porcentajeBono = Math.round((float)(bonoActual * 100) / bonoTotal);
        programasViewHolder.nombre.setText(mData.get(i).getProgramName());
        programasViewHolder.progreso.setMax(100);
        programasViewHolder.progreso.setProgress(porcentajeBono); //progressbar
        programasViewHolder.porcentaje.setText(porcentajeBono + "%");
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/"+ mData.get(i).getImgProgram();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl).error(R.drawable.sin_imagen).apply(new RequestOptions().override(258,360)).into(programasViewHolder.imagen);
        programasViewHolder.puntosGanados.setText(bonoActual + "/" + bonoTotal);
    }

    /**
     * <p>Método que devuelve la cantidad de items que se encuentran en una lista.</p>
     * @return El número de elementos que hay en una lista.
     */
    @Override
    public int getItemCount() {
        //return mList!=null? mList.size(): 0;
        return (mData != null) ? mData.size() : 0;
    }

    static class ProgramasViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imagen;
        TextView nombre;
        TextView puntosGanados;
        TextView porcentaje;
        ProgressBar progreso;

        public ProgramasViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.iv_programas_layout_imagen);
            nombre = itemView.findViewById(R.id.tv_programas_layout_nombre);
            puntosGanados = itemView.findViewById(R.id.tv_programas_layout_puntos_ganados);
            porcentaje = itemView.findViewById(R.id.tv_programas_layout_porcentaje);
            progreso = itemView.findViewById(R.id.pb_programas_item_progreso);
        }
    }
}
