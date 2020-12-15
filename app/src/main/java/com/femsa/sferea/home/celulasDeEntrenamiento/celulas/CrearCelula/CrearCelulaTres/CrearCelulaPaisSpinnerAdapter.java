package com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaTres;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.home.miCuenta.configuracion.Idiomas;

import java.util.List;

public class CrearCelulaPaisSpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<Idiomas> mIdiomasListado;
    private final int mResource;

    public CrearCelulaPaisSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects)
    {
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mIdiomasListado = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView nombreIdioma = view.findViewById(R.id.item_spinner_nombre_idioma);
        ImageView bamderaIcono = view.findViewById(R.id.item_spinner_bandera);

        Idiomas mIdiomaActual = mIdiomasListado.get(position);

        nombreIdioma.setText(mIdiomaActual.getNombreIdioma());

        int banderaImagen = -1;
        switch(mIdiomaActual.getBandera())
            {
                case 0:
                    banderaImagen = R.drawable.mexico_square;
                    break;
                case 1:
                    banderaImagen = R.drawable.colombia_square;
                    break;
                case 2:
                    banderaImagen = R.drawable.brasil_square;
                    break;
                case 3:
                    banderaImagen = R.drawable.otros_square;
                    break;
            }

        bamderaIcono.setImageDrawable(mContext.getResources().getDrawable(banderaImagen));
        return view;
    }
}