package com.ty.stockdata

import kotlinx.serialization.Serializable

@Serializable
data class CommonStockMeta(
    val date_from: String,
    val date_to: String,
    val max_period_days: Int
)

@Serializable
data class CommonStockData(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

@Serializable
data class CommonStockPack(
    val meta: CommonStockMeta,
    val data: List<CommonStockData>
)