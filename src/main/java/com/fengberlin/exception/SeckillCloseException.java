package com.fengberlin.exception;

/**
 * 秒杀结束(例如超时或者时间未到或者库存数量为0)还要去秒杀时抛出的异常
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
