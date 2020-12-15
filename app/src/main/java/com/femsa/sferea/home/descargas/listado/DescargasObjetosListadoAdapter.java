package com.femsa.sferea.home.descargas.listado;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.descargas.base.entity.ObjetoAprendizajeEntity;

import java.util.LinkedList;
import java.util.List;

public class DescargasObjetosListadoAdapter extends RecyclerView.Adapter<DescargasObjetosListadoAdapter.DescargasObjetosViewHolder>{

    private Context mContext;

    private List<ObjetoAprendizajeEntity> mListadoObjetos;

    private OnDescargaClicked mListener;

    public interface OnDescargaClicked{
        void onDescargaClicked(ObjetoAprendizajeEntity objeto);
        void onEliminarClicked(ObjetoAprendizajeEntity objeto);
    }

    public DescargasObjetosListadoAdapter(Context context, OnDescargaClicked listener, List<ObjetoAprendizajeEntity> objetos, int idPrograma) {
        mContext = context;
        mListener = listener;
        mListadoObjetos = new LinkedList<>();
        List<ObjetoAprendizajeEntity> mTemp = new LinkedList<>();
        for(ObjetoAprendizajeEntity obj : objetos){
            if(obj.getIdPrograma() == idPrograma){
                mTemp.add(obj);
            }
        }
        mListadoObjetos.addAll(mTemp);
    }

    @NonNull
    @Override
    public DescargasObjetosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_descarga, viewGroup, false);
        return new DescargasObjetosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescargasObjetosViewHolder descargasObjetosViewHolder, int i) {
        descargasObjetosViewHolder.mBorrarBoton.setOnClickListener(v->{
            mListener.onEliminarClicked(mListadoObjetos.get(i));
        });
        descargasObjetosViewHolder.mTitulo.setText(mListadoObjetos.get(i).getNombreObjeto());
        descargasObjetosViewHolder.mTipoObjeto.setText(StringManager.getTipe(mContext ,mListadoObjetos.get(i).getTipo()));
        descargasObjetosViewHolder.mDescripcion.setText(mListadoObjetos.get(i).getDescripcion());
        GlideApp.with(mContext)
                .load(mListadoObjetos.get(i).getRutaImagen())
                .error(R.drawable.sin_imagen)
                .into(descargasObjetosViewHolder.mImagenPreview);
    }

    @Override
    public int getItemCount() {
        return mListadoObjetos != null ? mListadoObjetos.size() : 0;
    }


    public class DescargasObjetosViewHolder extends RecyclerView.ViewHolder {
        TextView mTitulo, mTipoObjeto, mDescripcion;
        ImageView mImagenPreview, mBorrarBoton;
        public DescargasObjetosViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.kof_title_rv);
            mTipoObjeto = itemView.findViewById(R.id.kof_type_rv);
            mDescripcion = itemView.findViewById(R.id.kof_description_tv);
            mImagenPreview = itemView.findViewById(R.id.kof_image);
            mBorrarBoton = itemView.findViewById(R.id.delete_button);
            itemView.setOnClickListener(v-> mListener.onDescargaClicked(mListadoObjetos.get(getAdapterPosition())));
        }
    }
}
