package com.femsa.sferea.home.inicio.componentes.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.femsa.requestmanager.DTO.User.Home.DataPrograms;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class CarrouselAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<DataPrograms> mdata;
    private OnCarruselClick mListener;

    public interface OnCarruselClick
    {
        /**
         * Método que nos envia al detalle de cada programa.
         * @param id: el id de el programa seleccionado.
         */
        void OnCarruselClicked(int id);
    }

    public void setListener(OnCarruselClick listener)
    {
        mListener = listener;
    }

    public CarrouselAdapter(Context context, ArrayList<DataPrograms> data) {
        mdata = data;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pager, container,
                false);
        TextView maintitle = itemView.findViewById(R.id.pager_item_whatIs);
        TextView mainDesc = itemView.findViewById(R.id.whatis_description);
        TextView title = itemView.findViewById(R.id.pager_item_title);
        ImageView imageView = itemView.findViewById(R.id.bandera_pais_menu_lateral);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + mdata.get(i).getImageProgram();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop().placeholder(R.drawable.sin_imagen)
                .apply(new RequestOptions().override(768, 1024))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.femsa_white));
                        return false;
                    }
                })
                .into(imageView);
        /*Picasso.with(mContext).load(fullPath).error(R.drawable.sin_imagen).resize(768, 1024).onlyScaleDown().into(imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                imageView.setBackgroundColor(mContext.getResources().getColor(R.color.femsa_white));
            }

            @Override
            public void onError() {

            }
        });*/

        if(i == 0)
            {
                //title.setAlpha(0);
            }
        else
            {
                maintitle.setAlpha(0);
                mainDesc.setAlpha(0);
            }
        title.setText(mdata.get(i).getImageTitle());

        itemView.setOnClickListener(
                new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        mListener.OnCarruselClicked(mdata.get(i).getIdProgram());
                    }
                });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}