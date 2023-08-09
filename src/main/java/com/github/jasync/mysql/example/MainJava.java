package com.github.jasync.mysql.example;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.general.ArrayRowData;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import com.github.jasync.sql.db.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainJava {

  private static Logger logger = LoggerFactory.getLogger(MainJava.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    logger.error("starting");
    logger.warn("starting warn");
    logger.info("starting info");
    logger.debug("starting debug");
    logger.trace("starting trace");

    String host = "host.com";
    int port = 3306;
    String database = "schema";
    String username = "username";
    String password = "password";
    ConnectionPool<MySQLConnection> pool = MySQLConnectionBuilder.createConnectionPool(
            "jdbc:mysql://$host:$port/$database?user=$username&password=$password"
    );
    Connection connection = pool.connect().get();
    CompletableFuture<QueryResult> future = connection.sendPreparedStatement("select * from table limit 2");
    QueryResult queryResult = future.get();
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(0))).getColumns()));
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(1))).getColumns()));
  }
}
