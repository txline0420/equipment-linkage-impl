package com.txl.linkage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.txl.linkage.mapper")
public class EquipmentLinkageApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipmentLinkageApplication.class, args);
    }

}
