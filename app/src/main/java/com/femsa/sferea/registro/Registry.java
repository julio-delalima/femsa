package com.femsa.sferea.registro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogManager;

public class Registry extends AppCompatActivity implements RegistryView{

    private EditText mEmployeeText;
    
    private TextView mErrorMessage;
    
    Loader loader;
    
    private RegistryPresenter mPresenter;
    
    private ImageView mBackButton;

    private Button mRegistryButton;

    private String mRegistroBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        bindViews();
    }

    private void bindViews()
    {
        mRegistroBack = getIntent().getStringExtra("regreso");

       mRegistryButton = findViewById(R.id.registry_accept);
            mRegistryButton.setOnClickListener(v -> validateRegistry());
            
        mErrorMessage = findViewById(R.id.registry2_error_textview);
        
        mEmployeeText = findViewById(R.id.registry_usernumber);
            mEmployeeText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    OnEnableButton();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        
        loader = Loader.newInstance();
        
        mPresenter = new RegistryPresenter(this, new RegistryInteractor());
        
        mBackButton = findViewById(R.id.registry_back);
            mBackButton.setOnClickListener(v-> onBackPressed());

        if(mRegistroBack != null){
            mEmployeeText.setText(mRegistroBack);
            DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.usuario_inexistente_seguro));
        }
    }

    private void validateRegistry()
    {
        if(mEmployeeText.getText().toString().isEmpty())
            {
                mEmployeeText.setTextColor(getResources().getColor(R.color.femsa_red_b));
                mErrorMessage.setText(R.string.empty_fields);
            }
        else
            {
                mPresenter.OnInteractorCheckEmployeeExistance(mEmployeeText.getText().toString());
            }
    }


    private void OnEnableButton()
    {
        if(!mEmployeeText.getText().toString().isEmpty())//has text
        {
            mRegistryButton.setEnabled(true);
            mRegistryButton.setTextColor(getResources().getColor(R.color.femsa_red_b));
        }
        else
        {
            mRegistryButton.setEnabled(false);
            mRegistryButton.setTextColor(getResources().getColor(R.color.femsa_gray_c));
        }
    }

    @Override
    public void OnEmployeeExists(String employee) {
        Intent sendTo = new Intent(this, RegistryPart2.class);
        sendTo.putExtra("usuario", employee);
        sendTo.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(sendTo);
        overridePendingTransition(0,0); //0 for no animation
        finish();
    }

    @Override
    public void OnEmployeeNotExists() {
        String temp = mEmployeeText.getText().toString();
        mEmployeeText.getText().clear();
        DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.usuario_inexistente_seguro));
        mEmployeeText.setText(temp);
//        Toast.makeText(this, getResources().getText(R.string.usuario_inexistente), Toast.LENGTH_SHORT).show();
        //Intent sendto = new Intent(this, LoginActivity.class);
        //startActivity(sendto);
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void OnHideLoader() {
        try
        {
            if(loader != null && loader.isAdded())
            {
                loader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnShowLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if(!loader.isAdded())
            {
                loader.show(fm, "Loader");
            }
    }

    @Override
    public void OnUniqueLoginSuccess(UserResponseData data) {

    }

    @Override
    public void OnNoAuth() {

    }

    @Override
    public void OnDateSuccess() {

    }

    @Override
    public void OnDateFailure() {

    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
    /**
     * Método que cierra el Softkeyboard si se hace click afuera de él.
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
