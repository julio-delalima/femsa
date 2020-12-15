package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Evaluacion;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Evaluacion.PreguntasEvaluacion;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class EvaluacionPreguntasAdapter extends RecyclerView.Adapter<EvaluacionPreguntasAdapter.EvaluacionViewHolder> {

    private ArrayList<PreguntasEvaluacion> mListadoPreguntas;
    private Context mContext;
    private EvaluacionRespuestasAdapter.OnEvaluacionRespuestaElegida listener;
    private boolean mContestada;
    public static final int RADIO = 0;
    public static final int CHECK = 1;

    public EvaluacionPreguntasAdapter(Context context, ArrayList<PreguntasEvaluacion> data, EvaluacionRespuestasAdapter.OnEvaluacionRespuestaElegida listen, boolean contestada)
    {
        mContext = context;
        mListadoPreguntas = data;
        listener = listen;
        mContestada = contestada;
    }

    @NonNull
    @Override
    public EvaluacionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evaluacion_y_encuesta, viewGroup, false);
        return new EvaluacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluacionViewHolder evaluacionViewHolder, int i) {

        evaluacionViewHolder.mPreguntaIndice.setText(mContext.getResources().getString(R.string.de, i + 1, mListadoPreguntas.size()));
        evaluacionViewHolder.mPreguntaTexto.setText(mListadoPreguntas.get(i).getTextoPregunta());
        EvaluacionRespuestasAdapter respuestas =
                new EvaluacionRespuestasAdapter(
                        mContext,
                        mListadoPreguntas.get(i).getRespuestas(),
                        (mListadoPreguntas.get(i).getTipoPregunta().equals("check")) ? CHECK : RADIO,
                        mContestada
                );
        respuestas.setListener(listener);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        evaluacionViewHolder.mRespuestas.setLayoutManager(LayoutManager);
        evaluacionViewHolder.mRespuestas.setAdapter(respuestas);
    }

    @Override
    public int getItemCount() {
        return mListadoPreguntas != null ? mListadoPreguntas.size(): 0;
    }

    public static class EvaluacionViewHolder extends RecyclerView.ViewHolder
    {
        TextView mPreguntaIndice, mPreguntaTexto;
        RecyclerView mRespuestas;
        public EvaluacionViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaIndice = itemView.findViewById(R.id.pregunta_indice_tv);
            mPreguntaTexto = itemView.findViewById(R.id.pregunta_texto_tv);
            mRespuestas = itemView.findViewById(R.id.respuestas_recycler_evaluacion_rv);
        }
    }
}
