package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Encuesta.PreguntasEncuesta;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class EncuestaPreguntasAdapter extends RecyclerView.Adapter<EncuestaPreguntasAdapter.EncuestaViewHolder> {

    public static final int RADIO = 0;
    public static final int CHECK = 1;
    public static final int TEXTO = 2;
    private boolean mContestada = false;
    private ArrayList<PreguntasEncuesta> mListadoPreguntas;
    private Context mContext;
    private EncuestaRespuestasAdapter.OnEncuestaRespuestaElegida listener;

    public EncuestaPreguntasAdapter(Context context, ArrayList<PreguntasEncuesta> data, EncuestaRespuestasAdapter.OnEncuestaRespuestaElegida listen, boolean contestada)
    {
        mContext = context;
        mListadoPreguntas = data;
        listener = listen;
        mContestada = contestada;
    }


    @NonNull
    @Override
    public EncuestaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evaluacion_y_encuesta, viewGroup, false);
        return new EncuestaPreguntasAdapter.EncuestaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EncuestaViewHolder encuestaViewHolder, int i) {
        encuestaViewHolder.mPreguntaIndice.setText(mContext.getResources().getString(R.string.de, i + 1, mListadoPreguntas.size()));
        encuestaViewHolder.mPreguntaTexto.setText(mListadoPreguntas.get(i).getTextoPregunta());
        EncuestaRespuestasAdapter respuestas =
                new EncuestaRespuestasAdapter(
                        mContext,
                        mListadoPreguntas.get(i).getRespuestas(),
                        (mListadoPreguntas.get(i).getTipoPregunta().equals("check")) ? CHECK : (mListadoPreguntas.get(i).getTipoPregunta().equals("radio")) ? RADIO : TEXTO,
                        mListadoPreguntas.get(i).getIdPregunta() ,
                        mContestada
                );
        respuestas.setListener(listener);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        encuestaViewHolder.mRespuestas.setLayoutManager(LayoutManager);
        encuestaViewHolder.mRespuestas.setAdapter(respuestas);
    }

    @Override
    public int getItemCount() {
        return mListadoPreguntas != null ? mListadoPreguntas.size(): 0;
    }

    public class EncuestaViewHolder extends RecyclerView.ViewHolder
    {
        TextView mPreguntaIndice, mPreguntaTexto;
        RecyclerView mRespuestas;
        public EncuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaIndice = itemView.findViewById(R.id.pregunta_indice_tv);
            mPreguntaTexto = itemView.findViewById(R.id.pregunta_texto_tv);
            mRespuestas = itemView.findViewById(R.id.respuestas_recycler_evaluacion_rv);
        }
    }
}
