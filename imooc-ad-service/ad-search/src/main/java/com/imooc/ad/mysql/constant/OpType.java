package com.imooc.ad.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @author yuwen
 * @date 2019/1/29
 */
public enum OpType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

    /**
     * 判断mysql的操作类型
     * @param eventType 操作类型
     * @return 操作类型枚举
     */
    public static OpType to(EventType eventType) {
        if (EventType.isUpdate(eventType)) {
            return UPDATE;
        }
        if (EventType.isWrite(eventType)) {
            return ADD;
        }
        if (EventType.isDelete(eventType)) {
            return DELETE;
        }
        return OTHER;
    }
}
