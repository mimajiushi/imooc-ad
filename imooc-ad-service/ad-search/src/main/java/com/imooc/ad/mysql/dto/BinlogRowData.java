package com.imooc.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Qinyi.
 * 将binlog转换成Event
 */
@Data
public class BinlogRowData {

    private TableTemplate table;

    private EventType eventType;

    /**
     * 存储列名和值
     */
    private List<Map<String, String>> after;

    /**
     * 这个参数是无用的，只是为了对应
     */
    private List<Map<String, String>> before;
}
