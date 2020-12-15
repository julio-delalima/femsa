package com.femsa.sferea.home.celulasDeEntrenamiento.celulas;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.home.celulasDeEntrenamiento.celulas.CrearCelula.CrearCelulaUnoActivity;

public class ActivityDialog extends AppCompatActivity implements View.OnClickListener {
    // elementos que se necesitan para la tansicion
    public static final String EXTRA_CIRCULAR_REVELARX = "EXTRA_CIRCULAR_REVELARX";
    public static final String EXTRA_CIRCULAR_REVELARY = "EXTRA_CIRCULAR_REVELARY";
    private View mView;
    private int revealX;
    private int revealY;
    //elementos que presenta esta actividad
    private ImageButton btnCerrar;
    private TextView tvInduccion;
    private TextView tvCelulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        bindViews();
        bindlisteners();
        if(savedInstanceState == null && intent.hasExtra(EXTRA_CIRCULAR_REVELARX) && intent.hasExtra(EXTRA_CIRCULAR_REVELARY)){
            mView.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVELARX,0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVELARY,0);
            ViewTreeObserver viewTreeObserver = mView.getViewTreeObserver();
            if(viewTreeObserver.isAlive()){
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revelarActivity(revealX,revealY);
                    }
                });

            }else {
                mView.setVisibility(View.VISIBLE);
            }
        }
    }



    private void bindViews() {
        mView = findViewById(R.id.root_layout);
        btnCerrar = findViewById(R.id.ImBtnCerrar);
        tvInduccion = findViewById(R.id.TvInduccion);
        tvCelulas = findViewById(R.id.TvCelulas);
    }

    private void bindlisteners() {
        btnCerrar.setOnClickListener(this);
        tvInduccion.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intentI = new Intent(ActivityDialog.this, CrearCelulaUnoActivity.class);
                intentI.putExtra(CrearCelulaUnoActivity.BANDERA_CELULA_KEY, getResources().getString(R.string.Induccion));
                startActivity(intentI);
                finish();
            }
        });
        tvCelulas.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(ActivityDialog.this, CrearCelulaUnoActivity.class);
                intent.putExtra(CrearCelulaUnoActivity.BANDERA_CELULA_KEY, getResources().getString(R.string.Celula));
                startActivity(intent);
                finish();
            }
        });
    }

    protected void revelarActivity(int x, int y){
        float radio = (float)(Math.max(mView.getWidth(), mView.getHeight())*1.1);
        Animator circular = ViewAnimationUtils.createCircularReveal(mView, x, y, 0, radio);
        circular.setDuration(700);
        circular.setInterpolator(new AccelerateInterpolator());
        mView.setVisibility(View.VISIBLE);
        circular.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ImBtnCerrar:
                onBackPressed();
                break;
                default:
                    onBackPressed();

        }
    }


}
