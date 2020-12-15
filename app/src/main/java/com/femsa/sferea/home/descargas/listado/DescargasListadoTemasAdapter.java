package com.femsa.sferea.home.descargas.listado;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;
import com.femsa.sferea.home.descargas.base.entity.ProgramaEntity;

import java.util.LinkedList;
import java.util.List;

public class DescargasListadoTemasAdapter extends RecyclerView.Adapter<DescargasListadoTemasAdapter.DescargasTemarioViewHolder> {

    private DescargasObjetosListadoAdapter mAdapter;

    private Context mContext;

    private List<ProgramaEntity> mListadoProgramas = new LinkedList<>();

    private List<ObjetoAprendizajeEntity> mListadoObjetos = new LinkedList<>();

    private DescargasObjetosListadoAdapter.OnDescargaClicked mListener;

    public DescargasListadoTemasAdapter(Context context, DescargasObjetosListadoAdapter.OnDescargaClicked listener, List<ProgramaEntity> listadoProgramas, List<ObjetoAprendizajeEntity> objetos) {
        mListener = listener;
        mContext = context;
        mListadoProgramas.addAll(listadoProgramas);
        mListadoObjetos.addAll(objetos);
    }

    @NonNull
    @Override
    public DescargasTemarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kof_recycler, viewGroup, false);
        return new DescargasTemarioViewHolder(view);
    }

    public void setListados(List<ProgramaEntity> listadoProgramas, List<ObjetoAprendizajeEntity> listadoObjetos) {
        mListadoProgramas = listadoProgramas;
        mListadoObjetos = listadoObjetos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final DescargasTemarioViewHolder temarioViewHolder, int i) {
        LinearLayoutManager mLinear = new LinearLayoutManager(mContext);
        mLinear.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new DescargasObjetosListadoAdapter(mContext, mListener, mListadoObjetos, mListadoProgramas.get(i).getIdPrograma());
        temarioViewHolder.sectionRV.setAdapter(mAdapter);
        temarioViewHolder.sectionRV.setLayoutManager(mLinear);
        temarioViewHolder.title.setText(mListadoProgramas != null ? mListadoProgramas.get(i).getTituloPrograma() : "");
        temarioViewHolder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temarioViewHolder.sectionRV.getVisibility() == View.VISIBLE)
                {
                    temarioViewHolder.sectionRV.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    temarioViewHolder.sectionRV.setVisibility(View.GONE);
                                }
                            });
                    temarioViewHolder.closeButton.animate().rotation(180);

                }
                else
                {
                    temarioViewHolder.sectionRV.setVisibility(View.VISIBLE);//discollapse it
                    temarioViewHolder.sectionRV.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    temarioViewHolder.sectionRV.setVisibility(View.VISIBLE);
                                }
                            });
                    temarioViewHolder.closeButton.animate().rotation(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListadoProgramas != null ? mListadoProgramas.size() : 0;
    }

    public class DescargasTemarioViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        RecyclerView sectionRV;
        ImageView closeButton;
        CardView card;
        public DescargasTemarioViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.kof_section_title_tv);
            sectionRV = itemView.findViewById(R.id.kof_section_Recycler);
            closeButton = itemView.findViewById(R.id.kof_close_button);
            card = itemView.findViewById(R.id.kof_recycler_card);
        }
    }

}
