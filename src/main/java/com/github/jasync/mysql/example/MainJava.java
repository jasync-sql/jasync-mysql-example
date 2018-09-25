package com.github.jasync.mysql.example;

import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.SSLConfiguration;
import com.github.jasync.sql.db.general.ArrayRowData;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import com.github.jasync.sql.db.mysql.util.CharsetMapper;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.pool.PoolConfiguration;
import com.github.jasync.sql.db.util.ExecutorServiceUtils;
import com.github.jasync.sql.db.util.NettyUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainJava {

  private static Logger logger = LoggerFactory.getLogger(MainJava.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    logger.error("starting");
    logger.warn("starting warn");
    logger.info("starting info");
    logger.debug("starting debug");
    logger.trace("starting trace");

    PoolConfiguration poolConfiguration = new PoolConfiguration(
      100,                            // maxObjects
      TimeUnit.MINUTES.toMillis(15),  // maxIdle
      10_000,                         // maxQueueSize
      TimeUnit.SECONDS.toMillis(30)   // validationInterval
    );
    Connection connection = new ConnectionPool<>(
      new MySQLConnectionFactory(new Configuration(
        "username",
        "host.com",
        3306,
        "password",
        "schema"
      )), poolConfiguration);
    connection.connect().get();
    CompletableFuture<QueryResult> future = connection.sendPreparedStatement("select * from table limit 2");
    QueryResult queryResult = future.get();
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(0))).getColumns()));
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(1))).getColumns()));
  }
}
