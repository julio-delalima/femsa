package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres.ObtenerFacilitador;

import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class FacilitadoresAdapter  extends RecyclerView.Adapter<FacilitadoresAdapter.FacilitadorAdapterViewHolder> {

    private ArrayList<FacilitadorDTO> mData;

    private OnFacilitadorSeleccionado mListener;

    private int lastSelectedPosition = -1;

    public interface OnFacilitadorSeleccionado
    {
        void FacilitadorEscogido(FacilitadorDTO facilitador);
    }

    public void setListener(OnFacilitadorSeleccionado listener)
    {
        mListener = listener;
    }

    public FacilitadoresAdapter(ArrayList<FacilitadorDTO> data){
        mData = data;
    }

    @NonNull
    @Override
    public FacilitadorAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_facilitador, viewGroup, false);
        return new FacilitadorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitadorAdapterViewHolder facilitadorViewHolder, int i) {
        facilitadorViewHolder.nombre.setText(mData.get(i).getNombreFacilitador());
        facilitadorViewHolder.descripcion.setText(AppTalentoRHApplication.getApplication().getResources().getString(R.string.areafuncional, mData.get(i).getAreaFuncional()));
        facilitadorViewHolder.puesto.setText(mData.get(i).getDescPosicion());
        facilitadorViewHolder.facilitadorSeleccionado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            facilitadorViewHolder.facilitadorSeleccionado.
                setButtonTintList(ColorStateList.valueOf(AppTalentoRHApplication.getApplication().getResources().
                    getColor(
                        (facilitadorViewHolder.facilitadorSeleccionado.isChecked()) ? R.color.femsa_red_b : R.color.femsa_gray_b)
                    )
                );
        });
         facilitadorViewHolder.facilitadorSeleccionado.setChecked(lastSelectedPosition == i);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class FacilitadorAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView puesto, descripcion, nombre;

        RadioButton facilitadorSeleccionado;
        public FacilitadorAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            puesto = itemView.findViewById(R.id.item_facilitador_puesto);
            descripcion = itemView.findViewById(R.id.item_facilitador_area_funcional);
            nombre = itemView.findViewById(R.id.item_facilitador_nombre_tv);
            facilitadorSeleccionado = itemView.findViewById(R.id.item_facilitador_seleccionado);
            facilitadorSeleccionado.setOnClickListener(
                    v-> {
                        lastSelectedPosition = getAdapterPosition();
                        mListener.FacilitadorEscogido(new FacilitadorDTO(
                                mData.get(lastSelectedPosition).getIdFacilitador(),
                                mData.get(lastSelectedPosition).getNombreFacilitador(),
                                mData.get(lastSelectedPosition).getAreaFuncional(),
                                mData.get(lastSelectedPosition).getDescPosicion(),
                                mData.get(lastSelectedPosition).getCorreo()));
                        notifyDataSetChanged();
            });
        }
    }

}
