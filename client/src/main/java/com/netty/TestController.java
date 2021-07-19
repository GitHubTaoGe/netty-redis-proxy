package com.netty;

import com.netty.api.Redis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("cache")
public class TestController {

    @Resource
    private Redis redis;

    @GetMapping("/get/{key}")
    public ResponseEntity get(@PathVariable("key") String key) {
        return ResponseEntity.ok(redis.get(key));
    }

    @PostMapping("/set/{key}/{value}")
    public ResponseEntity set(@PathVariable("key") String key, @PathVariable("value") String value) {
        return ResponseEntity.ok(redis.set(key, value));
    }


    @DeleteMapping("/del/{key}")
    public ResponseEntity del(@PathVariable("key") String key) {
        return ResponseEntity.ok(redis.del(key));
    }
}
