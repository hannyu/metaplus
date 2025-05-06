package com.outofstack.metaplus.server.rest;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.server.MetaplusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<JsonObject> catchException(Throwable e) {
        log.warn("Catch exception in RestController", e);
        System.err.println("Catch exception in RestController, " + e.getMessage());

        if (e instanceof IllegalArgumentException || e instanceof MetaplusException) {
            return ResponseEntity.badRequest().body(
                    new HttpResponse<JsonObject>(400, e.getClass().getSimpleName() + ": " + e.getMessage()));
        } else {
            return ResponseEntity.internalServerError().body(
                    new HttpResponse<JsonObject>(500, e.getClass().getSimpleName() + ": " + e.getMessage()));
        }

    }
}
