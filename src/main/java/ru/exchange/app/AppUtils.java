package ru.exchange.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;

public class AppUtils {

    public static String getPageByUrl(String spec) throws IOException {
        URL url = new URL(spec);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer htmlBuffer = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            htmlBuffer.append(inputLine);
        }
        in.close();
        return htmlBuffer.toString();
    }

    public static String investmint2text(String text) {
        text = text.replace(" text-right","");
        text = text.replace(" text-center","");
        text = text.replace("&nbsp;<small class=\"text-muted\">"," ");
        text = text.replace("</small>","");
        text = text.replace("<td class=\"text-nowrap\">","");
        text = text.replace("</td>","");
        text = text.replace(" $","");
        text = text.replace(" ₽","");
        text = text.replace(",",".");
        return text;
    }

    public static Float investmint2number(String text) {
        text = investmint2text(text);
        return Float.valueOf(text);
    }

    public static String investmint2date(String text) throws ParseException {
        text = investmint2text(text);
        text = text.replace(".","");
        text = text.replace(" ","");
        text = text.replace(" ","");
        text = text.replace("янв",".01.");
        text = text.replace("фев",".02.");
        text = text.replace("мар",".03.");
        text = text.replace("апр",".04.");
        text = text.replace("мая",".05.");
        text = text.replace("июн",".06.");
        text = text.replace("июл",".07.");
        text = text.replace("авг",".08.");
        text = text.replace("сен",".09.");
        text = text.replace("окт",".10.");
        text = text.replace("ноя",".11.");
        text = text.replace("дек",".12.");
        return text;
    }

    public static Object finviz2float(String text, Boolean isPersent) {
        text = text.replace("<b>","");
        text = text.replace("</b>","");
        text = text.replace("<span style=\"color:#aa0000;\">","");
        text = text.replace("<span style=\"color:#008800;\">","");
        text = text.replace("</span>","");
        text = text.replace("<small>","");
        text = text.replace("</small>","");
        text = text.replace("%","");
        try {
            Float value = Float.valueOf(text);
            return isPersent ? value / 100 : value;
        } catch (Exception e) {
            return text;
        }
    }
}
