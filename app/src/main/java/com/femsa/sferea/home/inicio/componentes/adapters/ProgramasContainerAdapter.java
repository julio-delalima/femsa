package com.femsa.sferea.home.inicio.componentes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.Home.DataPrograms;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class ProgramasContainerAdapter extends RecyclerView.Adapter<ProgramasContainerAdapter.HomeViewHolder> implements ProgramasAdapter.OnItemListener {

    public ArrayList<String> mTitles = new ArrayList<>();

    ArrayList<DataPrograms> mElements = new ArrayList<>();
    Context mContext;
    int mWidth;
    private OnHomeListener mListener;
    private boolean mValidarTitulos;


    public void addAll(ArrayList<DataPrograms> elements, ArrayList<String> titles){
        mTitles.clear();
        mElements.clear();
        mTitles.addAll(titles);
        mElements.addAll(elements);
        notifyDataSetChanged();
    }

    public ProgramasContainerAdapter(ArrayList<String> titles, ArrayList<DataPrograms> elements, Context context, int width, boolean validarTitulos)
    {
        mTitles.addAll(titles);
        mElements.addAll(elements);
        mContext = context;
        mWidth = width;
        mValidarTitulos = validarTitulos;
    }

    public void setOnHomeListener(OnHomeListener listener)
    {
        mListener = listener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carrousel_home_recycler, viewGroup, false);
        return new ProgramasContainerAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {

        ArrayList<DataPrograms> allowedData = new ArrayList<>();

        if(mValidarTitulos)
            {
                for(int j = 0; j < mElements.size(); j++)
                {
                    if(mElements.get(j).getCategoryName().equals(mTitles.get(i))) //if the element is in the same category
                    {
                        allowedData.add(mElements.get(j));
                    }
                }
            }
        else
            {
                allowedData.addAll(mElements);
            }

        homeViewHolder.title.setText(mTitles.get(i)); //porque viste, lo más visto, etc
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        ProgramasAdapter adapter = new ProgramasAdapter(allowedData, mContext, mWidth);
        adapter.setOnItemListener(this);
        homeViewHolder.sectionRV.setLayoutManager(LayoutManager);
        homeViewHolder.sectionRV.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    @Override
    public void onIntemPressed(int id) {
        mListener.onResponseListener(id);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        RecyclerView sectionRV;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.kof_section_title_tv);
            sectionRV = itemView.findViewById(R.id.subsection_Recycler);
        }
    }

    public interface OnHomeListener
    {
        void onResponseListener(int id);
    }
}
