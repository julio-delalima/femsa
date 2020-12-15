package com.femsa.sferea.home.miCuenta.miRanking.listado;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.Ranking.ProgramRankingData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class RankingAdapter extends  RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    ArrayList<ProgramRankingData> mData;
    Context mContext;

    private OnItemClickListener mListener;

    public RankingAdapter(ArrayList<ProgramRankingData> data, Context context)
    {
        mData = new ArrayList<>();
        mData.addAll(data);
        mContext = context;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_recycler_ranking, viewGroup, false);
        return new RankingAdapter.RankingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder rankingViewHolder, int i) {
        rankingViewHolder.title.setText(mData.get(i).getProgramName());
        rankingViewHolder.position.setText(mData.get(i).getPosition()
                + " " + AppTalentoRHApplication.getApplication().getResources().getString(R.string.nden) + " "
                + mData.get(i).getNumOpponents());
        rankingViewHolder.earnedPoints.setText(mData.get(i).getBonus() + "/" +  mData.get(i).getTotalBonus());
        rankingViewHolder.itemView.setOnClickListener(v -> mListener.onClickVerDetalleRanking(mData.get(i).getIDProgram(), mData.get(i).getProgramName()));
        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + mData.get(i).getImgProgram();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl).error(R.drawable.sin_imagen).apply(new RequestOptions().override(250,300)).into(rankingViewHolder.image);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder
    {
        TextView earnedPoints, position, title;
        ImageView image;
        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            earnedPoints = itemView.findViewById(R.id.ranking_earned_point);
            position = itemView.findViewById(R.id.ranking_position);
            title = itemView.findViewById(R.id.ranking_title);
            image = itemView.findViewById(R.id.ranking_image);
        }
    }

    public interface OnItemClickListener{
        void onClickVerDetalleRanking(int idProgram, String nameProgram);
    }
}
