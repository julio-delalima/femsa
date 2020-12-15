package com.femsa.sferea.home.inicio.componentes.dialogFragments;

import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.femsa.requestmanager.Response.Home.CategoriesResponse;
import com.femsa.requestmanager.Response.Home.SubcategoriesResponse;
import com.femsa.sferea.R;
import com.femsa.sferea.home.inicio.componentes.adapters.FilterAdapter;

import java.util.Objects;

public class FilterDialog extends DialogFragment implements FilterAdapter.OnFilterDialogListener {

    private View mView;

    private ImageView mCloseButton;

    private RecyclerView mSubcategoryRV;

    private SubcategoriesResponse mData;

    private CategoriesResponse mDataCategories;

    FilterAdapter.OnHomeFilterListener mListener;

    /**
     * Bandera usada para distinguir el tipo de elementos a mostrar en el dialog y su comportamiento:
     * 0 es para subcategorias
     * 1 es para categorias
     * */
    int filterType;

    public void setData(SubcategoriesResponse data, FilterAdapter.OnHomeFilterListener listener, int filter)
    {
        mData = data;
        filterType = filter;
        mListener = listener;
    }

    public void setData(CategoriesResponse data, FilterAdapter.OnHomeFilterListener listener, int filter)
    {
        mDataCategories = data;
        filterType = filter;
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        assert window != null;
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout(size.x, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_filter, container);

        mCloseButton = mView.findViewById(R.id.filter_close_iv);
            mCloseButton.setOnClickListener(v->this.dismiss());

        FilterAdapter mFilterAdapter;
        if(filterType == 0)
            {
                mFilterAdapter = new FilterAdapter(mData, getContext(), 0);
            }
        else
            {
                mFilterAdapter = new FilterAdapter(mDataCategories, getContext(), 1);
            }

        mFilterAdapter.setOnHomeFilterListenet(mListener, this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mSubcategoryRV = mView.findViewById(R.id.filter_recycler);
            mSubcategoryRV.setLayoutManager(LayoutManager);
            mSubcategoryRV.setAdapter(mFilterAdapter);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        return mView;
    }

    @Override
    public void closeDialog() {
        this.dismiss();
    }
}
