package com.yupi.yuaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.stream.Collectors;

/**
 * 网页抓取工具
 */
public class WebScrapingTool {

    private static final int MAX_CONTENT_LENGTH = 5000;

    @Tool(description = "网页抓取工具")
    public String scrapeWebPage(@ToolParam(description = "URL地址") String url) {
        try {
            Document document = Jsoup.connect(url).get();
            document.select("script, style, noscript, svg").remove();

            String title = document.title();
            String text = document.body() == null ? "" : document.body().text();
            String links = formatElements(document.select("a[href]"), "href", 10);
            String images = formatElements(document.select("img[src]"), "src", 10);

            return """
                    网页标题：%s
                    网页正文摘要：%s
                    相关链接：%s
                    图片链接：%s
                    """.formatted(title, truncate(text), links, images);
        } catch (Exception e) {
            return "抓取网页失败: " + e.getMessage();
        }

    }

    private String formatElements(Elements elements, String attr, int limit) {
        String result = elements.stream()
                .limit(limit)
                .map(element -> formatElement(element, attr))
                .filter(item -> !item.isBlank())
                .collect(Collectors.joining("\n"));
        return result.isBlank() ? "无" : result;
    }

    private String formatElement(Element element, String attr) {
        String text = element.text();
        String value = element.absUrl(attr);
        if (value.isBlank()) {
            value = element.attr(attr);
        }
        if (value.isBlank()) {
            return "";
        }
        return text.isBlank() ? value : text + " - " + value;
    }

    private String truncate(String text) {
        if (text == null || text.length() <= MAX_CONTENT_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_CONTENT_LENGTH) + "...（内容过长，已截断）";
    }
}
