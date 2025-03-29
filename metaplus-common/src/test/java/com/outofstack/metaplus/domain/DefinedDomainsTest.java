package com.outofstack.metaplus.domain;

import com.outofstack.metaplus.common.model.DomainDoc;
import org.junit.jupiter.api.Test;

public class DefinedDomainsTest {

    @Test
    public void testOne() {
        DomainDoc tableDomain = DefinedDomains.getDefinedDomain(TableDomain.DOMAIN_TABLE);
        System.out.println(tableDomain);
    }


}
