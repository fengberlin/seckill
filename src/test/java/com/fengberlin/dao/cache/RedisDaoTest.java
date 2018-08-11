package com.fengberlin.dao.cache;

import com.fengberlin.dao.StockDao;
import com.fengberlin.dao.entity.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private StockDao stockDao;

    @Test
    public void testStock() {
        long id = 1000;
        Stock stock = redisDao.getStock(id);
        if (stock == null) {
            stock = stockDao.queryById(id);
            if (stock != null) {
                String result = redisDao.putStock(stock);
                System.out.println(result);
                stock = redisDao.getStock(id);
                System.out.println(stock);
            }
        }
    }
}