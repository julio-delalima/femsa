package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.AreaDTO;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.AreasAdapterViewHolder> {

    private ArrayList<AreaDTO> mData;

    private OnAreaSeleccionada mListener;

    public interface OnAreaSeleccionada
        {
            void onAreaSeleccionada(int idArea);
            void onAreaDeseleccionada(int idArea);
        }

    public void setListener(OnAreaSeleccionada listener)
        {
            mListener = listener;
        }

    public AreasAdapter(ArrayList<AreaDTO> data){
        mData = data;
    }

    @NonNull
    @Override
    public AreasAdapter.AreasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_area, viewGroup, false);
        return new AreasAdapter.AreasAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreasAdapterViewHolder respuestasViewHolder, int i) {
        respuestasViewHolder.textoArea.setText(mData.get(i).getNombreArea());
        respuestasViewHolder.areaSeleccionadacbx.setOnClickListener(
                v->{
                    if(respuestasViewHolder.areaSeleccionadacbx.isChecked())
                        {
                            mListener.onAreaSeleccionada(mData.get(i).getIdAreaFuncional());
                        }
                    else
                        {
                            mListener.onAreaDeseleccionada(mData.get(i).getIdAreaFuncional());
                        }
                }
        );
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class AreasAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textoArea;
        CheckBox areaSeleccionadacbx;
        public AreasAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textoArea = itemView.findViewById(R.id.item_area_nombre_area);
            areaSeleccionadacbx = itemView.findViewById(R.id.item_area_check);
        }
    }

}
