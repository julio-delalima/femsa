package com.femsa.sferea.home.inicio.componentes.dialogFragments;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.CuzYouSawResponse;
import com.femsa.requestmanager.Response.Home.MostSeenResponse;
import com.femsa.requestmanager.Response.Home.ProgramsResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.componentes.fragment.FragmentHomeInteractor;
import com.femsa.sferea.home.inicio.componentes.fragment.FragmentHomePresenter;
import com.femsa.sferea.home.inicio.componentes.fragment.FragmentHomeView;

public class UpdateDataDialog extends DialogFragment implements FragmentHomeView {

    private View mView;

    private Button mAcceptButton;

    private ImageView mPassReqsButton;

    private EditText mPassword, mPassConfirm, mMail;

    private TextView mError;

    private FragmentHomePresenter mPresenter;

    private Loader mLoader;

    private boolean isKOF = false;

    private OnPasswordSuccessDialog mListener;

    public UpdateDataDialog() {
        super();
    }

    public static UpdateDataDialog newInstance() {
        return new UpdateDataDialog();
    }

    public interface OnPasswordSuccessDialog
    {
        void successfulPassword();
    }

    public void setListener(OnPasswordSuccessDialog listener)
    {
        mListener = listener;
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
        mView = inflater.inflate(R.layout.dialog_firstlogin_second, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bindViews();
        initData();
        return  mView;
    }

    private void bindViews()
    {
        mLoader = Loader.newInstance();
        mPresenter = new FragmentHomePresenter(this, new FragmentHomeInteractor());
        mMail = mView.findViewById(R.id.email_first_login);
        mPassword = mView.findViewById(R.id.firstlogin_password);
        mPassConfirm = mView.findViewById(R.id.firstlogin_confirm);
        mPassReqsButton = mView.findViewById(R.id.pass_reqs_button);
            mPassReqsButton.setOnClickListener(v->showReqsDialog());
        mError = mView.findViewById(R.id.firstlogin_errormessage);
        mAcceptButton = mView.findViewById(R.id.first_login_accept);
            mAcceptButton.setOnClickListener(v->checkPassword());
    }

    private  void initData()
    {
        String tempMail = SharedPreferencesUtil.getInstance().getCorreo();
        if(tempMail.contains("@kof.com")) //.contains("@kof.com.mx")
            {
                mMail.setText(tempMail);
                mMail.setEnabled(false);
                isKOF = true;
            }
        else if(tempMail.length() > 0) //otro mail, gmail, etc
            {
                mMail.setText(tempMail);
            }
    }

    private void showReqsDialog(){
        ToolTipDialog mDialog = ToolTipDialog.newInstance();
        FragmentManager fm = getChildFragmentManager();
        mDialog.show(fm, "NewDialog3");
    }

    private void checkPassword()
    {
        String pass = mPassword.getText().toString();
        String confirmPass = mPassConfirm.getText().toString();
        String mail = mMail.getText().toString();
        if(pass.isEmpty() || confirmPass.isEmpty() || mail.isEmpty()) //empty fields
            {
                mError.setText(R.string.empty_fields);
            }
        else
            {
                if(pass.equals(confirmPass))
                    {
                        mError.setText("");
                        if(GlobalActions.isValidEmail(mail))
                            {
                                mail = (isKOF) ? "" : mail;
                                if(!isKOF)
                                    {
                                        SharedPreferencesUtil.getEditor().putString("correo", mail);
                                        SharedPreferencesUtil.getEditor().apply();
                                    }
                                mPresenter.OnInteractorCallChange(pass, SharedPreferencesUtil.getInstance().getTokenUsuario(), mail);
                            }
                        else
                            {
                                mError.setText(getResources().getString(R.string.invalid_mail));
                            }

                    }
                else
                    {
                        mError.setText(R.string.password_match);
                    }
            }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onLogOutSuccess() { //401
        mError.setText(R.string.wrong_pattern);
    }

    @Override
    public void getMostSeenSuccess(MostSeenResponse data) {

    }

    @Override
    public void getCuzYouSawSuccess(CuzYouSawResponse data) {

    }

    @Override
    public void getAllProgramsSuccess(ProgramsResponse data) {

    }

    @Override
    public void onShowLoader() {
        FragmentManager fm = getChildFragmentManager();
        mLoader.show(fm, "Loader");
    }

    @Override
    public void onHideLoader() {
        mLoader.dismiss();
    }

    /**
     * Método que le indica a la actividad principal que el cambio de contraseña durante el registro fue exitoso y cierra el cuadro de diálogo.
     * */
    @Override
    public void onPasswordSuccess() {
        mListener.successfulPassword();
        this.dismiss();
    }

    /**
     * Método que se ejecuta cuando se obtienen resultados del buscador de home
     *
     * @param data
     */
    @Override
    public void OnBrowserSuccess(ProgramsResponse data) {

    }

    @Override
    public void onShowMessage(int type) {

    }

    @Override
    public void onMostrarMensajeInesperado(int tipoMensaje, int codigoRespuesta) {

    }

    @Override
    public void getSubcategoriesSuccess(SubcategoriesResponse data) {

    }

    @Override
    public void getProgramsSubCategorySuccess(ProgramsResponse data) {

    }

    @Override
    public void getCategoriesSuccess(CategoriesResponse data) {

    }

    @Override
    public void onNoHayProgramas() {

    }

    @Override
    public void onPorqueVisteVacío() {

    }

    @Override
    public void OnPasswordFail() {
        Toast.makeText(getContext(), getResources().getText(R.string.server_error), Toast.LENGTH_SHORT).show();
        this.dismiss();
        GlobalActions g = new GlobalActions(null);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

}
