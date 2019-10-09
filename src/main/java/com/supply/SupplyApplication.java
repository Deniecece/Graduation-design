package com.supply;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SupplyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyApplication.class, args);
	}

}
