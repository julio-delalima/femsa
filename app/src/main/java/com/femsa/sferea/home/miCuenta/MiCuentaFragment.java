package com.femsa.sferea.home.miCuenta;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.ImageCompressorAsyncTask;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.miCuenta.configuracion.ConfiguracionActivity;
import com.femsa.sferea.home.miCuenta.miActividad.MiActividadActivity;
import com.femsa.sferea.home.miCuenta.miPerfil.ProfileActivity;
import com.femsa.sferea.home.miCuenta.miRanking.RankingActivity;
import com.femsa.sferea.home.miCuenta.misRecompensas.listado.MisRecompensasActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiCuentaFragment extends Fragment implements  ImageCompressorAsyncTask.ImageCompressorAsyncTaskInterface{

    private View mView;

    private LinearLayout optionContainer;

    private int numButtons = 5;

    private Button[] options;

    private String MAIL_KEY = "mail", PROFILE_PIC = "profilePicture", FULL_NAME = "FullName", BASE_URL = "", TOKEN_USER_KEY = "tokenUsuario";
    private String mFullname, mFullProfilePicPath, mEmail, mTokenUser;

    private ImageView mProfileImage, mNavheaderProfile, mBandera;

    private TextView mMyName, mMyMail;

    private Toolbar mToolbar;

    private OnMyAccountListener mListener;

    public interface OnMyAccountListener
    {
        void setMyAccountMainView();
    }


    public static MiCuentaFragment newInstance() {

        Bundle args = new Bundle();
        MiCuentaFragment fragment = new MiCuentaFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public MiCuentaFragment() {
        // Required empty public constructor
    }

    public void setListener(OnMyAccountListener listener)
    {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mListener != null)
            {
                mListener.setMyAccountMainView();
            }
        initData();
        options = new Button[numButtons];
        mView = inflater.inflate(R.layout.fragment_my_account, container, false);
        optionContainer = mView.findViewById(R.id.my_profile_container);
        mNavheaderProfile = getActivity().findViewById(R.id.header_profile_picture);
        mMyMail = mView.findViewById(R.id.configuracion_mi_correo_tv);
        mMyName = mView.findViewById(R.id.configuracion_mi_nombre_tv);
        mProfileImage = mView.findViewById(R.id.configuracion_foto_perfil_civ);
        mBandera = mView.findViewById(R.id.configuracion_bandera_iv);
        setData(mEmail, mFullname);
        createOptions();
        return mView;
    }

    private void initData()
    {
        Bundle args = getArguments();
        mFullname = args.getString(FULL_NAME);
        mEmail = SharedPreferencesUtil.getInstance().getCorreo();
        mTokenUser = SharedPreferencesUtil.getInstance().getTokenUsuario();
    }

    private void setData(String mail, String name)
    {
        mMyMail.setText(mail);
        mMyName.setText(SharedPreferencesUtil.getInstance().getNombreSP());
        setImage();
    }

    private boolean onOptionSelected(int index)
    {
        switch(index)
        {
            case 0://perfil
               Intent gotoProfile = new Intent(getContext(), ProfileActivity.class);
                   gotoProfile.putExtra(FULL_NAME, mFullname);
                   startActivityForResult(gotoProfile, 1);
            break;
            case 1: //ranking
               Intent gotoRanking = new Intent(getContext(), RankingActivity.class);
                    startActivityForResult(gotoRanking, 1);
                break;
            case 2: //actividad
                startActivity(new Intent(getContext(), MiActividadActivity.class));
                break;
            case 3: //Mis Recompensas
                Intent goToMisRecompensas = new Intent(getContext(), MisRecompensasActivity.class);
                startActivity(goToMisRecompensas);
                break;
            case 4: //settings
                startActivity(new Intent(getContext(), ConfiguracionActivity.class));
                break;
        }
        return true;
    }


    public void createOptions()
    {
        int[] icons = {R.mipmap.ic_circled_user, R.mipmap.ic_circled_trophy2, R.mipmap.ic_circled_stats, R.mipmap.ic_circled_rewards, R.mipmap.ic_circled_setting};
        int[] titles = {R.string.my_profile , R.string.my_profile_ranking, R.string.my_profile_stats, R.string.my_profile_rewards, R.string.my_profile_settings};

        for(int i = 0; i < numButtons; i++)
        {
            options[i] = new Button(getContext());
            options[i].setText(getResources().getText(titles[i]));
            options[i].setId(i);
            options[i].setCompoundDrawablesWithIntrinsicBounds(icons[i],0,0,0);
            options[i].setAllCaps(false);
            options[i].setBackground(null);
            options[i].setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onOptionSelected(v.getId());
            }});
            optionContainer.addView(customizeButton(options[i]));
        }
    }

    public Button customizeButton(Button btn)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 10);
        btn.setPadding(0,-10,0,-10);
        btn.setLayoutParams(params);
        btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btn.setCompoundDrawablePadding(10);
        btn.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.shape_rounded_button));
        btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        return btn;
    }

    private void updateProfilePics(final Uri uri)
    {
        String fullpath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();

        GlideUrl glideUrl = new GlideUrl(fullpath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(getContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mProfileImage);
        GlideApp.with(getContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mNavheaderProfile);
        /*
        mProfileImage.post(() -> {
            ImageCompressorAsyncTask task = new ImageCompressorAsyncTask("perfil", mProfileImage.getWidth(),
                    mProfileImage.getHeight(), MiCuentaFragment.this, getContext());
            task.execute(uri);

        });|
        mNavheaderProfile.post(() -> {
            ImageCompressorAsyncTask task = new ImageCompressorAsyncTask("perfil", mNavheaderProfile.getWidth(),
                    mNavheaderProfile.getHeight(), MiCuentaFragment.this, getContext());
            task.execute(uri);

        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
/*
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK)
                {
                    final String result = data.getStringExtra("result");
                    boolean dataChanged = data.getBooleanExtra("imageChanged", false);
                    if(dataChanged)
                        {
                            updateProfilePics(Uri.parse(result));
                        }
                }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }*/
    }

    /**
     * <p>Método de la interface que se ejecuta antes de realizar la compresión de imagenes.</p>
     *
     * @param imageSelected Imagenes seleccionadas.
     */
    @Override
    public void onPreExecuteImage(String imageSelected) {

    }

    /**
     * <p>Método de la interface que se ejecuta después de realizar la compresión de imagenes.</p>
     *
     * @param imageView Imagenes seleccionadas.
     * @param bitmap    Bitmap creado.
     */
    @Override
    public void onPostExecuteImage(String imageView, Bitmap bitmap) {
        mNavheaderProfile.setImageBitmap(bitmap);
        mProfileImage.setImageBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        setImage();
        updateProfilePics(null);
        mMyMail.setText(SharedPreferencesUtil.getInstance().getCorreo());
    }

    private void setImage()
    {
        String fullpath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();
        GlideUrl glideUrl = new GlideUrl(fullpath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());

        GlideApp.with(getContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mProfileImage);
        String fullPath = RequestManager.IMAGEN_circular_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBandera);
    }
}
