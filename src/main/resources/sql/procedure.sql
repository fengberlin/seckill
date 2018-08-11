-- 秒杀执行的储存过程
delimiter $$    -- 定义一个$$分隔符分隔每个存储过程
-- 定义存储过程 in:表示输入的参数, out:表示输出的参数
-- row_count():返回s数据库表被修改的(update, delete, insert)影响的行数
-- row_count():0表示未修改数据, >0表示修改的行数, <0表示sql语句错误或者未执行sql语句
create procedure `seckill`.`execute_seckill`(
    in v_seckill_id bigint, in v_phone_num bigint,
    in v_kill_time timestamp, out v_result int)
    begin
        declare insert_count int default 0;
        start transaction
        insert ignore into success_killed(seckill_id, user_phone, create_time, state)
        values(v_seckill_id, v_phone_num, v_kill_time, 0);
        select row_count() into insert_count;
        if (insert_count = 0) then
            rollback;
            set v_result = -1;    -- 重复秒杀
        else if (insert_count < 0) then
            rollback;
            set v_result = -2;    -- 系统异常
        else
            update seckill
            set number = number - 1;
            where seckill_id = v_seckill_id
            and end_time > v_kill_time
            and start_time < v_kill_time
            and number > 0;
            declare update_count int default 0;
            select row_count() into update_count;
            if (update_count = 0) then
                rollback;
                set v_result = 0;
            else if (update_count < 0) then
                rollback;
                set v_result = -2;
            else
                commit;
                set v_result = 1;
            end if;
        end if;
    end;
$$

delimiter ;
set @v_result = -4;
call execute_seckill(1009, 12345678909, now(), @v_result);
select @v_result;

-- 1.存储过程优化事务行级锁持有的时间
-- 2.不要过度依赖存储过程
-- 3.简单的逻辑可以应用存储过程