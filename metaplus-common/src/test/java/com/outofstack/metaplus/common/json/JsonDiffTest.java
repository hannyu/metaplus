package com.outofstack.metaplus.common.json;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonDiffTest {

    @Test
    public void testOne() {
        String json1 = "{" +
                "   \"a\": [" +
                "       {\"bb\":5}," +
                "       {\"cc\":6}," +
                "   ]," +
                "   \"x\": \"sss\"," +
                "   \"n\": {" +
                "       \"v1\": \"mm\"" +
                "   }" +
                "}";
        String json2 = "{" +
                "   \"a\": [" +
                "       {\"bb\":5}," +
                "       {\"cc\":7}," +
                "   ]," +
                "   \"y\": \"sss\"," +
                "   \"n\": {" +
                "       \"v1\": \"gg\"" +
                "   }" +
                "}";

        List<String[]> diffs = JsonDiff.diff(JsonObject.parse(json1), JsonObject.parse(json2));
        System.out.println(JsonObject.object2JsonString(diffs));
        assertEquals(4, diffs.size());
    }

    @Test
    public void testFive() {
        String json1 = "{\n" +
                "    \"accessType\": 0,\n" +
                "    \"isStatsCompliant\": false,\n" +
                "    \"lastAccessTime\": 0,\n" +
                "    \"owner\": \"han\",\n" +
                "    \"ownerType\": \"USER\",\n" +
                "    \"parameters\": {\n" +
                "        \"last_modified_time\": \"1741235193\",\n" +
                "        \"transient_lastDdlTime\": \"1741235193\",\n" +
                "        \"TRANSLATED_TO_EXTERNAL\": \"TRUE\"\n" +
                "    },\n" +
                "    \"parametersSize\": 8,\n" +
                "    \"partitionKeys\": [\n" +
                "        {\n" +
                "            \"name\": \"dt\",\n" +
                "            \"type\": \"string\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"partitionKeysIterator\": {\n" +
                "    },\n" +
                "    \"retention\": 0,\n" +
                "    \"rewriteEnabled\": false,\n" +
                "    \"sd\": {\n" +
                "        \"bucketCols\": [\n" +
                "        ],\n" +
                "        \"bucketColsIterator\": {\n" +
                "        },\n" +
                "        \"bucketColsSize\": 0,\n" +
                "        \"cols\": [\n" +
                "            {\n" +
                "                \"comment\": \"id\",\n" +
                "                \"name\": \"id\",\n" +
                "                \"type\": \"int\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"name\",\n" +
                "                \"name\": \"name\",\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"age_s\",\n" +
                "                \"name\": \"age\",\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"????1\",\n" +
                "                \"name\": \"add_col1\",\n" +
                "                \"type\": \"string\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"colsIterator\": {\n" +
                "        },\n" +
                "        \"colsSize\": 4,\n" +
                "        \"outputFormat\": \"org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat\",\n" +
                "        \"parameters\": {\n" +
                "        },\n" +
                "        \"parametersSize\": 0,\n" +
                "        \"serdeInfo\": {\n" +
                "            \"parameters\": {\n" +
                "                \"serialization.format\": \"1\"\n" +
                "            },\n" +
                "            \"parametersSize\": 1,\n" +
                "            \"serializationLib\": \"org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe\"\n" +
                "        },\n" +
                "        \"setCompressed\": true,\n" +
                "        \"setStoredAsSubDirectories\": true,\n" +
                "        \"sortCols\": [\n" +
                "        ],\n" +
                "        \"sortColsIterator\": {\n" +
                "        },\n" +
                "        \"sortColsSize\": 0,\n" +
                "        \"storedAsSubDirectories\": false\n" +
                "    },\n" +
                "    \"setIsStatsCompliant\": false,\n" +
                "    \"setRewriteEnabled\": true,\n" +
                "    \"temporary\": false,\n" +
                "    \"txnId\": 0,\n" +
                "    \"writeId\": 0\n" +
                "}";

        String json2 = "{\n" +
                "    \"accessType\": 8,\n" +
                "    \"isStatsCompliant\": false,\n" +
                "    \"lastAccessTime\": 0,\n" +
                "    \"owner\": \"han\",\n" +
                "    \"ownerType\": \"USER\",\n" +
                "    \"parameters\": {\n" +
                "        \"last_modified_by\": \"han\",\n" +
                "        \"last_modified_time\": \"1741687933\",\n" +
                "        \"transient_lastDdlTime\": \"1741687933\",\n" +
                "        \"TRANSLATED_TO_EXTERNAL\": \"TRUE\"\n" +
                "    },\n" +
                "    \"parametersSize\": 8,\n" +
                "    \"partitionKeys\": [\n" +
                "        {\n" +
                "            \"name\": \"dt\",\n" +
                "            \"type\": \"string\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"partitionKeysIterator\": {\n" +
                "    },\n" +
                "    \"retention\": 0,\n" +
                "    \"rewriteEnabled\": false,\n" +
                "    \"sd\": {\n" +
                "        \"bucketCols\": [\n" +
                "        ],\n" +
                "        \"bucketColsIterator\": {\n" +
                "        },\n" +
                "        \"bucketColsSize\": 0,\n" +
                "        \"cols\": [\n" +
                "            {\n" +
                "                \"comment\": \"id\",\n" +
                "                \"name\": \"id\",\n" +
                "                \"type\": \"int\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"name\",\n" +
                "                \"name\": \"name\",\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"age_s\",\n" +
                "                \"name\": \"age\",\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"????1\",\n" +
                "                \"name\": \"add_col1\",\n" +
                "                \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"comment\": \"any time\",\n" +
                "                \"name\": \"email\",\n" +
                "                \"type\": \"string\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"colsIterator\": {\n" +
                "        },\n" +
                "        \"colsSize\": 5,\n" +
                "        \"outputFormat\": \"org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat\",\n" +
                "        \"parameters\": {\n" +
                "        },\n" +
                "        \"parametersSize\": 0,\n" +
                "        \"serdeInfo\": {\n" +
                "            \"parameters\": {\n" +
                "                \"serialization.format\": \"1\"\n" +
                "            },\n" +
                "            \"parametersSize\": 1,\n" +
                "            \"serializationLib\": \"org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe\"\n" +
                "        },\n" +
                "        \"setCompressed\": true,\n" +
                "        \"setStoredAsSubDirectories\": true,\n" +
                "        \"sortCols\": [\n" +
                "        ],\n" +
                "        \"sortColsIterator\": {\n" +
                "        },\n" +
                "        \"sortColsSize\": 0,\n" +
                "        \"storedAsSubDirectories\": false\n" +
                "    },\n" +
                "    \"setIsStatsCompliant\": false,\n" +
                "    \"txnId\": 0,\n" +
                "    \"writeId\": 0\n" +
                "}";

        List<String[]> diffs = JsonDiff.diff(JsonObject.parse(json1), JsonObject.parse(json2));

        System.out.println(JsonObject.object2JsonString(diffs));
    }
}
