package com.femsa.requestmanager.DTO.User.Home;

public class DataPrograms
{

    private int idProgram;
    private String imageTitle;
    private String imageProgram;
    private String categoryName;
    private boolean mDestacado;

    public DataPrograms(int idProgram, String imageTitle, String imageProgram, String categoryName, boolean destacado) {
        this.idProgram = idProgram;
        this.imageTitle = imageTitle;
        this.imageProgram = imageProgram;
        this.categoryName = categoryName;
        mDestacado = destacado;
    }

    public int getIdProgram() {
        return idProgram;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageProgram() {
        return imageProgram;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isDestacado() {
        return mDestacado;
    }
}