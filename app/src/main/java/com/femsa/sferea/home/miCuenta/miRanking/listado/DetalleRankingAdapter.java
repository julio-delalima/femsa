package com.femsa.sferea.home.miCuenta.miRanking.listado;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Paises.PaisesDTO;
import com.femsa.requestmanager.DTO.User.Ranking.RankingTierlistData;
import com.femsa.requestmanager.RequestManager;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalleRankingAdapter extends RecyclerView.Adapter<DetalleRankingAdapter.RankingViewHolder> {

    private ArrayList<RankingTierlistData> mList;

    private Context mContext;

    private OnItemClickListener mListener;

    private ArrayList<PaisesDTO.PaisData> mListadoPaises;

    private int mIDProgram;

    public DetalleRankingAdapter(Context context, ArrayList<RankingTierlistData> data, ArrayList<PaisesDTO.PaisData> listadoPaises){
        mList = new ArrayList<>();
        mListadoPaises = new ArrayList<>();
        mList = data;
        mContext = context;
        mListadoPaises.addAll(listadoPaises);
    }

    public void setOnItemClickListener(OnItemClickListener listener, int idProgram){
        mListener = listener;
        mIDProgram = idProgram;
    }


    /**
     * <p>Permite crear la vista del contenedor del elemento del ViewHolder.</p>
     * @param viewGroup Vista padre del elemento.
     * @param i Tipo de vista a mostrar.
     * @return Posición a representar en el listado.
     */
    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detalle_ranking_clasificacion, viewGroup, false);
        return new DetalleRankingAdapter.RankingViewHolder(view);
    }

    /**
     * <p>Método utilizado para asignar los elementos de un ranking a un holder.</p>
     * @param rankingViewHolder Holder contenedor del elemento listado.
     * @param i Posición a representar en el listado.
     */
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder rankingViewHolder, int i) {
            //rankingViewHolder.banderaPais;
            rankingViewHolder.clasificacion.setText(mList.get(i).getPlacement() + "");
            String fullPath = RequestManager.IMAGE_BASE_URL + "/" + mList.get(i).getPhoto();

        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

            GlideApp.with(mContext).load(glideUrl).error(R.mipmap.ic_circled_user).into(rankingViewHolder.fotoPerfil);
        rankingViewHolder.like.setOnClickListener(v->mListener.onClickCallLike(mIDProgram, mList.get(i).getEmployeeID()));
            //rankingViewHolder.nombrePais;
            rankingViewHolder.numeroCorcholatas.setText(mList.get(i).getTotalBonus() + "");
            rankingViewHolder.numeroLikes.setText(mList.get(i).getTotalLikes() + "");
            rankingViewHolder.nombre.setText(GlobalActions.cutFullName(mList.get(i).getName()));
            rankingViewHolder.like.setBackgroundColor(AppTalentoRHApplication.getApplication().getResources().getColor(R.color.femsa_white));
            rankingViewHolder.like.setEnabled(true);
            if(mList.get(i).isHasLike())
                {
                    rankingViewHolder.like.setChecked(true);
                }
            else
                {
                    rankingViewHolder.like.setChecked(false);
                }
        if(mList.get(i).getName().equals(SharedPreferencesUtil.getInstance().getNombreSP()))
            {
                rankingViewHolder.like.setEnabled(false);
            }

        for(PaisesDTO.PaisData pais : mListadoPaises)
            {
                if(mList.get(i).getLangID() == pais.getIdPais())
                    {
                        rankingViewHolder.nombrePais.setText(pais.getNombrePais());
                        GlideApp.with(mContext)
                                .load(RequestManager.IMAGEN_circular_PAIS + pais.getRutaImagen())
                                .error(R.drawable.sin_imagen)
                                .into(rankingViewHolder.banderaPais);
                        break;
                    }
            }

    }

    public void setListadoPaises(ArrayList<PaisesDTO.PaisData> paises)
        {
            mListadoPaises.clear();
            mListadoPaises.addAll(paises);
            notifyDataSetChanged();
        }

    /**
     * <p>Método que devuelve la cantidad de items que se encuentran en una lista.</p>
     * @return Número de elementos de la lista.
     */
    @Override
    public int getItemCount() {
        return mList != null ? (mList.size() > 10 ? 10 : mList.size()) : 0;
    }

    /**
     * <p>Método que elimina los elementos de una lista-</p>
     */
    public void clear(){
        if (mList!=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    class RankingViewHolder extends RecyclerView.ViewHolder{

        CircleImageView fotoPerfil;
        TextView clasificacion;
        TextView nombre;
        ImageView banderaPais;
        TextView nombrePais;
        TextView numeroCorcholatas;
        CheckBox like;
        TextView numeroLikes;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoPerfil = itemView.findViewById(R.id.civ_detalle_ranking_clasificacion_foto_perfil);
            clasificacion = itemView.findViewById(R.id.tv_detalle_ranking_clasificacion);
            nombre = itemView.findViewById(R.id.tv_detalle_clasificacion_item_nombre);
            banderaPais = itemView.findViewById(R.id.iv_detalle_clasificacion_flag);
            nombrePais = itemView.findViewById(R.id.tv_detalle_clasificacion_pais);
            numeroCorcholatas = itemView.findViewById(R.id.tv_detalle_clasificacion_numero_corcholatas);
            like = itemView.findViewById(R.id.cb_detalle_clasificacion_like);
            numeroLikes = itemView.findViewById(R.id.tv_detalle_clasificacion_numero_likes);
        }
    }

    public interface OnItemClickListener{
        void onClickCallLike(int idProgram, int idEmployee);
    }
}
