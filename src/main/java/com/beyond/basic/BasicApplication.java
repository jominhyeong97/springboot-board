package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 스캔 가능하다.
@SpringBootApplication

//주로 웹서블릿 기반의 구성요소를 스캔, 자동으로 Bean 으로 등록
@ServletComponentScan
//스케쥴러 사용시 필요
@EnableScheduling
public class BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}

}
