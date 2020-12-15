package com.femsa.sferea.home.notificaciones.listado;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.Notificaciones.NotificacionesData;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.Utilities.SwipeRevealLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionesViewHolder> {

    private OnNotificacionListener mListener;
    private ArrayList<NotificacionesData> mData;

    public NotificacionesAdapter(ArrayList<NotificacionesData> data, OnNotificacionListener listener){
        mData = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public NotificacionesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notificacion_elemento_recycler, viewGroup, false);
        return new NotificacionesAdapter.NotificacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionesViewHolder notificacionesViewHolder, int i) {
        if(mData != null)
        {
            notificacionesViewHolder.mTitulo.setText(mData.get(i).getTituloNotificacion());

            SimpleDateFormat formatoActual = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

            SimpleDateFormat nuevoFormato = new SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.US);

            notificacionesViewHolder.mFecha.setText(StringManager.parseFecha(mData.get(i).getFecha(),formatoActual,nuevoFormato));

            notificacionesViewHolder.mHora.setVisibility(View.GONE);
            notificacionesViewHolder.mDescripcion.setText(mData.get(i).getTexto());
            notificacionesViewHolder.mIndicadorVisto.setImageDrawable(
                    (!mData.get(i).isStatus())
                            ? AppTalentoRHApplication.getApplication().getResources().getDrawable(R.color.femsa_red_b)
                            : AppTalentoRHApplication.getApplication().getResources().getDrawable(R.color.femsa_gray_b)
            );
            notificacionesViewHolder.mIndicadorImportante.setImageDrawable(
                    (mData.get(i).isImportante())
                            ? AppTalentoRHApplication.getApplication().getResources().getDrawable(R.drawable.ic_red_flag_notification)
                            : AppTalentoRHApplication.getApplication().getResources().getDrawable(R.drawable.ic_gray_flag_notification)
            );

            notificacionesViewHolder.mNotificacionContainer.setOnClickListener(v-> mListener.OnNotificacionPressed(mData.get(i)));
            notificacionesViewHolder.mFullSwipe.close(true);
        }
    }


    @Override
    public int getItemCount() {
        return (mData != null) ? mData.size() : 0;
    }

    public interface OnNotificacionListener
    {
        /***
         * Método definido en NotificacionesAdapter para cuando se haga click en cada elemento del buzón de notificaciones
         */
        void OnNotificacionPressed(NotificacionesData data);

        /**
         * Acción a ejecutar cuando se marca una notificación como importante.
         * */
        void OnNotificacionImportanteClick(int idNotificacion, int indice);

        /**
         * Se ejecuta cuando se va a eliminar una notificación
         * @param idNotificacion id de la notificación a eliminar.
         * */
        void OnEliminarNotificacion(int idNotificacion, int indice);
    }

    public void updateItem(int indice)
        {
            mData.get(indice).setImportante(!mData.get(indice).isImportante());
            notifyItemChanged(indice);
        }

    public void deleteItem(int indice)
        {
            notifyItemRemoved(indice);
            mData.remove(indice);
        }

    public class NotificacionesViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitulo, mFecha, mHora, mDescripcion;
        ImageView mIndicadorImportante;
        CircleImageView mIndicadorVisto;
        ConstraintLayout mBorrarContainer, mNotificacionContainer;
        SwipeRevealLayout mFullSwipe;

        public NotificacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitulo = itemView.findViewById(R.id.notificacion_titulo_tv);
            mFecha = itemView.findViewById(R.id.notificacion_fecha_tv);
            mHora = itemView.findViewById(R.id.notificacion_hora_tv);
            mDescripcion = itemView.findViewById(R.id.notificacion_texto_descripcion_tv);
            mIndicadorImportante = itemView.findViewById(R.id.notification_flag_indicator_iv);
                mIndicadorImportante.setOnClickListener(v-> mListener.OnNotificacionImportanteClick(mData.get(getAdapterPosition()).getIdNotificacion(), getAdapterPosition()));
            mIndicadorVisto = itemView.findViewById(R.id.notification_circle_indicator_iv);
            mBorrarContainer = itemView.findViewById(R.id.borrar_notificacion_button);
                mBorrarContainer.setOnClickListener(v-> mListener.OnEliminarNotificacion(mData.get(getAdapterPosition()).getIdNotificacion(), getAdapterPosition()));
            mFullSwipe = itemView.findViewById(R.id.full_swipe_notificacion);
            mNotificacionContainer = itemView.findViewById(R.id.notificacion_container);
            //itemView.setOnClickListener(v-> mListener.OnNotificacionPressed());
        }
    }
}
