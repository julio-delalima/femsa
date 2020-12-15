package com.femsa.sferea.home.inicio.programa.detallePrograma.listado;

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

import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.ObjectData;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class TemarioAdapter extends RecyclerView.Adapter<TemarioAdapter.TemarioViewHolder> implements  TemarioSubRecyclerAdapter.OnItemListener{

    private ArrayList<ObjectData> mData;
    private ArrayList<String> mTitles;
    private Context mContext;
    private OnHomeListener mListener;


    public TemarioAdapter(ArrayList<String> titles, ArrayList<ObjectData> data, Context context)//ArrayList<String> titles, ArrayList<ArrayList<String>> subtitles, ArrayList<ArrayList<String>> types, ArrayList<ArrayList<String>> descriptions,  ArrayList<ArrayList<Integer>> images,Context context)
    {
        mTitles = titles;
        mContext = context;
        mData = data;
    }

    public void setOnHomeListener(TemarioAdapter.OnHomeListener listener)
    {
        mListener = listener;
    }


    @NonNull
    @Override
    public TemarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kof_recycler, viewGroup, false);
        return new TemarioAdapter.TemarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TemarioViewHolder temarioViewHolder, int i) {
        temarioViewHolder.title.setText((i + 1) + " " + mTitles.get(i));//mTitles.get(i));
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ArrayList<ObjectData> allowedData = new ArrayList();
        for(int j = 0; j < mData.size(); j++)
            {
                if(mData.get(j).getModule().equals(mTitles.get(i)))
                    {
                        allowedData.add(mData.get(j));
                    }
            }
        TemarioSubRecyclerAdapter adapter = new TemarioSubRecyclerAdapter(mTitles.get(i), allowedData, mContext, i);
        adapter.setOnItemListener(this);
        temarioViewHolder.sectionRV.setLayoutManager(LayoutManager);
        temarioViewHolder.sectionRV.setAdapter(adapter);
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
        return mTitles.size();
    }

    @Override
    public void onItemPressed(ObjectData data) {
        mListener.onResponseListener(data);
    }


    public class TemarioViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        RecyclerView sectionRV;
        ImageView closeButton;
        CardView card;
        public TemarioViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.kof_section_title_tv);
            sectionRV = itemView.findViewById(R.id.kof_section_Recycler);
            closeButton = itemView.findViewById(R.id.kof_close_button);
            card = itemView.findViewById(R.id.kof_recycler_card);
        }
    }

    public interface OnHomeListener
    {
        void onResponseListener(ObjectData data);
    }

}
