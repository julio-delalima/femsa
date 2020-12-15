package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.CheckList.CheckListRespuesta;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class ChecklistRespuestasAdapter extends RecyclerView.Adapter<ChecklistRespuestasAdapter.CheckListRespuestasViewHolder> {

    private Context mContext;

    private ArrayList<CheckListRespuesta> mData;

    private OnCheckRespondido mListener;

    public void setListener(OnCheckRespondido listener)
        {
            mListener = listener;
        }

    //private boolean mContestado = false;

    public interface OnCheckRespondido
        {
            void onCheckElegido(String idCheck, boolean checked);
        }

    public ChecklistRespuestasAdapter(Context context, ArrayList<CheckListRespuesta> respuestas, boolean contestado) {
        mContext = context;
        mData = respuestas;
        //mContestado = contestado;
    }

    @NonNull
    @Override
    public CheckListRespuestasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_checklist_respuestas, viewGroup, false);
        return new CheckListRespuestasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListRespuestasViewHolder respuestasViewHolder, int i) {
        respuestasViewHolder.mPreguntaTexto.setText(mData.get(i).getRespuestaTextoCheck());
        respuestasViewHolder.mPreguntasCheck.setId(mData.get(i).getIdPreguntaCheck());
        respuestasViewHolder.mPreguntasCheck.setOnClickListener(v->
            mListener.onCheckElegido(String.valueOf(respuestasViewHolder.mPreguntasCheck.getId()), respuestasViewHolder.mPreguntasCheck.isChecked()));
        respuestasViewHolder.mPreguntasCheck.setChecked(mData.get(i).isStatus());
     //   respuestasViewHolder.mPreguntasCheck.setEnabled(!mContestado);
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size(): 0;
    }

     public class CheckListRespuestasViewHolder extends RecyclerView.ViewHolder {
        private TextView mPreguntaTexto;
        private CheckBox mPreguntasCheck;

        public CheckListRespuestasViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaTexto = itemView.findViewById(R.id.Titilo_label);
            mPreguntasCheck = itemView.findViewById(R.id.pregunta_checkbox_checklist);
        }
    }
}