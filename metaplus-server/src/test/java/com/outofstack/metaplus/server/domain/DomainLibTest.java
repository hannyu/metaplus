package com.outofstack.metaplus.server.domain;

import com.outofstack.metaplus.common.json.JsonRule;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.server.MetaplusServerAppTest;
import com.outofstack.metaplus.server.dao.DomainLib;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MetaplusServerAppTest.class)
public class DomainLibTest {

    private static final Logger log = LoggerFactory.getLogger(DomainLibTest.class);

    @Autowired
    private DomainLib domainLib;

    @Test
    public void testOne() {
        Map<String, DomainDoc> domains = domainLib.getAllDomainDocs();
        assert(!domains.isEmpty());
        for (String key : domains.keySet()) {
            DomainDoc doc = domains.get(key);
            System.out.println("========= domain:" + key);
            System.out.println("doc:" + doc);
        }
    }


    @Test
    public void testTwo() {
        List<JsonRule> jsonRules = domainLib.getRules("domain");
        for (JsonRule rule : jsonRules) {
            System.out.println("rule: " + rule);
            if ("$.meta.index.name".equals(rule.getJsonPath())) {
                assertTrue(rule.getRequired());
            }
            if ("$.meta.index.extend".equals(rule.getJsonPath())) {
                assertEquals("none", rule.getDefaultValue());
            }
        }
    }


    @Test
    public void testThree() {
        MetaplusDoc doc = domainLib.generateSampleDoc("domain");
        System.out.println("sample: " + doc);
        assertEquals("none", doc.getStringByPath("$.meta.index.extend"));
    }


    @Test
    public void testHour() {
    }

}
