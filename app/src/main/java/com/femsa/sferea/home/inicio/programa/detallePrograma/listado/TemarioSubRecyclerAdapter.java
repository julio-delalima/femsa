package com.femsa.sferea.home.inicio.programa.detallePrograma.listado;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.femsa.requestmanager.DTO.User.ObjetosAprendizaje.ObjectData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.OnSingleClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.*;

public class TemarioSubRecyclerAdapter extends RecyclerView.Adapter<TemarioSubRecyclerAdapter.SubtemarioViewHolder> {

    private ArrayList<ObjectData> mData;
    private Context mContext;
    private OnItemListener mListener;

    public TemarioSubRecyclerAdapter(String titles, ArrayList<ObjectData> data, Context context, int index) {
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public SubtemarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kof_recycler_subsection, viewGroup, false);
        return new TemarioSubRecyclerAdapter.SubtemarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtemarioViewHolder subtemarioViewHolder, int i) {
                String tempType = "";
                boolean isHabilitado;
                ObjectData objetoActual = mData.get(i);
                ObjectData objetoAnterior = (i != 0) ? mData.get(i - 1) : null;
                assert objetoAnterior != null;

                //está habilitado si el estatus del objeto anterior es true y el objeto actual está seriado
                if(objetoActual.ismSeriado())
                    {
                        isHabilitado = (i == 0) || objetoAnterior.ismStatusSeriado();
                    }
                else
                    {
                        isHabilitado = true;
                    }

                subtemarioViewHolder.mDisablerElement.setVisibility(isHabilitado ? View.GONE : View.VISIBLE);
                subtemarioViewHolder.mDisableImage.setVisibility(isHabilitado ? View.GONE : View.VISIBLE);


                subtemarioViewHolder.title.setText(objetoActual.getObjectName());
                if(objetoActual.isDescarga())
                    {
                        subtemarioViewHolder.miniDownloadIcon.setVisibility(View.VISIBLE);
                    }
                else
                {
                    subtemarioViewHolder.miniDownloadIcon.setVisibility(View.INVISIBLE);
                }
                int tipoObjeto = Integer.parseInt(objetoActual.getType());
                switch(tipoObjeto)
                    {
                        case DOC_PDF:
                            tempType = "PDF";
                        break;
                        case EBOOK:
                            tempType = "eBook";
                        break;
                        case VEDEO:
                            tempType = "Video";
                        break;
                        case VEDEO_INTERACTIVO:
                            tempType = mContext.getResources().getString(R.string.videoInteractivo);
                        break;
                        case GIF:
                            tempType = "GIF";
                        break;
                        case DOC_WORD:
                            tempType = "Word";
                        break;
                        case EXCEL:
                            tempType = "EXCEL";
                            break;
                        case POWER_POINT:
                            tempType = "PowerPoint";
                            break;
                        case CHECKLIST:
                            tempType = "CheckList";
                            break;
                        case EVALUACION:
                            tempType = mContext.getResources().getString(R.string.evaluacion);
                            break;
                        case ENCOESTA:
                            tempType = mContext.getResources().getString(R.string.encuesta_toolbar_title);
                            break;
                        case COMIC:
                            tempType = mContext.getResources().getString(R.string.comic);
                            break;
                        case CONFERENCIA:
                            tempType = mContext.getResources().getString(R.string.videoconferencia);
                            break;
                        case VEDEO_ASESORIA_CON_EXPERTO:
                            tempType = mContext.getResources().getString(R.string.charla_con_experto);
                            break;
                        case ENLACE:
                            tempType = mContext.getResources().getString(R.string.enlace);
                            break;
                        case JUEGO:
                            tempType = mContext.getResources().getString(R.string.juegos);
                            break;
                        case JUEGO_MULTIJUGADOR:
                            tempType =  mContext.getResources().getString(R.string.juego_multijugador);
                            break;
                        default:
                            tempType = objetoActual.getType();
                    }
                subtemarioViewHolder.type.setText(tempType);
                subtemarioViewHolder.description.setText(objetoActual.getContent());
                String fullPath = RequestManager.ARCHIVO_PROGRAMA_URL + "/" + objetoActual.getImageObject();

                //Picasso.with(mContext).load(fullPath).error(R.drawable.sin_imagen).into(subtemarioViewHolder.image);

                GlideApp.with(mContext).load(fullPath).
                        centerCrop().placeholder(R.drawable.sin_imagen)
                        .apply(new RequestOptions().override(350, 400))
                        .into(subtemarioViewHolder.image);
                subtemarioViewHolder.itemView.setOnClickListener(
                        new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                if(isHabilitado)
                                    {
                                        mListener.onItemPressed(objetoActual);
                                    }
                            }
                        }
                );

                boolean mCompletado = mData.get(i).ismStatusBonus() && mData.get(i).ismStatusSeriado();


                subtemarioViewHolder.mLeido.setImageDrawable(AppTalentoRHApplication.getApplication().
                        getDrawable((mCompletado) ? R.drawable.ic_red_checkmark : R.drawable.ic_gray_checkmark));

    }

    public void setOnItemListener(TemarioSubRecyclerAdapter.OnItemListener listener)
    {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SubtemarioViewHolder extends RecyclerView.ViewHolder
    {
        TextView description, type, title;
        ImageView image, miniDownloadIcon, mLeido;
        FrameLayout mDisableImage, mDisablerElement;
        public SubtemarioViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.kof_title_rv);
            type = itemView.findViewById(R.id.kof_type_rv);
            description = itemView.findViewById(R.id.kof_description_tv);
            image = itemView.findViewById(R.id.kof_image);
            miniDownloadIcon = itemView.findViewById(R.id.kof_mini_download_button);
            mDisableImage = itemView.findViewById(R.id.kof_disabler_image);
            mDisablerElement = itemView.findViewById(R.id.kof_disabler_element);
            mLeido = itemView.findViewById(R.id.ic_mark_as_read_listado);
        }
    }

    public interface OnItemListener
    {
        void onItemPressed(ObjectData data);
    }
}
