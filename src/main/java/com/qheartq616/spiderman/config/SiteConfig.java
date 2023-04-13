package com.qheartq616.spiderman.config;

import com.qheartq616.spiderman.pojo.Site;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "site")
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
public class SiteConfig {
    private List<Site> list;

    public List<Site> getList() {
        return list;
    }

    public void setList(List<Site> list) {
        this.list = list;
    }
}
