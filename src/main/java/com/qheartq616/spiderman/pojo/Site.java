package com.qheartq616.spiderman.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Site {
    //何地的站点
    private String name;
    //网站地址  www.baidu.com/tieba/index.html
    private String url;
    //怎样选取到审批链接元素
    private String select="ul li a";
    //有些网站的a标签链接路径为相对路径（如北京），须补充完整  www.baidu.com/tieba
    private String relativeUrl;
    //网站根地址  www.baidu.com
    private String baseUrl;
    //网站屏蔽地址，遍历到这些url就排除
    private List<String> abandonUrlList=new ArrayList<>();

    public Site() {
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
            relativeUrl=url.substring(0,url.lastIndexOf("/"));
            //截取http://或https://后出现的/前字符串
            baseUrl=url.substring(0,url.indexOf("/",9));
            //忽略列表
            abandonUrlList.add(url);
            abandonUrlList.add(url+"/");
            abandonUrlList.add(baseUrl);
            abandonUrlList.add(baseUrl+"/");
            abandonUrlList.add(relativeUrl);
            abandonUrlList.add(relativeUrl+"/");
        }
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getAbandonUrlList() {
        return abandonUrlList;
    }

    public void setAbandonUrlList(List<String> abandonUrlList) {
        this.abandonUrlList = abandonUrlList;
    }
}
