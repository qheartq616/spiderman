package com.qheartq616.spiderman.utils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BrowserUtil {
    public static Document getHtmlFromUrl(String url) throws IOException, FailingHttpStatusCodeException {
        //Htmlunit模拟的浏览器，设置css,js等支持及其它的一些简单设置
        WebClient browser = new WebClient();
        browser.getOptions().setCssEnabled(false);
        browser.getOptions().setJavaScriptEnabled(true);
        browser.getOptions().setThrowExceptionOnScriptError(false);
        //获取页面
        HtmlPage htmlPage = browser.getPage(url);
        //设置等待js的加载时间
        browser.waitForBackgroundJavaScript(3000);
        //使用xml的方式解析获取到jsoup的document对象
        return Jsoup.parse(htmlPage.asXml());
    }
}
