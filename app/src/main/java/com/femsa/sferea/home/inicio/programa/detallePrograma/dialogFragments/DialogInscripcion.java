package com.femsa.sferea.home.inicio.programa.detallePrograma.dialogFragments;

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
import android.widget.TextView;

import com.femsa.requestmanager.Response.ObjetosAprendizaje.ObjectResponseData;
import com.femsa.requestmanager.Response.Program.ProgramDetailResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.R;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.femsa.sferea.home.inicio.programa.detallePrograma.KOFSectionInteractor;
import com.femsa.sferea.home.inicio.programa.detallePrograma.KOFSectionPresenter;
import com.femsa.sferea.home.inicio.programa.detallePrograma.KOFSectionView;

public class DialogInscripcion extends DialogFragment implements KOFSectionView {

    private View mView;

    private KOFSectionPresenter mKOFSectionPresenter;

    private Loader mLoader;

    private int mIDPrograma = 0;

    private String mTituloPrograma;

    private Button mAcceptButton, mCancelButton;

    private TextView mDescripcionDialogo;

    private OnDialogDetalleInscripcion mListener;

    public interface OnDialogDetalleInscripcion
    {
        void OnInscripcionDialogExitosa();
    }

    public void setListener(OnDialogDetalleInscripcion listener)
    {
        mListener = listener;
    }

    public DialogInscripcion() {
        super();
    }

    public static DialogInscripcion newInstance(int mIDPrograma, String tituloPrograma) {
        DialogInscripcion frag = new DialogInscripcion();
        Bundle args = new Bundle();
        args.putInt(DetalleProgramaActivity.ID_PROGRAMA_KEY, mIDPrograma);
        args.putString(DetalleProgramaActivity.TITULO_PROGRAMA_KEY, tituloPrograma);
        frag.setArguments(args);
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
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.dialog_inscribirse, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mIDPrograma = getArguments().getInt(DetalleProgramaActivity.ID_PROGRAMA_KEY);
        mTituloPrograma = getArguments().getString(DetalleProgramaActivity.TITULO_PROGRAMA_KEY);

        mAcceptButton = mView.findViewById(R.id.inscribirse_si_button);
            mAcceptButton.setOnClickListener(v->onInscripcion());

        mCancelButton = mView.findViewById(R.id.inscribirse_button_no);
            mCancelButton.setOnClickListener(v->this.dismiss());

        String tempDescrpcion = getResources().getString(R.string.inscripcion_detalle_dialogo) + " " + mTituloPrograma;
        mDescripcionDialogo = mView.findViewById(R.id.dialog_inscribir_descripcion_tv);
            mDescripcionDialogo.setText(tempDescrpcion);

        mKOFSectionPresenter = new KOFSectionPresenter(this, new KOFSectionInteractor());

        mLoader = Loader.newInstance();
        return mView;
    }

    /**
     * Método que realizarla inscripción de un usuario a un programa de aprendizaje.
     * */
    private void onInscripcion()
    {
         mKOFSectionPresenter.OnInteractorHacerInscripcion(mIDPrograma);
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


    @Override
    public void getProgramDetailSuccess(ProgramDetailResponseData data) {

    }

    @Override
    public void likeSuccess() {

    }

    /**
     * Método para ocultar el loader, e mete dentro de un try catch para atrapar la excepción de
     * Can not perform this action after onSaveInstanceState cuando se pasa la app a segundo plano antes de terminar una petición.
     * */
    @Override
    public void hideLoader() {
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
    public void showLoader() {
        if(mLoader != null && !mLoader.isAdded())
        {
            FragmentManager fm = getChildFragmentManager();
            mLoader.show(fm, "Loader");
        }
    }


    @Override
    public void getObjectsSuccess(ObjectResponseData data) {

    }

    /**
     * Muetsra un SnackBar con el mensaje de error
     *
     * @param tipoMensaje
     */
    @Override
    public void onMostrarMensaje(int tipoMensaje) {

    }

    /**
     * Muetsra un SnackBar con el mensaje de error
     *
     * @param tipoMensaje
     * @param codigoError
     */
    @Override
    public void onMostrarMensajeInesperado(int tipoMensaje, int codigoError) {

    }

    /**
     * Método a llamar cuando se realize una correcta inscrpción al programa.
     */
    @Override
    public void onInscripcionExitosa() {
        this.dismiss();
        mListener.OnInscripcionDialogExitosa();
    }

    @Override
    public void onNoAuth() {

    }

    @Override
    public void onNoInternet() {

    }
}
