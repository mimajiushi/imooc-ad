package com.imooc.kafkastudy;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by Qinyi.
 * 分区类
 */
public class CustomPartitioner implements Partitioner {

    /**
     *
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic,
                         Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes,
                         Cluster cluster) {

        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
//        分区个数
        int numPartitions = partitionInfos.size();

        if (null == keyBytes || !(key instanceof String)) {
            throw new InvalidRecordException("kafka message must have key");
        }

        if (numPartitions == 1) {
            return 0;
        }

        if (key.equals("name")) {
            return numPartitions - 1;
        }
        //Utils.murmur2 根据key获取哈希值
        return Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
