package com.femsa.sferea.registro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.femsa.requestmanager.Response.Login.UserResponseData;
import com.femsa.requestmanager.Utilities.Loader;
import com.femsa.sferea.Login.LoginActivity;
import com.femsa.sferea.R;
import com.femsa.sferea.Utilities.DialogFragmentManager;
import com.femsa.sferea.Utilities.DialogManager;
import com.femsa.sferea.Utilities.OnSingleClickListener;
import com.femsa.sferea.Utilities.SharedPreferencesUtil;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.home.legal.LegalActivity;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RegistryPart2 extends AppCompatActivity implements RegistryView,
        DatePickerDialog.OnDateSetListener, DialogFragmentManager.OnDialogManagerButtonActions {

    private Button mEditTextDate, mNextButton;

    private CheckBox mCheckBoxTerms;

    private TextView mErrorTextview;

    Loader loader;

    private RegistryPresenter mPresenter;

    private String employeeNumber, dateInverseFormat = "aaaa-mm-dd";

    private UserResponseData mData;

    private ImageView mBackButton;

    private int mYear = 1980, mDay = 1, mMonth = 0;

    private boolean mTerminosAceptados = false;

    private Button termsAndConditions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        employeeNumber = getIntent().getStringExtra("usuario");
        initPresenter();
    }

    private void initPresenter()
    {
            mPresenter = new RegistryPresenter(this, new RegistryInteractor());
            mPresenter.OnInteractorDoUniqueLogin(employeeNumber);
    }

    private void bindViews()
    {
        mNextButton = findViewById(R.id.registry2_next);
            mNextButton.setOnClickListener(v -> validateDate());

        mEditTextDate = findViewById(R.id.registry_usernumber);
            mEditTextDate.setOnClickListener(v -> showDate(mYear, mMonth, mDay, R.style.NumberPickerStyle));
            mEditTextDate.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    OnEnableButton();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

        termsAndConditions = findViewById(R.id.termsAndConditions);
            termsAndConditions.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            openLegalActivity();
                        }
                    });

        mCheckBoxTerms = findViewById(R.id.registry2_terms_checkbox);
            mCheckBoxTerms.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    openLegalActivity();
                }
            });

        mErrorTextview = findViewById(R.id.registry2_error_textview);

        loader = Loader.newInstance();

        mBackButton = findViewById(R.id.registry2_back);
            mBackButton.setOnClickListener(v-> {onBackPressed();
            finish();});

    }

    private void validateDate()
    {
        if(mCheckBoxTerms.isChecked() && mTerminosAceptados)
            {
                String date = mEditTextDate.getText().toString();
                if(date.equals("dd/mmm/aaaa"))
                {
                    mEditTextDate.setTextColor(getResources().getColor(R.color.femsa_red_b));
                    mErrorTextview.setText(R.string.empty_fields);
                }
                else
                {
                    mPresenter.OnInteractorCheckDate(dateInverseFormat, mData.getmUser().getmIdEmpleado());
                }
            }
        else
            {
                mErrorTextview.setText(R.string.accept_first);
            }

    }

    private String buildDate(int day, int month, int year)
    {
        mDay = day;
        mYear = year;
        mMonth = month;

        String date = day + "/";
        int[] monthName = {R.string.january, R.string.february, R.string.march, R.string.april,
                R.string.may, R.string.june, R.string.july , R.string.august,
                R.string.september, R.string.october, R.string.november, R.string.december};
        date += getResources().getString(monthName[month]).substring(0, 3) + "/" + year;

        String monthnum = (month+1 > 9) ? (month + 1) + "" : "0" + (month + 1);
        String daynum = (day > 9) ? day + "" : "0" + day;

        dateInverseFormat = year + "-" + monthnum + "-" + daynum;

        return date;
    }

    void openLegalActivity()
    {
        Intent i = new Intent(this, LegalActivity.class);
        i.putExtra("Accept", true);
        startActivityForResult(i, 1);
    }

    private void OnEnableButton()
    {
        if(!mEditTextDate.getText().toString().equals("dd/mmm/aaaa"))//has text
        {
            mNextButton.setEnabled(true);
            mNextButton.setTextColor(getResources().getColor(R.color.femsa_red_b));
        }
        else
        {
            mNextButton.setEnabled(false);
            mNextButton.setTextColor(getResources().getColor(R.color.femsa_gray_c));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
                {
                    mCheckBoxTerms.setChecked(true);
                    mTerminosAceptados = true;
                }
            if (resultCode == Activity.RESULT_CANCELED)
                {
                    mCheckBoxTerms.setChecked(false);
                    mTerminosAceptados = false;
                }
        }
    }

    @Override
    public void OnEmployeeExists(String employee) {

    }

    @Override
    public void OnEmployeeNotExists() {

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
        if(loader != null){
            loader.show(fm, "Loader");
        }
    }

    @Override
    public void OnUniqueLoginSuccess(UserResponseData data) {
        String tempCredencial = SharedPreferencesUtil.getInstance().getCredencial();
        if (data.getmUser().getmNumeroTotalIngresoSesion() > 0)//!(tempCredencial == null || tempCredencial.isEmpty() || tempCredencial.length() < 8))
            {
                Intent regreso = new Intent(this, Registry.class);
                regreso.putExtra("regreso", employeeNumber);
                regreso.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(regreso);
                overridePendingTransition(0,0); //0 for no animation
                finish();
                //DialogManager.displaySnack(getSupportFragmentManager(), getResources().getString(R.string.usuario_inexistente_seguro), false, this);
                //Toast.makeText(this, getResources().getText(R.string.usuario_ya_registrado), Toast.LENGTH_SHORT).show();
            }
        else
            {
                setContentView(R.layout.activity_registry_2);
                bindViews();
                mData = data;
            }
    }

    @Override
    public void OnNoAuth() {
       Intent sendto = new Intent(this, LoginActivity.class);
       startActivity(sendto);
    }

    @Override
    public void OnDateSuccess() {
        SharedPreferencesUtil.getInstance().createLoginSession(mData.getmUser());
        Intent sendto = new Intent(this, HomeActivity.class);
            sendto.putExtra("new", "new");
            sendto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(sendto);
    }

    @Override
    public void OnDateFailure() {
        mErrorTextview.setText(R.string.date_wrong);
    }

    @Override
    public void OnMostrarMensaje(int tipoMensaje) {
        DialogManager.displaySnack(getSupportFragmentManager(), tipoMensaje);
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param monthOfYear      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth th selected day of the month (1-31, depending on
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mEditTextDate.setText(buildDate(dayOfMonth,monthOfYear,year));
    }

    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(RegistryPart2.this)
                .callback(RegistryPart2.this)
                .spinnerTheme(spinnerTheme)
                .showTitle(false)
                .maxDate(getTodayDate(2), getTodayDate(1), getTodayDate(0))
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    int getTodayDate(int returnType)
    {
        int dateValue;
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        switch(returnType)
            {
                case 0:
                    dateValue = calendar.get(Calendar.DAY_OF_MONTH);
                    break;
                case 1:
                    dateValue = calendar.get(Calendar.MONTH);
                    break;
                case 2:
                    dateValue = calendar.get(Calendar.YEAR);
                    break;
                default:
                    dateValue = 0;
            }
        return dateValue;
    }

    @Override
    public void OnDialogAceptarClick() {
        Intent sendto = new Intent(this, LoginActivity.class);
        startActivity(sendto);
        finish();
    }

    @Override
    public void OnDialogCancelarClick() {
    //No
    }
}
