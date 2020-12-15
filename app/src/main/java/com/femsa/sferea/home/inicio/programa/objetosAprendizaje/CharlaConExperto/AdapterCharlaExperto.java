package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.CharlaConExperto;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VedeoCharlaConExperto.IntervaloHorasSensuales;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class AdapterCharlaExperto extends RecyclerView.Adapter<AdapterCharlaExperto.HorarioViewHolder> {

    private OnHorarioSrensualSelected mListenner;

    private int lastSelectedPosition = -1;

    private Context mContext;

    private ArrayList<IntervaloHorasSensuales> mHoras;

    public interface OnHorarioSrensualSelected{

        void onSeleccionarIntervalo(IntervaloHorasSensuales data);

    }

    public AdapterCharlaExperto(ArrayList<IntervaloHorasSensuales> horas, Context context) {
        mHoras = new ArrayList<>();
        mHoras.addAll(horas);
        mContext = context;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_horarios_charla_experto,viewGroup,false);

        return new AdapterCharlaExperto.HorarioViewHolder(view);
    }

    public void updateHoras(ArrayList<IntervaloHorasSensuales> nuevasHoras){
        mHoras.clear();
        mHoras.addAll(nuevasHoras);
        notifyDataSetChanged();
        lastSelectedPosition = -1;
    }


    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder horarioViewHolder, int i) {
        horarioViewHolder.mRadio.setChecked(lastSelectedPosition == i);
        horarioViewHolder.mFechaInicio.setText(mHoras.get(i).getHoraInicio());
        horarioViewHolder.mFechaFin.setText(mHoras.get(i).getHoraFinal());
    }



    @Override
    public int getItemCount() {
        return mHoras != null ? mHoras.size() : 0;
    }

    public void setmListener(OnHorarioSrensualSelected Listenner) {
        mListenner = Listenner;
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView mFechaInicio;
        TextView mFechaFin;
        RadioButton mRadio;
        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            mFechaInicio = itemView.findViewById(R.id.fechaInicio);
            mFechaFin = itemView.findViewById(R.id.fecha_Fin);
            mRadio = itemView.findViewById(R.id.seleccionar);
            mRadio.setOnClickListener(v->{
                lastSelectedPosition = getAdapterPosition();
                mListenner.onSeleccionarIntervalo(mHoras.get(getAdapterPosition()));
                notifyDataSetChanged();
            });
        }
    }

}
