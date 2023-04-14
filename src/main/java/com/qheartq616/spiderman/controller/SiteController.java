package com.qheartq616.spiderman.controller;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.qheartq616.spiderman.config.SiteConfig;
import com.qheartq616.spiderman.pojo.ATag;
import com.qheartq616.spiderman.pojo.Site;
import com.qheartq616.spiderman.utils.BrowserUtil;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class SiteController {
    @Autowired
    SiteConfig siteConfig;

    @GetMapping("/query")
    public Map<String,String> query(@RequestParam String key){
        List<Site> siteList = siteConfig.getList();

        Map<String,String> returnMap = new HashMap<>();
        Document documentMain;
        for (Site site : siteList) {
            try {
                documentMain = BrowserUtil.getHtmlFromUrl(site.getUrl());
                //失败不管了，直接记录为访问审批列表网站失败
            } catch (HttpStatusException e){
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！状态码为："+e.getStatusCode()+"。具体原因为："+e);
                continue;
            } catch (FailingHttpStatusCodeException e){
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！状态码为："+e.getStatusCode()+"。具体原因为："+e);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！具体原因为："+e);
                continue;
            }
            Elements elements = documentMain.select(site.getSelect());

            //结果合集
            List<ATag> successList=new ArrayList<>();

            //遍历批文合集页元素
            for (Element element : elements) {
                //只能遍历a标签的链接  onclick事件无法爬取
                //无href属性a标签似乎默认指向relativeUrl，会导致奇怪的bug
                String href = element.attr("href");
                if (site.getAbandonUrlList().contains(href)){
                    continue;
                }
                String title = element.ownText();
                //地址是否完整，不完整需要分相对路径和绝对路径两种补全地址
                if (!href.contains("http")){
                    // /content.jsp
                    if(href.indexOf("/")==0){
                        //和站点地址拼一起
                        href = documentMain.location()+href;
                    }
                    else // content.jsp
                    {
                        //换下绝对路径就行
                        href = site.getRelativeUrl()+"/"+href;
                    }
                }

                //访问可能是批文的地址
                Document documentHref;
                try {
                    documentHref = BrowserUtil.getHtmlFromUrl(href);
                    //访问失败就忽略呗，继续访问下一个a标签，总不可能全部访问失败吧
                } catch (IOException | FailingHttpStatusCodeException e) {
                    e.printStackTrace();
                    System.out.println("错误url为"+href);
                    continue;
                }

                Elements shenPiElements = documentHref.select("*");
                //遍历a标签链接网页所有元素
                for (Element shenPiElement : shenPiElements) {
                    String text = shenPiElement.ownText();
                    if (text.contains(key)){
                        //成功搜到
                        ATag aTag=new ATag();
                        aTag.setTitle(title);
                        aTag.setHref(href);
                        successList.add(aTag);
                        break;
                    }
                }
            }
            //有结果
            returnMap.put(site.getName(),successList.toString());
            //无结果
            returnMap.putIfAbsent(site.getName(), "无搜索结果");
        }
        return returnMap;
    }
}
