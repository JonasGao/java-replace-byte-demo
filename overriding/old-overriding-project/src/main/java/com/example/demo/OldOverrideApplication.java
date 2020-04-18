package com.example.demo;

import com.example.demo.constant.Lang;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class OldOverrideApplication implements CommandLineRunner {

    public OldOverrideApplication(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(OldOverrideApplication.class, args);
    }

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        writeOld();
        readNew();
    }

    private void writeOld() {
        User user = new User();
        user.setName("John Walker");
        user.setAge(100);
        user.setLang(Lang.CN);
        HashOperations<Object, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put("FKEY-OLD", "SKEY-OLD", user);
    }

    private void readNew() {
        HashOperations<Object, Object, Object> opsForHash = redisTemplate.opsForHash();
        Object obj = opsForHash.get("FKEY-NEW", "SKEY-NEW");
        if (obj == null) {
            System.out.println("null");
            return;
        }
        User user = (User) obj;
        System.out.println(user);
    }
}
