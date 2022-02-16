package com.netease.cloud.nsf.demo.stock.viewer.web.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.alibaba.dubbo.config.annotation.Reference;
//import com.netease.cloud.nsf.demo.stock.api.WallService;
//import com.netease.cloud.nsf.agent.core.circuitbreaker.NsfRateLimiterInvokeException;
//import com.netease.cloud.nsf.agent.core.exception.NsfRateLimiterException;
import com.netease.cloud.nsf.demo.stock.viewer.web.entity.HttpResponse;
import com.netease.cloud.nsf.demo.stock.viewer.web.entity.Stock;
import com.netease.cloud.nsf.demo.stock.viewer.web.manager.LogManager;
import com.netease.cloud.nsf.demo.stock.viewer.web.service.IStockService;


/**
 * @author Chen Jiahan | chenjiahan@corp.netease.com
 */
@Controller
public class PanelController implements EnvironmentAware {

    private static Logger log = LoggerFactory.getLogger(PanelController.class);

    private Environment environment;

    @Autowired
    IStockService stockService;

//    @Reference
//    WallService wallService;
    
    @Value("${dubbo:false}")
    String dubbo_switch;
    
    @GetMapping(value = {"" , "/index"})
    public String indexPage(){
    	if(dubbo_switch != null && dubbo_switch.equals("true")) return "index";
        return "index_non_dubbo";
    }
    
    @GetMapping(value = "/stocks", produces = "application/json")
    @ResponseBody
    public HttpResponse getStockList(@RequestParam(name = "delay", required = false, defaultValue = "0") int delay) {

        List<Stock> stocks;
        try {
            stocks = stockService.getStockList(delay);
        } catch (Exception e) {
            log.warn("get stock list failed ...");
            log.warn("", e);
            return handleExceptionResponse(e);
        }
        return new HttpResponse(stocks);
    }

    @GetMapping(value = "/advices/hot", produces = "application/json")
    @ResponseBody
    public HttpResponse getHotAdvice() {

        List<Stock> stocks;
        try {
            stocks = stockService.getHotStockAdvice();
        } catch (Exception e) {
        	e.printStackTrace();
            log.warn("get hot stock advice failed ...");
            log.warn("", e);
            return handleExceptionResponse(e);
        }
        return new HttpResponse(stocks);
    }

    @GetMapping("/stocks/{stockId}")
    @ResponseBody
    public Stock getStockById(@PathVariable String stockId) {

        Stock stock = null;
        try {
            stock = stockService.getStockById(stockId);
        } catch (Exception e) {
        	e.printStackTrace();
            log.warn("get stock[{}] info failed ...", stockId);
        }
        return stock;
    }

    @GetMapping("/logs")
    @ResponseBody
    public HttpResponse getHttpLog() {
    	return new HttpResponse(LogManager.logs());
    }
    
    @GetMapping("/logs/clear")
    @ResponseBody
    public HttpResponse clearLogs() {
    	LogManager.clear();
    	return new HttpResponse("clear logs success");
    }
    
    @GetMapping("/echo/advisor")
    @ResponseBody
    public HttpResponse echoAdvisor(HttpServletRequest request,
    		@RequestParam(name = "time", defaultValue = "10", required = false) int time) {
    	String result = stockService.echoAdvisor(time);
    	LogManager.put(UUID.randomUUID().toString(), result);
    	return new HttpResponse(result);
    }

    @GetMapping("/echo/externalUrl")
    @ResponseBody
    public HttpResponse echoExternalUrl(HttpServletRequest request,
                                        @RequestParam(name = "externalUrl", defaultValue = "http://api.money.126.net/data/feed/000001", required = false) String externalUrl) {
        String result = stockService.getExternalUrl(externalUrl);
        LogManager.put(UUID.randomUUID().toString(), result);
        return new HttpResponse(result);
    }

    @GetMapping("/echobyecho")
    @ResponseBody
    public HttpResponse echobyecho(HttpServletRequest request,
                                    @RequestParam(name = "time", defaultValue = "10", required = false) int time) {
        String result = stockService.echobyecho(time);
        LogManager.put(UUID.randomUUID().toString(), result);
        return new HttpResponse(result);
    }

    @GetMapping("/echobyechoAsync")
    @ResponseBody
    public HttpResponse echobyechoAsync(HttpServletRequest request,
                                   @RequestParam(name = "time", defaultValue = "10", required = false) int time) {
        String result = stockService.echobyechoAsync(time);
        LogManager.put(UUID.randomUUID().toString(), result);
        return new HttpResponse(result);
    }

    @GetMapping("/echo/provider")
    @ResponseBody
    public HttpResponse echoProvider(HttpServletRequest request,
    		@RequestParam(name = "time", defaultValue = "10", required = false) int time) {
    	String result = stockService.echoProvider(time);
    	LogManager.put(UUID.randomUUID().toString(), result);
    	return new HttpResponse(result);
    }
    
    @GetMapping("/health")
    @ResponseBody
    public String health() {
    	return "I am good!";
    }
    
    @RequestMapping("/deepInvoke")
    @ResponseBody
    public String deepInvoke(@RequestParam int times) {
    	return stockService.deepInvoke(times);
    }
    
    private HttpResponse handleExceptionResponse(Exception e) {
        NsfExceptionUtil.NsfExceptionWrapper nsfException = NsfExceptionUtil.parseException(e);
        log.error(nsfException.getThrowable().getMessage());
        if(nsfException.getType() == NsfExceptionUtil.NsfExceptionType.NORMAL_EXCEPTION){
            //return new HttpResponse(nsfException.getThrowable().getMessage());
            return new HttpResponse(nsfException.getThrowable().toString());
        }
        return new HttpResponse(nsfException.getType().getDesc());
    }

    //最大/最小差价
    @GetMapping("/spread")
    @ResponseBody
    public String getMaxSpreadStock() {
        String MaxSpreadStock=null;
        try {
            MaxSpreadStock=stockService.getMaxSpreadStock();
        }
        catch (Exception e){
            log.warn("get max stock spread failed ...");
        }
        return MaxSpreadStock;
    }

    //预测股票数据
    @GetMapping("/predictPrice/{stockId}")
    @ResponseBody
    public String getPredictPriceById(@PathVariable String stockId) {
        String PredictPrice=null;
        try {
            PredictPrice=stockService.getPredictPriceById(stockId);
        }
        catch (Exception e){
            log.warn("get predict stock price failed ...");
        }
        return PredictPrice;
    }

    @GetMapping(value = "/getConfigs")
    @ResponseBody
    public String getConfig(@RequestParam String key) {
        return environment.getProperty(key);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}