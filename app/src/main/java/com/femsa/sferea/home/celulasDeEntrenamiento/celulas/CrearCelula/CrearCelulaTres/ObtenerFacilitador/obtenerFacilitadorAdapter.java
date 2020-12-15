package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class obtenerFacilitadorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<Object> mList;

    public obtenerFacilitadorAdapter(Context context, ArrayList<Object> list){
        mContext = context;
        mList = list;
    }

    /**
     * <p>Método que elimina los elementos de la lista-</p>
     */
    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder= null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mList!=null? mList.size(): 0;
    }

    private class ObtenerAreaFacilitadorViewHolder {
    }
}
