package com.uniware.integrations;

import java.net.URL;
import java.net.URLClassLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class FlipkartApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlipkartApplication.class, args);
//		ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//		URL[] urls = ((URLClassLoader)cl).getURLs();
//
//		for(URL url: urls){
//			System.out.println(url.getFile());
//		}

	}

}
