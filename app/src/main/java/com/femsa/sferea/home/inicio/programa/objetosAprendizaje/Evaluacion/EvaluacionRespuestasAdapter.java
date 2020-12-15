package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion.RespuestasEvaluacion;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class EvaluacionRespuestasAdapter extends RecyclerView.Adapter<EvaluacionRespuestasAdapter.RespuestasViewHolder> {

    private Context mContext;
    private ArrayList<RespuestasEvaluacion> mListadoRespuestas;
    private int tipoPregunta;
    private int lastSelectedPosition = -1;
    private boolean mContestada;
    private OnEvaluacionRespuestaElegida mListener;

    public void setListener(OnEvaluacionRespuestaElegida listener)
        {
            mListener = listener;
        }

    public interface OnEvaluacionRespuestaElegida
        {
            void onCheckElegida(String idPregunta, String idRespuesta, boolean add, boolean correcta);
            void onRadioElegida(String idPregunta, String idRespuesta, boolean correcta);
            void selecionarChecked(int position);
        }

    public EvaluacionRespuestasAdapter(Context context, ArrayList<RespuestasEvaluacion> data, int tipo, boolean contestada)
    {
        mContext = context;
        mListadoRespuestas = data;
        tipoPregunta = tipo;
        mContestada = contestada;
    }

    @NonNull
    @Override
    public RespuestasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evaluacion_respuestas, viewGroup, false);
        return new EvaluacionRespuestasAdapter.RespuestasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestasViewHolder respuestasViewHolder, int i) {
        respuestasViewHolder.mPreguntaRadio.setChecked(lastSelectedPosition == i);
        if(tipoPregunta == EvaluacionPreguntasAdapter.CHECK)
            {
                respuestasViewHolder.mPreguntaRadio.setVisibility(View.GONE);
                respuestasViewHolder.mCheckTexto.setText(mListadoRespuestas.get(i).getRespuestaTexto());
                respuestasViewHolder.mPreguntasCheck.setId(mListadoRespuestas.get(i).getIdRespuesta());
                respuestasViewHolder.mPreguntasCheck.setChecked(mListadoRespuestas.get(i).isStatus());
                respuestasViewHolder.mPreguntasCheck.setEnabled(!mContestada);
            }
        else if(tipoPregunta == EvaluacionPreguntasAdapter.RADIO)
            {
                respuestasViewHolder.mCheckTexto.setVisibility(View.GONE);
                respuestasViewHolder.mPreguntasCheck.setVisibility(View.GONE);
                respuestasViewHolder.mPreguntaRadio.setText(mListadoRespuestas.get(i).getRespuestaTexto());
                respuestasViewHolder.mPreguntaRadio.setId(mListadoRespuestas.get(i).getIdRespuesta());
                if(lastSelectedPosition == -1)
                    {
                        respuestasViewHolder.mPreguntaRadio.setChecked(mListadoRespuestas.get(i).isStatus());
                    }
                respuestasViewHolder.mPreguntaRadio.setEnabled(!mContestada);
            }
        else
            {
                respuestasViewHolder.mPreguntaRadio.setVisibility(View.GONE);
                respuestasViewHolder.mCheckTexto.setVisibility(View.GONE);
                respuestasViewHolder.mPreguntasCheck.setVisibility(View.GONE);
            }



        respuestasViewHolder.mPreguntasCheck.setOnClickListener(v->
                mListener.onCheckElegida(String.valueOf(mListadoRespuestas.get(i).getIdPregunta()), String.valueOf(respuestasViewHolder.mPreguntasCheck.getId()), respuestasViewHolder.mPreguntasCheck.isChecked(), mListadoRespuestas.get(i).isCorrecta())
        );

    }

    @Override
    public int getItemCount() {
        return mListadoRespuestas != null ? mListadoRespuestas.size(): 0;
    }


    public class RespuestasViewHolder extends RecyclerView.ViewHolder
    {
        RadioButton mPreguntaRadio;
        TextView mCheckTexto;
        CheckBox mPreguntasCheck;

        public RespuestasViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaRadio = itemView.findViewById(R.id.radio_pregunta_rb);
            mPreguntasCheck = itemView.findViewById(R.id.pregunta_checkbox);
            mCheckTexto = itemView.findViewById(R.id.Titilo_label);
            mPreguntaRadio.setOnClickListener(
                    v-> {
                        mListener.onRadioElegida(
                                String.valueOf(mListadoRespuestas.get(getAdapterPosition()).getIdPregunta()),
                                String.valueOf(mPreguntaRadio.getId()),
                                mListadoRespuestas.get(getAdapterPosition()).isCorrecta()
                            );
                        lastSelectedPosition = getAdapterPosition();
                        mListener.selecionarChecked(getAdapterPosition());
                        notifyDataSetChanged();
                    });
        }
    }

}
