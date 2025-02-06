package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.json.JsonObject;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * For test, to be deleted
 */
@RestController
public class HelloController {

    private static AtomicLong counter = new AtomicLong();

    @GetMapping("/getHello")
    public JsonObject greeting(@RequestParam(value="name", defaultValue = "zack") String name) {
        JsonObject resObj = new JsonObject();
        resObj.put("count", counter.incrementAndGet())
                .put("say", "Hello, " + name);
        return resObj;
    }

    @PostMapping("/postHello")
    public JsonObject greeting(@RequestBody JsonObject jsonObject) {
        JsonObject resObj = new JsonObject();
        resObj.put("count", counter.incrementAndGet())
                .put("say", "Hello, " + jsonObject.getString("name"));
        return resObj;
    }

}
