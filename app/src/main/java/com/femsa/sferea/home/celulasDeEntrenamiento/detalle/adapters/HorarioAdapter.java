package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.home.celulasDeEntrenamiento.CelulaEntrenamiento;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.porProgramar.facilitador.confirmarHorario.Horario;

import java.util.ArrayList;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>{

    private ArrayList<Horario> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    private String mTipoSolicitud;
    private String mTituloSolicitud;
    private String mTemaEspecifico;
    private String mNombreFacilitadr;

    public HorarioAdapter(ArrayList<Horario> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        if (tipoSolicitud.equals(CelulaEntrenamiento.INDUCCION)){
            mTipoSolicitud = mContext.getString(R.string.celula_entrenamiento_item_tipo_induccion);
        } else {
            mTipoSolicitud = mContext.getString(R.string.celula_entrenamiento_item_tipo_celula_entrenamiento);
        }
    }

    public void setNombreFacilitador(String nombreFacilitador){
        mNombreFacilitadr = nombreFacilitador;
    }

    public void setTituloSolicitud(String tituloSolicitud) {
        this.mTituloSolicitud = tituloSolicitud;
    }

    public void setTemaEspecifico(String temaEspecifico) {
        this.mTemaEspecifico = temaEspecifico;
    }

    public void setListHorario(ArrayList<Horario> list){
        mList = list;
    }

    /**
     * <p>Interface que define los métodos para llevar a cabo una acción cuando se presiona un
     * elemento del listado de horas.</p>
     */
    public interface OnItemClickListener{
        void onHorarioSeleccionado(View view, Horario horario);
    }

    /**
     * <p>Método que asigna un listener a cada elemento del RecyclerView, ya que estos no contienen
     * uno por defecto.</p>
     * @param listener Listener asignado.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * <p>Método que elimina los elementos del ArrayList.</p>
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
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_horario, viewGroup, false);
        return new HorarioViewHolder(view);
    }

    /**
     * <p>Método que asigna los elementos de un horario a un holder.</p>
     * @param horarioViewHolder ViewHolder que contiene los elementos del item.
     * @param i Posición en el ArrayList.
     */
    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder horarioViewHolder, int i) {
        Horario horario = mList.get(i);

        //Cuando se llegue al último elemento del RecyclerView se va a mostrar la horaSolicitud final y se va
        //a agregar un margen inferior.
        /*if (i==mList.size()-1){
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(horarioViewHolder.contenedorExterior);
            constraintSet.connect(R.id.view_item_horario,
                    ConstraintSet.BOTTOM,
                    R.id.cl_item_horario_contenedor_general,
                    ConstraintSet.BOTTOM,64);
            constraintSet.applyTo(horarioViewHolder.contenedorExterior);

            horarioViewHolder.horaFinal.setVisibility(View.VISIBLE);
        }*/

        horarioViewHolder.itemView.setOnClickListener(v -> mListener.onHorarioSeleccionado(v, horario));

        Float hora = Float.valueOf(horario.getHora());
        String[] arreglo = horario.getHora().split("\\.");

        if(hora%1.0!=0){
            //Asignando las horas. El texto cambia dependiendo si es AM o PM.
            if (Float.valueOf(arreglo[0])<12){
                horarioViewHolder.horaSolicitud.setText(arreglo[0]+":30 AM");
            } else {
                horarioViewHolder.horaSolicitud.setText(arreglo[0]+":30 PM");
            }
        } else {
            if (hora<12){
                horarioViewHolder.horaSolicitud.setText(arreglo[0]+":00 AM");
            } else {
                horarioViewHolder.horaSolicitud.setText(arreglo[0]+":00 PM");
            }
        }

        horarioViewHolder.tipoSolicitud.setText(mTipoSolicitud);
        horarioViewHolder.nombreFacilitador.setText(mNombreFacilitadr);
        horarioViewHolder.temaEspecifico.setText(mTemaEspecifico);


        if (!horario.isDisponible()){ //Si el horario no está disponible se harán visibles las vistas.
            horarioViewHolder.temaEspecifico.setVisibility(View.VISIBLE);
            horarioViewHolder.nombreFacilitador.setVisibility(View.VISIBLE);
            horarioViewHolder.tipoSolicitud.setVisibility(View.VISIBLE);
            horarioViewHolder.temaEspecifico.setText(mList.get(i).getmTema() != null ? mList.get(i).getmTema() : "");
            horarioViewHolder.nombreFacilitador.setText(mList.get(i).getmFacilitador() != null ? mList.get(i).getmFacilitador() : "");
        }
        if(!horario.isSeleccionado())
            {
                horarioViewHolder.temaEspecifico.setVisibility(View.INVISIBLE);
                horarioViewHolder.nombreFacilitador.setVisibility(View.INVISIBLE);
                horarioViewHolder.tipoSolicitud.setVisibility(View.INVISIBLE);
                horarioViewHolder.contenedorInterior.setBackground(mContext.getResources().getDrawable(R.drawable.border_item_horario));
            }
        else
            {
                horarioViewHolder.contenedorInterior.setBackgroundColor(mContext.getResources().getColor(R.color.femsa_gray_a));
                horarioViewHolder.temaEspecifico.setVisibility(View.VISIBLE);
                horarioViewHolder.nombreFacilitador.setVisibility(View.VISIBLE);
                horarioViewHolder.tipoSolicitud.setVisibility(View.VISIBLE);
            }
    }

    /**
     * <p>Método que devuelve la cantidad de elementos que se encuentran en la lista.</p>
     * @return Número de elementos en el ArrayList.
     */
    @Override
    public int getItemCount() {
        return mList!=null? mList.size(): 0;
    }

    class HorarioViewHolder extends RecyclerView.ViewHolder{

        TextView horaSolicitud;
        TextView tipoSolicitud;
        TextView temaEspecifico;
        TextView nombreFacilitador;

        TextView horaFinal;
        ConstraintLayout contenedorExterior;
        ConstraintLayout contenedorInterior;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            horaSolicitud = itemView.findViewById(R.id.tv_item_horario_hora);
            tipoSolicitud = itemView.findViewById(R.id.tv_item_horario_tipo);
            temaEspecifico = itemView.findViewById(R.id.tv_item_horario_tema_especifico);
            nombreFacilitador = itemView.findViewById(R.id.tv_item_nombre_facilitador);

            horaFinal = itemView.findViewById(R.id.tv_item_horario_hora_dos);
            contenedorExterior = itemView.findViewById(R.id.cl_item_horario_contenedor_general);
            contenedorInterior = itemView.findViewById(R.id.cl_item_horario_contenido);
        }
    }
}
