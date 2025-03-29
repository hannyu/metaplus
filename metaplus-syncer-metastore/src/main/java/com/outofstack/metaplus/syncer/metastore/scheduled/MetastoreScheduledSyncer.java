package com.outofstack.metaplus.syncer.metastore.scheduled;

import com.outofstack.metaplus.client.MetaplusClient;
import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonDiff;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.DocUtil;
import com.outofstack.metaplus.domain.TableDomain;
import com.outofstack.metaplus.syncer.metastore.MetastoreUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;

import java.util.List;

public class MetastoreScheduledSyncer {

    public static String scheduledSyncerName = "scheduled-" + PropertyConfig.getSyncerHostname();

    public static void main(String[] args) throws TException {

        /// 1 TODO：配置，metastore地址、黑白名单

        /// 2 TODO：FQMN去重

        /// 3 handle table one by one
        Configuration conf = new Configuration();
        conf.set("hive.metastore.uris", "thrift://localhost:9083"); // Metastore地址

        MetaplusClient mpclient = new MetaplusClient("http://localhost:8020/");

        // 2. 创建客户端
        try (HiveMetaStoreClient client = new HiveMetaStoreClient(conf)) {

            // 3. 操作元数据示例
            for (String catalogName : client.getCatalogs()) {
                System.out.println("Catalog: " + catalogName);

                // 获取所有数据库
                for (String dbName : client.getAllDatabases(catalogName)) {
                    System.out.println("Database: " + dbName);

                    // 获取所有表
                    for (String tableName : client.getAllTables(catalogName, dbName)) {
                        Table table = client.getTable(catalogName, dbName, tableName);
                        System.out.println("Table: " + tableName + ", Loc: " + table.getSd().getLocation());

                        String fqmnName = TableDomain.packFqmnName(catalogName, dbName, tableName);
                        HttpResponse<MetaplusDoc> response = mpclient.queryRead(DocUtil.packFqmn("", "table", fqmnName));
                        if (response.isNotFound()) {
                            /// create table/column/partition
                            String createdAt = DateUtil.formatNow();

                            // create table
                            MetaplusDoc doc = MetastoreUtil.packTableDoc(table);
                            doc.setSyncCreatedAt(createdAt);
                            doc.setSyncCreatedFrom(scheduledSyncerName);
                            System.out.println("-- metaCreate: " + doc.getFqmnFqmn());
                            mpclient.metaCreate(doc.getFqmnFqmn(), doc);

                            // create column
                            for (FieldSchema fs : table.getSd().getCols()) {
                                MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(table, fs);
                                colDoc.setSyncCreatedAt(createdAt);
                                colDoc.setSyncCreatedFrom(scheduledSyncerName);
                                System.out.println("-- metaCreate: " + colDoc.getFqmnFqmn());
                                mpclient.metaCreate(colDoc.getFqmnFqmn(), colDoc);
                            }

                            // create partitions? no.

                        } else if (response.isSuccess()) {
                            /// diff and update
                            MetaplusDoc oldDoc = response.getBody();
                            MetaplusDoc newDoc = MetastoreUtil.packTableDoc(table);
                            List<String[]> diffs = DocUtil.diffDoc(oldDoc, newDoc);
                            diffs = diffs.stream().filter(dif -> !dif[0].startsWith("$.meta.tableParams.")).toList();
                            System.out.println("diffs: " + JsonObject.object2JsonString(diffs));
                            if (!diffs.isEmpty()) {
                                String updatedAt = DateUtil.formatNow();

                                // update table
                                newDoc.setSyncUpdatedAt(updatedAt);
                                newDoc.setSyncUpdatedFrom(scheduledSyncerName);
                                System.out.println("-- metaUpdate: " + newDoc.getFqmnFqmn());
                                mpclient.metaUpdate(newDoc.getFqmnFqmn(), newDoc);

                                diffs.forEach(dif -> {
                                    if (dif[0].startsWith("$.meta.columns[")) {
                                        int i = Integer.parseInt(dif[0].substring("$.meta.columns[".length(), dif[0].indexOf("]")));
                                        if (dif[1].equals("+")) {
                                            // create column
                                            MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(table, table.getSd().getCols().get(i));
                                            colDoc.setSyncUpdatedAt(updatedAt);
                                            colDoc.setSyncUpdatedFrom(scheduledSyncerName);
                                            System.out.println("-- metaCreate: " + colDoc.getFqmnFqmn());
                                            mpclient.metaCreate(colDoc.getFqmnFqmn(), colDoc);
                                        } else if (dif[1].equals("-")) {
                                            // delete column, no way in hive?
                                            MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(table, table.getSd().getCols().get(i));
                                            colDoc.setSyncUpdatedAt(updatedAt);
                                            colDoc.setSyncUpdatedFrom(scheduledSyncerName);
                                            System.out.println("-- metaDelete: " + colDoc.getFqmnFqmn());
                                            mpclient.metaDelete(colDoc.getFqmnFqmn(), colDoc);
                                        } else if (dif[1].equals("!")) {
                                            // update column, no way in hive?
                                            MetaplusDoc colDoc = MetastoreUtil.packColumnDoc(table, table.getSd().getCols().get(i));
                                            colDoc.setSyncUpdatedAt(updatedAt);
                                            colDoc.setSyncUpdatedFrom(scheduledSyncerName);
                                            System.out.println("-- metaUpdate: " + colDoc.getFqmnFqmn());
                                            mpclient.metaUpdate(colDoc.getFqmnFqmn(), colDoc);
                                        }
                                    }
                                });
                            }
                        } else {
                            /// error, why?
                        }
                    }
                }
            }
        }
    }


}
