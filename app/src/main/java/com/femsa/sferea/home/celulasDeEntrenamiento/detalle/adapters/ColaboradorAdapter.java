package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.ColaboradorDTO;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.ColaboradorViewHolder>{

    private Context mContext;
    private ArrayList<ColaboradorDTO> mListColaboradores;

    public ColaboradorAdapter(Context context, ArrayList<ColaboradorDTO> listColaboradores){
        mContext = context;
        mListColaboradores = listColaboradores;
    }

    /**
     * <p>Método que permite eliminar los elementos que existen en el ArrayList de colaboradores.</p>
     */
    public void clear(){
        if (mListColaboradores!=null){
            mListColaboradores.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * <p>Método que permite agregar una lista de colaboradores al adapter.</p>
     * @param listaColaboradores Lista de colaboradores que se va a agregar al adapter.
     */
    public void addListaColaboradores(ArrayList<ColaboradorDTO> listaColaboradores){
        mListColaboradores = listaColaboradores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColaboradorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_colaborador, viewGroup, false);
        return new ColaboradorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColaboradorViewHolder colaboradorViewHolder, int i) {
        ColaboradorDTO colaboradorDTO = mListColaboradores.get(i);

        colaboradorViewHolder.nombre.setText(colaboradorDTO.getNombreColaborador());
        colaboradorViewHolder.puesto.setText(colaboradorDTO.getPosicionColaborador());
    }

    @Override
    public int getItemCount() {
        return mListColaboradores!=null? mListColaboradores.size():0;
    }

    class ColaboradorViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView puesto;

        public ColaboradorViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_item_colaborador_nombre);
            puesto = itemView.findViewById(R.id.tv_item_colaborador_puesto);
        }
    }
}
