package com.imooc.ad.sender;


import com.imooc.ad.mysql.dto.MySqlRowData;

/**
 * 投递增量数据
 *
 * @author yuwen
 * @date 2019/1/31
 */
public interface ISender {

    void sender(MySqlRowData rowData);

}
