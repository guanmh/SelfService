package com.gmh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author 18380
 */
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.gmh.mapper.*", properties = {
		"notEmpty=true",
		"mappers=tk.mybatis.mapper.common.Mapper",
		"mappers=tk.mybatis.mapper.common.IdsMapper",
		"mappers=tk.mybatis.mapper.common.MySqlMapper"
})
@EnableCaching
@EnableWebSecurity
@ServletComponentScan
public class MyBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBootApplication.class, args);
	}

}
