package com.outofstack.metaplus.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyConfigTest {

    @Test
    public void testOne() {

        String tmpdir = PropertyConfig.get("tmpdir");
        System.out.println("tmpdir: " + tmpdir);
        assertFalse(tmpdir.isEmpty());

        System.setProperty("tmpdir", "/tmp/");
        tmpdir = PropertyConfig.get("tmpdir");
        System.out.println("tmpdir: " + tmpdir);
        assertEquals("/tmp/", tmpdir);
    }
}
