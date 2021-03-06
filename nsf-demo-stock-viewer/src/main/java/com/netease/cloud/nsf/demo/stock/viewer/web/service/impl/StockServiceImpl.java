package com.netease.cloud.nsf.demo.stock.viewer.web.service.impl;

import java.util.List;
import java.util.concurrent.*;

import com.netease.cloud.nsf.demo.stock.viewer.client.IProviderFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netease.cloud.nsf.demo.stock.viewer.web.entity.Stock;
import com.netease.cloud.nsf.demo.stock.viewer.web.service.IStockService;
import com.netease.cloud.nsf.demo.stock.viewer.web.util.CastKit;

@Service
public class StockServiceImpl implements IStockService {

    private static Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("custom")
    RestTemplate restTemplateCustom;

    @Autowired
    IProviderFeignClient providerFeignClient;

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Value("${stock_provider_url}")
    String stockProviderUrl;

    @Value("${stock_advisor_url}")
    String stockAdvisorUrl;

    @Value("${stock_predictor_url}")
    String stockPredictorUrl;
    
	// 超时
    @Override
    public  List<Stock> getStockList(int delay) throws  Exception {

        log.info("start to get stock list ...");
        List<Stock> stocks;
        String finalUrl = stockProviderUrl + "/stocks?delay=" + delay;
        String stocksStr = restTemplate.getForObject(finalUrl, String.class);
        stocks = CastKit.str2StockList(stocksStr);
        log.info("get all stocks from {} successful : {}", finalUrl, stocks);
        return stocks;

    }


    @Override
    public Stock getStockById(String stockId) throws Exception {

        Stock stock = null;
        String finalUrl = stockProviderUrl + "/stocks/" + stockId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("stockCode", stockId);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> respEntity = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, String.class);

        stock = CastKit.str2Stock(respEntity.getBody());
        log.info("get stock from {} by {} successful : {}", finalUrl, stockId, stock);
        return stock;
    }


    @Override
    public List<Stock> getHotStockAdvice() throws Exception {

        log.info("start to get hot stock advice ...");
        List<Stock> stocks;
//        counter++;
//        if (counter % 3 != 0) {
//            throw new Exception("cannot not fetch hot advice : connection failed");
//        }
        String finalUrl = stockAdvisorUrl + "/advices/hot";
        String stocksStr = restTemplate.getForObject(finalUrl, String.class);
        stocks = CastKit.str2StockList(stocksStr);
        log.info("get hot stock advice from {} successful : {}", finalUrl, stocks);

        return stocks;
    }

    @Override
    public String echoAdvisor(int times) {

        StringBuilder sBuilder = new StringBuilder();
        String url = stockAdvisorUrl + "/echo";
        while (times-- > 0) {
            sBuilder.append(restTemplate.getForObject(url + "?p=" + times, String.class));
        }
        return sBuilder.toString();
    }

    @Override
    public String echobyecho(int times) {
        StringBuilder sBuilder = new StringBuilder();
        String url = stockAdvisorUrl + "/echobyecho";
        while (times-- > 0) {
            sBuilder.append(restTemplate.getForObject(url + "?p=" + times, String.class));
        }
        return sBuilder.toString();
    }

    @Override
    public String echobyechoAsync(int times) {
        StringBuilder sBuilder = new StringBuilder();
        String url = stockAdvisorUrl + "/echobyechoAsync";
        while (times-- > 0) {
            Future<String> f = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        return "viewer execute in " + Thread.currentThread().getName() + " get " + restTemplate.getForObject(url, String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "error: " + e.getMessage();
                    }
                }
            });
            try {
                sBuilder.append(f.get());
            } catch (Exception e) {
                sBuilder.append(e.getMessage() + System.lineSeparator());
                e.printStackTrace();
            }
        }
        return sBuilder.toString();
    }

    @Override
    public String echoProvider(int times) {

        StringBuilder sBuilder = new StringBuilder();
//        String url = stockProviderUrl + "/echo";
        String result = "use OpenFeignClient\n";
        while (times-- > 0) {
            result ="";
            try {
//                result = restTemplate.getForObject(url + "?p=" + times, String.class);
                result  = providerFeignClient.echo();
            } catch (Exception e) {
                result = e.getMessage() + "\r\n";
            }

            sBuilder.append(result);
        }
        return sBuilder.toString();
    }

    @Override
    public String getExternalUrl(String externalUrl) {

        StringBuilder sBuilder = new StringBuilder();
        String url = externalUrl;
        String result;

        sBuilder.append(url);

        try {
            result = restTemplateCustom.getForObject(url , String.class);
        } catch (Exception e) {
            result = e.getMessage() + "\r\n";
        }

        sBuilder.append(result);
        return sBuilder.toString();
    }

    @Override
    public String deepInvoke(int times) {
        if (times-- > 0) {
            return restTemplate.getForObject(stockAdvisorUrl + "/deepInvoke?times=" + times, String.class);
        }
        return "finish";
    }

    @Override
    public String getMaxSpreadStock() {
        log.info("start to get max spread stock ...");
        String finalUrl = stockPredictorUrl + "/stocks/spread";
        String spread = restTemplate.getForObject(finalUrl, String.class);
        log.info("get max spread stock from {} successful : {}", finalUrl, spread);
        return spread;
    }

    @Override
    public String getPredictPriceById(String stockId) {
        log.info("start to get predict stock price...");
        String finalUrl = stockPredictorUrl + "/stocks/predictPrice/" + stockId;
        String predictPrice = restTemplate.getForObject(finalUrl, String.class);
        log.info("get predict stock price from {} successful : {}", finalUrl, predictPrice);
        return predictPrice;
    }
}
