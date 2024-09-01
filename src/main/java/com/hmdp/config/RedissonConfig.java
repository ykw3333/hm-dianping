package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.78.130:6379").setPassword("789600");
        // 创建RedissonClient对象
        return Redisson.create(config);
    }

//    @Bean
//    public RedissonClient redissonClient2() {
//        // 配置
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.78.130:6382").setPassword("789600");
//        // 创建RedissonClient对象
//        return Redisson.create(config);
//    }
//
//    @Bean
//    public RedissonClient redissonClient3() {
//        // 配置
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.78.130:6383").setPassword("789600");
//        // 创建RedissonClient对象
//        return Redisson.create(config);
//    }
}
