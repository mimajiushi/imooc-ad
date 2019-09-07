package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qinyi.
 * todo 暂时不明
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    /**
     * 操作类型和要操作的字段列表
     */
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     * 字段索引 -> 字段名
     * */
    private Map<Integer, String> posMap = new HashMap<>();
}
