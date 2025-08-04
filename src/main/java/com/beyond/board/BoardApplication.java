package com.beyond.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 스캔 가능하다.
@SpringBootApplication
//스케쥴러 사용시 필요
@EnableScheduling
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
