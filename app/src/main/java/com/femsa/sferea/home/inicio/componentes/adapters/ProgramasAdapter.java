package com.femsa.sferea.home.inicio.componentes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.Home.DataPrograms;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class ProgramasAdapter extends RecyclerView.Adapter<ProgramasAdapter.SubRecyclerViewHolder> {

    public ArrayList<DataPrograms> mElements;
    public Context mContext;
    private int mWidth;
    private OnItemListener mListener;

    public ProgramasAdapter(ArrayList<DataPrograms> elements, Context context, int width)
    {
        mElements = elements;
        mContext = context;
        mWidth = width;
    }

    public void setOnItemListener(OnItemListener listener)
    {
        mListener = listener;
    }

    @NonNull
    @Override
    public SubRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subrecycler_element, viewGroup, false);
        return new ProgramasAdapter.SubRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubRecyclerViewHolder subRecyclerViewHolder, int i) {
        int clickCount = 0;
        int ancho = (int) (mWidth / 2.7);
        int alto = (int) (ancho * 4 / 2.4);
        //final int elementId = (mTitleIndex * 10 ) + i;
        subRecyclerViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ancho, alto));
        subRecyclerViewHolder.title.setText(mElements.get(i).getImageTitle());
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + mElements.get(i).getImageProgram();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl).
                centerCrop().placeholder(R.drawable.sin_imagen)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(new RequestOptions().override(ancho + 100, alto))
                .into(subRecyclerViewHolder.iconButton);

        //Picasso.with(mContext).load(fullPath).error(R.drawable.sin_imagen).resize(ancho, alto).onlyScaleDown().into(subRecyclerViewHolder.iconButton);

        subRecyclerViewHolder.itemView.setId(mElements.get(i).getIdProgram());
        subRecyclerViewHolder.itemView.setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        mListener.onIntemPressed(mElements.get(i).getIdProgram());
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return mElements.size();
    }

    public class SubRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView iconButton;

        public SubRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subTextview);
            iconButton = itemView.findViewById(R.id.subButton);
        }
    }

    public interface OnItemListener
    {
        void onIntemPressed(int id);
    }
}
