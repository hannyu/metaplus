package com.outofstack.metaplus.domain;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefinedDomains {


    public static DomainDoc getDefinedDomain(String domain) {
        String fileName = "domain_" + domain + ".json";
        try (InputStream is = DefinedDomains.class.getClassLoader().getResourceAsStream(fileName)) {
            if (null == is) {
                throw new IllegalArgumentException("Not find defined domain file '" + fileName + "'");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            return new DomainDoc(JsonObject.parse(br));
        } catch (IOException e) {
            throw new IllegalArgumentException("Read defined domain file '" + fileName + "' fail.", e);
        }
    }

}
