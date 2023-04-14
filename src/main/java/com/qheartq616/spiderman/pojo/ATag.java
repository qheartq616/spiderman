package com.qheartq616.spiderman.pojo;

public class ATag {
    private String title;
    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "搜索结果{" +
                "标题='" + title + '\'' +
                ", 链接='" + href + '\'' +
                '}';
    }
}
