package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.SubtemaEspecificoDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.TemaEspecificoDTO;
import com.femsa.sferea.R;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;

import java.util.ArrayList;

public class TemaEspecificoAdapter extends RecyclerView.Adapter<TemaEspecificoAdapter.TemaEspecificoViewHolder>{

    private ArrayList<TemaEspecificoDTO> mList;
    private Context mContext;

    //Bandera con la que se determina si se debe mostrar la información acerca del tiempo sugerido de
    //la solicitud. Esta información solo se debe mostrar si la solicitud es una célula.
    private String mTipoSolicitud;

    private boolean estatusPorProgramar = false;
    private boolean rolFacilitador = false;

    public TemaEspecificoAdapter(Context context){
        mList = new ArrayList<>();
        mContext = context;
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

    public void setTipoSolicitud(String tipoSolicitud){
        mTipoSolicitud = tipoSolicitud;
    }

    public void setEstatusPorProgramar(boolean estatusPorProgramar){
        this.estatusPorProgramar = estatusPorProgramar;
    }

    public void setRolFacilitador(boolean rolFacilitador){
        this.rolFacilitador = rolFacilitador;
    }

    /**
     * <p>Método que permite agregar al Adapter el listado de temas específicos.</p>
     * @param list Listado de temas específicos.
     */
    public void addTemasEspecificos(ArrayList<TemaEspecificoDTO> list){
        mList = list;
    }

    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public TemaEspecificoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tema_especifico, viewGroup, false);
        return new TemaEspecificoViewHolder(view);
    }

    /**
     * <p>Método utilizado para asignar los datos de un tema específico a un holder.</p>
     * @param temaEspecificoViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull TemaEspecificoViewHolder temaEspecificoViewHolder, int i) {
        TemaEspecificoDTO temaEspecifico = mList.get(i);
        SubtemaEspecificoDTO subtema = temaEspecifico.getListSubtemas().get(0);
        if (temaEspecifico.getListSubtemas().size()>0) {
            temaEspecificoViewHolder.subtemas.setText(subtema.getSubtema());
            temaEspecificoViewHolder.subareaProceso.setText(subtema.getSubarea());
            //temaEspecificoViewHolder.textoTiempo.setVisibility(View.VISIBLE);
            //temaEspecificoViewHolder.tiempoSugerido.setVisibility(View.VISIBLE);
            temaEspecificoViewHolder.tiempoSugerido.setText(CelulaEntrenamiento.obtenerTiempoSugerido(subtema.getTiempo()));
        }
        temaEspecificoViewHolder.temaEspecifico.setText(temaEspecifico.getNombreTema());
        temaEspecificoViewHolder.posicionFacilitador.setText(temaEspecifico.getDescripcionPosicion());
        temaEspecificoViewHolder.nombreFacilitador.setText(temaEspecifico.getNombreFacilitador());

        //Sección de código para mostrar el tiempo sugerido solo en células de entrenamiento.
        /*if (mTipoSolicitud.equals(CelulaEntrenamiento.CELULA)){
            temaEspecificoViewHolder.textoTiempo.setVisibility(View.VISIBLE);
            temaEspecificoViewHolder.tiempoSugerido.setVisibility(View.VISIBLE);
        }*/

        //Sección de código para mostrar el tiempo sugerido solo si se es facilitador y el estatus
        //es "Por Programar"
        if (estatusPorProgramar && rolFacilitador){
            temaEspecificoViewHolder.textoTiempo.setVisibility(View.VISIBLE);
            temaEspecificoViewHolder.tiempoSugerido.setVisibility(View.VISIBLE);
        }
    }

    /**
     * <p>Método que devuelve la cantidad de items que se encuentran en una lista.</p>
     * @return El número de elementos que hay en la lista.
     */
    @Override
    public int getItemCount() {
        return mList!=null? mList.size():0;
    }

    class TemaEspecificoViewHolder extends RecyclerView.ViewHolder{

        TextView temaEspecifico;
        TextView subtemas;
        TextView subareaProceso;
        TextView posicionFacilitador;
        TextView nombreFacilitador;
        TextView textoTiempo; //TextView en el que se muestra el texto "Tiempo sugerido".
        TextView tiempoSugerido;

        public TemaEspecificoViewHolder(@NonNull View itemView) {
            super(itemView);

            temaEspecifico = itemView.findViewById(R.id.tv_tema_especifico_layout_tema_especifico);
            subtemas = itemView.findViewById(R.id.tv_tema_especifico_layout_subtema_especifico);
            subareaProceso = itemView.findViewById(R.id.tv_tema_especifico_layout_subarea_proceso);
            posicionFacilitador = itemView.findViewById(R.id.tv_tema_especifico_layout_facilitador);
            nombreFacilitador = itemView.findViewById(R.id.tv_tema_especifico_layout_nombre_facilitador);
            textoTiempo = itemView.findViewById(R.id.tv_tiempo_sugerido);
            tiempoSugerido = itemView.findViewById(R.id.tv_tema_especifico_layout_tiempo_sugerido);
        }
    }
}
