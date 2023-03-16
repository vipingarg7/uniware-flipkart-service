package com.uniware.integrations;

import java.net.URL;
import java.net.URLClassLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.unicommerce.*"}, exclude = { SecurityAutoConfiguration.class,  MongoAutoConfiguration.class  })
public class FlipkartApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlipkartApplication.class, args);
	}

}
