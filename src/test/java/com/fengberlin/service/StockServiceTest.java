package com.fengberlin.service;

import com.fengberlin.dao.entity.Stock;
import com.fengberlin.dto.Exposer;
import com.fengberlin.dto.SeckillExecution;
import com.fengberlin.enums.SeckillStateInfoEnum;
import com.fengberlin.exception.RepeatKillException;
import com.fengberlin.exception.SeckillCloseException;
import com.fengberlin.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class StockServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StockService stockService;

    @Test
    public void getStockList() {
        List<Stock> stockList = stockService.getStockList();
        logger.info("list = {}", stockList);
    }

    @Test
    public void getById() {
        long id = 1000;
        Stock stock = stockService.getById(id);
        logger.info("stock = {}", stock);
    }

    @Test
    public void testSeckillLogic() {
        long id = 1000;
        Exposer exposer = stockService.exportSeckillURL(id);
        if (exposer.isExposed() == true) {
            logger.info("exposer = {}", exposer);
            long userPhone = 8221721729L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = stockService.executeSeckill(id, userPhone, md5);
                logger.info("result = {}", seckillExecution);
            }catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (SeckillException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开启
            logger.warn("exposer = {}", exposer);
        }
    }
}