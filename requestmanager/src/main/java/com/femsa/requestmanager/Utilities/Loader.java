package com.femsa.requestmanager.Utilities;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.requestmanager.R;

public class Loader extends DialogFragment {

    private View mView;

    public Loader() {
        super();
    }

    public static Loader newInstance() {
        Loader frag = new Loader();
        frag.setCancelable(false);
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.progress_bar_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
}
