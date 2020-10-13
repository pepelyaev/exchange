package ru.exchange.app;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллено нужен для передачи параметров на клиентскую сторону (разные среды, разные настройки)
 */
@Primary
@RestController()
public class AppController {

    @RequestMapping(value = "/app/request/ticker={tickers}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> request(@PathVariable String tickers) throws IOException, ParseException {
        List<Object> result = new ArrayList<>();
        if (tickers != null) {
            for (String ticker : tickers.split(",")) {
                Map<String, Object> row = new HashMap<>();
                //investmint(ticker, row);
                row.put("ticker", ticker);
                finviz(ticker, row);
                result.add(row);
            }
        }
        return result;
    }

    private void investmint(String ticker, Map<String, Object> row) throws IOException, ParseException {
        String html = AppUtils.getPageByUrl("https://investmint.ru/" + ticker + "-spb/");
        int beginIndex = html.indexOf("<div class=\"row\" id=\"history\">");
        if (beginIndex != -1) {
            int endIndex = html.indexOf("<div class=\"text-center mb-2\">", beginIndex);
            if (endIndex != -1) {
                html = html.substring(beginIndex, endIndex);
                // Создание Pattern объекта
                Pattern pattern = Pattern.compile("<td class=\"text-nowrap(.*?)</td>");
                // Создание matcher объекта
                Matcher m = pattern.matcher(html);
                int index = 0;
                boolean flagBreak = false;
                while (m.find()) {
                    if (flagBreak) {
                        break;
                    }
                    switch (index % 5) {
                        case 0:  //Утвержен
                            row.put("recommended", m.group().indexOf("svg") != -1);
                            break;
                        case 1:  //Купить до
                            row.put("dateBue", AppUtils.investmint2date(m.group()));
                            break;
                        case 2:  //Дата зарытия реестра
                            row.put("dateClose", AppUtils.investmint2date(m.group()));
                            break;
                        case 3:  //Дата выплаты
                            row.put("datePay", AppUtils.investmint2date(m.group()));
                            break;
                        case 4: //Размер дивов
                            row.put("value", AppUtils.investmint2number(m.group()));
                            flagBreak = true;
                            break;
                    }
                    index++;
                }
            }
        }
    }

    private void finviz(String ticker, Map<String, Object> row) throws IOException {
        ticker = ticker.replace('.', '-');
        String html = AppUtils.getPageByUrl("https://finviz.com/quote.ashx?ty=c&p=d&b=1&t=" + ticker);
        int beginIndex = html.indexOf("snapshot-table2");
        if (beginIndex != -1) {
            int endIndex = html.indexOf("</table>", beginIndex);
            if (endIndex != -1) {
                html = html.substring(beginIndex, endIndex);
                // Создание Pattern объекта
                Pattern pattern = Pattern.compile("<b>(.*?)</b>");
                // Создание matcher объекта
                Matcher m = pattern.matcher(html);
                int rowId = 0;
                while (m.find()) {
                    switch (rowId++) {
                        case 55:  // Debt/Eq
                            row.put("debtEq", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 61:  // LT Debt/Eq (Долгосрочный долг)
                            row.put("ltDebtEq", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 21:  // EPS this Y
                            row.put("epsThis1y", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 8:  // EPS next Y
                            row.put("epsNext1y", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 36:  // EPS past 5Y
                            row.put("epsPast5y", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 32:  // EPS next 5Y
                            row.put("epsNext5y", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 63:  // Payout (Сколько направляют на Дивы)
                            row.put("payout", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 1:  // P/E
                            row.put("pe", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 7:  // Forward P/E
                            row.put("fpe", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 19:  // P/S
                            row.put("ps", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 25:  // P/B
                            row.put("pb", AppUtils.finviz2float(m.group(), false));
                            break;
                        case 45:  // Gross Margin
                            row.put("grossMargin", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 51:  // Oper. Margin
                            row.put("operMargin", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 57:  // Profit Margin
                            row.put("profitMargin", AppUtils.finviz2float(m.group(), true));
                            break;
                        case 28:  // Target Price
                            row.put("targetPrice", AppUtils.finviz2float(m.group(), false));
                            break;
                    }
                }
            }
        }
    }
}
