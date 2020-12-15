package com.femsa.sferea.Login;

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

import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.DialogManager;

public class PasswordDialog extends DialogFragment implements LoginView, DialogFragmentManager.OnDialogManagerButtonActions{

    private View mView;

    private Button mAcceptButton, mCancelButton;

    private EditText mMailET;

    private TextView mErrorMessage;

    private LoginPresenter mPresenter;

    private Loader mLoader;


    public PasswordDialog() {
        super();
    }

    public static PasswordDialog newInstance() {
        PasswordDialog frag = new PasswordDialog();
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
        mView = inflater.inflate(R.layout.dialog_layout_password, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mAcceptButton = mView.findViewById(R.id.forgottenpass_accept);
            mAcceptButton.setOnClickListener(v->{validateEmail();});

        mCancelButton = mView.findViewById(R.id.forgottenpass_cancel);
            mCancelButton.setOnClickListener(v->{this.dismiss();});

        mErrorMessage = mView.findViewById(R.id.forgottenpass_error);

        mMailET = mView.findViewById(R.id.forgotten_email_ET);

        mPresenter = new LoginPresenter(this, new LoginInteractor());

        mLoader = Loader.newInstance();

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
                mPresenter.OnInteractorDoMail(mMailET.getText().toString());
            }
            else
                {
//                    mErrorMessage.setText(getResources().getString(R.string.correo_ambiguo));
//                    mMailET.setTextColor(getResources().getColor(R.color.femsa_red_b));
//                    mMailET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
                    DialogManager.displaySnack(getChildFragmentManager(), getResources().getString(R.string.correo_ambiguo), false, this);
                }
        }
        else // campos vacíos
            {
                mErrorMessage.setText(getResources().getString(R.string.empty_fields));
                mMailET.setTextColor(getResources().getColor(R.color.femsa_red_b));
                mMailET.setHintTextColor(getResources().getColor(R.color.femsa_red_b));
            }
    }

    @Override
    public void OnHideLoader() {
        mLoader.dismiss();
    }

    @Override
    public void OnShowLoader() {
        FragmentManager fm = getChildFragmentManager();
        mLoader.show(fm, "Loader");
    }

    @Override
    public void OnSuccessFulLogin(UserResponseData usuarioDTO) {

    }

    @Override
    public void OnWrongCredentials() {

    }

    @Override
    public void OnActiveSession() {

    }

    @Override
    public void OnSuccessMail() {
        DialogManager.displaySnack(getChildFragmentManager(), getResources().getString(R.string.correo_ambiguo), false, this);
        //DialogManager.displaySnack(getChildFragmentManager(), getResources().getString(R.string.successful_password_recovery), false, this);
      //  this.dismiss();
    }

    @Override
    public void OnFailMail() {
        DialogManager.displaySnack(getChildFragmentManager(), getResources().getString(R.string.correo_ambiguo), false, this);
        //mErrorMessage.setText(getResources().getString(R.string.correo_ambiguo));
    }

    @Override
    public void OnBlocked() {    }

    @Override
    public void OnShowMessage(int messageType) {    }

    @Override
    public void OnDialogAceptarClick() {
        this.dismiss();
    }

    @Override
    public void OnDialogCancelarClick() {

    }
}
