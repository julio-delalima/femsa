package com.femsa.sferea.home.celulasDeEntrenamiento.detalle.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.JefeDTO;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.detalle.facilitador.FechaDTO;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.celulasDeEntrenamiento.detalle.PRUEBA.AdministradorPaisPRUEBA;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>Adapter que permite mostrar dos tipos de vista: Jefe directo y Facilitador.</p>
 */
public class AutorizacionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    //Lista de elementos a mostrar en el RecyclerView.
    private ArrayList<Object> mList;
    private int mTotalFacilitadores = 0;
    private ArrayList<FechaDTO> mListadoFechas;
    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    //Ruta para obtener las imágenes del perfil.
    private static final String FULL_PATH = RequestManager.IMAGE_BASE_URL+"/";

    public AutorizacionAdapter(Context context, ArrayList<Object> list, ArrayList<PaisesDTO.PaisData> listadoPaises){
        mListadoPaises = new ArrayList<>();
        mContext = context;
        mList = list;
        mListadoPaises.addAll(listadoPaises);
    }

    public void setPaisesListado(ArrayList<PaisesDTO.PaisData> listadoPaises)
    {
        mListadoPaises = listadoPaises;
        notifyDataSetChanged();
    }

    public void setListadoFechasFacilitador(ArrayList<FechaDTO> fechas)
        {
            mListadoFechas = new ArrayList<>();
            mListadoFechas.addAll(fechas);
        }

    //Identificador para los tipos de elemento a mostrar.
    private final int JEFE_DIRECTO = 0;
    private final int FACILITADOR = 1;
    private final int ADMINISTRADOR = 2;

    /**
     * <p>Método que elimina los elementos de la lista-</p>
     */
    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * <p>Método que permite agregar una lista de autorizaciones al adapter.</p>
     * @param lista
     */
    public void addListaAutorizacion(ArrayList<Object> lista){
        mList = lista;
    }

    /**
     * <p>Método que asigna los datos de un Jefe Directo a un layout correspondiente.</p>
     * @param viewHolder ViewHolder del tipo de Jefe Directo.
     * @param position Posición en el RecyclerView.
     */
    private void configureJefeDirectoViewHolder(JefeDirectoViewHolder viewHolder, int position){
        JefeDTO jefe = (JefeDTO) mList.get(position);
        if (jefe!=null){
            GlideUrl glideUrl = new GlideUrl(FULL_PATH,
                    new LazyHeaders.Builder()
                            .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                            .build());
            GlideApp.with(mContext)
                    .load(glideUrl+jefe.getFotoPerfil())
                    .error(R.mipmap.ic_circled_user)
                    .into(viewHolder.fotoJefe);
            viewHolder.autorizacionJefe.setChecked(jefe.getAutorizacion());



            if(jefe.getTipoAdmin() == JefeDTO.PAIS_RECEPTOR || jefe.getTipoAdmin() == JefeDTO.PAIS_SOLICITANTE)
                {
                    switch(jefe.getTipoAdmin())
                        {
                            case JefeDTO.PAIS_RECEPTOR:
                                viewHolder.mTituloElemento.setText(R.string.adninistrador_pais_item_receptor);
                            break;
                            case JefeDTO.PAIS_SOLICITANTE:
                                viewHolder.mTituloElemento.setText(R.string.administrador_pais_item_solicitante);
                            break;
                            default:
                                viewHolder.mTituloElemento.setText(R.string.jefe_directo_item_titulo);
                        }

                    for(PaisesDTO.PaisData paisActual : mListadoPaises)
                    {
                        if(paisActual.getIdPais() == jefe.getIDPais())
                        {
                            GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(viewHolder.banderaJefe);
                            break;
                        }
                    }
                }
            else{
                for(PaisesDTO.PaisData paisActual : mListadoPaises)
                {
                    if(paisActual.getNombrePais().equals(jefe.getPais()))
                    {
                        GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(viewHolder.banderaJefe);
                        break;
                    }
                }
            }

            viewHolder.nombreJefe.setText(jefe.getNombreJefe());
            viewHolder.puestoJefe.setText(jefe.getPosicion());

            ColaboradorAdapter colaboradorAdapter = new ColaboradorAdapter(mContext, jefe.getListColaboradores());
            viewHolder.listaColaboradores.setLayoutManager(new LinearLayoutManager(mContext));
            viewHolder.listaColaboradores.setAdapter(colaboradorAdapter);
        }
    }

    /**
     * <p>Método que asigna los datos de un Facilitador a un layout correspondiente.</p>
     * @param viewHolder ViewHolder del tipo de Facilitador.
     * @param position Posición en el RecyclerView.
     */
    private void configureFacilitadorViewHolder(FacilitadorViewHolder viewHolder, int position){
        FacilitadorDTO facilitador = (FacilitadorDTO) mList.get(position);
        String fecha = "", horainicio = "", horafin = "";
        FechaDTO fechita = mListadoFechas != null ? mListadoFechas.get(mTotalFacilitadores) : null;
        mTotalFacilitadores++;
        if(fechita != null && !fechita.getFechaInicio().equals("")) //Si no esta vacia
            {
                SimpleDateFormat viejoFormato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                SimpleDateFormat viejoFormatoSinSegundos = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
                SimpleDateFormat horasFormato = new SimpleDateFormat("hh:mm", Locale.getDefault());
                try
                {
                    Date viejaFechaInicio = viejoFormato.parse(fechita.getFechaInicio());
                    Date viejaFechaFin = viejoFormato.parse(fechita.getFechaFin());
                    fecha = sdf.format(viejaFechaInicio);
                    horainicio = horasFormato.format(viejaFechaInicio);
                    horafin = horasFormato.format(viejaFechaFin);
                }
                catch (ParseException ignored)
                    {
                        try
                            {
                                Date viejaFechaInicio = viejoFormatoSinSegundos.parse(fechita.getFechaInicio());
                                Date viejaFechaFin = viejoFormatoSinSegundos.parse(fechita.getFechaFin());
                                fecha = sdf.format(viejaFechaInicio);
                                horainicio = horasFormato.format(viejaFechaInicio);
                                horafin = horasFormato.format(viejaFechaFin);
                            }
                        catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                    }

            }

        if (facilitador!=null){
            GlideUrl glideUrl = new GlideUrl(FULL_PATH,
                    new LazyHeaders.Builder()
                            .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                            .build());
            GlideApp.with(mContext)
                    .load(glideUrl+facilitador.getFotoPerfil())
                    .error(R.mipmap.ic_circled_user)
                    .into(viewHolder.fotoFacilitador);

            for(PaisesDTO.PaisData paisActual : mListadoPaises)
            {
                if(paisActual.getIdPais() == facilitador.getIdPais())
                {
                    GlideApp.with(mContext).load(RequestManager.IMAGEN_CUADRADA_PAIS + paisActual.getRutaImagen()).error(R.drawable.otros_square).into(viewHolder.banderaFaciltador);
                    break;
                }
            }

            viewHolder.nombreFacilitador.setText(facilitador.getNombreFacilitador());
            viewHolder.puestoFacilitador.setText(facilitador.getDescPosicion());
            viewHolder.temaEspecifico.setText(facilitador.getNombreTema());
            viewHolder.fechaSolicitud.setText(fecha.isEmpty() ? "" : StringManager.parseFecha(fecha, new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()), SharedPreferencesUtil.getInstance().getIdioma()));
            viewHolder.horarioSolicitud.setVisibility(horainicio.equals("") ? View.INVISIBLE : View.VISIBLE);
            viewHolder.horarioSolicitud.setText(
                    new StringBuilder(
                            mContext.getResources().getString(R.string.hrs, horainicio)));
            viewHolder.autorizacionFacilitador.setChecked(facilitador.isAutorizacion());
        }
    }

    /**
     * <p>Método que asigna los datos de un Administrador a un layout correspondiente.</p>
     * @param viewHolder ViewHolder del tipo de Administrador.
     * @param position Posición en el RecyclerView.
     */
    private void configureAdministradorViewHolder(AdministradorViewHolder viewHolder, int position){
        AdministradorPaisPRUEBA administradorPais = (AdministradorPaisPRUEBA) mList.get(position);
        if (administradorPais!=null){
            viewHolder.tipoAdministrador.setText(administradorPais.getCategoria());
            viewHolder.nombreAdministrador.setText(administradorPais.getNombre());
            viewHolder.puestoAdministrador.setText(administradorPais.getPuesto());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType){
            case JEFE_DIRECTO:
                View viewJefe = inflater.inflate(R.layout.jefe_directo_item, viewGroup, false);
                viewHolder = new JefeDirectoViewHolder(viewJefe);
                break;

            case FACILITADOR:
                View viewFacilitador = inflater.inflate(R.layout.item_facilitadores, viewGroup, false);
                viewHolder = new FacilitadorViewHolder(viewFacilitador);
                break;
            case ADMINISTRADOR:
                View viewAdministrador = inflater.inflate(R.layout.item_administrador_pais, viewGroup, false);
                viewHolder = new AdministradorViewHolder(viewAdministrador);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()){
            case JEFE_DIRECTO:
                JefeDirectoViewHolder jefeDirectoViewHolder = (JefeDirectoViewHolder) viewHolder;
                configureJefeDirectoViewHolder(jefeDirectoViewHolder, i);
                break;

            case FACILITADOR:
                FacilitadorViewHolder facilitadorViewHolder = (FacilitadorViewHolder) viewHolder;
                configureFacilitadorViewHolder(facilitadorViewHolder, i);
                break;
            case ADMINISTRADOR:
                AdministradorViewHolder administradorViewHolder = (AdministradorViewHolder) viewHolder;
                configureAdministradorViewHolder(administradorViewHolder, i);
                break;
        }
    }

    /**
     * <p>Método que regresa el tamaño de la lista.</p>
     * @return Tamaño de la lista de elementos que mostrará el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return mList!=null? mList.size(): 0;
    }

    /**
     * <p>Método que infla los diferentes tipos de vista con base en el tipo de elemento.</p>
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof JefeDTO) {
            return JEFE_DIRECTO;
        } else if (mList.get(position) instanceof FacilitadorDTO) {
            return FACILITADOR;
        } else if (mList.get(position) instanceof AdministradorPaisPRUEBA){
            return ADMINISTRADOR;
        }
        return -1;
    }

    /**
     * <p>Clase que permite inflar una vista de un Jefe Directo (jefe_directo.xml).</p>
     */
    class JefeDirectoViewHolder extends RecyclerView.ViewHolder{

        CheckBox autorizacionJefe;
        CircleImageView fotoJefe;
        ImageView banderaJefe; //Bandera que aparece en la parte inferior derecha de la foto de perfil.
        TextView nombreJefe;
        TextView puestoJefe;
        TextView mTituloElemento;
        RecyclerView listaColaboradores;
        //TextView nombreColaborador;
        //TextView puestoColaborador;

        public JefeDirectoViewHolder(@NonNull View itemView) {
            super(itemView);
            autorizacionJefe = itemView.findViewById(R.id.cb_jefe_directo_item_autorizar);
            fotoJefe = itemView.findViewById(R.id.civ_jefe_directo_item_foto);
            banderaJefe = itemView.findViewById(R.id.iv_jefe_directo_item_pais);
            nombreJefe = itemView.findViewById(R.id.tv_jefe_directo_item_nombre);
            puestoJefe = itemView.findViewById(R.id.tv_jefe_directo_item_puesto);
            listaColaboradores = itemView.findViewById(R.id.rv_jefe_directo_item);
            mTituloElemento = itemView.findViewById(R.id.tv_jefe_directo);
            //nombreColaborador = itemView.findViewById(R.id.tv_jefe_directo_item_colaborador_nombre);
            //puestoColaborador = itemView.findViewById(R.id.tv_jefe_directo_item_colaborador_puesto);
        }
    }

    /**
     * <p>Clase que permite inflar una vista de Facilitador (facilitador_item.xml).</p>
     */
    class FacilitadorViewHolder extends RecyclerView.ViewHolder{

        CheckBox autorizacionFacilitador;
        CircleImageView fotoFacilitador;
        ImageView banderaFaciltador; //Bandera que aparece en la parte inferior derecha de la foto de perfil.
        TextView nombreFacilitador;
        TextView puestoFacilitador;
        TextView temaEspecifico;
        TextView fechaSolicitud;
        TextView horarioSolicitud;

        public FacilitadorViewHolder(@NonNull View itemView) {
            super(itemView);
            autorizacionFacilitador = itemView.findViewById(R.id.cb_facilitador_item_autorizar);
            fotoFacilitador = itemView.findViewById(R.id.civ_facilitador_item_foto);
            banderaFaciltador = itemView.findViewById(R.id.iv_facilitador_item_pais);
            nombreFacilitador = itemView.findViewById(R.id.tv_facilitador_item_nombre);
            puestoFacilitador = itemView.findViewById(R.id.tv_facilitador_item_puesto);
            temaEspecifico = itemView.findViewById(R.id.tv_facilitador_item_tema_especifico);
            fechaSolicitud = itemView.findViewById(R.id.item_facilitadores_fecha);
            horarioSolicitud = itemView.findViewById(R.id.item_facilitadores_horario);
        }
    }

    /**
     * <p>Clase que permite inflar una vista de Administrador (item_administrador_pais.xml).</p>
     */
    class AdministradorViewHolder extends RecyclerView.ViewHolder{

        TextView tipoAdministrador;
        CheckBox autorizacionAdministrador;
        CircleImageView fotoAdministrador;
        ImageView banderaAdministrador;
        TextView nombreAdministrador;
        TextView puestoAdministrador;

        public AdministradorViewHolder(@NonNull View itemView){
            super(itemView);
            tipoAdministrador = itemView.findViewById(R.id.tv_item_administrador_pais_tipo);
            autorizacionAdministrador = itemView.findViewById(R.id.cb_item_administrador_pais_autorizar);
            fotoAdministrador = itemView.findViewById(R.id.civ_item_administrador_pais_foto);
            banderaAdministrador = itemView.findViewById(R.id.iv_item_administrador_pais_pais);
            nombreAdministrador = itemView.findViewById(R.id.tv_item_administrador_pais_nombre);
            puestoAdministrador = itemView.findViewById(R.id.tv_item_administrador_pais_puesto);
        }
    }
}
