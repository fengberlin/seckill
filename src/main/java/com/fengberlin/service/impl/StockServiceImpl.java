package com.fengberlin.service.impl;

import com.fengberlin.dao.StockDao;
import com.fengberlin.dao.SuccessKilledDao;
import com.fengberlin.dao.cache.RedisDao;
import com.fengberlin.dao.entity.Stock;
import com.fengberlin.dao.entity.SuccessKilled;
import com.fengberlin.dto.Exposer;
import com.fengberlin.dto.SeckillExecution;
import com.fengberlin.enums.SeckillStateInfoEnum;
import com.fengberlin.exception.RepeatKillException;
import com.fengberlin.exception.SeckillCloseException;
import com.fengberlin.exception.SeckillException;
import com.fengberlin.service.StockService;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StockDao stockDao;

    @Autowired
    SuccessKilledDao successKilledDao;

    @Autowired
    RedisDao redisDao;

    // 盐，用作混淆，俗称加盐
    private static final String salt = "^bd&*jdlks#@`~./cjcj`\"dasjdja')eba151sa21hb-=ahga@^#$&()(*%$#FGHJD6265";

    // md5加密
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public List<Stock> getStockList() {
        List<Stock> stockList = stockDao.queryByLimit(0, 8);
        return stockList;
    }

    @Override
    public Stock getById(long seckillId) {
        Stock stock = stockDao.queryById(seckillId);
        return stock;
    }

    // 优化点：缓存优化。因为存在高并发，所以为了减轻数据库的压力，使用缓存来缓存接口地址。
    @Override
    public Exposer exportSeckillURL(long seckillId) {

        // 超时的基础上维护一致性
        Stock stock = redisDao.getStock(seckillId);
        if (stock == null) {
            stock = this.getById(seckillId);
            if (stock == null) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putStock(stock);
            }
        }

        Date startTime = stock.getStartTime();
        Date endTime = stock.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() ||
                nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillCloseException {
        if (md5 == null || md5.equals(getMd5(seckillId)) == false) {
            throw new SeckillException("seckill data rewrite.");
        }

        // 秒杀逻辑：插入秒杀明细+减库存 先减库存后插入明细
        // 因为mysql的update操作会持有行级锁，insert则可以并发操作，
        // 所以先insert后update能减少网络延迟和GC时间
        try {
            // 因为在xml映射文件中的sql语句加了ignore，所以插入失败时会返回0
            int insertCount = successKilledDao.insertItem(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                int updateCount = stockDao.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    // 没有更新记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed.");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithStockItem(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateInfoEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常转化为RuntimeException
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}
