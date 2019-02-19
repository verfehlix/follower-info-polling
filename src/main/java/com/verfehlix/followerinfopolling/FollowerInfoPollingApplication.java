package com.verfehlix.followerinfopolling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FollowerInfoPollingApplication {

	public static void main(String[] args) {

		SpringApplication.run(FollowerInfoPollingApplication.class, args);

	}


}

