import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class StockMeta(
    val date_from: String,
    val date_to: String,
    val max_period_days: Int
)

@Serializable
data class StockData(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

@Serializable
data class StockPack(
    val meta: StockMeta,
    val data: List<StockData>
)

fun main() = runBlocking {
    val apiKey = "W4hE0cT7cqpTrJId3QmtJyHNI7GiAgWuuJyEUpoQ" // 請替換為你的 API Key
    val symbol = "AAPL" // Apple 股票代碼
    val startDate = "2023-01-01"
    val endDate = "2024-01-01"
    val url = "https://api.stockdata.org/v1/data/eod?symbols=$symbol&date_from=$startDate&date_to=$endDate&api_token=$apiKey"

    println("url: $url")

    val client = HttpClient(CIO)
    try {
        val response: HttpResponse = client.get(url)
        val json = response.body<String>()

//        saveToJsonFile(symbol, json)
//        return@runBlocking

        val stockPack: StockPack = Json.decodeFromString(json)
        val stockMeta: StockMeta = stockPack.meta
        val stockData: List<StockData> = stockPack.data

//        if (stockDataList.isEmpty()) {
//            println("未獲取到歷史數據。")
//            return@runBlocking
//        }

        saveToCSV(symbol, stockData)
        println("數據下載完成並儲存至 $symbol.csv")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        client.close()
    }
}

fun saveToCSV(symbol: String, data: List<StockData>) {
    val filePath = "src/main/resources/csv/"
    val fileName = "$filePath$symbol.csv"
    File(fileName).printWriter().use { writer ->
        writer.println("Date,Open,High,Low,Close,Volume")
        data.forEach { record ->
            writer.println("${record.date},${record.open},${record.high},${record.low},${record.close},${record.volume}")
        }
    }
}

fun saveToJsonFile(symbol: String, json: String) {
    val filePath = "src/main/resources/json/"
    val fileName = "$filePath$symbol.json"
    File(fileName).printWriter().use { writer ->
        writer.print(json)
    }
}
