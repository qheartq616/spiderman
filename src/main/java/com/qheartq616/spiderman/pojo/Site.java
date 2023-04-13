package com.qheartq616.spiderman.pojo;

import org.apache.commons.lang3.StringUtils;

public class Site {
    //何地的站点
    private String name;
    //网站地址
    private String url;
    //怎样选取到审批链接元素
    private String select="ul li a";
    //有些网站的a标签链接路径为相对路径（如北京），须补充完整
    private String baseUrl;

    public Site() {
    }

    public Site(String name, String url, String select, String baseUrl) {
        this.name = name;
        this.url = url;
        this.select = select;
        this.baseUrl = baseUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if (StringUtils.isNotEmpty(url)){
            this.setBaseUrl(url.substring(0,url.lastIndexOf("/")+1));
        }
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
