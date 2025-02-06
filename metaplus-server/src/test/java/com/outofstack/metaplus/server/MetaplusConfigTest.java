package com.outofstack.metaplus.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MetaplusServerAppTest.class)
public class MetaplusConfigTest {

    @Autowired
    private MetaplusConfig metaplusConfig;

    @Test
    public void testOne() {
        System.out.println("profile: " + metaplusConfig.getProfile());
        System.out.println("baseUrl: " + metaplusConfig.getEs().getBaseUrl());
    }
}
