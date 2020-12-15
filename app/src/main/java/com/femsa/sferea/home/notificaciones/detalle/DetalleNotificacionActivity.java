package com.femsa.sferea.home.notificaciones.detalle;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.femsa.requestmanager.DTO.User.Notificaciones.NotificacionesData;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.FirebaseMessagingPush;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.StringManager;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.femsa.sferea.home.notificaciones.NotificacionesFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.femsa.sferea.Utilities.FirebaseMessagingPush.RETADOR_ID_KEY;
import static com.femsa.sferea.Utilities.FirebaseMessagingPush.RETADOR_NOMBRE_KEY;
import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_OBJECT_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.TIPO_OBJETO_KEY;

public class DetalleNotificacionActivity extends AppCompatActivity {

    private TextView mRemitenteNombreTV, mHoraTV, mDestinatarioTV, mDescripcionTV, mTituloNotificacionTV;
    private CircleImageView mRemitenteFotoCIV;
    private Button mAutorizarBTN;
    private Toolbar mToolbar;
    private NotificacionesData mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notificacion);
        bindViews();
        getData();
    }

    private void bindViews()
        {
            mRemitenteNombreTV = findViewById(R.id.detalle_notificacion_remitente_tv);
            mHoraTV = findViewById(R.id.detalle_notificacion_hora_tv);
            mDestinatarioTV = findViewById(R.id.detalle_notificacion_destinatario_tv);
            mDescripcionTV = findViewById(R.id.detalle_notificacion_descripcion_tv);
            mTituloNotificacionTV = findViewById(R.id.detalle_notificacion_titulonotif_tv);
            mRemitenteFotoCIV = findViewById(R.id.detalle_notificacion_foto_remitente_civ);
            mAutorizarBTN = findViewById(R.id.detalle_notificacion_aceptar_bn);
            mToolbar = findViewById(R.id.detalle_notificacion_toolbar);
            initializeToolbar();
        }


    private void getData()
        {
            mData = (NotificacionesData) getIntent().getSerializableExtra(NotificacionesFragment.DETALLE_NOTIFICACION_DTO);
            if(mData != null)
                {
                    //Si es celula
                    mDescripcionTV.setText(mData.getTexto());
                    mAutorizarBTN.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            irACelula(mData.getiSolicitud());
                        }
                    });

                    mAutorizarBTN.setVisibility(mData.getiSolicitud() < 1 ? View.GONE : View.VISIBLE);

                    SimpleDateFormat formatoActual = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

                    SimpleDateFormat nuevoFormato = new SimpleDateFormat("hh:mm a", Locale.US);

                    mHoraTV.setText(StringManager.parseFecha(mData.getFecha(),formatoActual,nuevoFormato));

                    mTituloNotificacionTV.setText(mData.getTituloNotificacion());

                    //Si es juego
                    if(mData.getIdObjeto() > 0 && mData.getIdPrograma() > 0){
                        mAutorizarBTN.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                irAJuego(mData);
                            }
                        });
                        mAutorizarBTN.setText(getResources().getString(R.string.retar_label));
                        mAutorizarBTN.setVisibility(View.VISIBLE);
                    }
                }
        }

    /**
     * Método que manda al detalle de una célula en específico a través de su ID.
     * @param idSolicitud ID de la célula o inducción que se va a abrir.
     * */
    private void irACelula(int idSolicitud)
        {
            Intent irACelula = new Intent(getApplication(), HomeActivity.class);
            irACelula.putExtra("Celulas", true);
            irACelula.putExtra(FirebaseMessagingPush.ID_SOLICITUD, idSolicitud);
            startActivity(irACelula);
        }

    /**
     * Método que manda a la activity del juego de Gusanos y Escaleras mediante el ID
     * @param data Datos de la notificación de los que se extraerán los IDs necesarios para el juego.
     * */
    private void irAJuego(NotificacionesData data)
    {
        Intent intent = new Intent(this, ObjetoAprendizajeActivity.class);
        intent.putExtra(ID_OBJECT_KEY, data.getIdObjeto());
        intent.putExtra(ID_PROGRAMA_KEY, data.getIdPrograma());
        intent.putExtra(RETADOR_NOMBRE_KEY, data.getNombre());
        intent.putExtra(RETADOR_ID_KEY, data.getIdEmpleadoRetador());
        intent.putExtra(TIPO_OBJETO_KEY, JUEGO_MULTIJUGADOR);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    /**
     * <p>Método que permite configurar la toolbar de la Activity.</p>
     */
    private void initializeToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
