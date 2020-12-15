package com.femsa.requestmanager.DTO.User.Ranking;

public class ProgramRankingData {
    private String mProgramName;
    private int mBonus;
    private int mIDProgram;
    private int mIDObj;
    private int mPosition;
    private int mNumOpponents;
    private String mImgProgram;
    private int mGroupID;
    private int mTotalBonus;

    public ProgramRankingData(String programName, int bonus, int IDProgram, int IDObj, int position, int numOpponents, String imgProgram, int groupID, int totalBonus) {
        mProgramName = programName;
        mBonus = bonus;
        mIDProgram = IDProgram;
        mIDObj = IDObj;
        mPosition = position;
        mNumOpponents = numOpponents;
        mImgProgram = imgProgram;
        mGroupID = groupID;
        mTotalBonus = totalBonus;
    }

    public String getProgramName() {
        return mProgramName;
    }

    public int getBonus() {
        return mBonus;
    }

    public int getIDProgram() {
        return mIDProgram;
    }

    public int getIDObj() {
        return mIDObj;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getNumOpponents() {
        return mNumOpponents;
    }

    public String getImgProgram() {
        return mImgProgram;
    }

    public int getGroupID() {
        return mGroupID;
    }

    public int getTotalBonus() {
        return mTotalBonus;
    }
}