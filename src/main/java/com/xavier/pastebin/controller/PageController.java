package com.xavier.pastebin.controller;


import com.xavier.pastebin.config.ratelimit.RateLimit;
import com.xavier.pastebin.entity.DataEntity;
import com.xavier.pastebin.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class PageController {

    private CacheService cacheService;

    @GetMapping("/")
    @RateLimit(times = 120, duration = 60)
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @GetMapping("/list")
    @RateLimit(times = 120, duration = 60)
    public ModelAndView list() {
        return new ModelAndView("list");
    }


    @PostMapping("/submit")
    @RateLimit(times = 60, duration = 60)
    public ModelAndView submit(@ModelAttribute DataEntity dataEntity) {
        String key = dataEntity.key();
        cacheService.putCache(key, dataEntity);
        return new ModelAndView("redirect:/" + key);
    }

    @GetMapping("/{pathHashValue}")
    @RateLimit(times = 120, duration = 60)
    public ModelAndView view(@PathVariable String pathHashValue) {
        ModelAndView mv = new ModelAndView("view");
        mv.addAllObjects(cacheService.getCache(pathHashValue).toMap());
        return mv;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
