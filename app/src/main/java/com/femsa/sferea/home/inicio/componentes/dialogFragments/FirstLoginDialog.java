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

import com.femsa.sferea.R;

public class FirstLoginDialog extends DialogFragment {

        private View mView;

        private Button mAcceptButton;

        private UpdateDataDialog mDialog;

        private UpdateDataDialog.OnPasswordSuccessDialog mListener;

        public void setListener(UpdateDataDialog.OnPasswordSuccessDialog listener)
        {
            mListener = listener;
        }

        public FirstLoginDialog() {
            super();
        }

        public static FirstLoginDialog newInstance() {
            return new FirstLoginDialog();
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
            mView = inflater.inflate(R.layout.dialog_firstlogin_first, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            mAcceptButton = mView.findViewById(R.id.first_login_accept);
                mAcceptButton.setOnClickListener(v->onShowSecondDialog());
            return  mView;
        }

        private void onShowSecondDialog()
        {
            mDialog = UpdateDataDialog.newInstance();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            mDialog.setCancelable(false);
            mDialog.setListener(mListener);
            mDialog.show(fm, "NewDialog2");
            this.dismiss();
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
