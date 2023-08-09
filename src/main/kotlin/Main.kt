package com.github.jasync.mysql.example

import com.github.jasync.sql.db.general.ArrayRowData
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    //just to make sure which log level is active
    logger.error("starting")
    logger.warn("starting warn")
    logger.info("starting info")
    logger.debug("starting debug")
    logger.trace("starting trace")
    val host = "host.com"
    val port = 3306
    val database = "schema"
    val username = "username"
    val password = "password"
    val pool = MySQLConnectionBuilder.createConnectionPool(
        "jdbc:mysql://$host:$port/$database?user=$username&password=$password"
    )
    val future = pool.sendPreparedStatement("select * from table limit 2")
    val queryResult = future.get()
    println((queryResult.rows!![0] as ArrayRowData).columns.toList())
    println((queryResult.rows!![1] as ArrayRowData).columns.toList())
    pool.disconnect().get()
}
