package com.femsa.sferea.home.miCuenta.miActividad.logros;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.MiActividad.obtenerAllLogros.LogroDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogrosAdapter extends RecyclerView.Adapter<LogrosAdapter.LogrosViewHolder>{

    private ArrayList<String> mList;
    private Context mContext;
    private ArrayList<LogroDTO> mLogros;
    private boolean bandera = true;

    public LogrosAdapter(Context context, ArrayList<LogroDTO> data){
        mList = new ArrayList<>();
        mContext = context;
        mLogros = data;
    }

    /**
     * <p>Método que elimina los elementos de una lista.</p>
     */
    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public LogrosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_logros, viewGroup, false);
        return new LogrosViewHolder(view);
    }

    /**
     * <p>Método utilizado para asignar los datos de un logro a un holder.</p>
     * @param logrosViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull LogrosViewHolder logrosViewHolder, int i) {

        String fullPath = RequestManager.IMAGE_PROGRAM_BASE_URL + "/" + mLogros.get(i).getImgLogro(); //Ruta para obtener la imagen
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(mContext).load(glideUrl).error(R.drawable.sin_imagen).into(logrosViewHolder.imagen);
        logrosViewHolder.descripcion.setText(mLogros.get(i).getDescripcionLogro());

        //Verificando si se consiguió el logro
        if (mLogros.get(i).getFechaAlcanzado()==null){
            formatDate(new Date(mLogros.get(i).getFechaAlcanzado()));
        }
        else{
            logrosViewHolder.fechaAlcanzado.setVisibility(View.GONE);
            logrosViewHolder.alcanzado.setVisibility(View.GONE);
        }

        /**
         * Para validar lo de los logros
         * */
        if(mLogros.get(i).isObtenida() && mLogros.get(i).isProcesada())
            {
                logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logros_estrella_clara));
            }
        else if((!mLogros.get(i).isObtenida() && mLogros.get(i).isProcesada()) || (mLogros.get(i).isObtenida() && !mLogros.get(i).isProcesada()))
            {
                logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logro_estrella_rojo));
            bandera = false;
            }
        else if((!mLogros.get(i).isObtenida() && !mLogros.get(i).isProcesada()))
            {

                logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logros_estrella_gris));

                if(bandera)
                {logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logros_estrella_clara));}
                else
                {
                    logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logros_estrella_gris));

                }
            }
        else
            {
                logrosViewHolder.imagen.setImageDrawable(AppTalentoRHApplication.getApplication().getDrawable(R.drawable.ic_logros_estrella_clara));
            }

        logrosViewHolder.titulo.setText(mLogros.get(i).getNombreLogro());
    }

    @Override
    public int getItemCount() {
        //return mList!=null? mList.size() : 0;
        return mLogros.size();
    }

    /**
     * <p>Método que da formato a la fecha con base en un Date</p>
     */
    private String formatDate(Date date){
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
        String dateText="";
        int mes = Integer.parseInt(fecha.format(date).substring(3,4)); //Obteniendo la cadena de mes
        switch (mes){
            case 1:
                dateText = "Ene"; break;
            case 2:
                dateText = "Feb"; break;
            case 3:
                dateText = "Mar"; break;
            case 4:
                dateText = "Abr"; break;
            case 5:
                dateText = "May"; break;
            case 6:
                dateText = "Jun"; break;
            case 7:
                dateText = "Jul"; break;
            case 8:
                dateText = "Ago"; break;
            case 9:
                dateText = "Sep"; break;
            case 10:
                dateText = "Oct"; break;
            case 11:
                dateText = "Nov"; break;
            case 12:
                dateText = "Dec"; break;
        }
        return dateText;
    }

    class LogrosViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imagen;
        TextView titulo;
        TextView descripcion;
        TextView alcanzado;
        TextView fechaAlcanzado;

        public LogrosViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.iv_logros_layout_imagen);
            titulo = itemView.findViewById(R.id.tv_logros_layout_titulo);
            descripcion = itemView.findViewById(R.id.tv_logros_layout_descripcion);
            alcanzado = itemView.findViewById(R.id.tv_alcanzado);
            fechaAlcanzado = itemView.findViewById(R.id.tv_logros_layout_fecha_alcanzado);
        }
    }
}
