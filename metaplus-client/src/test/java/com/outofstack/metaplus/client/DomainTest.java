package com.outofstack.metaplus.client;

import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.DomainDoc;
import com.outofstack.metaplus.domain.DefinedDomains;
import com.outofstack.metaplus.domain.TableDomain;

public class DomainTest {

    public static void main(String[] args) {
        MetaplusClient mpclient = new MetaplusClient("http://localhost:8020/");

        HttpResponse<JsonObject> response;
        DomainDoc tableDomain = DefinedDomains.getDefinedDomain(TableDomain.DOMAIN_TABLE);
//        mpclient.domainDelete(tableDomain.getFqmnName(), tableDomain);
        response = mpclient.domainCreate(tableDomain.getFqmnName(), tableDomain);
        System.out.println("response: " + response);

        DomainDoc tableColumnDomain = DefinedDomains.getDefinedDomain(TableDomain.DOMAIN_TABLE_COLUMN);
//        mpclient.domainDelete(tableColumnDomain.getFqmnName(), tableColumnDomain);
        response = mpclient.domainCreate(tableColumnDomain.getFqmnName(), tableColumnDomain);
        System.out.println("response: " + response);

        DomainDoc tablePartitionDomain = DefinedDomains.getDefinedDomain(TableDomain.DOMAIN_TABLE_PARTITION);
//        mpclient.domainDelete(tablePartitionDomain.getFqmnName(), tablePartitionDomain);
        response = mpclient.domainCreate(tablePartitionDomain.getFqmnName(), tablePartitionDomain);
        System.out.println("response: " + response);

    }
}
