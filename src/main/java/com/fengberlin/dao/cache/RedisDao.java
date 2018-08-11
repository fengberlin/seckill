package com.fengberlin.dao.cache;

import com.fengberlin.dao.entity.Stock;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private RuntimeSchema<Stock> schema = RuntimeSchema.createFrom(Stock.class);

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Stock getStock(long stockId) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "stock:" + stockId;
                // 使用protocolstuff序列化工具
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    // 创建一个空的Stock对象
                    Stock stock = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes, stock, schema);
                    return stock;    // 反序列化
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public String putStock(Stock stock) {
        // 序列化stock对象
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "stock:" + stock.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(stock, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 设置超时时间
                int timeout = 60*60;    // 一小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
