package com.ty.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class DbDefaultCollection private constructor(): DbCollection {

    private val url = "jdbc:mysql://localhost:3306/stock"
    private val user = "root"
    private val password = "Testmysql12340"

    private var connection: Connection? = null

    init {
        this.connect()
    }

    companion object {

        var dbDefaultCollection: DbDefaultCollection = DbDefaultCollection()

        fun closeConnection() {
            this.dbDefaultCollection.disconnect()
        }
    }

    private fun connect() {

        try {
            connection = DriverManager.getConnection(url, user, password)
        } catch (e: Exception) {
            e.printStackTrace()
            connection?.close()
        }

        println("✅ 成功連接 MySQL！")
    }

    private fun disconnect() {
        if (connection == null)
            return
        connection?.close()
        println("✅ 成功斷連 MySQL！")
    }

    override fun select(query: String): Any {

        val safeConnection = connection ?: throw IllegalArgumentException("MySQL connection is null")

        val statement = safeConnection.createStatement()

        return statement.executeQuery(query)
    }

    /**
     * 通用 INSERT 函式
     * @param tableName 表格名稱
     * @param data 欄位名稱與值的對應 Map
     * @return 插入成功時回傳影響的行數 (成功: 1, 失敗: 0)
     */
    override fun insert(tableName: String, data: Map<String, Any>): Int {
        if (data.isEmpty()) return 0

        val safeConnection = connection ?: throw IllegalArgumentException("MySQL connection is null")

        val columns = data.keys.joinToString(", ") // 取得所有欄位名稱
        val placeholders = data.keys.joinToString(", ") { "?" } // 產生 ?, ?, ?
        val sql = "INSERT INTO $tableName ($columns) VALUES ($placeholders)"

        var preparedStatement: PreparedStatement? = null

        return try {
            preparedStatement = safeConnection.prepareStatement(sql)

            // 設定 SQL 參數
            data.values.forEachIndexed { index, value ->
                preparedStatement.setObject(index + 1, value)
            }

            preparedStatement.executeUpdate() // 執行 SQL 並回傳影響的行數
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            preparedStatement?.close()
        }
    }

    /**
     * 批次插入資料
     * @param tableName 表格名稱
     * @param dataList 欄位名稱與值的 List，每個 Map 代表一筆資料
     * @return 插入成功的筆數
     */
    override fun batchInsert(tableName: String, dataList: List<Map<String, Any>>): Int {
        if (dataList.isEmpty()) return 0

        val columns = dataList.first().keys.joinToString(", ") // 取得欄位名稱
        val placeholders = dataList.first().keys.joinToString(", ") { "?" } // 產生 ?, ?, ?
        val sql = "INSERT INTO $tableName ($columns) VALUES ($placeholders)"

        val safeConnection = connection ?: throw IllegalArgumentException("MySQL connection is null")

        var preparedStatement: PreparedStatement? = null

        return try {

            safeConnection.autoCommit = false // ⚡ 提升效能：關閉自動提交

            preparedStatement = safeConnection.prepareStatement(sql)

            for (data in dataList) {
                data.values.forEachIndexed { index, value ->
                    preparedStatement.setObject(index + 1, value)
                }
                preparedStatement.addBatch() // 加入批次
            }

            val result = preparedStatement.executeBatch().sum() // 執行批次插入並計算影響的行數
            safeConnection.commit() // 提交交易
            safeConnection.autoCommit = true
            result
        } catch (e: Exception) {
            e.printStackTrace()
            connection?.rollback() // 發生錯誤時回滾
            0
        } finally {
            preparedStatement?.close()
            connection?.close()
        }
    }
}