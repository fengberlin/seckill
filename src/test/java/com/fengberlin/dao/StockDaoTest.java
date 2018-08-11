package com.fengberlin.dao;

import com.fengberlin.dao.entity.Stock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class StockDaoTest {

    @Autowired
    private StockDao stockDao;

    @Test
    public void reduceNumber() throws Exception {
        Date now = new Date();
        long id = 1000;
        int updateCount = stockDao.reduceNumber(id, now);
        System.out.println("updateCount = " + updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Stock stock = stockDao.queryById(id);
        Assert.assertEquals(id, stock.getSeckillId());
        System.out.println(stock.toString());
    }

    @Test
    public void queryByLimit() throws Exception {
        List<Stock> stockList = stockDao.queryByLimit(0, 10);
        for (Stock stock : stockList) {
            System.out.println(stock.toString());
        }
    }
}