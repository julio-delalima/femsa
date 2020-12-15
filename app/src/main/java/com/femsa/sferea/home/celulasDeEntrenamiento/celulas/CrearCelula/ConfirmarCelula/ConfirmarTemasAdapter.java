package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.Request.CelulasDeEntrenamiento.DTOCelula.TemaEspecificoDTO;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class ConfirmarTemasAdapter extends RecyclerView.Adapter<ConfirmarTemasAdapter.ConfirmarTemaEspecificoViewHolder> {

    private ArrayList<TemaEspecificoDTO> mListadoTemas;
    private Context mContext;

    public ConfirmarTemasAdapter(ArrayList<TemaEspecificoDTO> listadoTemas, Context context) {
        mListadoTemas = new ArrayList<>();
        mListadoTemas.addAll(listadoTemas);
        mContext = context;
    }

    @NonNull
    @Override
    public ConfirmarTemaEspecificoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_confirmar_celula_temas_adapter, viewGroup, false);
        return  new ConfirmarTemaEspecificoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmarTemaEspecificoViewHolder confirmarTemaEspecificoViewHolder, int i) {
        confirmarTemaEspecificoViewHolder.facilitador_nombre.setText(mListadoTemas.get(i).getFacilitador().getNombreFacilitador());
        confirmarTemaEspecificoViewHolder.facilitador_puesto.setText(mListadoTemas.get(i).getFacilitador().getDescPosicion());
        confirmarTemaEspecificoViewHolder.mHoras.setText(mContext.getString(R.string.hrs, mListadoTemas.get(i).getTiempoSugerido()));
        confirmarTemaEspecificoViewHolder.mSubProceso.setText(mListadoTemas.get(i).getAreaProceso());
        confirmarTemaEspecificoViewHolder.mSubtema.setText(mListadoTemas.get(i).getSubtemaEspecifico());
        confirmarTemaEspecificoViewHolder.mTituloTemaEspecifico.setText(mListadoTemas.get(i).getTituloTemaEspecifico());
    }

    @Override
    public int getItemCount() {
        return mListadoTemas != null ? mListadoTemas.size() : 0;
    }

    public class ConfirmarTemaEspecificoViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTituloTemaEspecifico, facilitador_nombre, facilitador_puesto, mHoras, mSubtema, mSubProceso;

        public ConfirmarTemaEspecificoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTituloTemaEspecifico = itemView.findViewById(R.id.tema_especifico_cv);
            mSubProceso = itemView.findViewById(R.id.subproceso_tema_et);
            mSubtema = itemView.findViewById(R.id.sub_temas_especificos);
            mHoras = itemView.findViewById(R.id.item_tema_especifico_horas);
            facilitador_nombre = itemView.findViewById(R.id.facilitador_tema_nombre);
            facilitador_puesto = itemView.findViewById(R.id.facilitador_tema_puesto);
        }
    }
}
