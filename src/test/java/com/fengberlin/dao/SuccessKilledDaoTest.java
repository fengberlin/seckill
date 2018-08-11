package com.fengberlin.dao;

import com.fengberlin.dao.entity.SuccessKilled;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    SuccessKilledDao successKilledDao;

    @Test
    public void insertItem() throws Exception {
        long id = 1000L;
        long userPhone = 12345678L;
        int insertCount = successKilledDao.insertItem(id, userPhone);
        System.out.println("insertCount = " + insertCount);
    }

    @Test
    public void queryByIdWithStockItem() throws Exception {
        long id = 1000L;
        long userPhone = 12345678L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithStockItem(id, userPhone);
        Assert.assertEquals(id, successKilled.getSeckillId());
        Assert.assertEquals(userPhone, successKilled.getUserPhone());
        System.out.println(successKilled.toString());
        System.out.println(successKilled.getStock().toString());
    }
}