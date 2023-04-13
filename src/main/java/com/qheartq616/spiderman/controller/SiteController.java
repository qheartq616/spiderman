package com.qheartq616.spiderman.controller;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
//                Document document = Jsoup.connect(site.getUrl())
//                        .header("User-Agent", Constant.userAgent)
//                        .get();

                //Htmlunit模拟的浏览器，设置css,js等支持及其它的一些简单设置
                WebClient browser = new WebClient();
                browser.getOptions().setCssEnabled(false);
                browser.getOptions().setJavaScriptEnabled(true);
                browser.getOptions().setThrowExceptionOnScriptError(false);
                //获取页面
                HtmlPage htmlPage = browser.getPage(site.getUrl());
                //设置等待js的加载时间
                browser.waitForBackgroundJavaScript(3000);
                //使用xml的方式解析获取到jsoup的document对象
                Document document = Jsoup.parse(htmlPage.asXml());

                Elements elements = document.select(site.getSelect());
                //遍历批文元素
                for (Element element : elements) {
                    //只能遍历a标签的链接  onclick事件无法爬取
                    String href = element.attr("href");
                    String title = element.ownText();
                    //地址是否完整，不完整需要分相对路径和绝对路径两种补全地址
                    if (!href.contains("http")){
                        href = site.getBaseUrl()+href;
                        //todo
                    }

                    Document shenPiHref = Jsoup.connect(href).get();
                    Elements shenPiElements = shenPiHref.select("*");
                    //遍历a标签链接网页所有元素
                    for (Element shenPiElement : shenPiElements) {
                        String text = shenPiElement.ownText();
                        if (text.contains(key)){
                            //成功搜到
                            returnMap.put(site.getName(),"成功找到：标题-"+title+"-链接-"+href);
                            break;
                        }
                    }
                }
            //失败
            } catch (HttpStatusException e){
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！状态码为："+e.getStatusCode()+"。具体原因为："+e);
            } catch (FailingHttpStatusCodeException e){
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！状态码为："+e.getStatusCode()+"。具体原因为："+e);
            } catch (IOException e) {
                e.printStackTrace();
                returnMap.put(site.getName(),"失败！具体原因为："+e);
            } finally {
                if (returnMap.get(site.getName())==null){
                    //无结果
                    returnMap.put(site.getName(),"无搜索结果");
                }
            }
        }
        return returnMap;
    }
}
