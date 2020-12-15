package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.ConfirmarCelula;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmarParticipantesAdapter extends RecyclerView.Adapter<ConfirmarParticipantesAdapter.ConfirmarParticipantesViewHolder> {

    private ArrayList<ParticipanteDTO> mListadoParticipantes;
    private Context mContext;
    private static final String mRutaFoto = RequestManager.IMAGE_BASE_URL;
    private OnConfirmarParticipantes mListener;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    public interface OnConfirmarParticipantes
        {
            void onEliminarParticipante(int idParticipante);
        }

    public void setListener(OnConfirmarParticipantes listener)
        {
            mListener = listener;
        }

    public ConfirmarParticipantesAdapter(ArrayList<ParticipanteDTO> listadoParticipantes, Context context, ArrayList<PaisesDTO.PaisData> listadoPaises ) {
        mListadoParticipantes = new ArrayList<>();
        mListadoPaises = new ArrayList<>();
        mListadoParticipantes.addAll(listadoParticipantes);
        mListadoPaises.addAll(listadoPaises);
        mContext = context;
    }

    public void setPaisesData(ArrayList<PaisesDTO.PaisData> listadoPaises)
        {
            mListadoPaises.addAll(listadoPaises);
            notifyDataSetChanged();
        }

    public void removeParticipante(int indice)
        {
            mListadoParticipantes.remove(indice);
            notifyItemRemoved(indice);
        }

    @NonNull
    @Override
    public ConfirmarParticipantesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_confirmar_celula_participantes, viewGroup, false);
        return  new ConfirmarParticipantesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmarParticipantesViewHolder confirmarParticipantesViewHolder, int i) {
        //GlideApp.with(mContext).load().error(R.drawable.sin_imagen).into(confirmarParticipantesViewHolder.mBandera);
        GlideUrl glideUrl = new GlideUrl(mRutaFoto,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl + "/" +mListadoParticipantes.get(i).getmFotoPerfil()).error(R.drawable.sin_imagen).into(confirmarParticipantesViewHolder.mFotoParticipante);
        confirmarParticipantesViewHolder.mSubArea.setText(mListadoParticipantes.get(i).getSubAreaProcesoParticipante());
        confirmarParticipantesViewHolder.mAreaFuncional.setText(
                mContext.getString(R.string.activity_detalle_celula_entrenamiento_area_funcional,mListadoParticipantes.get(i).getAreaFuncionalParticipante()));
        confirmarParticipantesViewHolder.mPuesto.setText(mListadoParticipantes.get(i).getPuestoParticipante());
        confirmarParticipantesViewHolder.mNombre.setText(mListadoParticipantes.get(i).getNombreParticipante());
        confirmarParticipantesViewHolder.mNumeroEmpleado.setText(
                mContext.getString(R.string.activity_detalle_celula_entrenamiento_numero_empleado_2, mListadoParticipantes.get(i).getNumeroParticipante())
        );

        for(PaisesDTO.PaisData paisActual : mListadoPaises)
        {
            if(paisActual.getIdPais() == mListadoParticipantes.get(i).getIDPais())
            {
                GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(confirmarParticipantesViewHolder.mBandera);
                break;
            }
        }



    }

    @Override
    public int getItemCount() {
        return (mListadoParticipantes != null) ? mListadoParticipantes.size() : 0;
    }

    public class ConfirmarParticipantesViewHolder extends RecyclerView.ViewHolder{

        TextView mNombre, mNumeroEmpleado, mPuesto, mAreaFuncional, mSubArea;
        CircleImageView mFotoParticipante;
        ImageView mBandera;
        ImageButton mEliminar;

        public ConfirmarParticipantesViewHolder(@NonNull View itemView) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.tv_participantes_layout_nombre);
            mNumeroEmpleado = itemView.findViewById(R.id.tv_participantes_layout_numero_empleado);
            mPuesto = itemView.findViewById(R.id.tv_participantes_layout_posicion_solicitante);
            mAreaFuncional  = itemView.findViewById(R.id.tv_participantes_layout_area_funcional);
            mSubArea  = itemView.findViewById(R.id.tv_participantes_layout_subarea_proceso);
            mFotoParticipante = itemView.findViewById(R.id.civ_participantes_layout_foto_perfil);
            mBandera =  itemView.findViewById(R.id.iv_participantes_layout_flag);
            mEliminar = itemView.findViewById(R.id.ib_participantes_layout_close);
            mEliminar.setOnClickListener(
                    v-> mListener.onEliminarParticipante(getAdapterPosition())
            );
        }
    }
}
