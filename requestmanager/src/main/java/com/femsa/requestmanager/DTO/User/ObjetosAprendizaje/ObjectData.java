package com.femsa.requestmanager.DTO.User.ObjetosAprendizaje;

import java.io.Serializable;

public class ObjectData implements Serializable {

    private int mIdProgram;
    private int mIdObject;
    private String mObjectName;
    private String mImageObject;
    private String mType;
    private String mContent;
    private String mImageDetail;
    private String mDetailContent;
    private String mDetailName;
    private String mEstimatedTime;
    private int mModuleID;
    private int mLanguageID;
    private int mBonus;
    private int mLike;
    private String mFilename;
    private String mFiletype;
    private String mModule;
    private boolean mDescarga;
    private boolean mSeriado;
    private boolean mObligatorio;
    private boolean mStatusSeriado;
    private boolean mStatusBonus;

    public ObjectData(int mIdProgram, int mIdObject, String mObjectName, String mImageObject, String mType, String mContent, String mImageDetail, String mDetailContent, String mDetailName, String mEstimatedTime, int mModuleID, int mLanguageID, int mBonus, int mLike, String mFilename, String mFiletype, String mModule, boolean mDescarga, boolean mSeriado, boolean mObligatorio, boolean mStatusSeriado, boolean mStatusBonus) {
        this.mIdProgram = mIdProgram;
        this.mIdObject = mIdObject;
        this.mObjectName = mObjectName;
        this.mImageObject = mImageObject;
        this.mType = mType;
        this.mContent = mContent;
        this.mImageDetail = mImageDetail;
        this.mDetailContent = mDetailContent;
        this.mDetailName = mDetailName;
        this.mEstimatedTime = mEstimatedTime;
        this.mModuleID = mModuleID;
        this.mLanguageID = mLanguageID;
        this.mBonus = mBonus;
        this.mLike = mLike;
        this.mFilename = mFilename;
        this.mFiletype = mFiletype;
        this.mModule = mModule;
        this.mDescarga = mDescarga;
        this.mSeriado = mSeriado;
        this.mObligatorio = mObligatorio;
        this.mStatusSeriado = mStatusSeriado;
        this.mStatusBonus = mStatusBonus;
    }

    public int getIdProgram() {
        return mIdProgram;
    }

    public int getIdObject() {
        return mIdObject;
    }

    public String getObjectName() {
        return mObjectName;
    }

    public String getImageObject() {
        return mImageObject;
    }

    public String getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

    public String getImageDetail() {
        return mImageDetail;
    }

    public String getDetailContent() {
        return mDetailContent;
    }

    public String getDetailName() {
        return mDetailName;
    }

    public String getEstimatedTime() {
        return mEstimatedTime;
    }

    public int getModuleID() {
        return mModuleID;
    }

    public int getLanguageID() {
        return mLanguageID;
    }

    public int getBonus() {
        return mBonus;
    }

    public int getLike() {
        return mLike;
    }

    public String getFilename() {
        return mFilename;
    }

    public String getFiletype() {
        return mFiletype;
    }

    public String getModule() {
        return mModule;
    }

    public boolean isDescarga() {
        return mDescarga;
    }

    public boolean ismSeriado() {
        return mSeriado;
    }

    public boolean ismObligatorio() {
        return mObligatorio;
    }

    public boolean ismStatusSeriado() {
        return mStatusSeriado;
    }

    public boolean ismStatusBonus()
    {
        return  mStatusBonus;
    }

    public void setType(String type) {
        mType = type;
    }
}
