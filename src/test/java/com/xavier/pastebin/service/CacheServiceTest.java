package com.xavier.pastebin.service;

import com.xavier.pastebin.PastebinApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheServiceTest extends PastebinApplicationTests {

    @Autowired
    private CacheService cacheService;

    public void getAllCacheTest(){
        cacheService.getAllCache();
    }
}
