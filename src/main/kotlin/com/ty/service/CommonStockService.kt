package com.ty.service

import com.ty.db.DbDefaultCollection
import com.ty.stockdata.CommonStockData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CommonStockService {

    fun saveToDb(symbol: String, commonStockDatas: List<CommonStockData>) {

        val commonStockDatas = commonStockDatas.map {
            hashMapOf(
                "symbol" to symbol,
                "date" to LocalDateTime.ofInstant(Instant.parse(it.date), ZoneId.systemDefault()),
                "open" to it.open,
                "close" to it.close,
                "high" to it.high,
                "low" to it.low,
                "volume" to it.volume
            )
        }

        DbDefaultCollection.dbDefaultCollection
            .batchInsert("common_stock", commonStockDatas)
    }
}