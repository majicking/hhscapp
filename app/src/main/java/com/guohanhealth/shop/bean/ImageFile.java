package com.guohanhealth.shop.bean;

public class ImageFile {
    public int file_id; // 字段id
    public String file_name; // 文件名称
    public String origin_file_name; // 原文件名称
    public String file_url; // 图片路径

    @Override
    public String toString() {
        return "ImageFile{" +
                "file_id=" + file_id +
                ", file_name='" + file_name + '\'' +
                ", origin_file_name='" + origin_file_name + '\'' +
                ", file_url='" + file_url + '\'' +
                '}';
    }
}
