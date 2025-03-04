package com.outofstack.metaplus.common;

import org.junit.jupiter.api.Test;

public class PropertyUtilTest {

    @Test
    public void testOne() {
        String tempdir = PropertyUtil.getString("TMPDIR", "/tmp");
        System.out.println("tempdir: " + tempdir);
    }
}
