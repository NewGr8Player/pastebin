package com.xavier.pastebin.service;

import com.xavier.pastebin.entity.DataEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheService {

    private StringRedisTemplate stringRedisTemplate;

    /**
     * Puts the dataEntity object into the cache with the specified key.
     *
     * @param key        the key to associate with the dataEntity
     * @param dataEntity the DataEntity object to be cached
     */
    public void putCache(String key, DataEntity dataEntity) {
        Pair<Long, TimeUnit> expire = dataEntity.expire();

        stringRedisTemplate.opsForHash().putAll(key, dataEntity.toMap());
        stringRedisTemplate.expire(key, expire.getFirst(), expire.getSecond());
    }

    /**
     * Retrieves the cached DataEntity object associated with the specified key.
     *
     * @param key the key associated with the cached DataEntity object
     * @return the cached DataEntity object, or null if it doesn't exist
     */
    public DataEntity getCache(String key) {
        Map<String, String> typedStringEntryMap = Optional.of(stringRedisTemplate.opsForHash().entries(key))
                .orElse(new HashMap<>())
                .entrySet().stream()
                .collect(Collectors.toMap(
                        k -> k.getKey().toString(),
                        v -> v.getValue().toString())
                );

        return DataEntity.fromMap(typedStringEntryMap);
    }

    public List<Map<String,Object>> getAllCache() {
        Set<String> keys = stringRedisTemplate.opsForHash().keys("X*").stream().map(e -> (String) e).collect(Collectors.toSet());

        return List.of(Map.of("keys", keys));
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
