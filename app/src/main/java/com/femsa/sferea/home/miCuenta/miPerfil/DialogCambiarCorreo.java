package com.femsa.sferea.home.miCuenta.miPerfil;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;

public class DialogCambiarCorreo extends DialogFragment implements ProfileView{

    private View mView;

    private Button mAcceptButton, mCancelButton;

    private EditText mMailET;

    private TextView mErrorMessage;

    private String mNuevoCorreo;

    private Loader mLoader;

    private ProfilePresenter mProfilePresenter;

    private OnCorreoDialogCambio mListener;

    public DialogCambiarCorreo() {
        super();
    }

    public interface OnCorreoDialogCambio
    {
        void OnNuevoCorreo(String correo);

        void OnNuevoCorreoEsKOF();
    }

    public void setListener(OnCorreoDialogCambio listener)
    {
        mListener = listener;
    }

    public static DialogCambiarCorreo newInstance() {
        DialogCambiarCorreo frag = new DialogCambiarCorreo();
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        assert window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.dialog_editar_correo, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mAcceptButton = mView.findViewById(R.id.forgottenpass_accept);
            mAcceptButton.setOnClickListener(v->validateEmail());

        mCancelButton = mView.findViewById(R.id.forgottenpass_cancel);
            mCancelButton.setOnClickListener(v->this.dismiss());

        mErrorMessage = mView.findViewById(R.id.forgottenpass_error);

        mMailET = mView.findViewById(R.id.forgotten_email_ET);

        mLoader = Loader.newInstance();

        mProfilePresenter = new ProfilePresenter(this, new ProfileInteractor());

        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void validateEmail()
    {
        if(!mMailET.getText().toString().isEmpty()) //campos no vacíos
        {
            if (GlobalActions.isValidEmail(mMailET.getText().toString())) //email valido
            {
                mNuevoCorreo = mMailET.getText().toString();
                mProfilePresenter.OnInteractorActualizaCorreo(mMailET.getText().toString());
            }
            else
            {
                mErrorMessage.setText(getResources().getString(R.string.invalid_mail));
                mMailET.setTextColor(getResources().getColor(R.color.femsa_red_b));
                mMailET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
            }
        }
        else // campos vacíos
        {
            mErrorMessage.setText(getResources().getString(R.string.empty_fields));
            mMailET.setTextColor(getResources().getColor(R.color.femsa_red_b));
            mMailET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
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
            if(mLoader != null && mLoader.isAdded())
            {
                mLoader.dismiss();
            }
        }
        catch (IllegalStateException ignored)
        {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
    }

    @Override
    public void OnShowLoader() {
        if(mLoader != null && !mLoader.isAdded())
        {
            FragmentManager fm = getChildFragmentManager();
            mLoader.show(fm, "Loader");
        }
    }


    @Override
    public void OnSuccessfulChange(String newImage) {

    }

    @Override
    public void OnNoAuth() {
        GlobalActions g = new GlobalActions(getActivity());
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getChildFragmentManager(), tipoMensaje, codigoRespuesta);
    }


    @Override
    public void OnMostrarToast(int mensaje) {

    }

    @Override
    public void OnCorreoActualizado() {
        this.dismiss();
        SharedPreferencesUtil.getEditor().putString(SharedPreferencesUtil.CORREO_KEY, mNuevoCorreo);
        SharedPreferencesUtil.getEditor().apply();
        mListener.OnNuevoCorreo(mNuevoCorreo);
        if(mNuevoCorreo.contains("@kof.com"))
            {
                mListener.OnNuevoCorreoEsKOF();
            }
        //Toast.makeText(getContext(), getResources().getString(R.string.correo_actualizado), Toast.LENGTH_SHORT).show();
        String message = AppTalentoRHApplication.getApplication().getResources().getString(R.string.correo_actualizado);
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.mensaje),
                message, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
        fragment.show(getChildFragmentManager(), "mensaje");
    }

    @Override
    public void OnCorreoYaRegistrado() {
        mErrorMessage.setText(getResources().getString(R.string.existing_mail));
    }
}
