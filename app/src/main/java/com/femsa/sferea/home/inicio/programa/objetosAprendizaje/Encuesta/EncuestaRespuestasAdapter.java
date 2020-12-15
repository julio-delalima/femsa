package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Encuesta;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.Encuesta.RespuestasEncuesta;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class EncuestaRespuestasAdapter extends RecyclerView.Adapter<EncuestaRespuestasAdapter.RespuestasViewHolder> {

    private Context mContext;
    private ArrayList<RespuestasEncuesta> mListadoRespuestas;
    private int tipoPregunta, idPregunta = -1;
    private int lastSelectedPosition = -1;
    private boolean mContestada = false;
    private OnEncuestaRespuestaElegida mListener;

    public void setListener(OnEncuestaRespuestaElegida listener)
    {
        mListener = listener;
    }

    public interface OnEncuestaRespuestaElegida
    {
        void onCheckElegida(String idPregunta, String respuesta, boolean add, boolean correcta);
        void onRadioElegida(String idPregunta, String respuesta, boolean correcta);
        void onPregntaAbiertaRespondida(String idPregunta, String respuesta, boolean add);
        void selecionarChecked(int position);
    }

    public EncuestaRespuestasAdapter(Context context, ArrayList<RespuestasEncuesta> data, int tipo, int idPregunta, boolean contestada)
    {
        mContext = context;
        mListadoRespuestas = data;
        tipoPregunta = tipo;
        this.idPregunta = idPregunta;
        mContestada = contestada;
    }

    @NonNull
    @Override
    public RespuestasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_encuesta, viewGroup, false);
        return new EncuestaRespuestasAdapter.RespuestasViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RespuestasViewHolder respuestasViewHolder, int i) {
        respuestasViewHolder.mPreguntaRadio.setChecked(lastSelectedPosition == i);
        respuestasViewHolder.mPreguntaRadio.setVisibility(View.GONE);
        respuestasViewHolder.mCheckTexto.setVisibility(View.GONE);
        respuestasViewHolder.mPreguntasCheck.setVisibility(View.GONE);
        respuestasViewHolder.mAbiertacontainer.setVisibility(View.GONE);
        switch(tipoPregunta)
            {
                case EncuestaPreguntasAdapter.CHECK:
                        respuestasViewHolder.mPreguntasCheck.setVisibility(View.VISIBLE);
                        respuestasViewHolder.mPreguntasCheck.setChecked(mListadoRespuestas.get(i).isStatus());
                        if(mContestada)
                            {
                                respuestasViewHolder.mPreguntasCheck.setEnabled(false);
                            }
                        respuestasViewHolder.mCheckTexto.setVisibility(View.VISIBLE);
                        respuestasViewHolder.mCheckTexto.setText(mListadoRespuestas.get(i).getRespuestaTexto());
                        respuestasViewHolder.mPreguntasCheck.setId(mListadoRespuestas.get(i).getIdRespuesta());
                    break;
                case EncuestaPreguntasAdapter.RADIO:
                        respuestasViewHolder.mPreguntaRadio.setVisibility(View.VISIBLE);
                        respuestasViewHolder.mPreguntaRadio.setText(mListadoRespuestas.get(i).getRespuestaTexto());
                        respuestasViewHolder.mPreguntaRadio.setId(mListadoRespuestas.get(i).getIdRespuesta());
                        if(lastSelectedPosition == -1)
                            {
                                respuestasViewHolder.mPreguntaRadio.setChecked(mListadoRespuestas.get(i).isStatus());
                            }
                            if(mContestada)
                            {
                                respuestasViewHolder.mPreguntaRadio.setEnabled(false);
                            }
                    break;
                case EncuestaPreguntasAdapter.TEXTO:
                    respuestasViewHolder.mPreguntaAbiertaET.setVisibility(View.VISIBLE);
                    try
                        {
                            String texto = mListadoRespuestas.get(i).getRespuestaGuardada();
                            respuestasViewHolder.mPreguntaAbiertaET.setText(texto != null ? texto : "");
                            if(mContestada)
                            {
                                respuestasViewHolder.mPreguntaAbiertaET.setEnabled(false);
                            }
                        }
                    catch (Exception ignored){}
                    respuestasViewHolder.mAbiertacontainer.setVisibility(View.VISIBLE);
                    break;
                default:
                    respuestasViewHolder.mPreguntaRadio.setVisibility(View.GONE);
                    respuestasViewHolder.mCheckTexto.setVisibility(View.GONE);
                    respuestasViewHolder.mPreguntasCheck.setVisibility(View.GONE);
                    respuestasViewHolder.mAbiertacontainer.setVisibility(View.GONE);
            }

        respuestasViewHolder.mPreguntasCheck.setOnClickListener(v->
                mListener.onCheckElegida(String.valueOf(idPregunta),
                        String.valueOf(mListadoRespuestas.get(i).getIdRespuesta()), respuestasViewHolder.mPreguntasCheck.isChecked(), mListadoRespuestas.get(i).isCorrecta())
        );

        respuestasViewHolder.mPreguntaAbiertaET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                mListener.onPregntaAbiertaRespondida(String.valueOf(idPregunta), s.toString(), true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mListener.onPregntaAbiertaRespondida(String.valueOf(idPregunta), s.toString(), false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListadoRespuestas != null
                ?
                    (tipoPregunta != EncuestaPreguntasAdapter.TEXTO  ? mListadoRespuestas.size() : 1)
                : 0;
    }


    public class RespuestasViewHolder extends RecyclerView.ViewHolder
    {
        RadioButton mPreguntaRadio;
        TextView mCheckTexto;
        CheckBox mPreguntasCheck;
        EditText mPreguntaAbiertaET;
        TextInputLayout mAbiertacontainer;


        public RespuestasViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaRadio = itemView.findViewById(R.id.radio_pregunta_rb);
            mPreguntasCheck = itemView.findViewById(R.id.pregunta_checkbox);
            mCheckTexto = itemView.findViewById(R.id.Titilo_label);
            mPreguntaAbiertaET = itemView.findViewById(R.id.encuesta_respuesta_abierta_et);
            mAbiertacontainer = itemView.findViewById(R.id.textInputLayout5);
            mPreguntaRadio.setOnClickListener(
                    v-> {
                        mListener.onRadioElegida(String.valueOf(idPregunta),
                                String.valueOf(mListadoRespuestas.get(getAdapterPosition()).getIdRespuesta()),
                                mListadoRespuestas.get(getAdapterPosition()).isCorrecta()
                        );
                        lastSelectedPosition = getAdapterPosition();
                        mListener.selecionarChecked(getAdapterPosition());
                        notifyDataSetChanged();
                    });
        }
    }

}
