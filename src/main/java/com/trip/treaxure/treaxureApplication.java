package com.trip.treaxure;

import com.trip.treaxure.user.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackageClasses = { User.class })
public class treaxureApplication {

	public static void main(String[] args) {
		SpringApplication.run(treaxureApplication.class, args);
	}
}