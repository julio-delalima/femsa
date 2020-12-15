package com.femsa.sferea.UtilsMK.dialog;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.femsa.sferea.R;

public class NoInternetDialog extends DialogFragment implements View.OnClickListener
{
    public static final String VOTE_TITLE_KEY = "VOTE_TITLE_KEY";
    public static final String VOTE_MESSAGE_KEY = "VOTE_MESSAGE_KEY";
    View mFragmentView;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public static NoInternetDialog newInstance(String title, String message)
    {
        Bundle args = new Bundle();
        args.putString(VOTE_TITLE_KEY, title);
        args.putString(VOTE_MESSAGE_KEY, message);
        NoInternetDialog fragment = new NoInternetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mFragmentView = inflater.inflate(R.layout.game_loading_dialog, container, false);
        Bundle arguments = getArguments();
        mFragmentView.findViewById(R.id.vote_close).setOnClickListener(this);
        ((TextView) mFragmentView.findViewById(R.id.vote_title)).setText(arguments.getString(VOTE_TITLE_KEY));
        ((TextView) mFragmentView.findViewById(R.id.vote_message)).setText(arguments.getString(VOTE_MESSAGE_KEY));
        Drawable background = ActivityCompat.getDrawable(getContext(),R.drawable.corner_color_background);
        background.setColorFilter(getResources().getColor(R.color.femsa_red_b), PorterDuff.Mode.MULTIPLY);
        Button okButton = mFragmentView.findViewById(R.id.vote_ok);
        okButton.setBackground(background);
        okButton.setTextColor(getResources().getColor(R.color.femsa_red_b));
        okButton.setOnClickListener(this);
        return mFragmentView;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.vote_ok:
            case R.id.vote_close:
                if(mOnDismissListener != null)
                    mOnDismissListener.onDismiss(null);
                dismiss();
                break;
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
    {
        mOnDismissListener = onDismissListener;
    }
}