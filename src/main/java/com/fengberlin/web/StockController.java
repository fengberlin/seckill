package com.fengberlin.web;

import com.fengberlin.dao.entity.Stock;
import com.fengberlin.dto.Exposer;
import com.fengberlin.dto.SeckillExecution;
import com.fengberlin.dto.SeckillResult;
import com.fengberlin.enums.SeckillStateInfoEnum;
import com.fengberlin.exception.RepeatKillException;
import com.fengberlin.exception.SeckillCloseException;
import com.fengberlin.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {

        List<Stock> stockList = stockService.getStockList();
        model.addAttribute("stockList", stockList);
        return "list";    // 视图名
    }

    @RequestMapping(value = "/{stockId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("stockId") Long stockId, Model model) {

        if (stockId == null) {
            return "redirect:/stock/list";
        }
        Stock stock = stockService.getById(stockId);
        if (stock == null) {
            return "forward:/stock/list";
        }
        model.addAttribute("stock", stock);
        return "detail";    // 视图名
    }

    // 返回JSON
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

        SeckillResult<Exposer> result = null;
        try {
            Exposer exposer = stockService.exportSeckillURL(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "{seckillId}/{md5}/execution",
            method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "killPhone", required = false) Long phoneNum) {

        if (phoneNum == null) {
            return new SeckillResult<>(false, "未注册");
        }

        SeckillResult<SeckillExecution> result = null;
        try {
            SeckillExecution execution = stockService.executeSeckill(seckillId, phoneNum, md5);
            return new SeckillResult<>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateInfoEnum.REPEAT_KILL);
            return new SeckillResult<>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateInfoEnum.END);
            return new SeckillResult<>(true, execution);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateInfoEnum.INNER_ERROR);
            return new SeckillResult<>(true, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {

        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
