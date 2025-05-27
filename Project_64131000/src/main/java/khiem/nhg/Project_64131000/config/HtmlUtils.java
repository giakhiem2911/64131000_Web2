package khiem.nhg.Project_64131000.config;

import org.jsoup.Jsoup;

public class HtmlUtils {
    public static String stripHtml(String html) {
        if (html == null) return "";
        return Jsoup.parse(html).text();
    }
}

