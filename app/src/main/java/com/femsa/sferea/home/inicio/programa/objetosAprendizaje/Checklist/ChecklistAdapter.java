package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Checklist;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.CheckListResponseData;
import com.femsa.sferea.R;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {


    private Context mContext;

    private CheckListResponseData mData;

    private ChecklistRespuestasAdapter respuestas;

    //private boolean mContestado = false;

    private ChecklistRespuestasAdapter.OnCheckRespondido mListener;

    public ChecklistAdapter(Context context, CheckListResponseData data, ChecklistRespuestasAdapter.OnCheckRespondido listener, boolean contestado) {
        mContext = context;
        mData = data;
        mListener = listener;
       // mContestado = contestado;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_checklist, viewGroup, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder checkListViewHolder, int i) {
        checkListViewHolder.mPreguntaTexto.setText(mData.getCheckList().getCheckList().getPreguntas().get(i).getTextoPregunta());
        respuestas = new ChecklistRespuestasAdapter(mContext, mData.getCheckList().getCheckList().getPreguntas().get(i).getRespuestas(), false);
        respuestas.setListener(mListener);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        checkListViewHolder.mRespuestas.setLayoutManager(LayoutManager);
        checkListViewHolder.mRespuestas.setAdapter(respuestas);
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.getCheckList().getCheckList().getPreguntas().size() : 0;
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {
        TextView mPreguntaTexto;
        RecyclerView mRespuestas;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            mPreguntaTexto = itemView.findViewById(R.id.checklist_pregunta_texto_tv);
            mRespuestas = itemView.findViewById(R.id.respuestas_recycler_checklist_rv);
        }
    }
}