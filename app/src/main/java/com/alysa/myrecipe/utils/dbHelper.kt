package com.alysa.myrecipe.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object dbHelper {

    private const val URL = "jdbc:postgresql://127.0.0.1:5433/resep_makanan"
    private const val USER = "lysa"
    private const val PASSWORD = "lysa2004"

    init {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    suspend fun getData(query: String): ResultSet? {
        return withContext(Dispatchers.IO) {
            var connection: Connection? = null
            var resultSet: ResultSet? = null
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD)
                val statement = connection.createStatement()
                resultSet = statement.executeQuery(query)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            resultSet
        }
    }
}
