package com.mrping.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mrping.face")
public class FaceDistinguishApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FaceDistinguishApplication.class, args);
	}
}
