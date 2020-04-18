package com.example.demo;

import com.example.demo.constants.Lang;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@SpringBootApplication
public class NewReplaceApplication implements CommandLineRunner {

    public NewReplaceApplication(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = replaceTemplate(redisTemplate.getConnectionFactory());
    }

    private RedisTemplate<Object, Object> replaceTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        SerializingConverter serializingConverter = new ReplacingSerializingConverter();
        DeserializingConverter deserializingConverter = new ReplacingDeserializingConverter();
        JdkSerializationRedisSerializer serializer =
                new JdkSerializationRedisSerializer(serializingConverter, deserializingConverter);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewReplaceApplication.class, args);
    }

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        readOld();
        // writeNew();
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
