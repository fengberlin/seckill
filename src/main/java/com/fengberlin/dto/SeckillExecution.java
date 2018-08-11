package com.fengberlin.dto;

import com.fengberlin.dao.entity.SuccessKilled;
import com.fengberlin.enums.SeckillStateInfoEnum;

/**
 * 封装秒杀执行后的结果
 */
public class SeckillExecution {

    private long seckillId;

    // 秒杀执行结果状态
    private int state;

    // 解释state具体信息
    private String stateInfo;

    // 秒杀成功返回秒杀明细
    private SuccessKilled successKilled;

    // 成功时
    public SeckillExecution(long seckillId, SeckillStateInfoEnum stateInfoEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateInfoEnum.getState();
        this.stateInfo = stateInfoEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    // 失败时


    public SeckillExecution(long seckillId, SeckillStateInfoEnum stateInfoEnum) {
        this.seckillId = seckillId;
        this.state = stateInfoEnum.getState();
        this.stateInfo = stateInfoEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
