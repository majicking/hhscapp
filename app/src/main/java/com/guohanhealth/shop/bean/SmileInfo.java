package com.guohanhealth.shop.bean;

import com.guohanhealth.shop.R;

import java.util.ArrayList;
import java.util.List;

public class SmileInfo {
    public String name;
    public String title;
    public int path;

    public SmileInfo(String name, String title, int path) {
        this.name = name;
        this.title = title;
        this.path = path;

    }


    public static List<SmileInfo> getSmileList() {
        List<SmileInfo> mlist = new ArrayList<>();
        mlist.add(new SmileInfo("微笑", ":smile:", R.mipmap.smile));
        mlist.add(new SmileInfo("难过", ":sad:", R.mipmap.sad));
        mlist.add(new SmileInfo("呲牙", ":biggrin:", R.mipmap.biggrin));
        mlist.add(new SmileInfo("大哭", ":cry:", R.mipmap.cry));
        mlist.add(new SmileInfo("发怒", ":huffy:", R.mipmap.huffy));
        mlist.add(new SmileInfo("惊讶", ":shocked:", R.mipmap.shocked));
        mlist.add(new SmileInfo("调皮", ":tongue:", R.mipmap.tongue));
        mlist.add(new SmileInfo("害羞", ":shy:", R.mipmap.shy));
        mlist.add(new SmileInfo("偷笑", ":titter:", R.mipmap.titter));
        mlist.add(new SmileInfo("流汗", ":sweat:", R.mipmap.sweat));
        mlist.add(new SmileInfo("抓狂", ":mad:", R.mipmap.mad));
        mlist.add(new SmileInfo("阴险", ":lol:", R.mipmap.lol));
        mlist.add(new SmileInfo("可爱", ":loveliness:", R.mipmap.loveliness));
        mlist.add(new SmileInfo("惊恐", ":funk:", R.mipmap.funk));
        mlist.add(new SmileInfo("咒骂", ":curse:", R.mipmap.curse));
        mlist.add(new SmileInfo("晕", ":dizzy:", R.mipmap.dizzy));
        mlist.add(new SmileInfo("闭嘴", ":shutup:", R.mipmap.shutup));
        mlist.add(new SmileInfo("睡", ":sleepy:", R.mipmap.sleepy));
        mlist.add(new SmileInfo("拥抱", ":hug:", R.mipmap.hug));
        mlist.add(new SmileInfo("胜利", ":victory:", R.mipmap.victory));
        mlist.add(new SmileInfo("太阳", ":sun:", R.mipmap.sun));
        mlist.add(new SmileInfo("月亮", ":moon:", R.mipmap.moon));
        mlist.add(new SmileInfo("示爱", ":kiss:", R.mipmap.kiss));
        mlist.add(new SmileInfo("握手", ":handshake:", R.mipmap.handshake));
        return mlist;
    }


}
