package com.ty.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

inline fun <reified T> readJsonFile(filePath: String): T {
    val jsonString = File(filePath).readText()
    return Json.decodeFromString(jsonString)
}
