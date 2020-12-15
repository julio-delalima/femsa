package com.femsa.sferea.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.registro.Registry;

public class LoginActivity extends AppCompatActivity implements LoginView
{
    private String employeeNumber, password;

    private EditText mEmployeeNumberET, mPasswordET;

    private Button forgottenPasswordBtn, mLoginButton;

    private TextView mErrorMessageTV, registryButton, registryButton2;

    private boolean loginRequired = true;

    Loader loader;

    private LoginPresenter mPresenter;

    private PasswordDialog mPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this, new LoginInteractor());
        if(! isPreferencesEnabled()) {
            bindElements();
            customizeElements();
        }
    }

    private boolean isPreferencesEnabled()
    {
        if(SharedPreferencesUtil.getInstance().isLogged())
        {
            Intent mainIntent = new Intent().setClass(LoginActivity.this, HomeActivity.class);
            startActivity(mainIntent);
            finish();
            return true;
        }
        return false;
    }

    private void bindElements()
    {
        mLoginButton = findViewById(R.id.login_btn);
        mEmployeeNumberET = findViewById(R.id.login_emplyee_ET);
            mEmployeeNumberET.setOnFocusChangeListener((v, hasFocus) -> {
                    OnEnableButton();
            });

        mPasswordET = findViewById(R.id.login_password_ET);
            mPasswordET.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    OnEnableButton();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

        mPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        forgottenPasswordBtn = findViewById(R.id.login_forgotten_Btn);
            forgottenPasswordBtn.setOnClickListener(v->{
                onContrasenaOlvidadaClick();
            });

        mErrorMessageTV = findViewById(R.id.login_errormessage_TV);
        loader = Loader.newInstance();
        registryButton = findViewById(R.id.registry_button_login);
            registryButton.setOnClickListener(v->{
                Intent sendto = new Intent(this, Registry.class);
                startActivity(sendto);
            });

        registryButton2 = findViewById(R.id.registry_button_login2);
        registryButton2.setOnClickListener(v->{
            Intent sendto = new Intent(this, Registry.class);
            startActivity(sendto);
        });
    }

    private void onContrasenaOlvidadaClick()
        {
            mPasswordDialog = PasswordDialog.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            mPasswordDialog.show(fm, "PasswordDialog");
        }

    private void customizeElements()
    {
        mLoginButton.setOnClickListener(v -> {
            if( validateLogin() )
                {
                    onLoginPressed(v);
                }
        });
    }

    private boolean validateLogin()
    {

        if(TextUtils.isEmpty(mEmployeeNumberET.getText().toString())) //empty data
            {
                displayPasswordErrorMessage(1);
                mEmployeeNumberET.setTextColor(getResources().getColor(R.color.femsa_red_b));
                mEmployeeNumberET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
            }
        if(TextUtils.isEmpty(mPasswordET.getText().toString()))
            {
                displayPasswordErrorMessage(1);
                mPasswordET.setTextColor(getResources().getColor(R.color.femsa_red_b));
                mPasswordET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
            }
        else
            {
                return true;
            }
            return false;
    }

    protected void displayPasswordErrorMessage(int errorType)
        {
            int textResource = 0;
            switch (errorType)
                {
                    case 0: //wrong password
                        textResource = R.string.wrong_password_message;
                    break;
                    case 1: //empty
                        textResource = R.string.empty_fields;
                    break;
                    case 2: //500
                        textResource = R.string.server_error;
                    break;
                    case 3: //timeout
                        textResource = R.string.timeout;
                    break;
                    case 4:
                        textResource = R.string.no_internet;
                    break;
                    case 5:
                        textResource = R.string.user_doesnt_exists;
                    break;
                    case 6:
                        textResource = R.string.blocked_user;
                    break;
                    case 7:
                        textResource = R.string.active_session;
                        break;
                    default:
                }
                //mLoginAttempts++;
            mErrorMessageTV.setText(textResource);
        }

    public void onLoginPressed(View view)
        {
            if (loginRequired)
                {
                    employeeNumber = mEmployeeNumberET.getText() + "";
                    password = mPasswordET.getText() + "";
                    mPresenter.OnInteractorDoLogin(employeeNumber, password);
                }
            else
                {
                  startActivity(new Intent(this, HomeActivity.class));
                }
        }

    private void OnEnableButton()
    {
        if(!mPasswordET.getText().toString().isEmpty() && !mEmployeeNumberET.getText().toString().isEmpty())//has text
            {
                mLoginButton.setEnabled(true);
                mLoginButton.setTextColor(getResources().getColor(R.color.femsa_red_b));
            }
        else
            {
                mLoginButton.setEnabled(false);
                mLoginButton.setTextColor(getResources().getColor(R.color.femsa_gray_c));
            }
    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void OnHideLoader() {
        try
        {
            loader.dismiss();
        }
        catch (Exception ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnShowLoader() {
        FragmentManager fm = getSupportFragmentManager();
        if (!loader.isAdded())
        {
            loader.show(fm, "Loader");
        }
    }

    @Override
    public void OnSuccessFulLogin(UserResponseData usuarioDTO) {
        if(usuarioDTO.getmUser().isStatusCredencial())
            {
                Intent mainIntent = new Intent().setClass(LoginActivity.this, HomeActivity.class);
                SharedPreferencesUtil.getInstance().createLoginSession(usuarioDTO.getmUser());
                startActivity(mainIntent);
                finish();
            }
        else
            {
                onContrasenaOlvidadaClick();
                GlobalActions g = new GlobalActions(this);
                g.setIrALogin(false);
                g.OnNoAuthCloseSession(usuarioDTO.getmUser().getmTokenUsuario());
            }
    }

    @Override
    public void OnWrongCredentials() {
        displayPasswordErrorMessage(0);
    }

    @Override
    public void OnActiveSession() {
        displayPasswordErrorMessage(0);
    }

    @Override
    public void OnSuccessMail() {

    }

    @Override
    public void OnFailMail() {

    }

    @Override
    public void OnBlocked() {
        displayPasswordErrorMessage(0);
    }

    @Override
    public void OnShowMessage(int messageType) {
        displayPasswordErrorMessage(messageType);
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
