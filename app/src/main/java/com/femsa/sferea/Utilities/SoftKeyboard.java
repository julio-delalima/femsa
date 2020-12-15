package com.femsa.sferea.Utilities;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SoftKeyboard implements View.OnFocusChangeListener,View.OnTouchListener,EditText.OnEditorActionListener, CustomEditText.KeyImeChange
{

    private ViewGroup layout;
    private InputMethodManager im;
    private boolean isKeyboardShow;
    private List<CustomEditText> editTextList;

    private View currentFocusedText;

    public SoftKeyboard(ViewGroup layout, InputMethodManager im)
    {
        this.layout = layout;
        keyboardHideByDefault();
        initEditTexts(layout);
        this.im = im;
        this.isKeyboardShow = false;
    }

    public void openSoftKeyboard()
    {
        if(!isKeyboardShow)
        {
            im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            isKeyboardShow = true;
        }
    }

    public void closeSoftKeyboard()
    {
        if(isKeyboardShow)
        {
            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            isKeyboardShow = false;
            mCallback.onSoftKeyboardHide();
        }
    }

    private SoftKeyboardChanged mCallback;

    public void setSoftKeyboardCallback(SoftKeyboardChanged callback)
    {
        mCallback = callback;
    }

    public interface SoftKeyboardChanged
    {
        void onSoftKeyboardHide();
        void onSoftKeyboardShow();
        void onCloseKeyboard();
    }

    private void keyboardHideByDefault()
    {
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
    }

    private void initEditTexts(ViewGroup viewgroup)
    {
        if(editTextList == null)
            editTextList = new ArrayList<CustomEditText>();

        int childCount = viewgroup.getChildCount();
        for(int i=0; i<= childCount-1;i++)
        {
            View v = viewgroup.getChildAt(i);

            if(v instanceof ViewGroup)
            {
                initEditTexts((ViewGroup) v);
            }

            if(v instanceof CustomEditText)
            {
                CustomEditText editText = (CustomEditText) v;
                editText.setOnFocusChangeListener(this);
                editText.setOnTouchListener(this);
                editText.setCursorVisible(true);
                editText.setOnEditorActionListener(this);
                editText.setKeyImeChangeListener(this);
                editTextList.add(editText);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        currentFocusedText = v;
        if(!isKeyboardShow)
        {
            mCallback.onSoftKeyboardShow();
            isKeyboardShow = true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(hasFocus)
        {
            currentFocusedText = v;
            if(!isKeyboardShow)
            {
                mCallback.onSoftKeyboardShow();
                isKeyboardShow = true;
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            if(editTextList.indexOf(currentFocusedText) == (editTextList.size()-1))
            {
                closeSoftKeyboard();
                mCallback.onCloseKeyboard();
            }
            else
            {
                int index = editTextList.indexOf(currentFocusedText);
                editTextList.get(index+1).requestFocus();
            }
        }
        return false;
    }

    @Override
    public void onKeyIme(int keyCode, KeyEvent event)
    {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode())
        {
            closeSoftKeyboard();
        }
    }
}