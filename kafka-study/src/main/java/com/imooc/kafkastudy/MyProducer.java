package com.imooc.kafkastudy;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.lang.reflect.Executable;
import java.util.Properties;

/**
 * Created by Qinyi.
 * 我们用的是虚拟机，要额外设置kafka允许客户端访问
 */
public class MyProducer {

    private static KafkaProducer<String, String> producer;

    static {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.129.133:9092");
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        properties.put("partitioner.class",
                "com.imooc.kafkastudy.CustomPartitioner");

        producer = new KafkaProducer<>(properties);
    }

    /**
     * 只发送消息不管结果
     */
    private static void sendMessageForgetResult() {

        ProducerRecord<String, String> record = new ProducerRecord<>(
                "imooc_ad_test_x", "name", "ForgetResult"
        );
        producer.send(record);
        producer.close();
        System.out.println("send success!");
    }

    /**
     * 同步发送消息
     * @throws Exception 异常
     */
    private static void sendMessageSync() throws Exception {

        ProducerRecord<String, String> record = new ProducerRecord<>(
                "imooc_ad_test_x", "name", "sync"
        );
        //同步等待获取响应
        RecordMetadata result = producer.send(record).get();

        System.out.println(result.topic());
        // 分区
        System.out.println(result.partition());
        // 偏移量
        System.out.println(result.offset());

        producer.close();
    }

    /**
     * 异步发送
     * 创建topic
     * ./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic imooc_ad_study_x
     */
    private static void sendMessageCallback() {

        ProducerRecord<String, String> record = new ProducerRecord<>(
                "imooc_ad_study_x", "name", "callback"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "imooc_ad_study_x", "name-x", "callback"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "imooc_ad_study_x", "name-y", "callback"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "imooc_ad_study_x", "name-z", "callback"
        );
        producer.send(record, new MyProducerCallback());

        producer.close();
    }

    /**
     * 异步发送回调类
     */
    private static class MyProducerCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {

            if (e != null) {
                e.printStackTrace();
                return;
            }

            System.out.println("主题:" + recordMetadata.topic());
            // 分区
            System.out.println("分区:" + recordMetadata.partition());
            // 便宜量
            System.out.println("偏移量" + recordMetadata.offset());
            System.out.println("Coming in MyProducerCallback");
        }
    }

    public static void main(String[] args) throws Exception {

//        sendMessageForgetResult();
//        sendMessageSync();
        sendMessageCallback();
    }
}
