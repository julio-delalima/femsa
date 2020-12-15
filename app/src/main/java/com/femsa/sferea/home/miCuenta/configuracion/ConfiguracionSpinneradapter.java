package com.femsa.sferea.home.miCuenta.configuracion;

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

import java.util.List;

public class ConfiguracionSpinneradapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<Idiomas> mIdiomasListado;
    private final int mResource;

    public ConfiguracionSpinneradapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects)
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

        switch (mIdiomasListado.get(position).getISO()){
            case "es":
                bamderaIcono.setImageDrawable(mContext.getResources().getDrawable(R.drawable.mexico_circle));
            break;
            case "en":
                bamderaIcono.setImageDrawable(mContext.getResources().getDrawable(R.drawable.usa_circle));
            break;
            case "pt":
                bamderaIcono.setImageDrawable(mContext.getResources().getDrawable(R.drawable.brasil_circle));
            break;
            default:
        }


        //GlideApp.with(mContext).load(RequestManager.IMAGEN_circular_PAIS +mIdiomasListado.get(position).getRutaImagen()).error(R.drawable.sin_imagen).into(bamderaIcono);
        return view;
    }
}