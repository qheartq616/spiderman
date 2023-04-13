package com.qheartq616.spiderman.controller;

import com.qheartq616.spiderman.config.SiteConfig;
import com.qheartq616.spiderman.constant.Constant;
import com.qheartq616.spiderman.pojo.Site;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SiteController {
    @Autowired
    SiteConfig siteConfig;

    @GetMapping("/query")
    public Map<String,String> query(@RequestParam String key){
        List<Site> siteList = siteConfig.getList();

        Map<String,String> returnMap = new HashMap<>();

        for (Site site : siteList) {
            try {
                Document document = Jsoup.connect(site.getUrl())
                        .header("User-Agent", Constant.userAgent)
                        .get();

                Elements elements = document.select(site.getSelect());
                //遍历批文
                for (Element element : elements) {
                    String href = element.attr("href");
                    String title = element.ownText();
                    if (!href.contains("http")){
                        href = site.getBaseUrl()+href;
                    }

                    Document shenPiHref = Jsoup.connect(href).get();
                    Elements shenPiElements = shenPiHref.select("*");

                    for (Element shenPiElement : shenPiElements) {
                        String text = shenPiElement.ownText();
                        if (text.contains(key)){
                            returnMap.put(site.getName(),"成功找到：标题-"+title+"-链接-"+href);
                            break;
                        }
                    }
                }

            } catch (HttpStatusException e){
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！状态码为："+e.getStatusCode()+"。具体原因为："+e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                if (returnMap.get(site.getName())==null){
                    returnMap.put(site.getName(),"无搜索结果");
                }
            }
        }
        return returnMap;
    }
}
