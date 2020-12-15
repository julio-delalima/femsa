package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Enlace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.femsa.sferea.R;

/**
 * <p>Clasde que contiene el detalle del enlace. Se muestra la imagen, título, corcholatas, descripción, etc.</p>
 */
public class EnlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mIvImagen; //Imagen del objeto de aprendizaje.
    private ImageView mIvAccion; //Imagen que permite iniciar el objeto de aprendizaje al ser presionada.
    private TextView mTvNombre;
    private ImageView mIvCorazon; //Icono para indicar que te gusta el objeto de aprendizaje.
    private TextView mTvNumeroCorcholatas;
    private TextView mTvTiempoEstimado;
    private TextView mTvDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlace);
        bindViews();
        initializeToolbar();
        bindListeners();
    }

    private void bindViews(){
        mToolbar = findViewById(R.id.toolbar_activity_enlace);
        mIvImagen = findViewById(R.id.iv_activity_enlace_imagen);
        mIvAccion = findViewById(R.id.iv_activity_enlace_accion);
        mTvNombre = findViewById(R.id.tv_activity_enlace_nombre);
        mIvCorazon = findViewById(R.id.iv_activity_enlace_like);
        mTvNumeroCorcholatas = findViewById(R.id.tv_activity_enlace_corcholatas);
        mTvTiempoEstimado = findViewById(R.id.tv_activity_enlace_tiempo_estimado);
        mTvDescripcion = findViewById(R.id.tv_activity_enlace_descripcion);
    }

    private void bindListeners(){
        mIvAccion.setOnClickListener(this);
        mIvCorazon.setOnClickListener(this);
    }

    /**
     * <p>Método que permite configurar la Toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    /**
     * <p>Método que genera una animación y un Toast al indicar que te gusta el objeto de aprendizaje.</p>
     */
    private void darLike() {
        mIvCorazon.animate().scaleX(0.2f);
        mIvCorazon.animate().scaleY(0.2f);
        //Toast.makeText(this, "Me gusta", Toast.LENGTH_SHORT).show();
        mIvCorazon.animate().scaleX(0.7f);
        mIvCorazon.animate().scaleY(0.7f);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_activity_enlace_accion: //Iniciar objeto de aprendizaje.
                Intent intent = new Intent(this, EnlaceWebViewActivity.class);
                intent.putExtra("url", "https://stackoverflow.com/");
                startActivity(intent);
                break;
            case R.id.iv_activity_enlace_like: //Indicar que te gusta el objeto de aprendizaje.
                darLike();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
