package com.fengberlin.dao;

import com.fengberlin.dao.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {

    /**
     * 插入秒杀购买条目
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertItem(@Param("seckillId") long seckillId,
                   @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled明细并返回这个对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithStockItem(@Param("seckillId") long seckillId,
                                         @Param("userPhone") long userPhone);
}