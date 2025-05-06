package com.outofstack.metaplus.syncer.metastore.scheduled;

import com.outofstack.metaplus.client.MetaplusClient;
import com.outofstack.metaplus.common.TimeUtil;
import com.outofstack.metaplus.common.PropertyConfig;
import com.outofstack.metaplus.common.http.HttpResponse;
import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;
import com.outofstack.metaplus.common.model.DocUtil;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.common.model.search.Query;
import com.outofstack.metaplus.domain.TableDomain;
import com.outofstack.metaplus.syncer.metastore.MetastoreUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;

import java.util.List;


public class MetastoreScheduledSyncer {

    public static String scheduledSyncerName = "scheduled-" + PropertyConfig.getSyncerHostname();

    private String metastoreUri;
    private String metaplusServerUrl;

    public MetastoreScheduledSyncer(String metastoreUri, String metaplusServerUrl) {
        this.metastoreUri = metastoreUri;
        this.metaplusServerUrl = metaplusServerUrl;
    }


    public synchronized void scheduledSync() throws Exception {

        /// 1 TODO：配置，metastore地址、黑白名单

        /// 3 handle table one by one
        Configuration conf = new Configuration();
        conf.set("hive.metastore.uris", "thrift://localhost:9083"); // Metastore地址

        MetaplusClient mpclient = new MetaplusClient("http://localhost:8020/");

        // 2. 创建客户端
        try (HiveMetaStoreClient hmsclient = new HiveMetaStoreClient(conf)) {

            // TODO: 按黑白名单过滤，生成新的数组，提前掌握同步工作量，了解进度

            // 3. 操作元数据示例
            for (String catalogName : hmsclient.getCatalogs()) {
                System.out.println("Catalog: " + catalogName);

                // 获取所有数据库
                for (String dbName : hmsclient.getAllDatabases(catalogName)) {
                    System.out.println("Database: " + dbName);

                    // 获取所有表
                    for (String tableName : hmsclient.getAllTables(catalogName, dbName)) {
                        Table table = hmsclient.getTable(catalogName, dbName, tableName);
                        System.out.println("Table: " + tableName + ", Loc: " + table.getSd().getLocation());

                        String fqmnName = TableDomain.packTableFqmn(catalogName, dbName, tableName);
                        HttpResponse<MetaplusDoc> response = mpclient.queryRead(DocUtil.packFqmn("", "table", fqmnName));
                        if (response.isNotFound()) {
                            /// create table/column/partition
                            String createdAt = TimeUtil.formatNow();

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
                        } else if (response.isSuccess()) {
                            /// diff and update
                            MetaplusDoc oldDoc = response.getBody();
                            MetaplusDoc newDoc = MetastoreUtil.packTableDoc(table);
                            List<String[]> diffs = DocUtil.diffDoc(oldDoc, newDoc);
                            diffs = diffs.stream().filter(dif -> !dif[0].startsWith("$.meta.tableParams.")).toList();
                            System.out.println("diffs: " + JsonObject.object2JsonString(diffs));
                            if (!diffs.isEmpty()) {
                                String updatedAt = TimeUtil.formatNow();

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
                            // error, why?
                            // print error
                            System.out.println("WHY? " + response);
                            continue;
                        }

                        // diff partition
                        /// TODO: 搞清楚 listPartitionNames 的返回顺序？
                        /// 答：Metastore的返回是按分区名的字符顺序返回（并不是按添加分区的时间顺序），
                        /// 若 分区数 大于 指定max，则会取不到最新的分区。

                        /// --- 不太行的方案 ---
                        /// 解决方案：一次读500，一直读，直到读完（最后一次读取返回不足500），
                        /// 取最后的0~1000条，只对这最多1000条分区做diff（时间越久远，越不重要）
                        /// diff只有新增，没有删除，更没有修改
                        ///
                        /// --- 只处理分区数小于10k的 ---

                        int partitionCnt = hmsclient.getNumPartitionsByFilter(catalogName, dbName, tableName, "");
                        System.out.println("partitionCnt: " + partitionCnt);

                        if (partitionCnt <= MetastoreUtil.getMaxSupportedPartitions()) {
                            List<String> newPartitions = hmsclient.listPartitionNames(catalogName, dbName, tableName, -1);
//                            System.out.println("newPartitions: " + JsonObject.object2JsonString(newPartitions));
                            System.out.println("newPartitions: " + newPartitions.size());

                            Query query = new Query();
                            query.setQuery(new JsonObject("bool", new JsonObject("filter", new JsonArray()
                                    .add(new JsonObject("term", new JsonObject("meta.catalogName", catalogName)))
                                    .add(new JsonObject("term", new JsonObject("meta.dbName", dbName)))
                                    .add(new JsonObject("term", new JsonObject("meta.tableName", tableName)))
                            )));
                            query.setSort(new JsonArray(new JsonObject("meta.partitionValues", new JsonObject("order", "asc"))));
                            query.setSource(new JsonArray("meta.partitionValues"));
                            query.setSize(10000);
//                            System.out.println("query: " + query);
                            HttpResponse<Hits> hits = mpclient.querySearch(TableDomain.DOMAIN_TABLE_PARTITION, query);
//                            System.out.println("hits: " + hits);
                            List<String> oldPartitions = MetastoreUtil.unpackPartitionsFromHits(hits.getBody());
//                            System.out.println("oldPartitions: " + oldPartitions);
                            System.out.println("oldPartitions: " + oldPartitions.size());

                            /// add partitions
                            newPartitions.stream().filter(p -> !oldPartitions.contains(p)).forEach(p -> {
                                MetaplusDoc partDoc = MetastoreUtil.packPartitionDoc(catalogName, dbName, tableName, p);
                                mpclient.metaCreate(partDoc.getFqmnFqmn(), partDoc);
                                System.out.println("create partition " + partDoc.getFqmnFqmn());
                            });

                            /// delete partitions
                            oldPartitions.stream().filter(p -> !newPartitions.contains(p)).forEach(p -> {
                                MetaplusDoc partDoc = MetastoreUtil.packPartitionDoc(catalogName, dbName, tableName, p);
                                mpclient.metaDelete(partDoc.getFqmnFqmn(), partDoc);
                                System.out.println("delete partition " + partDoc.getFqmnFqmn());
                            });

                        } else {
                            System.out.println("Table has '" + partitionCnt + "' partitions, exceed MaxSupportedPartitions '" +
                                    MetastoreUtil.getMaxSupportedPartitions()+ "', so NOT sync the partition info.");
                        }
                    }
                }
            }
        }
    }

//    private List<String> getLatestPartitionNames(HiveMetaStoreClient hmsclient, String catalogName, String dbName,
//                                                 String tableName, short maxHalf) throws TException {
//        List<String> lastHalfPartitionNames = null;
//        List<String> newHalfPartitionNames = null;
//        String lastPartitionName = null;
//
//        do {
//            lastHalfPartitionNames = newHalfPartitionNames;
//
//            PartitionsByExprRequest request = new PartitionsByExprRequest();
//            request.setCatName(catalogName);
//            request.setDbName(dbName);
//            request.setTblName(tableName);
//            request.setMaxParts(maxHalf);
//            if (null != lastPartitionName) {
//
//            }
//
//            newHalfPartitionNames = hmsclient.listPartitionNames(catalogName, dbName, tableName, maxHalf);
//        } while (newHalfPartitionNames.size() == maxHalf);
//
//        if (null == lastHalfPartitionNames) {
//            Collections.reverse(newHalfPartitionNames);
//            return newHalfPartitionNames;
//        } else {
//            List<String> merged = new ArrayList<>(lastHalfPartitionNames.size() + newHalfPartitionNames.size());
//            merged.addAll(lastHalfPartitionNames);
//            merged.addAll(newHalfPartitionNames);
//            Collections.reverse(merged);
//            return merged;
//        }
//    }

    public static void main(String[] args) throws Exception {
        MetastoreScheduledSyncer mss = new MetastoreScheduledSyncer("thrift://localhost:9083/",
                "http://localhost:8020/");
        mss.scheduledSync();
    }


}
