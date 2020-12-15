package com.femsa.sferea.home.inicio.componentes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.sferea.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>{

    private SubcategoriesResponse mData;

    private CategoriesResponse mDataCategories;

    private Context mContext;

    /**
     * Bandera usada para distinguir el tipo de elementos a mostrar en el dialog y su comportamiento:
     * 0 es para subcategorias
     * 1 es para categorias
     * */
    int mType;

    private OnHomeFilterListener mListener;

    private OnFilterDialogListener mDialogListener;

    public FilterAdapter(SubcategoriesResponse subCategories, Context context, int type)
    {
        mData = subCategories;
        mContext = context;
        mType = type;
    }

    public FilterAdapter(CategoriesResponse categories, Context context, int type)
    {
        mDataCategories = categories;
        mType = type;
        mContext = context;
    }

    public void setOnHomeFilterListenet(OnHomeFilterListener listener, OnFilterDialogListener dialogListener)
    {
        mListener = listener;
        mDialogListener = dialogListener;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_filter_recycle_element, viewGroup, false);
        return new FilterAdapter.FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder filterViewHolder, int i) {
        if(mType == 0)
        {
            filterViewHolder.mCategoryTV.setText(mData.getSubcategories().getCategoryNames().get(i)); //nombrecategoria}
            filterViewHolder.mCategoryTV.setOnClickListener(v->{
                mDialogListener.closeDialog();
                mListener.onSubCategoriaFiltroClick(mData.getSubcategories().getSubCategoryID().get(i), mData.getSubcategories().getCategoryNames().get(i));
                });
        }
        else if(mType == 1)
        {
            filterViewHolder.mCategoryTV.setText(mDataCategories.getCategories().getAllCategories().get(i));
            filterViewHolder.mCategoryTV.setOnClickListener(v->{
                    mDialogListener.closeDialog();
                    mListener.onCategoriaFiltroHeaderClick(mDataCategories.getCategories().getAllCategories().get(i), mDataCategories.getCategories().getAllIDCategories().get(i));
                });
        }
    }

    @Override
    public int getItemCount() {
        if(mType == 0)
            {
                return (mData != null) ? (mData.getSubcategories().getCategoryNames() != null ? mData.getSubcategories().getCategoryNames().size() : 0 ) : 0;
            }
        else
            {
                return (mDataCategories != null) ? (mDataCategories.getCategories().getAllCategories() != null ? mDataCategories.getCategories().getAllCategories().size() : 0) : 0;
            }
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder
    {
        TextView mCategoryTV;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryTV = itemView.findViewById(R.id.filter_element_vh);
        }
    }

    public interface OnHomeFilterListener
    {
        /**
         * Método que se ejecuta cuando se hace click sobre la subcategoria en el filtro.
         * @param idSubcategoria el id de la subcategoria que se va a consultar.
         * @param textoSubcategoria el nombre de la subcategoria.
         * */
        void onSubCategoriaFiltroClick(int idSubcategoria, String textoSubcategoria);

        /**
         * Mpetodo que se ejecuta cuando se hace clik sobre una categoría en el filtro.
         * @param textoCategoria el nombre de la categoría.
         * @param idCategoria el idi de la categoría.
         * */
        void onCategoriaFiltroHeaderClick(String textoCategoria, int idCategoria);
    }

    public interface OnFilterDialogListener
    {
        void closeDialog();
    }

}
