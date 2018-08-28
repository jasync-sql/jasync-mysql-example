package com.github.jasync.mysql.example

import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.general.ArrayRowData
import com.github.jasync.sql.db.mysql.MySQLConnection
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.launch
import mu.KotlinLogging
import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.experimental.suspendCoroutine

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
  //just to make sure which log level is active
  logger.error("starting")
  logger.warn("starting warn")
  logger.info("starting info")
  logger.debug("starting debug")
  logger.trace("starting trace")
  val connection: Connection = MySQLConnection(
      Configuration(
          username = "username",
          password = "password",
          host = "host.com",
          port = 3306,
          database = "schema"
      )
  )
  connection.connect().get()

  launch {
    val queryResult = connection.sendPreparedStatementAwait("select * from table limit 2")
    println((queryResult.rows!![0] as ArrayRowData).columns.toList())
    println((queryResult.rows!![1] as ArrayRowData).columns.toList())
  }

  launch {
    val future = connection.sendPreparedStatement("select * from table limit 2")
    val queryResult = future.await()
    println((queryResult.rows!![0] as ArrayRowData).columns.toList())
    println((queryResult.rows!![1] as ArrayRowData).columns.toList())
  }
}

suspend fun Connection.sendPreparedStatementAwait(query: String, values: List<Any> = emptyList()): QueryResult =
    this.sendPreparedStatement(query, values).await()

