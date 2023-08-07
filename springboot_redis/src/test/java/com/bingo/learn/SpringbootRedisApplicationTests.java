package com.bingo.learn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootRedisApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void setTest(){
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("name","lisi");
    }
    @Test
    void getTest(){
        ValueOperations ops = redisTemplate.opsForValue();
        System.out.println(ops.get("name"));

    }
    @Test
    void hSetTest(){
        HashOperations ops = redisTemplate.opsForHash();
        ops.put("user","name","zhangsan");
    }
    @Test
    void hGetTest(){
        HashOperations ops = redisTemplate.opsForHash();
        System.out.println(ops.get("user", "name"));
    }
}
