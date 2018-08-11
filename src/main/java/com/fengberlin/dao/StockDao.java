package com.fengberlin.dao;

import com.fengberlin.dao.entity.Stock;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StockDao {

    /**
     * 减库存
     * @param seckillId 商品库存id
     * @param killTime 秒杀的时间，对应于create-time
     * @return
     * @Param注解声明的参数是mapper映射xml文件中编写的sql语句用#{}或${}中引用的参数
     * 如果方法只有一个参数，可以不加注解，但两个参数或以上需要加上,但是为了统一，全部都加上@Param注解
     */
    int reduceNumber(@Param("seckillId") long seckillId,
                     @Param("killTime") Date killTime);

    /**
     * 按照商品库存的id查询商品库存对象
     * @param seckillId 商品库存id
     * @return
     */
    Stock queryById(@Param("seckillId") long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Stock> queryByLimit(@Param("offset") int offset,
                             @Param("limit") int limit);
}

