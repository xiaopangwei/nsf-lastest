package com.netease.cloud.nsf.demo.stock.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Chen Jiahan | chenjiahan@corp.netease.com
 */
@EnableFeignClients
@SpringBootApplication
public class StockViewerApplication extends SpringBootServletInitializer {



    public static void main(String[] args) {
        SpringApplication.run(StockViewerApplication.class, args);
    }
    
//    @Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(StockViewerApplication.class);
//	}

}
