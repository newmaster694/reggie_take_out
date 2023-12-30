package com.newmaster.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class ReggieApplicationTests {

    @Test
    public void testRedis() {
        //获取连接
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        //执行具体的操作
        jedis.set("username", "newmaster");
        String username = jedis.get("username");
        System.out.println(username);

        jedis.del("username");

        //关闭连接
        jedis.close();
    }

}
