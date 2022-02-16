package com.netease.cloud.nsf.demo.stock.provider.web.controller;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.netease.cloud.nsf.demo.stock.provider.web.service.IProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import com.netease.cloud.nsf.demo.stock.provider.web.entity.Stock;

/**
 * @author Chen Jiahan | chenjiahan@corp.netease.com
 */
@RestController
public class StockController {

	private static Logger log = LoggerFactory.getLogger(StockController.class);

	@Autowired
	IProviderService stockService;

	/**
	 * @param stockIds
	 *            以","分隔 , 单个id也可查询
	 * @return
	 * @throws InterruptedException
	 */
	@GetMapping("/stocks/{stockIds}")
	public List<Stock> getStocksByIds(@PathVariable String stockIds,
			@RequestParam(name = "delay", required = false, defaultValue = "0") int delay) throws InterruptedException {
		Thread.sleep(delay * 1000);
		log.info("get /stocks/{} success", stockIds);
		return stockService.getStocksByIds(stockIds);
	}

	@GetMapping("/stocks")
	public List<Stock> getAllStocks(@RequestParam(name = "delay", required = false, defaultValue = "0") int delay)
			throws InterruptedException {
		Thread.sleep(delay * 1000);
		return stockService.getAllStocks();
	}

	@Value("${spring.application.name}")
	String name;

	@GetMapping("/hi")
	public String greeting(HttpServletRequest request) {

		String host = request.getServerName();
		int port = request.getServerPort();

		return "greeting from " + name + "[" + host + ":" + port + "]" + System.lineSeparator();
	}

	@GetMapping("/echo")
	public String echo(HttpServletRequest request) {
		log.info(" echo provider invoked");
		String host = request.getServerName();
		int port = request.getServerPort();

		return "echo from " + name + "[" + host + ":" + port + "]" + System.lineSeparator();
	}

    @GetMapping("/echobyecho")
    public String echobyecho(HttpServletRequest request) {
        String host = request.getServerName();
        int port = request.getServerPort();
        String color = request.getHeader("X-NSF-COLOR");
        StringBuilder sb = new StringBuilder(" meta[");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = (String) headerNames.nextElement();
            if (header.startsWith("nsf-biz-")) {
                sb.append(header).append(":").append(request.getHeader(header)).append(" ");
            }
        }
        sb.append("]");
        return "echo from " + name + "[" + host + ":" + port + "] color:" + color + sb.toString();
    }

    @PostMapping("echoPost")
    public Object echoPost(@RequestBody Object obj) {
        return obj;
    }

	@GetMapping("/health")
	@ResponseBody
	public String health() {
		return "I am good!";
	}

	/**
	 * 熔断测试
	 */
	int count = 0;
	@GetMapping("/sleepgw")
	public String sleepgw(HttpServletRequest request, String msg)  throws InterruptedException {
		if (count++ % 5 < 3) {
			TimeUnit.SECONDS.sleep(10);
		}
		return "第" + count + "次sleepgw,参数:" + msg + ",响应服务地址:" + request.getServerName() + ":" + request.getServerPort();
	}

	/**
	 * 测试网关自定义插件
	 * @param request
	 * @return
	 */
	@GetMapping("/getQueryString")
	public String getQueryString(HttpServletRequest request) {
		String result = "No Query String";

		Map<String, String[]> names = request.getParameterMap();
		if(name != null && names.size()>0){
			result = "";
			for(String key: names.keySet()){
				result = result + key + " : " + names.get(key)[0] + "<br/>";
			}
		}

		return result;
	}

}
