package com.github.jasync.mysql.example;

import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.SSLConfiguration;
import com.github.jasync.sql.db.general.ArrayRowData;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.util.CharsetMapper;
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

public class MainJava {

  private static Logger logger = LoggerFactory.getLogger(MainJava.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    logger.error("starting");
    logger.warn("starting warn");
    logger.info("starting info");
    logger.debug("starting debug");
    logger.trace("starting trace");
    Connection connection = new MySQLConnection(
      new Configuration(
        "username",
        "host.com",
        3306,
        "password",
        "schema",
        new SSLConfiguration(),
        CharsetUtil.UTF_8,
        16777216,
        PooledByteBufAllocator.DEFAULT,
        Duration.ofSeconds(5),
        Duration.ofSeconds(5),
        null
      ),
      CharsetMapper.Companion.getInstance(),
      NettyUtils.INSTANCE.getDefaultEventLoopGroup(),
      ExecutorServiceUtils.INSTANCE.getCommonPool()
    );
    connection.connect().get();
    CompletableFuture<QueryResult> future = connection.sendPreparedStatement("select * from table limit 2", new ArrayList<>());
    QueryResult queryResult = future.get();
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(0))).getColumns()));
    System.out.println(Arrays.toString(((ArrayRowData) (queryResult.getRows().get(1))).getColumns()));
  }
}
