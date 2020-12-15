package com.femsa.sferea.home.inicio.componentes.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.sferea.R;

public class SeccionHeaderAdapter extends RecyclerView.Adapter<SeccionHeaderAdapter.SeccionHeaderViewHolder>{

    private CategoriesResponse mData;

    private OnSeccionHeaderListener mListener;

    public interface OnSeccionHeaderListener
    {
        /**
         * Método que se ejecuta cuando se hace click sobre una de las categorías mostradas en el header, activando
         * el filtro por cada categoría.
         * @param filtro el nombre de la categoría sobre la que se hizo click.
         * @param ID el ID de dicha categoría.
         * */
        void OnClickSeccionHeader(String filtro, int ID);
    }

    public SeccionHeaderAdapter(CategoriesResponse data, OnSeccionHeaderListener listener)
    {
        mData = data;

        mListener = listener;
    }

    /**
     * Inicialziacón del layout del item
     * */
    @NonNull
    @Override
    public SeccionHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_seccion, viewGroup, false);
        return new SeccionHeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionHeaderViewHolder seccionHeaderViewHolder, int position) {
        seccionHeaderViewHolder.headerButton.setText(mData.getCategories().getAllCategories().get(position));
        seccionHeaderViewHolder.headerButton.setOnClickListener(
                v -> mListener.OnClickSeccionHeader(mData.getCategories().getAllCategories().get(position), mData.getCategories().getAllIDCategories().get(position))
        );
    }

    @Override
    public int getItemCount() {
        return (mData != null) ? mData.getCategories().getAllCategories().size() : 0;
    }

    public class SeccionHeaderViewHolder extends RecyclerView.ViewHolder
        {
            Button headerButton;
            public SeccionHeaderViewHolder(@NonNull View itemView) {
                super(itemView);
                headerButton = itemView.findViewById(R.id.headerSectionButton);
            }
        }
}
