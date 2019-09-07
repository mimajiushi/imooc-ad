package com.imooc.ad.runner;

import com.imooc.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Qinyi.
 * 启动时开启mysqlbinlog监听
 * CommandLineRunner是springboot的知识点，当项目启动后执行run
 */
@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {

    private final BinlogClient client;

    /** 将BinlogClient注入到 **/
    @Autowired
    public BinlogRunner(BinlogClient client) {
        this.client = client;
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info("Coming in BinlogRunner...");
        client.connect();
    }
}
