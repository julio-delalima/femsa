package com.femsa.sferea.home.inicio.programa.objetosAprendizaje.Juegos.multijugador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.Juegos.RetadorResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;

public class RetarOponenteActivity extends AppCompatActivity implements View.OnClickListener, RetarAdapter.OnClickRetador, MultiJugadorView, DialogFragmentManager.OnDialogManagerButtonActions {

    private RetarAdapter mRetarAdapter;

    private int mIDPrograma, mIdEmpleadoRetado = 0;

    private Loader loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retar_jugador);
        bindviews();
        bindData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    private void bindData(){
        MultijugadorPresenter presenter = new MultijugadorPresenter(this, new MultijugadorInteractor());
        mIDPrograma = getIntent().getIntExtra("idprograma", 0);
        presenter.onTraeListado(mIDPrograma, SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    private void bindviews(){
        mRetarAdapter = new RetarAdapter(this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        loader = Loader.newInstance();

        Toolbar mToolbar = findViewById(R.id.toolbar_retar_jugador);
        mToolbar.setTitle("");
        mToolbar.setSubtitle("");
        setSupportActionBar(mToolbar);

        RecyclerView listadoRetadores = findViewById(R.id.recycler_listado_rivales);
            listadoRetadores.setAdapter(mRetarAdapter);
            listadoRetadores.setLayoutManager(manager);

        TextView buscador = findViewById(R.id.edittext_busqueda_rivales);
            buscador.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() < 1){
                        mRetarAdapter.resetListado();
                    }
                    else{
                        mRetarAdapter.filtraListado(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
    }


    /**
     * <p>Método que se ejecuta cuando hago click sobr eun retador</p>
     *
     * @param id id del retador que elegí.
     */
    @Override
    public void onClickRetador(int id, String nombre) {
        mIdEmpleadoRetado = id;
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.reto, nombre.split(" ")[0]), getResources().getString(R.string.reto_titulo), false, this);
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void onMuestraMensaje(int tipoMensaje, int codigo) {
        DialogManager.displaySnack(getSupportFragmentManager(),tipoMensaje, codigo);
    }

    @Override
    public void onCargaListadoRetadores(RetadorResponseData data) {
        mRetarAdapter.setData(data);
    }

    @Override
    public void onSinRetadores() {
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.sin_retadores));
    }

    @Override
    public void onNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void onMuestraLoader() {
        try
        {
            FragmentManager fm = getSupportFragmentManager();
            if(loader != null && !loader.isAdded())
            {
                loader.show(fm,"loader");
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void onQuitaLoader() {
        try
        {
            if(loader != null && loader.isAdded())
            {
                loader.dismiss();
            }
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnRetoEnviado() {

    }

    @Override
    public void OnRetoAceptado() {

    }

    /**
     * Método que cierra el Softkeyboard si se hace click afuera de él.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void OnDialogAceptarClick() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("juego", JUEGO_MULTIJUGADOR);
        returnIntent.putExtra("idEmpleado", mIdEmpleadoRetado);
        setResult(Activity.RESULT_OK,returnIntent);
        //Toast.makeText(this, "Vamos a jugar con " +mIdEmpleadoRetado + " :3", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}
