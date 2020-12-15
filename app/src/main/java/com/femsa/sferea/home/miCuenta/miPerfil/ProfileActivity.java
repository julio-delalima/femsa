package com.femsa.sferea.home.miCuenta.miPerfil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.femsa.requestmanager.RequestManager;
import com.femsa.requestmanager.Utilities.ImageCompressorAsyncTask;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.requestmanager.Utilities.RealPath;
import com.femsa.requestmanager.Utilities.Utilities;
import com.femsa.sferea.Utilities.AppTalentoRHApplication;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.GlideApp;
import com.femsa.sferea.Utilities.GlobalActions;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.Utilities.DialogManager;


public class ProfileActivity extends AppCompatActivity implements
        BSImagePicker.OnSingleImageSelectedListener,
        ImageCompressorAsyncTask.ImageCompressorAsyncTaskInterface,
        ProfileView,
        DialogCambiarCorreo.OnCorreoDialogCambio {

    private BSImagePicker singleSelectionPicker;

    private ImageView mChangeProfilePictureButton;

    private TextView mEmployeeTV, mDescPositionTV, mContributionTV, mFunctionalAreaTV, mCenterCostTV, mCountryTV, mDBossTV, mDBNumberTV, mNameTV, mMailTV;

    private String mUserName;

    private final String FULL_NAME = "FullName";

    private ImageView mProfileImageView, mEditarPerfilButton;

    private BSImagePicker mImagePicker;

    private Uri imageUri = Uri.parse("/");

    private ImageView mBandera;

    private boolean imageChanged = false;

    private static final String FOTO_PERFIL_KEY = "fotoPerfil";

    private Bitmap mBitmap;

    private Loader mLoader;

    private ProfilePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_profile);

        getData();

        bindComponents();

        setData();

        setImage();
    }

    private void getData()
    {
        mUserName = getIntent().getStringExtra(FULL_NAME);
    }

    private void bindComponents()
    {
        Toolbar toolbar = findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        // add back arrow to toolbar
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mChangeProfilePictureButton = findViewById(R.id.my_profile_change_pic_button);
            mChangeProfilePictureButton.setOnClickListener(v -> {
                singleSelectionPicker = new BSImagePicker.Builder("com.femsa.sferea.fileprovider").build();
                singleSelectionPicker.show(getSupportFragmentManager(), "picker");
            });

        mEmployeeTV = findViewById(R.id.my_profile_employee_dataTV);

        mDescPositionTV = findViewById(R.id.my_profile_position_dataTV);

        mContributionTV = findViewById(R.id.my_profile_contribution_dataTV);

        mFunctionalAreaTV = findViewById(R.id.my_profile_funcional_area_dataTV);

        mCenterCostTV = findViewById(R.id.my_profile_denomination_dataTV);

        mCountryTV = findViewById(R.id.my_profile_country_dataTV);

        mDBossTV = findViewById(R.id.my_profile_direct_boss_dataTV);

        mDBNumberTV = findViewById(R.id.my_profile_dboss_number_dataTV);

        mBandera = findViewById(R.id.perfil_banderita);

        mNameTV = findViewById(R.id.configuracion_mi_nombre_tv);

        mMailTV = findViewById(R.id.configuracion_mi_correo_tv);

        mProfileImageView = findViewById(R.id.my_profile_profile_pic);

        mLoader = Loader.newInstance();//findViewById(R.id.my_profile_picture_loader);

        mEditarPerfilButton = findViewById(R.id.miperfil_editar_correo_button);
            mEditarPerfilButton.setOnClickListener(v -> onEditarCorreo());
        mPresenter = new ProfilePresenter(this, new ProfileInteractor());
    }

    /**
     * Método que llama al DialogFragment que permitirá editar el correo del usuario cuando éste no
     * pertenezca al dominio @kof.com.mx
     * */
    private void onEditarCorreo()
    {
        FragmentManager fm = getSupportFragmentManager();
        DialogCambiarCorreo correo = DialogCambiarCorreo.newInstance();
        correo.setListener(this);
        correo.show(fm, "ActualizarCorreo.");
    }

    /**
     * Método para colocar los datos traidos del Web Service en los elementos correspondientes.
     * */
    private void setData()
    {
        mEmployeeTV.setText(SharedPreferencesUtil.getInstance().getNumEmpleado().replace("\n", ""));
        mDescPositionTV.setText(SharedPreferencesUtil.getInstance().getDescriptionPosition());
        mContributionTV.setText(SharedPreferencesUtil.getInstance().getDescrNvContribution());
        mFunctionalAreaTV.setText(SharedPreferencesUtil.getInstance().getFunctionalArea());
        mCenterCostTV.setText(SharedPreferencesUtil.getInstance().getDescUnOrg());
        mCountryTV.setText(SharedPreferencesUtil.getInstance().getNombrePais());

        String fullPath = RequestManager.IMAGEN_circular_PAIS + "/" + SharedPreferencesUtil.getInstance().getImagenPais();
        GlideApp.with(getApplicationContext()).load(fullPath).error(R.drawable.sin_imagen).into(mBandera);


        //mCountryTV.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_mexico_flag),null, null, null);
        mDBossTV.setText(SharedPreferencesUtil.getInstance().getNombrePersonalSuperior());
        mDBNumberTV.setText(SharedPreferencesUtil.getInstance().getNumPersonalSuperior() + "");
        mNameTV.setText(SharedPreferencesUtil.getInstance().getNombreSP());
        mMailTV.setText(SharedPreferencesUtil.getInstance().getCorreo());
            if(SharedPreferencesUtil.getInstance().getCorreo().contains("@kof.com"))
                {
                    mEditarPerfilButton.setVisibility(View.INVISIBLE);
                }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)
            {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("result",imageUri.toString());
                returnIntent.putExtra("imageChanged", imageChanged);
                setResult(Activity.RESULT_OK,returnIntent);
                finish(); // close this activity and return to preview activity (if there is any)
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSingleImageSelected(final Uri uri, String tag) {
        String imageInBase64 = Utilities.convertUrlToBase64(this,RealPath.getRealPath(getBaseContext(), uri), 300,300);
        updateProfilePic(imageInBase64); //WS

        imageUri = uri;
        mProfileImageView.post(() -> {
            ImageCompressorAsyncTask task = new ImageCompressorAsyncTask("perfil", mProfileImageView.getWidth(),
                    mProfileImageView.getHeight(), ProfileActivity.this, getBaseContext());
            task.execute(uri);
        });
        imageChanged = true;
    }

    @Override
    public void onPreExecuteImage(String imageSelected) {

    }

    @Override
    public void onPostExecuteImage(String imageView, Bitmap bitmap) {
        if (mImagePicker != null)
            mImagePicker.dismiss();
        mBitmap = bitmap;
    }


    private void updateProfilePic(String image)
    {
        mPresenter.OnInteractorChangeImage(image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }

    private void setImage()
    {
        String fullPath = RequestManager.IMAGE_BASE_URL + "/" + SharedPreferencesUtil.getInstance().getFotoPerfil();
        GlideUrl glideUrl = new GlideUrl(fullPath,
                new LazyHeaders.Builder()
                        .addHeader("tokenUsuario", SharedPreferencesUtil.getInstance().getTokenUsuario())
                        .build());
        GlideApp.with(getApplicationContext()).load(glideUrl).error(R.mipmap.ic_circled_user).into(mProfileImageView);
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
            FragmentManager fm = getSupportFragmentManager();
            mLoader.show(fm, "Loader");
        }
    }

    @Override
    public void OnSuccessfulChange(String newImage) {
        SharedPreferencesUtil.getEditor().putString(FOTO_PERFIL_KEY, newImage).apply();
        mProfileImageView.setImageBitmap(mBitmap);
    }


    @Override
    public void OnNoAuth() {
        GlobalActions g = new GlobalActions(this);
        g.OnNoAuthCloseSession(SharedPreferencesUtil.getInstance().getTokenUsuario());
    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje, int codigoRespuesta) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje, codigoRespuesta);
    }

    @Override
    public void OnMostrarToast(int mensaje) {
        String message = AppTalentoRHApplication.getApplication().getResources().getString(mensaje);
        DialogFragmentManager fragment = DialogFragmentManager.newInstance(true, false, true, AppTalentoRHApplication.getApplication().getString(R.string.mensaje),
                message, AppTalentoRHApplication.getApplication().getString(R.string.ok), "", true);
        fragment.show(getSupportFragmentManager(), "Error");
    }

    @Override
    public void OnCorreoActualizado() {
    }

    @Override
    public void OnCorreoYaRegistrado() {

    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void OnNuevoCorreo(String correo) {
        mMailTV.setText(correo);
    }

    @Override
    public void OnNuevoCorreoEsKOF() {
        mEditarPerfilButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
