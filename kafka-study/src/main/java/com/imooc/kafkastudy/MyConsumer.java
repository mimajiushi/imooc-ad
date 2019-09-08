package com.imooc.kafkastudy;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Qinyi.
 * 我们用的是虚拟机，要额外设置kafka允许客户端访问
 * 记得关掉虚拟机上的消费者，不然就会被它消费掉了
 * 这里提到的位移就是当前消费的哪个地方，避免同个组重复消费(大概是这个意思)
 */
public class MyConsumer {

    private static KafkaConsumer<String, String> consumer;
    private static Properties properties;

    static {

        properties = new Properties();

        properties.put("bootstrap.servers", "192.168.129.133:9092");
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        // 只要是不同消费组都能接受到全量信息，通常同一个消费组的不同消费者会消费不同的分区（也可以是一个）
        properties.put("group.id", "KafkaStudy");
    }

    /**
     * 自动提交消息的位移
     */
    private static void generalConsumeMessageAutoCommit() {

        properties.put("enable.auto.commit", true);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("imooc_ad_study_x"));

        try {
            while (true) {

                boolean flag = true;
                //拉取数据
                ConsumerRecords<String, String> records = consumer.poll(100); //超时时间

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(),
                            record.key(), record.value()
                    ));
                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }

                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动同步提交位移
     */
    private static void generalConsumeMessageSyncCommit() {

        //手动提交位移需要设置
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc_ad_study_x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            try {
                //手动提交，同步
                consumer.commitSync();
            } catch (CommitFailedException ex) {
                System.out.println("commit failed error: "
                        + ex.getMessage());
            }

            if (!flag) {
                break;
            }
        }
    }

    //手动异步提交
    private static void generalConsumeMessageAsyncCommit() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc_ad_study_x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            // commit A, offset 2000
            // commit B, offset 3000
            // 为什么异步不能有重试机制？假设a提交失败，b提交成功，a重试，位移又回到2000，那就会造成消息的重复消费
            // 异步提交，缺点，返回失败不会自动重试，但是同步会
            consumer.commitAsync();

            if (!flag) {
                break;
            }
        }
    }

    /**
     * 手动异步提交，回调，记录失败情况
     */
    private static void generalConsumeMessageAsyncCommitWithCallback() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc_ad_study_x"));

        while (true) {
            boolean flag = true;

            ConsumerRecords<String, String> records =
                    consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(),
                        record.key(), record.value()
                ));
                if (record.value().equals("done")) {
                    flag = false;
                }
            }

            //位移提交之后回调
            consumer.commitAsync((map, e) -> {
                // 错误 在这里仅打印一下
                if (e != null) {
                    System.out.println("commit failed for offsets: " +
                    e.getMessage());
                }
            });

            if (!flag) {
                break;
            }
        }
    }

    /**
     * 混合同步与异步提交(常用)
     * 程序退出了，希望提交也能成功
     */
    @SuppressWarnings("all")
    private static void mixSyncAndAsyncCommit() {

        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("imooc_ad_study_x"));

        try {

            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, " +
                                    "value = %s",
                            record.topic(), record.partition(),
                            record.key(), record.value()
                    ));
                }

                consumer.commitAsync();
            }
        } catch (Exception ex) {
            System.out.println("commit async error: " + ex.getMessage());
        } finally {
            try {
                // 这里再同步提交
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
//        generalConsumeMessageAutoCommit();
        mixSyncAndAsyncCommit();
    }
}
