package com.outofstack.metaplus.common.json;

import java.util.LinkedList;
import java.util.List;

public class JsonDiff {

    /**
     * return:
     * [
     *      ["$.a.b.c","+"],
     *      ["$.a.x.y","-"],
     *      ["$.a.b[5],"!"],
     *      ...
     * ]
     *
     */
    public static List<String[]> diff(JsonObject left, JsonObject right) {
        List<String[]> diffList = new LinkedList<>();
        diffRecursively(diffList, "$", left, right);
        return diffList;
    }

    private static void diffRecursively(List<String[]> diffList, String path, Object left, Object right) {
        if (null == left && null != right) {
            diffList.add(new String[] {path, "+"});
        } else if (null != left && null == right) {
            diffList.add(new String[] {path, "-"});
        } else if (null != left && null != right) {
            if (left instanceof JsonObject && right instanceof JsonObject) {
                JsonObject leftJo = (JsonObject) left;
                JsonObject rightJo = (JsonObject) right;
                // -, !
                for (String key : leftJo.keySet()) {
                    if (rightJo.containsKey(key)) {
                        diffRecursively(diffList, path + "." + key, leftJo.getObject(key), rightJo.getObject(key));
                    } else {
                        diffList.add(new String[] { path + "." + key, "-" });
                    }
                }
                // +
                for (String key : rightJo.keySet()) {
                    if (!leftJo.containsKey(key)) {
                        diffList.add(new String[] { path + "." + key, "+" });
                    }
                }
            } else if (left instanceof JsonArray && right instanceof JsonArray) {
                JsonArray leftJa = (JsonArray) left;
                JsonArray rightJa = (JsonArray) right;
                int size = Math.max(leftJa.size(), rightJa.size());
                for (int i = 0; i < size; i++) {
                    if (i < leftJa.size() && i < rightJa.size()) {
                        diffRecursively(diffList, path + "[" + i + "]", leftJa.getObject(i), rightJa.getObject(i));
                    } else if (i < leftJa.size()) {
                        diffList.add(new String[] { path + "[" + i + "]", "-" });
                    } else {
                        diffList.add(new String[] { path + "[" + i + "]", "+" });
                    }
                }
            } else if (!left.equals(right)) {
                diffList.add(new String[] {path, "!"});
            }
        }
    }
}
