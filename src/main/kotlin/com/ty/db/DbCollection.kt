package com.ty.db

interface DbCollection {

//    fun connect()
//
//    fun disconnect()

//    fun getConnection(): DbDefaultCollection
//
//    fun closeConnection()

    fun select(query: String): Any

    fun insert(tableName: String, data: Map<String, Any>): Int

    fun batchInsert(tableName: String, dataList: List<Map<String, Any>>): Int
}