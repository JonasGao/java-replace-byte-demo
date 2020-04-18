package com.example.demo;

import com.example.demo.constants.Lang;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class NewOverrideApplication implements CommandLineRunner {

    public NewOverrideApplication(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewOverrideApplication.class, args);
    }

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        readOld();
        writeNew();
    }

    private void writeNew() {
        User user = new User();
        user.setName("Alan");
        user.setAge(99);
        user.setLang(Lang.EN);
        HashOperations<Object, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put("FKEY-NEW", "SKEY-NEW", user);
    }

    private void readOld() {
        HashOperations<Object, Object, Object> opsForHash = redisTemplate.opsForHash();
        Object obj = opsForHash.get("FKEY-OLD", "SKEY-OLD");
        if (obj == null) {
            System.out.println("null");
            return;
        }
        User user = (User) obj;
        System.out.println(user);
    }
}
