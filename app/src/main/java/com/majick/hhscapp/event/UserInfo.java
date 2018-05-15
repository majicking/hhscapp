package com.majick.hhscapp.event;


public class UserInfo {

    private String nickName;
    private String iconId;

    public UserInfo(String nickName, String iconId) {
        this.nickName = nickName;
        this.iconId = iconId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }
}
