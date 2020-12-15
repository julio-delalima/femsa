package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.VideoInteractivo;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.VideoInteractivo.Respuesta;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class RespuestasInteractiveAdapter extends RecyclerView.Adapter<RespuestasInteractiveAdapter.RespuestasViewHolder> {

    private ArrayList<Respuesta> mData;
    private OnRespuestasListener mListenner;
    private int lastSelectedPosition = -1;



    public void setmListenner(OnRespuestasListener Listenner){
        mListenner = Listenner;
    }

    public RespuestasInteractiveAdapter(ArrayList<Respuesta> data, PreguntasFragment preguntasFragment){
        mData = data;
    }

    @NonNull
    @Override
    public RespuestasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pregunta, viewGroup, false);
        return new RespuestasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestasViewHolder respuestasViewHolder, int i) {
        respuestasViewHolder.respuesta.setText(mData.get(i).getPreguntasTexto());
        respuestasViewHolder.check.setChecked(lastSelectedPosition == i);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class RespuestasViewHolder extends RecyclerView.ViewHolder {
        TextView respuesta;
        RadioButton check;
        CardView mCard;
        public RespuestasViewHolder(@NonNull View itemView) {
            super(itemView);
            respuesta = itemView.findViewById(R.id.respuesta);
            check = itemView.findViewById(R.id.rb_respuesta);
            check.setChecked(false);
            mCard = itemView.findViewById(R.id.item_pregunta_card);
                mCard.setCardBackgroundColor(Color.WHITE);
            check.setOnClickListener(v -> {
                lastSelectedPosition = getAdapterPosition();
                mListenner.selecionarChecked(getAdapterPosition(), mData.get(lastSelectedPosition).isPreguntaCorrecta());
                notifyDataSetChanged();
            });

        }
    }
    public interface  OnRespuestasListener {
        void selecionarChecked(int position, boolean correcto);
    }
}
