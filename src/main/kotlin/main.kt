import self.tekichan.demo.yfinance4j.YFinance4J
import self.tekichan.demo.yfinance4j.model.HistoricalQuote
import self.tekichan.demo.yfinance4j.model.Interval
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Function


fun main() {

    val symbol = "APPL"
    val timeout = 10
    val startDate = LocalDate.of(2025, 1, 25)
    val endDate = LocalDate.of(2025, 1, 26)
    val interval = Interval.DAILY
    val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val historicalQuoteList = YFinance4J.historicalQuoteCtrl()
        .symbol(symbol)
        .timeout(timeout * 1000)
        .startDate(startDate)
        .endDate(endDate)
        .interval(interval)
        .historicalData

    println(
        "[" +
                java.lang.String.join(
                    ",",
                    historicalQuoteList.stream()
                        .map<String>(
                            Function<HistoricalQuote, String> {
                                    quote: HistoricalQuote -> symbol.formatted(
                                DATE_FORMATTER.format(quote.tradeDate()),
                                quote.openPrice().toString(),
                                quote.highPrice().toString(),
                                quote.lowPrice().toString(),
                                quote.closePrice().toString(),
                                quote.adjustPrice().toString(),
                                quote.volume().toString()
                            )
                            }
                        )
                        .toList()
                ) + "]"
    )
}
