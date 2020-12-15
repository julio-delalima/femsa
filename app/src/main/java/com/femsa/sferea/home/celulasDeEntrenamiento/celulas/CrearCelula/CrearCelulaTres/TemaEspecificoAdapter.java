package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.CelulasDeEntrenamiento.Facilitador.FacilitadorDTO;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;

import java.util.ArrayList;

public class TemaEspecificoAdapter extends RecyclerView.Adapter<TemaEspecificoAdapter.TemaEspecificoViewHolder> implements View.OnFocusChangeListener{

    private Context mContext;
    private ArrayList<String> mTituloTemas, mHoras, mAreaProceso, mSubtemaEspecifico;
    private ArrayList<FacilitadorDTO> mFacilitadores;
    private OnTemaEspecifico mListener;
    private View view;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            InputMethodManager imm = (InputMethodManager) AppTalentoRHApplication.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken() , InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public interface OnTemaEspecifico
        {
            void onTraerFacilitadores(int indice);
            void onEliminarFacilitador(int indice);
            void onAgregarHora(int indice);
            void onAgregarSubtema(int indice, String subtema);
            void onAgregarAreaProceso(int indice, String areaProceso);
            void onAgregarTemaEspecifico(int indice, String tema);
            void onEliminarTema(int indice);
            void onPosicion(int posicion);
        }

    public void setListener(OnTemaEspecifico listener)
        {
            mListener = listener;
        }

    public TemaEspecificoAdapter(Context context) {
        mContext = context;
        mTituloTemas = new ArrayList<>();
        mHoras = new ArrayList<>();
        mFacilitadores = new ArrayList<>();
        mAreaProceso = new ArrayList<>();
        mSubtemaEspecifico = new ArrayList<>();
    }

    public void addItems(ArrayList<String> titulos, ArrayList<FacilitadorDTO> facilitadores, ArrayList<String> mSubtemas, ArrayList<String> mAreaProceso, ArrayList<String> horas) {
        mTituloTemas.addAll(titulos);
        mFacilitadores.addAll(facilitadores);
        mHoras.addAll(horas);
        mAreaProceso.addAll(mAreaProceso);
        mSubtemaEspecifico.addAll(mSubtemas);
        notifyDataSetChanged();
    }

    public void addItem(String titulo, FacilitadorDTO facilitador, String hora, String area, String subtema) {
        mTituloTemas.add(titulo);
        mFacilitadores.add(facilitador);
        mHoras.add(hora);
        mAreaProceso.add(area);
        mSubtemaEspecifico.add(subtema);
        //notifyItemInserted(mTituloTemas.size());
        notifyDataSetChanged();
    }

    public void addFacilitador(FacilitadorDTO facilitadorDTO, int indice)
        {
            mFacilitadores.set(indice, facilitadorDTO);
            notifyDataSetChanged();
        }

    public void addHoras(String hora, int indice)
        {
            mHoras.set(indice, hora);
            notifyDataSetChanged();
        }

    public void addSubtema(String subtema, int indice)
        {
            mSubtemaEspecifico.set(indice, subtema);
            notifyDataSetChanged();
        }

    public void addArea(String area, int indice)
        {
            mAreaProceso.set(indice, area);
            notifyDataSetChanged();
        }

    public void addTema(String area, int indice)
    {
        mTituloTemas.set(indice, area);
        notifyDataSetChanged();
    }

    public void removeTema(int indice)
        {
            mTituloTemas.remove(indice);
            mFacilitadores.remove(indice);
            mHoras.remove(indice);
            mAreaProceso.remove(indice);
            mSubtemaEspecifico.remove(indice);
            notifyDataSetChanged();
        }

    public ArrayList<String> getTituloTemas() {
        return mTituloTemas;
    }

    public ArrayList<String> getHoras() {
        return mHoras;
    }

    public ArrayList<String> getAreaProceso() {
        return mAreaProceso;
    }

    public ArrayList<String> getSubtemaEspecifico() {
        return mSubtemaEspecifico;
    }

    public ArrayList<FacilitadorDTO> getFacilitadores() {
        return mFacilitadores;
    }

    @NonNull
    @Override
    public TemaEspecificoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tema_especifico_cv, viewGroup, false);
        return  new TemaEspecificoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemaEspecificoViewHolder temaEspecificoViewHolder, int i) {
        mListener.onPosicion(i);

        temaEspecificoViewHolder.mRelojHoras.setOnClickListener(v-> mListener.onAgregarHora(i));
        temaEspecificoViewHolder.mHoras.setOnClickListener(v-> mListener.onAgregarHora(i));
            temaEspecificoViewHolder.mHoras.setText(mHoras.get(i));

        temaEspecificoViewHolder.mPuestoContainer.setOnClickListener(v-> mListener.onTraerFacilitadores(i));
        temaEspecificoViewHolder.anadirFacilitadorButton.setOnClickListener(v-> mListener.onTraerFacilitadores(i));

        temaEspecificoViewHolder.mEliminarTema.setOnClickListener(v-> mListener.onEliminarTema(i));

        temaEspecificoViewHolder.mEliminarFacilitador.setOnClickListener(v-> {
                    temaEspecificoViewHolder.mPuestoContainer.setVisibility(View.VISIBLE);
                    temaEspecificoViewHolder.mFacilitadorContainer.setVisibility(View.GONE);
                    mFacilitadores.set(i, null);
                    mListener.onEliminarFacilitador(i);
                });
        if(mFacilitadores.get(i) != null) //Si ya hay facilitador
            {
                temaEspecificoViewHolder.mPuestoContainer.setVisibility(View.GONE);
                temaEspecificoViewHolder.mFacilitadorContainer.setVisibility(View.VISIBLE);
                temaEspecificoViewHolder.facilitador_puesto.setText(mFacilitadores.get(i).getDescPosicion());
                temaEspecificoViewHolder.facilitador_nombre.setText(mFacilitadores.get(i).getNombreFacilitador());
            }
        else
            {
                temaEspecificoViewHolder.mPuestoContainer.setVisibility(View.VISIBLE);
                temaEspecificoViewHolder.mFacilitadorContainer.setVisibility(View.GONE);
            }

        temaEspecificoViewHolder.mTituloTemaEspecifico.setText(mTituloTemas.get(i));


        temaEspecificoViewHolder.mTituloTemaEspecifico.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListener.onAgregarTemaEspecifico(i, s + "");


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        temaEspecificoViewHolder.mSubtema.setText(mSubtemaEspecifico.get(i));
            temaEspecificoViewHolder.mSubtema.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mListener.onAgregarSubtema(i, s + "");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        //temaEspecificoViewHolder.mSubtema.setOnFocusChangeListener((v, hasFocus) -> mListener.onAgregarSubtema(i, temaEspecificoViewHolder.mSubtema.getText().toString()));

        temaEspecificoViewHolder.mSubProceso.setText(mAreaProceso.get(i));
        temaEspecificoViewHolder.mSubProceso.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListener.onAgregarAreaProceso(i, s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
          //temaEspecificoViewHolder.mSubProceso.setOnFocusChangeListener((v, hasFocus) -> mListener.onAgregarAreaProceso(i, temaEspecificoViewHolder.mSubProceso.getText().toString()));
    }

    @Override
    public int getItemCount() {
        return (mTituloTemas != null) ? mTituloTemas.size(): 0;
    }


    public class TemaEspecificoViewHolder extends RecyclerView.ViewHolder
        {
            TextView facilitador_nombre, facilitador_puesto, mHoras;
            EditText mSubtema, mSubProceso,mTituloTemaEspecifico;
            ConstraintLayout mPuestoContainer;
            CardView mFacilitadorContainer;
            ImageView mEliminarTema, mEliminarFacilitador, mRelojHoras, anadirFacilitadorButton;

            public TemaEspecificoViewHolder(@NonNull View itemView) {
                super(itemView);
                mTituloTemaEspecifico = itemView.findViewById(R.id.tema_especifico_cv);
                mSubProceso = itemView.findViewById(R.id.subproceso_tema_et);
                mSubtema = itemView.findViewById(R.id.sub_temas_especificos);
                mPuestoContainer = itemView.findViewById(R.id.container_anadir_puesto);
                mFacilitadorContainer = itemView.findViewById(R.id.container_facilitador_tema);
                mEliminarFacilitador = itemView.findViewById(R.id.facilitador_tema_eliminar_facilitador);
                mEliminarTema = itemView.findViewById(R.id.eliminar_tema_especifico);
                mHoras = itemView.findViewById(R.id.item_tema_especifico_horas);
                facilitador_nombre = itemView.findViewById(R.id.facilitador_tema_nombre);
                facilitador_puesto = itemView.findViewById(R.id.facilitador_tema_puesto);
                mRelojHoras = itemView.findViewById(R.id.item_tema_horas_reloj);
                anadirFacilitadorButton = itemView.findViewById(R.id.anadir_puesto);
            }
        }


}
