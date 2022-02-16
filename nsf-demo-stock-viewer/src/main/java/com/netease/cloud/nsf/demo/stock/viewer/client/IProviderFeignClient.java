package com.netease.cloud.nsf.demo.stock.viewer.client;

import com.netease.cloud.nsf.demo.stock.viewer.web.entity.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZJF | zhujianfeng01@corp.netease.com
 */
@FeignClient(name = "stock-provider-local")
//@RequestMapping("/feign")
public interface IProviderFeignClient {

//	@GetMapping("/stocks/{stockIds}")
//	public List<Stock> getStocksByIds(@PathVariable String stockIds,
//									  @RequestParam(name = "delay", required = false, defaultValue = "0") int delay) throws InterruptedException;
//
//	@GetMapping("/stocks")
//	public List<Stock> getAllStocks(@RequestParam(name = "delay", required = false, defaultValue = "0") int delay)throws InterruptedException;

//
//	@GetMapping("/hi")
//	public String greeting(HttpServletRequest request);
//
	@GetMapping("/echo")
	public String echo();
//
//	@GetMapping("/echobyecho")
//	public String echobyecho(HttpServletRequest request);
//
//	@PostMapping("echoPost")
//	public Object echoPost(@RequestBody Object obj);
//
//	@GetMapping("/health")
//	@ResponseBody
//	public String health();
//
//	@GetMapping("/sleepgw")
//	public String sleepgw(HttpServletRequest request, String msg) throws InterruptedException;
//
//	@GetMapping("/getQueryString")
//	public String getQueryString(HttpServletRequest request);

}
