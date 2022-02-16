package com.netease.cloud.nsf.demo.stock.advisor.web.controller;


import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netease.cloud.nsf.demo.stock.advisor.web.entity.Stock;
import com.netease.cloud.nsf.demo.stock.advisor.web.service.IAdvisorService;

@RestController
public class AdvisorController {

    private static Logger log = LoggerFactory.getLogger(AdvisorController.class);

    @Autowired
    IAdvisorService advisorService;

	/*@Autowired
	TestJavaConfigBean testJavaConfigBean;*/
	
	@GetMapping("/advices/hot")
	public List<Stock> getHotAdvice() throws Exception {
		return advisorService.getHotStocks();
	}
	
	@GetMapping("/hi")
	public List<String> greeting() {
		return advisorService.batchHi();
	}
	
	@Value("${spring.application.name}")
	String name;

    @GetMapping("/echo")
    public String echo(HttpServletRequest request) {
        log.info("echo advisor invoked");
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
        String res = advisorService.echobyecho();
        return "echo from " + name + "[" + host + ":" + port + "] color:" + color + sb.toString() + " | " + res + System.lineSeparator();
    }

    @GetMapping("/echobyechoAsync")
    public String echobyechoAsync(HttpServletRequest request) {
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
        String res = advisorService.echobyechoAsync();
        return "echo from " + name + "[" + host + ":" + port + "] color:" + color + sb.toString() + " | " + res + System.lineSeparator();
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "I am good!";
    }

    @RequestMapping("/deepInvoke")
    @ResponseBody
    public String deepInvoke(@RequestParam int times) {
        return advisorService.deepInvoke(times);
    }

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestBody String jsonString) {
		return "register json :\r\n" + jsonString;
	}

	@Value("${test:hi}")
	String test;

	@GetMapping("/test")
	public String TestApollo(){
		return test;
	}

	@Value("${test2}")
	String test2;

	@GetMapping("/test2")
	public String TestApollo2(){
		return test2;
	}
	
	@GetMapping("/divide")
	public String divide(HttpServletRequest request) {
		
		return advisorService.divide(request);

		
	}
}
