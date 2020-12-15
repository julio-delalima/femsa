package com.femsa.requestmanager.DTO.User.Ranking;

public class RankingTierlistData {

    private int mTotalBonus;
    private int mEmployeeID;
    private int mGroupID;
    private String mName;
    private int mLangID;
    private int mPlacement;
    private String mPhoto;
    private int mTotalLikes;
    private boolean mHasLike;

    public RankingTierlistData(int totalBonus, int employeeID, int groupID, String name, int langID, int placement, String photo, int totalLikes, boolean hasLike) {
        mTotalBonus = totalBonus;
        mEmployeeID = employeeID;
        mGroupID = groupID;
        mName = name;
        mLangID = langID;
        mPlacement = placement;
        mPhoto = photo;
        mTotalLikes = totalLikes;
        mHasLike = hasLike;
    }

    public int getTotalBonus() {
        return mTotalBonus;
    }

    public int getEmployeeID() {
        return mEmployeeID;
    }

    public int getGroupID() {
        return mGroupID;
    }

    public String getName() {
        return mName;
    }

    public int getLangID() {
        return mLangID;
    }

    public int getPlacement() {
        return mPlacement;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public int getTotalLikes() {
        return mTotalLikes;
    }

    public boolean isHasLike() {
        return mHasLike;
    }
}