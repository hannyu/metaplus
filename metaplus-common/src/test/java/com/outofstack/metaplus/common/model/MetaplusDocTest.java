package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MetaplusDocTest {

    @Test
    public void testOne() {
        // init from JsonObject
        JsonObject jo1 = JsonObject.parse("{\"fqmn\": {\"corp\": \"\", \"domain\": \"domain\", \"name\": \"book\"}," +
                " \"sync\": { \"createdAt\": \"2025-01-21T21:45:00.912+0800\"},\"meta\":{}}");
        MetaplusDoc doc = new MetaplusDoc(jo1);
        System.out.println("doc: " + doc);
        assertEquals("book", doc.getFqmnName());
        jo1.putByPath("$.fqmn.name", "water");
        assertEquals("water", doc.getFqmnName());
        System.out.println("doc: " + doc);
    }
}
