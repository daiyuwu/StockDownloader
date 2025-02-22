package com.ty

import com.ty.db.DbCollection
import com.ty.db.DbDefaultCollection

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.ty.service.CommonStockService
import java.io.File
import com.ty.stockdata.CommonStockData
import com.ty.stockdata.CommonStockPack
import java.sql.ResultSet

//@Serializable
//data class StockMeta(
//    val date_from: String,
//    val date_to: String,
//    val max_period_days: Int
//)
//
//@Serializable
//data class StockData(
//    val date: String,
//    val open: Double,
//    val high: Double,
//    val low: Double,
//    val close: Double,
//    val volume: Long
//)
//
//@Serializable
//data class StockPack(
//    val meta: StockMeta,
//    val data: List<StockData>
//)

fun main(args: Array<String>) = runBlocking {
//    val dbCollection = DbDefaultCollection()
//    dbCollection.connect()

    val symbol = args[0]
    val startDate = args[1]
    val endDate = args[2]

//    val symbol = "AMD" // 股票代碼
//
//    val startDate = "2021-01-01"
//    val endDate = "2022-12-31"

    try {
        runStock(symbol, startDate, endDate)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        DbDefaultCollection.closeConnection()
    }
}

suspend fun runStock(symbol: String, startDate: String, endDate: String) {

    val commonStockService = CommonStockService()

//    testMysql(dbCollection)

//    val symbol = "AMD" // 股票代碼
//
//    val startDate = "2021-01-01"
//    val endDate = "2022-12-31"
    val json = getFromPlatform(symbol, startDate, endDate)

//    saveToJsonFile(symbol, json)

//    val filePath = "src/main/resources/json/KMB.json"
//    val json = File(filePath).readText()

    val commonStockPack: CommonStockPack = Json.decodeFromString(json)
//    val commonStockMeta: CommonStockMeta = commonStockPack.meta
    val commonStockDatas: List<CommonStockData> = commonStockPack.data

    commonStockService.saveToDb(symbol, commonStockDatas)
}

suspend fun getFromPlatform(symbol: String, startDate: String, endDate: String): String {

    val apiKey = "W4hE0cT7cqpTrJId3QmtJyHNI7GiAgWuuJyEUpoQ" // 請替換為你的 API Key

    val url = "https://api.stockdata.org/v1/data/eod?symbols=$symbol&date_from=$startDate&date_to=$endDate&api_token=$apiKey"

    println("url: $url")

    val client = HttpClient(CIO)

    return try {
        val response: HttpResponse = client.get(url)
        response.body()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    } finally {
        client.close()
    }
}

fun testMysql(dbCollection: DbCollection) {

    println("TEST: START")

    val rs: ResultSet = dbCollection.select("SELECT * FROM city LIMIT 10") as ResultSet

    while (rs.next()) {
        val id = rs.getInt("ID")
        val name = rs.getString("Name")
        val countryCode = rs.getString("CountryCode")
        val district = rs.getString("District")

        println("id: $id, name: $name, countryCode: $countryCode, district: $district")
    }

    println("TEST: END")
}

fun saveToCSV(symbol: String, data: List<CommonStockData>) {
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
