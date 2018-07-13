package com.guohanhealth.shop.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SearchBean {
    @Id(autoincrement  = true)
    private Long id;
    private String name;

    public SearchBean(String name) {
        this.name = name;
    }

    public SearchBean() {
    }

    @Generated(hash = 118660477)
    public SearchBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
