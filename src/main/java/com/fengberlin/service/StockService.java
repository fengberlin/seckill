package com.fengberlin.service;

import com.fengberlin.dao.entity.Stock;
import com.fengberlin.dto.Exposer;
import com.fengberlin.dto.SeckillExecution;
import com.fengberlin.enums.SeckillStateInfoEnum;
import com.fengberlin.exception.RepeatKillException;
import com.fengberlin.exception.SeckillCloseException;

import java.util.List;

/**
 * 业务逻辑接口：要站在“使用者”的角度来设计接口
 * 三个方面：方法定义粒度，参数，返回类型
 */
public interface StockService {

    /**
     * 查询所有秒杀商品库存信息
     * @return
     */
    List<Stock> getStockList();

    /**
     * 查询单个秒杀商品库存信息
     * @param seckillId
     * @return
     */
    Stock getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
     * 这个方法的作用时在秒杀未开始时谁也不能知道秒杀的地址，防止不公平性
     * @param seckillId
     */
    Exposer exportSeckillURL(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillCloseException;
}
