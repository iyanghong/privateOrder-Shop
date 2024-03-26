package com.clever.util;

import java.security.Signature;

/**
 * @Author xixi
 * @Date 2023-12-14 15:39
 **/
public class SnowflakeIdGenerator {
    private static final long EPOCH = 1672502400000L; // 设置起始时间戳，这里使用2021年1月1日的时间戳
    private static final long WORKER_ID_BITS = 5L; // 机器ID所占的位数
    private static final long DATACENTER_ID_BITS = 5L; // 数据中心ID所占的位数
    private static final long SEQUENCE_BITS = 12L; // 序列号所占的位数

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS); // 最大机器ID
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS); // 最大数据中心ID

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS; // 机器ID向左移12位
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS; // 数据中心ID向左移17位
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS; // 时间戳向左移22位
    private static SnowflakeIdGenerator instance;
    private final long workerId; // 机器ID
    private final long datacenterId; // 数据中心ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上次生成ID的时间戳

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID超出范围");
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID超出范围");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }


    public static synchronized SnowflakeIdGenerator getInstance() {
        if (instance == null) {
            // 在第一次调用时创建实例
            instance = new SnowflakeIdGenerator(1, 1);
        }
        return instance;
    }

    public static String getSnowflakeNextId() {
        return String.valueOf(getInstance().nextId());
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，无法生成ID");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT) |
                (datacenterId << DATACENTER_ID_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
