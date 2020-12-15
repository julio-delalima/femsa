package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaDos;

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
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Participante.ParticipanteDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

public class AdapterParticipantes  extends RecyclerView.Adapter<AdapterParticipantes.ParticipantesViewHolder> {

    private ArrayList<ParticipanteDTO> mList;
    private Context mContext;

    private OnParticipanteAdapter mListener;

    public interface OnParticipanteAdapter
        {
            void OnParticipanteCambiado(int posicion);

            void OnEliminarParticipante(int posicion);
        }

    public void setListener(OnParticipanteAdapter listener)
        {
            mListener = listener;
        }

    public AdapterParticipantes(Context context, ArrayList<ParticipanteDTO> listaP) {
        mContext = context;
        mList = listaP;
    }

    @NonNull
    @Override
    public ParticipantesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_participantes_view_pager, viewGroup,false);
        return new AdapterParticipantes.ParticipantesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantesViewHolder participantesViewHolder, int i) {
        mListener.OnParticipanteCambiado(i);
        participantesViewHolder.mNombreEmpleado.setText(mList.get(i).getNombreParticipante());
        participantesViewHolder.mNumEmpleado.setText(mContext.getString(R.string.numero_empleado_cardview, mList.get(i).getNumeroParticipante()));
        participantesViewHolder.mPuesto.setText(mList.get(i).getPuestoParticipante());
        participantesViewHolder.mAreaFuncional.setText(mContext.getString(R.string.areafuncional, mList.get(i).getAreaFuncionalParticipante()));
        //subproceso participantesViewHolder.mSubProceso.setText(mList.get(i).);
        participantesViewHolder.mNumJefeDirecto.setText(String.valueOf(mList.get(i).getNumeroJefeParticipante()));
        participantesViewHolder.mNombreJefe.setText(mList.get(i).getNombreJefeParticipante());
        participantesViewHolder.mPuestoJefe.setText(mList.get(i).getPosicionJefeParticipante());
        String fullPath = RequestManager.IMAGE_BASE_URL + "/" + mList.get(i).getmFotoPerfil();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(participantesViewHolder.mFotoPerfil.getContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(participantesViewHolder.mFotoPerfil);
        if (mList.get(i).getCorreoJefeParticipante() != "null"){
            participantesViewHolder.mCorreoJefe.setText(mList.get(i).getCorreoJefeParticipante());
        }else {
            participantesViewHolder.mCorreoJefe.setText("");
        }

        participantesViewHolder.mEliminarParticiapnte.setOnClickListener(v -> mListener.OnEliminarParticipante(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class ParticipantesViewHolder extends RecyclerView.ViewHolder {
        TextView mNombreEmpleado;
        TextView mNumEmpleado;
        TextView mPuesto;
        TextView mAreaFuncional;
        TextView mSubProceso;

        ImageView mFotoPerfil;
        ImageView mEliminarParticiapnte;
        TextView mNumJefeDirecto;
        TextView mNombreJefe;
        TextView mPuestoJefe;
        TextView mCorreoJefe;

        public ParticipantesViewHolder(@NonNull View itemView) {
            super(itemView);
            mNombreEmpleado = itemView.findViewById(R.id.nombreEmpleado);
            mNumEmpleado = itemView.findViewById(R.id.NumEmpleado);
            mPuesto = itemView.findViewById(R.id.Puesto);
            mAreaFuncional = itemView.findViewById(R.id.AreaFuncional);
            mSubProceso = itemView.findViewById(R.id.subProceso);
            mFotoPerfil = itemView.findViewById(R.id.fotoPerfil);
            mNumJefeDirecto = itemView.findViewById(R.id.Num_jefe);
            mNombreJefe = itemView.findViewById(R.id.tv_nombre_jefe);
            mPuestoJefe = itemView.findViewById(R.id.tv_puesto_jefe);
            mCorreoJefe = itemView.findViewById(R.id.tv_correo);
            mEliminarParticiapnte = itemView.findViewById(R.id.eliminar_participante);
        }
    }
}
