package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RequestMapping("/")
@RestController
public class HelloController {

    private final AtomicLong counter = new AtomicLong(10000);

    @GetMapping({"/", "/hello"})
    public HttpResponse<JsonObject> hello(@RequestParam(value="name", defaultValue="buddy") String name) {
        return HttpResponse.ok(new JsonObject("say",
                "(" + counter.incrementAndGet() + ") Hey " + name + ", is everything okay?"));
    }


    @PostMapping({"/hello/echo/{name}", "/echo"})
    public HttpResponse<JsonObject> echo(@PathVariable(name = "name", required = false) String name,
                                         @RequestBody JsonObject requestBody) {
        return HttpResponse.ok(new JsonObject("reply", "(" + counter.incrementAndGet() + ") Hey " +
                (null!=name?name:"buddy") + ", I got your post.").put("your_post", requestBody));
    }

    @GetMapping("/wrong")
    public HttpResponse<JsonObject> wrong() {
        throw new IllegalArgumentException("Something wrong!");
    }

}
